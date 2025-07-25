package com.example.carrental.view

import android.content.Context
import android.content.SharedPreferences // Not used in this snippet, but was in your original. Keep if needed.
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carrental.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    // private lateinit var sharedPreferences: SharedPreferences // Uncomment if you actually use this
    private lateinit var auth: FirebaseAuth

    // Callback reference to switch tabs
    private var tabSwitcher: AuthTabSwitcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) { // Changed to android.content.Context for clarity
        super.onAttach(context)
        // Try to obtain the callback from the parent fragment
        if (parentFragment is AuthTabSwitcher) {
            tabSwitcher = parentFragment as AuthTabSwitcher
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set click listener for the Sign Up button
        binding.signupButton.setOnClickListener {
            userSignup()
        }

        // Set click listener for the "Switch to Login" TextView
        binding.switchlogin.setOnClickListener {
            tabSwitcher?.switchToTab(0) // Switch to the "Sign In" tab (index 0)
        }
    }

    private fun userSignup() {
        // Get values from form fields
        val firstName = binding.signupFirstName.text.toString().trim()
        val lastName = binding.signupLastName.text.toString().trim()
        val email = binding.signupEmail.text.toString().trim()
        val contact = binding.signupContact.text.toString().trim()
        val password = binding.signupPwd.text.toString().trim()
        val confirmPassword = binding.signupConfirmPwd.text.toString().trim()

        // Validate that all required fields are filled
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            contact.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            // Create user using Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                        // After successful registration, switch to the "Sign In" tab via callback
                        tabSwitcher?.switchToTab(0) // Redirect to the "Log_in" tab (index 0)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Registration Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabSwitcher = null // Clear the callback reference when the fragment is detached
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}