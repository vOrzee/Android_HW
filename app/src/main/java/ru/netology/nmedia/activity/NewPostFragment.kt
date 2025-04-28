package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringArg

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        binding.content.requestFocus()

//        arguments?.textArg?.let {
//            binding.content.setText(it)
//        } эта запись аналогична тому, что ниже, просто тут для примера it = textArg

        arguments?.textArg?.let(binding.content::setText)

        binding.ok.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNotBlank()) {
                viewModel.changeContentAndSave(content)
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
    companion object {
        //тут мы делигируем свойство объекту StringArg (его методы будут использованы для свойства)
        var Bundle.textArg by StringArg
    }
}