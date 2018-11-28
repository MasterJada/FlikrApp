package ua.com.bpst.flikrtest.mainMVP

import android.app.ActivityOptions

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v7.widget.GridLayoutManager
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*
import ua.com.bpst.flikrtest.*
import ua.com.bpst.flikrtest.adapters.ImageAdapter
import ua.com.bpst.flikrtest.helpers.PaginationHelper

class MainActivity : AppCompatActivity() {


    val adapter = ImageAdapter()
    private lateinit var autocompleteAdapter: ArrayAdapter<String>
    private val presenter: MainPresenter by lazy {
        MainPresenter(MainModel())
    }

    var onRequest = false
        set(value) {
            field = value
            ib_search.isEnabled = !value
        }

    var query: String
        get() {
            return tv_autocomplete.text.toString()
        }
        set(value) = tv_autocomplete.setText(value)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ib_search.setOnClickListener { view ->
            animateEnter()
            view.setOnClickListener { presenter.sendRequest() }
        }
        presenter.attachView(this)

        tv_autocomplete.onEdit { _ ->
            makeSearchButton()
        }
        tv_autocomplete.setOnEditorActionListener { tv, i, _ ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (tv.text.isNotEmpty())
                        presenter.sendRequest()
                    true
                }
                else -> false
            }
        }
        val columns = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 6
        rv_images.layoutManager = GridLayoutManager(this, columns)
        rv_images.adapter = adapter
        rv_images.addOnScrollListener(PaginationHelper().setupCallback {
            presenter.loadMore()
        })
        adapter.onClick { photo, vh ->
            val intent = Intent(this, ShowImageActivity::class.java)
            val option = ActivityOptions.makeSceneTransitionAnimation(this, vh.image, "image")
            intent.putExtra("img", photo.smallUrl)
            intent.putExtra("bigImg", photo.url)
            intent.putExtra("title", photo.title)

            startActivity(intent, option.toBundle())
        }

    }

    private fun animateEnter() {
        val constraintStart = ConstraintSet()
        constraintStart.clone(this, R.layout.activity_main_detailed)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 600
        TransitionManager.beginDelayedTransition(root, transition)
        constraintStart.applyTo(root)
        Handler().postDelayed({
            tv_autocomplete.requestFocus()
            showKeyboard()
        }, 650)
    }

    fun loadAutocomplete(autoComplete: ArrayList<String>) {
        autocompleteAdapter = ArrayAdapter(this, R.layout.suggestion_layout, autoComplete)
        tv_autocomplete.setAdapter(autocompleteAdapter)
    }

    fun loaded() {
        hideKeyboard()
        tv_autocomplete.clearFocus()
        makeClearButton()
        onRequest = false
    }

    private fun makeSearchButton() {
        (ib_search as ImageButton).setImageResource(R.drawable.ic_search)
        ib_search.setOnClickListener {
            presenter.sendRequest()
        }
    }

    fun makeClearButton() {
        ib_search.setImageResource(R.drawable.ic_cancel)
        ib_search.setOnClickListener {
            tv_autocomplete.setText("")
            showKeyboard()
            presenter.resetSearch()
            adapter.resetSearch()
            makeSearchButton()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
