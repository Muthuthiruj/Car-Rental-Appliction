package com.example.carrental.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carrental.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    // Callback reference for tab switching
    private var tabSwitcher: AuthTabSwitcher? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Get the callback from the parent fragment
        tabSwitcher = parentFragment as? AuthTabSwitcher
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

        // Set click listener on the "Register Now" text
        binding.register.setOnClickListener {
            // Use the callback to switch the parent's TabLayout to "Sign Up"
            tabSwitcher?.switchToTab(1)
            // Optionally, remove the NavController call if using tab switching exclusively:
            // findNavController().navigate(R.id.signUpFragment)
        }

        // Optional: Set click listener on "Forgot Password" text
        binding.forgotpassword.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot Password clicked", Toast.LENGTH_SHORT).show()
            // Navigate to Forgot Password screen if available.
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
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    activity?.finish()
                // Optional: finish the current activity
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
        tabSwitcher = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
