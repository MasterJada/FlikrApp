package ua.com.bpst.flikrtest

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Pair
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ImageButton
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.Util
import ua.com.bpst.flikrtest.helpers.ApiHelper

class MainActivity : AppCompatActivity() {



    private val adapter = ImageAdapter()
    private  lateinit var autocompleteAdapter: ArrayAdapter<String>
    private var page = 0
    private var onRequest = false
    set(value) {
        field = value
        ib_search.isEnabled = !value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ib_search.setOnClickListener { view ->
            animateEnter()
            view.setOnClickListener { sendRequest() }
        }
        loadAutocomplete()

        tv_autocomplete.onEdit {
            ib_search.setImageResource(R.drawable.ic_search)
            ib_search.setOnClickListener { sendRequest() }
        }
        tv_autocomplete.setOnEditorActionListener { tv, i, _ ->
            return@setOnEditorActionListener when(i){
                EditorInfo.IME_ACTION_SEARCH -> {
                    if(tv.text.isNotEmpty())
                    sendRequest()
                    true
                }
                else -> false
            }
        }
        rv_images.layoutManager = GridLayoutManager(this, 3)
        rv_images.adapter = adapter
        rv_images.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? GridLayoutManager)?.let { lm ->

                    val totalItems = lm.itemCount
                    val visibleItem = lm.findLastVisibleItemPosition()
                    if (visibleItem >= (totalItems - 4)) {
                        if (!onRequest) {
                            loadMore()
                        }
                        print("")
                    }
                }
            }
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
        },650)
    }

    private fun loadAutocomplete() {
         val autoComplete = ArrayList<String>()
        Realm.getDefaultInstance().use { realm ->
            val realmResult = realm.where<SearchHistory>().findAll()
            realmResult.forEach {
                autoComplete.add(it.query!!)
            }
        }
        autocompleteAdapter = ArrayAdapter(this, R.layout.suggestion_layout, autoComplete)
        tv_autocomplete.setAdapter(autocompleteAdapter)
    }

    private fun addQueryToDB(query: String) {
        Realm.getDefaultInstance().use { realm ->
            val itm = SearchHistory()
            itm.query = query
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(itm)
            realm.commitTransaction()
        }
        loadAutocomplete()
    }

    private fun validateQuery(query: String): Boolean {
        var valid = true
        if (query.isEmpty()) {
            tv_autocomplete.error = "Empty field"
            valid = false
        }
        return valid
    }

    private fun sendRequest() {
        onRequest = true
        val query = tv_autocomplete.text.toString()
        if (validateQuery(query)) {
            addQueryToDB(query)
            ApiHelper.instance.search(query, page).send({ result ->
                page = result.photos.page
                adapter.addItems(result.photos.photo)
                loadet()
            }, {
                it.printStackTrace()
                onRequest = false

            })
        }
    }

    private fun loadMore() {
        onRequest = true
        val query = tv_autocomplete.text.toString()
        ApiHelper.instance.search(query, ++page).send({ result ->
            page = result.photos.page
            adapter.addItems(result.photos.photo)
            onRequest = false
        }, {
            it.printStackTrace()
            onRequest = false
        })
    }


    private fun loadet(){
        hideKeyboard()
        ib_search.setImageResource(R.drawable.ic_cancel)
        ib_search.setOnClickListener { view ->
            tv_autocomplete.setText("")
            showKeyboard()
            page = 0
            adapter.resetSearch()
            (view as ImageButton).setImageResource(R.drawable.ic_search)
            view.setOnClickListener {
                sendRequest()
            }
        }
        onRequest = false
    }
}
