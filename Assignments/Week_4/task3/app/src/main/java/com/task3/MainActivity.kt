package com.task3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

val Teal = Color(0xFF2A8C8C)
val SkyBlue = Color(0xFF4A90E2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppNav() } }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val ctx = LocalContext.current
    val data = remember { ResetData() }      // 1 đối tượng dùng chung cho 4 màn hình

    NavHost(nav, startDestination = "email") {

        // ===== Màn 1: Nhập email =====
        composable("email") {
            // Quay về từ màn 4 sau khi Submit: hiện dữ liệu nhận được
            LaunchedEffect(data.submitted) {
                if (data.submitted) {
                    Toast.makeText(
                        ctx, "email: ${data.email}\notp: ${data.otp}\npass: ${data.password}",
                        Toast.LENGTH_LONG
                    ).show()
                    data.submitted = false
                }
            }
            AuthLayout(
                title = "Forget Password?",
                sub = "Enter your Email, we will send you a verification code.",
                btn = "Next", onBack = null,
                onNext = {
                    if (data.isEmailValid()) nav.navigate("otp")
                    else Toast.makeText(ctx, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                }
            ) {
                OutlinedTextField(
                    data.email, { data.email = it }, Modifier.fillMaxWidth(),
                    placeholder = { Text("Your Email") },
                    leadingIcon = { Text("✉") }, singleLine = true
                )
            }
        }

        // ===== Màn 2: Nhập mã OTP (5 ô) =====
        composable("otp") {
            val digits = remember { mutableStateListOf("", "", "", "", "") }
            AuthLayout(
                title = "Verify Code",
                sub = "Enter the code\nwe just sent you on your registered Email",
                btn = "Next", onBack = { nav.popBackStack() },
                onNext = {
                    data.otp = digits.joinToString("")
                    if (data.isOtpValid()) nav.navigate("password")
                    else Toast.makeText(ctx, "Nhập đủ 5 số", Toast.LENGTH_SHORT).show()
                }
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    digits.forEachIndexed { i, d ->
                        OutlinedTextField(
                            d, { if (it.length <= 1) digits[i] = it },
                            Modifier.weight(1f), singleLine = true,
                            textStyle = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                }
            }
        }

        // ===== Màn 3: Tạo mật khẩu mới =====
        composable("password") {
            var confirm by remember { mutableStateOf("") }
            AuthLayout(
                title = "Create new password",
                sub = "Your new password must be different form previously used password",
                btn = "Next", onBack = { nav.popBackStack() },
                onNext = {
                    if (data.password.isNotEmpty() && data.password == confirm) nav.navigate("confirm")
                    else Toast.makeText(ctx, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                }
            ) {
                OutlinedTextField(
                    data.password, { data.password = it }, Modifier.fillMaxWidth(),
                    placeholder = { Text("Password") }, leadingIcon = { Text("🔒") },
                    visualTransformation = PasswordVisualTransformation(), singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    confirm, { confirm = it }, Modifier.fillMaxWidth(),
                    placeholder = { Text("Confirm Password") }, leadingIcon = { Text("🔒") },
                    visualTransformation = PasswordVisualTransformation(), singleLine = true,
                    isError = confirm.isNotEmpty() && confirm != data.password
                )
            }
        }

        // ===== Màn 4: Xác nhận =====
        composable("confirm") {
            AuthLayout(
                title = "Confirm", sub = "We are here to help you!",
                btn = "Submit", onBack = { nav.popBackStack() },
                onNext = {
                    data.submitted = true
                    nav.navigate("email") { popUpTo("email") { inclusive = true } }   // quay về màn 1
                }
            ) {
                OutlinedTextField(
                    data.email, {}, Modifier.fillMaxWidth(), readOnly = true,
                    leadingIcon = { Text("👤") }, singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    data.otp, {}, Modifier.fillMaxWidth(), readOnly = true,
                    leadingIcon = { Text("✉") }, singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    data.maskedPassword(), {}, Modifier.fillMaxWidth(), readOnly = true,
                    leadingIcon = { Text("🔒") }, singleLine = true
                )
            }
        }
    }
}

// Khung giao diện dùng chung cho cả 4 màn hình (tránh viết lặp)
@Composable
fun AuthLayout(
    title: String, sub: String, btn: String,
    onBack: (() -> Unit)?, onNext: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth().height(48.dp)) {
            if (onBack != null) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(SkyBlue),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp)
                ) { Text("←", fontSize = 20.sp) }   // nút quay lại
            }
        }
        Text("UTH", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Teal)
        Text("SmartTasks", fontSize = 22.sp, color = SkyBlue)
        Spacer(Modifier.height(24.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(sub, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        content()
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onNext, Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(SkyBlue)
        ) { Text(btn) }
    }
}