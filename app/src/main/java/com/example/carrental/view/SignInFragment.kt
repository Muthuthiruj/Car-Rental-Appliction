package com.example.carrental.view

import android.content.Context
import android.os.Bundle // No longer need Intent for HomeActivity directly here
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // Import NavController
import com.example.carrental.R // Make sure R is imported for action ID
import com.example.carrental.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var authNavigator: AuthNavigator? = null //

    // Callback reference for tab switching
    private var tabSwitcher: AuthTabSwitcher? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Get the callback from the parent fragment
        if (parentFragment is AuthTabSwitcher) {
            tabSwitcher = parentFragment as AuthTabSwitcher
        }
        // Get the navigation callback from the parent fragment
        if (parentFragment is AuthNavigator) {
            authNavigator = parentFragment as AuthNavigator
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set click listener on the "Sign In" button
        binding.loginButton.setOnClickListener {
            userLogin()
        }

        // Set click listener on the "Register Now" text to switch to Sign Up tab
        binding.register.setOnClickListener {
            tabSwitcher?.switchToTab(1) // 1 is the index for the Sign Up tab
        }

        // Optional: Set click listener on "Forgot Password" text
        binding.forgotpassword.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot Password clicked", Toast.LENGTH_SHORT).show()
            // Implement navigation to Forgot Password screen if available in your NavGraph
            // e.g., findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }

        // Optional: Handle Google Login button click
        binding.googleLoginBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Google Login clicked", Toast.LENGTH_SHORT).show()
            // Implement Google sign-in if needed.
        }
    }

    private fun userLogin() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        // Validate required fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase sign in
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show()

                    authNavigator?.navigateToHome() // Use the new navigation callback



                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDetach() {
        super.onDetach()
        tabSwitcher = null // Clear the callback reference
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}