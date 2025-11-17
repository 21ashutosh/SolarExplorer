package com.example.solarexplorer.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val phone = mutableStateOf("")

    private val _registerState = MutableStateFlow<UiState>(UiState.Idle)
    val registerState: StateFlow<UiState> get() = _registerState

    private val _loginState = MutableStateFlow<UiState>(UiState.Idle)
    val loginState: StateFlow<UiState> get() = _loginState

    val errorMessage = mutableStateOf("")
    val successMessage = mutableStateOf("")

    fun register() {

        val fName = firstName.value.trim()
        val lName = lastName.value.trim()
        val mail = email.value.trim()
        val ph = phone.value.trim()

        // --- VALIDATIONS (unchanged except password) ---
        if (fName.length < 3) {
            errorMessage.value = "First name must be at least 3 characters"
            return
        }

        if (lName.length < 3) {
            errorMessage.value = "Last name must be at least 3 characters"
            return
        }

        if (!mail.contains("@") || !mail.contains(".")) {
            errorMessage.value = "Enter a valid email"
            return
        }

        if (ph.length < 10) {
            errorMessage.value = "Enter a valid phone number"
            return
        }

        // --- STRONG PASSWORD VALIDATION (only change requested) ---
        val pass = password.value
        val cPass = confirmPassword.value

        val passwordRegex =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

        if (!passwordRegex.matches(pass)) {
            errorMessage.value =
                "Password must include uppercase, lowercase, number, special character & be 8+ characters."
            return
        }

        if (pass != cPass) {
            errorMessage.value = "Passwords do not match"
            return
        }

        // Clear previous messages
        errorMessage.value = ""
        successMessage.value = ""
        _registerState.value = UiState.Loading

        // --- Firebase Registration (unchanged) ---
        auth.createUserWithEmailAndPassword(mail, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: run {
                    errorMessage.value = "Registration failed"
                    _registerState.value = UiState.Error("Registration failed")
                    return@addOnSuccessListener
                }

                val userData = hashMapOf(
                    "uid" to uid,
                    "firstName" to fName,
                    "lastName" to lName,
                    "email" to mail,
                    "phone" to ph,
                    "createdAt" to System.currentTimeMillis()
                )

                db.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        successMessage.value = "Registration successfully Done"
                        _registerState.value = UiState.Success("Registration successfully Done")
                    }
                    .addOnFailureListener { exc ->
                        val msg = exc.message ?: "Failed to save user data"
                        errorMessage.value = msg
                        _registerState.value = UiState.Error(msg)
                    }
            }
            .addOnFailureListener { exc ->
                val msg = exc.message ?: "Registration failed"
                errorMessage.value = msg
                _registerState.value = UiState.Error(msg)
            }
    }

    fun login() {
        val mail = email.value.trim()
        val pass = password.value.trim()

        if (!mail.contains("@") || !mail.contains(".")) {
            errorMessage.value = "Enter a valid email"
            return
        }

        if (pass.isEmpty()) {
            errorMessage.value = "Password can't be empty"
            return
        }

        errorMessage.value = ""
        successMessage.value = ""
        _loginState.value = UiState.Loading

        auth.signInWithEmailAndPassword(mail, pass)
            .addOnSuccessListener {
                successMessage.value = "Login successful"
                _loginState.value = UiState.Success("Login successful")
            }
            .addOnFailureListener { exc ->
                val msg = exc.message ?: "Invalid user or password"
                errorMessage.value = msg
                _loginState.value = UiState.Error(msg)
            }
    }
}
