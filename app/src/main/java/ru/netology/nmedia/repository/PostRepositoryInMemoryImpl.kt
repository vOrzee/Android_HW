package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl: PostRepository {

    var nextId: Long = 1

    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fybПривет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 1099,
            reposts = 999998,
            views = 0,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется",
            published = "22 мая в 17:20",
            likedByMe = false,
            likes = 2199,
            reposts = 213,
            views = 0
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется. В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется",
            published = "30 мая в 17:20",
            likedByMe = false,
            likes = 2199,
            reposts = 213,
            views = 0
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map{ post: Post ->
            if (post.id != id) post else post.copy(likedByMe = !post.likedByMe, likes = if(!post.likedByMe) post.likes + 1 else post.likes - 1)
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map{post: Post ->
            if(post.id != id) post else post.copy(reposts = post.reposts + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter{
            it.id != id
        }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me")) + posts
        } else {
            posts.map{
                if(it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }
}