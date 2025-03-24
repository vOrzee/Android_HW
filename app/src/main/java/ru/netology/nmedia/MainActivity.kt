package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
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
            }
        }
        binding.liked.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener {
            viewModel.share()
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