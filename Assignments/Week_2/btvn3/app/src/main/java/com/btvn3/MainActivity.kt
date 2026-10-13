package com.btvn3

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

// Bước 1: class Student, score có thể null (dấu ?)
class Student(val name: String, val score: Double?)

// Dữ liệu khai báo sẵn
val students = listOf(
    Student("Nguyễn Văn An", 9.0),
    Student("Trần Thị Bình", 7.2),
    Student("Lê Minh Chi", null),
    Student("Phạm Quốc Dũng", 4.5),
    Student("Võ Thanh Em", 5.8)
)

// Hàm xếp loại (dùng lại từ Bài 2)
fun xepLoai(d: Double): String {
    if (d < 0 || d > 10) return "Không hợp lệ"
    else if (d >= 8.0) return "Giỏi"
    else if (d >= 6.5) return "Khá"
    else if (d >= 5.0) return "Trung bình"
    else return "Yếu"
}

// Màu chữ theo xếp loại
fun mauChu(loai: String): Color {
    if (loai == "Giỏi") return Color(0xFF2E7D32)
    else if (loai == "Khá") return Color(0xFF1565C0)
    else if (loai == "Trung bình") return Color(0xFFEF6C00)
    else if (loai == "Yếu") return Color(0xFFC62828)
    else return Color.Gray
}

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
        setContent { MaterialTheme { BangDiemScreen() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangDiemScreen() {
    // Bước 3: dùng for + if để tính các số liệu
    var tong = 0.0        // tổng điểm
    var soCoDiem = 0      // số người có điểm
    var soGioi = 0        // số sinh viên Giỏi
    var soChuaCo = 0      // số sinh viên chưa có điểm
    var caoNhat = 0.0     // điểm cao nhất

    for (s in students) {
        val sc = s.score
        if (sc == null) {
            soChuaCo++                  // chưa có điểm: bỏ qua khi tính TB
        } else {
            tong += sc
            soCoDiem++
            if (xepLoai(sc) == "Giỏi") soGioi++
            if (sc > caoNhat) caoNhat = sc
        }
    }
    val trungBinh = if (soCoDiem > 0) tong / soCoDiem else 0.0

    Scaffold(
        containerColor = Color(0xFFF2F4F8),
        topBar = {                                   // AppBar
            TopAppBar(
                title = { Text("Bảng điểm lớp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C4A7C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Thẻ tổng kết màu xanh đậm
            Container(color = Color(0xFF2C4A7C), modifier = Modifier.fillMaxWidth()) {
                Text("Điểm trung bình lớp", fontSize = 13.sp, color = Color(0xFFB8C4DD))
                Text(
                    String.format("%.2f", trungBinh),   // làm tròn 2 chữ số: 6.63
                    fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
                SizedBox(12.dp)
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Container(color = Color.Transparent, padding = 0.dp) {
                        Text("Giỏi", fontSize = 12.sp, color = Color(0xFFB8C4DD))
                        Text("$soGioi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Container(color = Color.Transparent, padding = 0.dp) {
                        Text("Chưa có điểm", fontSize = 12.sp, color = Color(0xFFB8C4DD))
                        Text("$soChuaCo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Container(color = Color.Transparent, padding = 0.dp) {
                        Text("Cao nhất", fontSize = 12.sp, color = Color(0xFFB8C4DD))
                        Text("$caoNhat", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            SizedBox(16.dp)
            Text("DANH SÁCH · ${students.size} SINH VIÊN", fontSize = 12.sp, color = Color.Gray)
            SizedBox(8.dp)

            // Bước 2: dùng for hiển thị tên, điểm, xếp loại
            for (s in students) {
                val sc = s.score
                Container(color = Color.White, padding = 12.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Container(color = Color.Transparent, padding = 0.dp) {
                            Text(s.name, fontWeight = FontWeight.Bold)
                            if (sc == null) {
                                // score == null thì hiện "Chưa có điểm"
                                Text("Chưa có điểm", fontSize = 12.sp, color = Color.Gray)
                            } else {
                                val loai = xepLoai(sc)
                                Text(loai, fontSize = 12.sp, color = mauChu(loai))
                            }
                        }
                        if (sc == null) {
                            Text("—", fontSize = 20.sp, color = Color.Gray)
                        } else {
                            Text("$sc", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                SizedBox(8.dp)
            }
        }
    }
}