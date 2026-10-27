package com.task2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { LibraryApp() } }
    }
}

// Khung chính: thanh tab dưới + chuyển giữa 3 màn hình
@Composable
fun LibraryApp() {
    val lib = remember { Library() }          // 1 đối tượng Library dùng chung cho cả 3 tab
    var tab by remember { mutableStateOf(0) }

    Scaffold(bottomBar = {
        NavigationBar {
            listOf("Quản lý" to "🏠", "DS Sách" to "📖", "Sinh viên" to "👤")
                .forEachIndexed { i, (label, icon) ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = { Text(icon) },
                        label = { Text(label) }
                    )
                }
        }
    }) { padding ->
        Box(Modifier.padding(padding)) {
            when (tab) {
                0 -> ManageScreen(lib)
                1 -> BookListScreen(lib)
                else -> StudentListScreen(lib)
            }
        }
    }
}

// ===== Tab 1: Quản lý (đổi tên, mượn/trả sách) =====
@Composable
fun ManageScreen(lib: Library) {
    val student = lib.current
    var text by remember(student) { mutableStateOf(student.name) }   // đổi sinh viên thì ô nhập cập nhật theo

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Hệ thống\nQuản lý Thư viện", Modifier.fillMaxWidth(), fontSize = 20.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Text("Sinh viên", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(text, { text = it }, Modifier.weight(1f), singleLine = true)
            Spacer(Modifier.width(8.dp))
            Button(onClick = { student.rename(text) }) { Text("Thay đổi") }
        }
        Spacer(Modifier.height(16.dp))
        Text("Danh sách sách", fontWeight = FontWeight.Bold)
        LazyColumn(
            Modifier.weight(1f).fillMaxWidth()
                .background(Color(0xFFDDDDDD), RoundedCornerShape(12.dp)).padding(8.dp)
        ) {
            items(lib.books) { book ->
                val mine = student.hasBorrowed(book)
                Row(
                    Modifier.fillMaxWidth().padding(4.dp).background(Color.White, RoundedCornerShape(20.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = mine,
                        enabled = !book.isBorrowed || mine,        // sách người khác mượn thì khóa
                        onCheckedChange = { if (it) student.borrow(book) else student.giveBack(book) }
                    )
                    Text(book.title, Modifier.weight(1f))
                    if (book.isBorrowed && !mine)
                        Text("Đã mượn: ${book.borrowedBy?.name}", fontSize = 11.sp, color = Color.Red,
                            modifier = Modifier.padding(end = 12.dp))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { lib.addBook() }, Modifier.align(Alignment.CenterHorizontally)) { Text("Thêm") }
    }
}

// ===== Tab 2: Danh sách sách + trạng thái mượn =====
@Composable
fun BookListScreen(lib: Library) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Danh sách sách", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        items(lib.books) { book ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text(book.info(), fontWeight = FontWeight.Bold)      // đa hình: Book và EBook in khác nhau
                    if (book.isBorrowed)
                        Text("Đã mượn bởi ${book.borrowedBy?.name}", color = Color.Red)
                    else
                        Text("Chưa mượn", color = Color(0xFF2E7D32))
                }
            }
        }
    }
}

// ===== Tab 3: Danh sách sinh viên (bấm để chọn) =====
@Composable
fun StudentListScreen(lib: Library) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Danh sách sinh viên", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Bấm vào sinh viên để chọn người mượn sách", fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.weight(1f)) {
            items(lib.students) { s ->
                val selected = s == lib.current
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { lib.current = s },
                    colors = CardDefaults.cardColors(if (selected) Color(0xFFD6E4FF) else Color(0xFFF2F2F2))
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${s.name} (${s.code}) - ${s.role()}" + if (selected) "  ✔ đang chọn" else "",
                            fontWeight = FontWeight.Bold)
                        Text("Đã mượn: " + s.borrowed.joinToString { it.title }.ifEmpty { "chưa có" })
                    }
                }
            }
        }
        Button(onClick = { lib.addStudent() }, Modifier.align(Alignment.CenterHorizontally)) { Text("Thêm sinh viên") }
    }
}