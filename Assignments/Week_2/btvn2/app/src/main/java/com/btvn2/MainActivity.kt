package com.btvn2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Bước 1: biến điểm
val diem = 8.5

// Bước 2: hàm nhận điểm, trả về xếp loại
fun xepLoai(d: Double): String {
    if (d < 0 || d > 10) return "Không hợp lệ"  // kiểm tra trước
    else if (d >= 8.0) return "Giỏi"
    else if (d >= 6.5) return "Khá"
    else if (d >= 5.0) return "Trung bình"
    else return "Yếu"
}

// Bước 3: chọn màu chữ bằng if
fun mauChu(loai: String): Color {
    if (loai == "Giỏi") return Color(0xFF2E7D32)
    else if (loai == "Khá") return Color(0xFF1565C0)
    else if (loai == "Trung bình") return Color(0xFFEF6C00)
    else if (loai == "Yếu") return Color(0xFFC62828)
    else return Color.Gray
}

// Hiện 9.0 thành 9
fun hienDiem(d: Double): String =
    if (d % 1.0 == 0.0) d.toInt().toString() else d.toString()

@Composable
fun Container(
    color: Color,
    radius: Dp = 12.dp,
    padding: Dp = 16.dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(color, RoundedCornerShape(radius))
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun SizedBox(height: Dp) {
    Spacer(Modifier.height(height))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { XepLoaiScreen() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XepLoaiScreen() {
    Scaffold(
        containerColor = Color(0xFFF2F4F8),
        topBar = {                                   // AppBar
            TopAppBar(
                title = { Text("Xếp loại học lực") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C4A7C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Thẻ điểm chính
            val loai = xepLoai(diem)
            val mau = mauChu(loai)
            Container(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Text("Điểm trung bình", fontSize = 13.sp, color = Color.Gray)
                Text(hienDiem(diem), fontSize = 48.sp, fontWeight = FontWeight.Bold)
                SizedBox(8.dp)
                Container(
                    color = mau.copy(alpha = 0.15f),
                    radius = 8.dp,
                    padding = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(loai, color = mau, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            SizedBox(20.dp)
            Text("KIỂM TRA THÊM", fontSize = 12.sp, color = Color.Gray)
            SizedBox(8.dp)

            // Bước 4: gọi hàm với điểm 9, 7, 5.5, 3, 11
            Container(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                for (d in listOf(9.0, 7.0, 5.5, 3.0, 11.0)) {
                    val l = xepLoai(d)
                    val m = mauChu(l)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Điểm ${hienDiem(d)}")
                        Container(color = m.copy(alpha = 0.15f), radius = 6.dp, padding = 6.dp) {
                            Text(l, color = m, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    SizedBox(10.dp)
                }
            }
        }
    }
}