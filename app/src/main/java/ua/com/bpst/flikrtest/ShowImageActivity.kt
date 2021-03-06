package ua.com.bpst.flikrtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_image.*
import org.jetbrains.anko.contentView
import ua.com.bpst.flikrtest.helpers.SwipeHelper


class ShowImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        Picasso.with(this).load(intent.getStringExtra("bigImg"))
            .into(iv_image, object : Callback {
                override fun onSuccess() {
                    scheduleStartPostponedTransition(iv_image)
                }

                override fun onError() {
                    showError("Loading photo error")
                }

            })

        contentView?.setOnTouchListener(SwipeHelper().setupCallback { diff, state ->
            when(state){
                SwipeHelper.SwipeState.UP, SwipeHelper.SwipeState.DOWN -> {
                    iv_image.translationY = diff ?: 0F
                }
                SwipeHelper.SwipeState.END ->{
                    finishAfterTransition()
                }
                SwipeHelper.SwipeState.CANCEL ->{
                    iv_image.animate().translationY(0F).setDuration(100).start()
                }

            }
        })
    }

    fun showError(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }

        })
    }

}
