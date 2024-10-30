package com.mdinterior.mdinterior.domain.validation

data class ValidationData(var value: String? = null, var type: ValidationType)

enum class ValidationType {
    EMAIL, PASSWORD
}
