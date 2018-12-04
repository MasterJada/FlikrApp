package ua.com.bpst.flikrtest.helpers

import android.view.MotionEvent
import android.view.View

class SwipeHelper: View.OnTouchListener {
    private var startY = 0F


    private var swipeCallback: ((diff: Float?, state: SwipeState) -> Unit)? = null

    fun setupCallback(callback: (diff: Float?, state: SwipeState) -> Unit): SwipeHelper {

        swipeCallback = callback
        return this
    }

    enum class SwipeState {
        UP,
        DOWN,
        CANCEL,
        END
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
                swipeCallback?.invoke(diff, SwipeState.UP)
                if (diff < -maxDif){
                    swipeCallback?.invoke(null, SwipeState.END)
                }
            }else
                if(startY < motionEvent.y){
                    val diff = startY - motionEvent.y
                    swipeCallback?.invoke(-diff, SwipeState.DOWN)
                    if(diff < -maxDif){
                        swipeCallback?.invoke(null, SwipeState.END)
                    }
                }

        }

        if(action == MotionEvent.ACTION_UP){
            startY = 0F
            swipeCallback?.invoke(null, SwipeState.CANCEL)

        }
       return true
    }
}