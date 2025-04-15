package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import java.util.Locale

// этот alias определяет следующий тип: лямбда функция, у которой на входе Post, а на выходе Unit
// заменили изначальную реализацию алиасов интерфейсом
interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun onPlayVideo(post: Post) {}
}

//в качестве параметров принимает две lambda функции, имена которых определены typealias выше
class PostAdapter(
    private val onInteractionListener: OnInteractionListener
    ): ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    // отвечает за создание viewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            share.text = numToString(post.reposts)
            viewCount.text = numToString(post.views)
            if (post.video.isNotBlank()){
                videoGroup.visibility = View.VISIBLE
            }
            liked.apply {
                isChecked = post.likedByMe
                text = numToString(post.likes)
            }

            video.setOnClickListener{
                onInteractionListener.onPlayVideo(post)
            }

            videoButton.setOnClickListener{
                onInteractionListener.onPlayVideo(post)
            }

            liked.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_actions)
                    setOnMenuItemClickListener {item ->
                        when(item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }

                    }
                }.show()
            }
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

// наблюдает за изменением списка
object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}