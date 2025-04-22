package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nmedia.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val intent = Intent()
        binding.content.requestFocus()
        val message = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        if (message.isNotBlank()) {
            binding.content.setText(message)
        }
        binding.ok.setOnClickListener {
            val content = binding.content.text.toString()
            val intent = Intent()
            if (content.isBlank()) {
//                setResult(Activity.RESULT_CANCELED)
            } else{
                intent.putExtra(Intent.EXTRA_TEXT, content)
//                setResult(Activity.RESULT_OK, intent)
            }
//            finish()
        }
        return binding.root
    }
}