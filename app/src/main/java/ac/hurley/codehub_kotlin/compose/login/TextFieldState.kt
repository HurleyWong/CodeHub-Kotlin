package ac.hurley.codehub_kotlin.compose.login

import androidx.compose.runtime.*

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorFor: (String) -> String = { "" }
) {
    var text: String by mutableStateOf("")
    var isFocusedDirty: Boolean by mutableStateOf(false)
    var isFocused: Boolean by mutableStateOf(false)
    private var showErrors: Boolean by mutableStateOf(false)

    open val isValid: Boolean
        get() = validator(text)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun enableShowErrors() {
        if (isFocusedDirty) {
            showErrors = true
        }
    }

    fun showErrors() = !isValid && showErrors

    open fun getError(): String? {
        return if (showErrors) {
            errorFor(text)
        } else {
            null
        }
    }
}