package com.task3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// 1 đối tượng giữ toàn bộ dữ liệu, dùng chung cho cả 4 màn hình
class ResetData {
    var email by mutableStateOf("")
    var otp by mutableStateOf("")
    var password by mutableStateOf("")
    var submitted by mutableStateOf(false)   // đã bấm Submit ở màn 4 chưa

    // Ẩn mật khẩu thành dấu *
    fun maskedPassword() = "*".repeat(password.length)

    // Kiểm tra dữ liệu hợp lệ
    fun isEmailValid() = email.contains("@") && email.contains(".")
    fun isOtpValid() = otp.length == 5
}