package com.example.ul_todo_android_app.utilities

import com.example.ul_todo_android_app.constants.UserInfo

class AuthUtilities {

    companion object {
        private fun isEmailMatched(
            email: String?, typeOfAuth: String?
        ): Boolean {
            var validEmail = false
            when (typeOfAuth) {
                "login" -> {
                    validEmail = email in UserInfo.LOGIN_USERS
                }
            }
            return validEmail
        }

        private fun isPasswordMatched(password: String?, typeOfAuth: String?): Boolean {
            var validPassword = false
            when (typeOfAuth) {
                "login" -> {
                    validPassword = password == UserInfo.LOGIN_PASSWORD
                }
            }
            return validPassword
        }

        private fun arePasswordAndConfirmPasswordMatched(
            password: String?,
            confirmPassword: String?
        ): Boolean {
            return password == confirmPassword
        }

        fun areLoginCredentialsMatched(email: String?, password: String?): Boolean {
            val typeOfAuth = "login"
            return isEmailMatched(email, typeOfAuth) && isPasswordMatched(password, typeOfAuth)
        }

        fun areRegisterCredentialsMatched(
            password: String?,
            confirmPassword: String?
        ): Boolean {
            return arePasswordAndConfirmPasswordMatched(
                password,
                confirmPassword
            )
        }
    }
}