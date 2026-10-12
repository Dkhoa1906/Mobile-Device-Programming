package com.example.btvn1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Bước 1: class Student, email và phone có thể null (dấu ?)
class Student(val name: String, val email: String?, val phone: String?)

// Bước 2: danh sách 5 sinh viên, có người thiếu email/SĐT (null)
    val students = listOf(
    Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
    Student("Trần Thị Bình", null, "0912 888 999"),
    Student("Lê Minh Chi", "chi@sv.edu.vn", null),
    Student("Phạm Quốc Dũng", null, null),
    Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudentListScreen()
            }
        }
    }
}

// Một dòng thông tin: null thì hiện "Chưa cập nhật" (đỏ, nghiêng)
@Composable
fun Field(label: String, value: String?) {
    Row {
        Text("$label: ", fontSize = 13.sp, color = Color.DarkGray)
        Text(
            text = value ?: "Chưa cập nhật", // Bước 4: toán tử ?: (tương đương ?? của Dart)
            fontSize = 13.sp,
            color = if (value == null) Color.Red else Color.DarkGray,
            fontStyle = if (value == null) FontStyle.Italic else FontStyle.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen() {
    // Bước 5: dùng for để đếm sinh viên thiếu email
    var missingEmail = 0
    for (s in students) {
        if (s.email == null) missingEmail++
    }

    Scaffold(
        containerColor = Color(0xFFF2F4F8),
        topBar = {
            TopAppBar(
                title = { Text("Danh sách sinh viên") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C4A7C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            // Bước 3: dùng for để tạo thẻ cho từng sinh viên
            for (s in students) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(s.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Field("Email", s.email)
                        Spacer(Modifier.height(2.dp))
                        Field("SĐT", s.phone)
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            // Thanh tổng kết ở cuối danh sách
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8CAAEA), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text("Tổng: ")
                    Text("${students.size} sinh viên", fontWeight = FontWeight.Bold)
                }
                Row {
                    Text("Thiếu email: ")
                    Text("$missingEmail", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}