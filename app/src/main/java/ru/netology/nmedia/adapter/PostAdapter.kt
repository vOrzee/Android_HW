package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import java.util.Locale

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostAdapter(private val onLikeListener: OnLikeListener):
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    // отвечает за создание viewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onLikeListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onLikeListener: OnLikeListener,
//    private val onShareListener: OnShareListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likedCount.text = numToString(post.likes)
            shareCount.text = numToString(post.reposts)
            viewCount.text = numToString(post.views)

            liked.setImageResource(R.drawable.baseline_favorite_24)

            liked.setImageResource(
                if (post.likedByMe) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            )
            liked.setOnClickListener {
                onLikeListener(post)
            }
//            share.setOnClickListener {
//                onShareListener(post)
//            }
        }
    }
    private fun numToString(likes: Long): CharSequence? {
        if (likes in 1000..9999){

            val thousands = (likes/1000).toString()
            var dozens = ((likes - (likes/1000) * 1000)/100).toString()
            dozens = if (dozens.equals("0")) {
                ""
            } else {
                ".$dozens"
            }
            val kilo = "K"
            return "$thousands$dozens$kilo"

        } else if (likes in 10000..999999){

            return "%dK".format(Locale.ROOT, likes/1000)

        } else if (likes >= 1000000){

            val millions = (likes/1_000_000).toString()
            var thousands = ((likes - (likes/1_000_000) * 1_000_000)/100_000).toString()
            thousands = if (thousands.equals("0")) {
                ""
            } else {
                ".$thousands"
            }
            val mega = "M"
            return "$millions$thousands$mega"

        }
        return likes.toString()
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}