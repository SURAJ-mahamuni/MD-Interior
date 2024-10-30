package com.mdinterior.mdinterior.domain.validation

import com.mdinterior.mdinterior.R
import java.util.regex.Pattern

class ValidationManager {

    fun validateFields(vararg data: ValidationData): ValidationEvent {
        data.forEach {
            when (it.type) {
                ValidationType.EMAIL -> {
                    val appEvent = isValidEmailValid(it.value ?: "")
                    if (appEvent is ValidationEvent.ErrorMessage) return appEvent
                }

                ValidationType.PASSWORD -> {
                    val appEvent = isValidPassword(it.value ?: "")
                    if (appEvent is ValidationEvent.ErrorMessage) return appEvent
                }
            }
        }
        return ValidationEvent.Continue
    }

    private fun isValidEmailValid(email: String): ValidationEvent {
        if (!Pattern.compile("[a-zA-Z0-9+_.-]+@[a-zA-Z0-9]+[.-][a-zA-Z][a-z.A-Z]+").matcher(email)
                .matches()
        ) {
            return ValidationEvent.ErrorMessage(R.string.please_enter_valid_email_id)
        }
        return ValidationEvent.Continue
    }

    private fun isValidPassword(name: String): ValidationEvent {
        if (name.trim().isNullOrEmpty()) {
            return ValidationEvent.ErrorMessage(R.string.password_empty_error_msg)
        }
        else if (name.length <= 7) {
            return ValidationEvent.ErrorMessage(R.string.password_must_contain_8_char_msg)
        }
        return ValidationEvent.Continue
    }

}

sealed class ValidationEvent() {
    data object Continue : ValidationEvent()
    data class ErrorMessage(val message: Int) : ValidationEvent()
}



object ValidationConstants {
    const val CONTINUE = "Continue"

}