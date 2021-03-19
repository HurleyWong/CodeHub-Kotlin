package ac.hurley.codehub_kotlin.compose.login

import ac.hurley.codehub_kotlin.R
import com.blankj.utilcode.util.StringUtils
import java.util.regex.Pattern

/**
 * 判断邮箱是否有效的正则表达式
 */
private const val EMAIL_VALIDATION_REGEX = "^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}\$"

/**
 * 判断手机号是否有效的正则表达式
 */
private const val TEL_VALIDATION_REGEX = "^[1]([3-9])[0-9]{9}\$"

class AccountState : TextFieldState(validator = ::isAccountValid, errorFor = ::accountValidationError)

/**
 * 判断账号是否有效
 */
private fun isAccountValid(account: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, account) || Pattern.matches(TEL_VALIDATION_REGEX, account)
}

/**
 * 账号格式无效的错误提示
 */
private fun accountValidationError(account: String): String {
    return if (account.isEmpty()) {
        StringUtils.getString(R.string.invalid_account)
    } else {
        "${StringUtils.getString(R.string.invalid_account)}: $account"
    }
}