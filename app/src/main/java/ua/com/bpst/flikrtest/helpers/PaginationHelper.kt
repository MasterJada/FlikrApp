package ua.com.bpst.flikrtest.helpers

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

class PaginationHelper(val lastVisibleitems: Int = 4): RecyclerView.OnScrollListener() {

    private var endCallback: (()-> Unit)? = null
    fun setupCallback (callback: (()->Unit)? = null): PaginationHelper{
        endCallback = callback
        return this
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        (recyclerView.layoutManager as? GridLayoutManager)?.let {lm ->
            val totalItems = lm.itemCount
            val visibleItem = lm.findLastVisibleItemPosition()

            if(visibleItem >= (totalItems - lastVisibleitems)){
                endCallback?.invoke()
            }
        }
    }
}