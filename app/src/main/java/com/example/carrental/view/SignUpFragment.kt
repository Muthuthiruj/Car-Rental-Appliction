package com.example.carrental.view

import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences
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



    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        // Try to obtain the callback from the parent fragment
        tabSwitcher = parentFragment as? AuthTabSwitcher
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
            // Use the callback to switch to the "Sign In" tab (index 0)
            tabSwitcher?.switchToTab(0)
            // Remove the NavController navigation call if using tab switching exclusively
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
                        tabSwitcher?.switchToTab(0)
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
        tabSwitcher = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
