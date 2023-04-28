package com.example.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class CaptionedImagesAdapter(private val captions: MutableList<String>, private val imagesUrl: MutableList<String?>) :
    RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>() {

    inner class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_captioned_image, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView
        val imageView = cardView.findViewById<ImageView>(R.id.info_image)
        val url = imagesUrl[position]
        if(url != null) Picasso.get().load(url).into(imageView)
        val textView = cardView.findViewById<TextView>(R.id.info_text)
        textView.text = captions[position]

        cardView.setOnClickListener {
            listener?.onClick(captions[position])
        }
    }

    override fun getItemCount(): Int = captions.size

    private var listener: Listener? = null

    interface Listener {
        fun onClick(name: String)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }
}
