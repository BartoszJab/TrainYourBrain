package com.example.projectandroid.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.projectandroid.R
import com.example.projectandroid.databinding.FragmentRegisterBinding
import com.example.projectandroid.home.HomeActivity
import com.example.projectandroid.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    lateinit var myAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.INVISIBLE

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

//        if (binding.etEmail.text.isNotEmpty() &&
//            !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
//                val errorMessage = "Give a correct email"
//                binding.etEmail.error = errorMessage
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAuth = FirebaseAuth.getInstance()
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val username = binding.etUsername.text.toString()

        if (username.isEmpty()) {
            val errorMessage = "Username must not be empty"
            binding.etUsername.error = errorMessage
            return
        }

        // check passwords length
        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
            return
        }

        // check if email is of correct pattern
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val errorMessage = "Provide correct email"
            binding.etEmail.error = errorMessage
            return
        }

        // check if "confirmPassword" matches the "password"
        if (!isConfirmPasswordCorrect()) {
            val errorMessage = "Passwords must be equal"
            binding.etPasswordConfirm.error = errorMessage
            return
        }

        // if edit texts are not empty sing in user with given credentials
        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    myAuth.createUserWithEmailAndPassword(email, password).await()
                    //userCollectionRef.add(User(username)).await()

                    val profileUpdate = userProfileChangeRequest {
                        displayName = username
                    }
                    myAuth.currentUser!!.updateProfile(profileUpdate).await()

                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.INVISIBLE
                        activity?.finish()
                        startActivity(Intent(activity, HomeActivity::class.java))
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isConfirmPasswordCorrect(): Boolean {
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirm.text.toString()

        if (confirmPassword.isEmpty() || confirmPassword != password) {
            return false
        }

        return true
    }

}