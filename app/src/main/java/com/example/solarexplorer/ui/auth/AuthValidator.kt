package com.example.solarexplorer.ui.auth

object AuthValidator {

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.matches("^[A-Za-z ]{2,}$".toRegex())
    }

    fun isValidEmail(email: String): Boolean {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.matches("^[0-9]{10}$".toRegex())
    }

    fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.matches(".*[A-Z].*".toRegex()) &&
                password.matches(".*[a-z].*".toRegex()) &&
                password.matches(".*[0-9].*".toRegex()) &&
                password.matches(".*[@#\$%^&+=!].*".toRegex())
    }
}
