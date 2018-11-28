package ua.com.bpst.flikrtest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageVH>() {
    private val items = ArrayList<ImagesResult.Photos.Photo>()

    private var click: ((photo: ImagesResult.Photos.Photo, vh: ImageVH)->Unit)? = null

    fun onClick(click: (photo: ImagesResult.Photos.Photo, vh: ImageVH)->Unit){
        this.click = click
    }

    fun resetSearch() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItems(items: List<ImagesResult.Photos.Photo>) {
        val from = this.items.size - 1
        this.items += items
        notifyItemRangeChanged(from, items.size)
    }

    fun setItems(items: List<ImagesResult.Photos.Photo>){
        this.items.clear()
        this.items += items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ImageVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(vh: ImageVH, pos: Int) {
        items[pos].apply {
            vh.image.loadFromUrl(url)
            vh.text.text = title
            vh.itemView.setOnClickListener {
                click?.invoke(this, vh)
            }

        }


    }

    inner class ImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_item)
        val text: TextView = itemView.findViewById(R.id.tv_photo_text)
    }
}