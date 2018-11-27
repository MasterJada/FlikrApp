package ua.com.bpst.flikrtest.helpers

import android.view.MotionEvent
import android.view.View

class SwipeHelper: View.OnTouchListener {
    private var startY = 0F

    private var swipeUpCallback: ((diff: Float) -> Unit)? = null
    private var swipeDownCallback: ((diff: Float) -> Unit)? = null
    private var cancelSwipeCallback: (()->Unit)? = null
    private var swipeEndCallback: (()->Unit)? = null

    fun setupCallbacks(swipeUp:((diff: Float) -> Unit)? = null,
                       swipeDown: ((diff: Float) -> Unit)? = null,
                       cancelSwipe: (()->Unit)? = null,
                       swipeEnd: (()->Unit)? = null): SwipeHelper {
        swipeUpCallback = swipeUp
        swipeDownCallback = swipeDown
        cancelSwipeCallback = cancelSwipe
        swipeEndCallback = swipeEnd

        return this
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val maxDif = (view.height / 4).toFloat()
        val action = motionEvent.action
        if(action == MotionEvent.ACTION_DOWN){
            startY =  motionEvent.y
        }

        if(action == MotionEvent.ACTION_MOVE){

            if(startY > motionEvent.y) {
                val diff = motionEvent.y - startY
                swipeUpCallback?.invoke(diff)
                if (diff < -maxDif){
                   swipeEndCallback?.invoke()
                }
            }else
                if(startY < motionEvent.y){
                    val diff = startY - motionEvent.y
                    swipeDownCallback?.invoke(-diff)
                    if(diff < -maxDif){
                        swipeEndCallback?.invoke()
                    }
                }

        }

        if(action == MotionEvent.ACTION_UP){
            startY = 0F
            cancelSwipeCallback?.invoke()

        }
       return true
    }
}