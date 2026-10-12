package com.example.thuchanh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorScreen()
            }
        }
    }
}

fun calculate(aText: String, bText: String, op: String): String {
    val a = aText.trim().toDoubleOrNull()
    val b = bText.trim().toDoubleOrNull()

    if (a == null || b == null) return "Vui lòng nhập số hợp lệ"

    val res = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> {
            if (b == 0.0) return "Không thể chia cho 0"
            a / b
        }
        else -> return ""
    }

    // Bỏ phần thập phân thừa
    return if (res % 1.0 == 0.0) res.toLong().toString()
    else String.format("%.4f", res).trimEnd('0').trimEnd('.')
}

@Composable
fun CalculatorScreen() {
    // State
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var selectedOp by remember { mutableStateOf("") }

    // Kết quả tự tính lại mỗi khi num1, num2 hoặc phép toán thay đổi
    val result = if (selectedOp.isEmpty()) "" else calculate(num1, num2, selectedOp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(100.dp))

        Text(
            text = "Thực hành",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(Modifier.height(80.dp))

        NumberField(value = num1, onValueChange = { num1 = it })

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OpButton("+", Color(0xFFE04040), selectedOp == "+") { selectedOp = "+" }
            OpButton("-", Color(0xFFDDA640), selectedOp == "-") { selectedOp = "-" }
            OpButton("*", Color(0xFF5B3CDD), selectedOp == "*") { selectedOp = "*" }
            OpButton("/", Color(0xFF000000), selectedOp == "/") { selectedOp = "/" }
        }

        Spacer(Modifier.height(24.dp))

        NumberField(value = num2, onValueChange = { num2 = it })

        Spacer(Modifier.height(16.dp))

        Text(text = "Kết quả: $result", fontSize = 16.sp)
    }
}

@Composable
fun NumberField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun OpButton(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        border = if (selected) BorderStroke(3.dp, Color.Black) else null
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}