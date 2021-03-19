package ac.hurley.codehub_kotlin.compose.state

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.state.TextFieldState
import com.blankj.utilcode.util.StringUtils
import java.util.regex.Pattern


class PasswordState : TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError)

/**
 * 判断密码是否规则有效的正则表达式
 */
private const val PASSWORD_VALIDATION_REGEX = "^[A-Za-z0-9]{6,}"

private fun isPasswordValid(password: String): Boolean {
    return Pattern.matches(PASSWORD_VALIDATION_REGEX, password)
}

/**
 * 密码无效的错误提示
 */
private fun passwordValidationError(password: String): String {
    return StringUtils.getString(R.string.invalid_password)
}