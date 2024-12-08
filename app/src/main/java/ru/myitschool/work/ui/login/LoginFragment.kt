package ru.myitschool.work.ui.login

import android.content.Context.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentLoginBinding
import ru.myitschool.work.utils.collectWhenStarted

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        val savedLogin = sharedPreferences.getString("LOGIN", "")

        if (!savedLogin.isNullOrEmpty()) {
            //переход на гл экран
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            Log.w("test", savedLogin)
        }
        subscribe()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }
    private fun subscribe() {
        viewModel.state.collectWhenStarted(this) { state ->
            binding.login.setOnClickListener { view ->

                if (true){ // если правильно то логин должен быть сохранён и при следующем открытии приложения экран авторизации не должен быть показан.
                    val username = binding.username.text.toString()
                    val editor = sharedPreferences.edit()
                    editor.putString("LOGIN", username)
                    editor.apply()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                else{
                    binding.error.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}