package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils


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

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { content ->
            content ?:return@registerForActivityResult
            viewModel.changeContentAndSave(content)
        }

        // инициализировали адаптер для получения данных и View
        // передаём в качестве двух аргументов две функции
        val adapter = PostAdapter (object: OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                newPostLauncher.launch()

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_chare_post))
                startActivity(shareIntent)

                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                val myIntent = Intent(this@MainActivity, NewPostActivity::class.java).also {
                    it.putExtra("EXTRA_MESSAGE", post.content)
                    startActivity(it)
                }
                val content =  intent.getStringExtra("EXTRA_MESSAGE").toString()
                viewModel.edit(post)
            }

        })

        //подключили адаптер к элементам списка наших views
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost){
                    binding.list.scrollToPosition(0)
                }
            }
        }


//        binding.editCancel.setOnClickListener {
//            binding.content.setText("")
//            binding.editPreview.text = ""
//            binding.editGroup.visibility = View.GONE
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(it)
//            viewModel.edit()
//        }


        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }


//        viewModel.edited.observe(this) {
//            if (it.id != 0L) {
//                binding.content.setText(it.content)
//                binding.content.requestFocus()
//            }
//        }
//
//        binding.add.setOnClickListener {
//            val text = binding.content.text.toString()
//            if (text.isNullOrBlank()) {
//                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//
//            viewModel.changeContentAndSave(text)
//            binding.editGroup.visibility = View.GONE
//            binding.content.setText("")
//            binding.content.clearFocus()
//
//            AndroidUtils.hideKeyboard(it)
//        }

    }
}