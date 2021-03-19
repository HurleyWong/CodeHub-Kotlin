package ac.hurley.codehub_kotlin.compose.login

import ac.hurley.codehub_kotlin.R
import com.blankj.utilcode.util.StringUtils
import java.util.regex.Pattern

class EmailState : TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

/**
 * 判断邮箱是否有效的正则表达式
 */
private const val EMAIL_VALIDATION_REGEX = "^[A-Za-z0-9]{6,}"

/**
 * 判断邮箱是否有限
 */
private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}

/**
 * 邮箱无效的错误提示
 */
private fun emailValidationError(email: String): String {
    return if (email.isEmpty()) {
        StringUtils.getString(R.string.invalid_email)
    } else {
        "${StringUtils.getString(R.string.invalid_email)}: $email"
    }
}