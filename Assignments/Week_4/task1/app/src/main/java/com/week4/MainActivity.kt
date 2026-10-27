package com.week4   // <-- giữ nguyên dòng package gốc của bạn

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay

val Blue = Color(0xFF3B5BE0)

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
    val pages = remember { OnboardingRepository().getPages() }   // dữ liệu lấy từ class OOP
    val finish = { Toast.makeText(ctx, "Bắt đầu!", Toast.LENGTH_SHORT).show() }

    NavHost(nav, startDestination = "splash") {
        composable("splash") {
            SplashScreen { nav.navigate("onboarding/0") { popUpTo("splash") { inclusive = true } } }
        }
        composable("onboarding/{index}", arguments = listOf(navArgument("index") { type = NavType.IntType })) { entry ->
            val i = entry.arguments!!.getInt("index")
            OnboardingScreen(
                page = pages[i], index = i, total = pages.size,
                onNext = { if (i < pages.size - 1) nav.navigate("onboarding/${i + 1}") else finish() },
                onSkip = finish
            )
        }
    }
}

@Composable
fun SplashScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) { delay(2000); onDone() }   // chờ 2 giây rồi chuyển màn
    Column(
        Modifier.fillMaxSize().background(Blue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("☑", color = Color.White, fontSize = 64.sp)
        Text("UTH Todo", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Việc học, việc nhà – gọn gàng mỗi ngày", color = Color.White, fontSize = 13.sp)
        Spacer(Modifier.height(120.dp))
        Text("Phiên bản 2.1.0", color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun OnboardingScreen(page: OnboardingPage, index: Int, total: Int, onNext: () -> Unit, onSkip: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("UTH Todo", color = Blue, fontWeight = FontWeight.Bold)
            Text("Bỏ qua", Modifier.clickable { onSkip() }, color = Color.Gray)
        }
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(page.icon, fontSize = 100.sp)
        }
        Text(page.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(page.desc, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Row {   // 3 chấm báo trang hiện tại
            repeat(total) {
                Box(
                    Modifier.padding(end = 6.dp).size(if (it == index) 24.dp else 8.dp, 8.dp)
                        .background(if (it == index) Blue else Color.LightGray, CircleShape)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onNext, Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(Blue)) {
            Text(if (index == total - 1) "Bắt đầu ngay" else "Tiếp tục")
        }
    }
}