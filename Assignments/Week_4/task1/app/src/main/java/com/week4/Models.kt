package com.week4

// Mỗi trang onboarding là 1 đối tượng
data class OnboardingPage(val title: String, val desc: String, val icon: String)

// Class quản lý dữ liệu, UI chỉ việc gọi getPages()
class OnboardingRepository {
    fun getPages() = listOf(
        OnboardingPage("Mọi việc, gọn trong một chỗ", "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.", "✅"),
        OnboardingPage("Chia theo Học tập và Cuộc sống", "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.", "📚"),
        OnboardingPage("Nhắc đúng lúc", "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.", "🔔")
    )
}