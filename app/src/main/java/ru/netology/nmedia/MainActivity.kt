package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.util.Locale

class MainActivity : AppCompatActivity() {
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

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999999,
            reposts = 1,
            views = 0
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            if (post.likedByMe) {
                liked.setImageResource(R.drawable.baseline_favorite_24)
            }
            liked.setOnClickListener {
                post.likedByMe = !post.likedByMe
                liked.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
                )
                if (post.likedByMe) post.likes ++ else post.likes --
                likedCount.text = if (post.likes < 1) "" else numToString(post.likes)

            }

            share.setOnClickListener {
                post.reposts ++
                shareCount.text = if (post.reposts < 1) "" else numToString(post.reposts)
            }
        }

    }

    private fun numToString(likes: Long): CharSequence? {
        if (likes in 1000..9999){
            return "%.1fK".format(Locale.ROOT, likes/1000.0)
        } else if (likes in 10000..999999){
            return "%dK".format(Locale.ROOT, likes/1000)
        } else if (likes > 1000000){
            return "%.1fM".format(Locale.ROOT, likes/1_000_000.0)
        }
        return likes.toString()
    }
}