package com.task2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// ===== TRỪU TƯỢNG: lớp cha của mọi món đồ trong thư viện =====
abstract class Item(val id: Int, val title: String) {
    abstract fun info(): String            // lớp con bắt buộc tự viết
}

// ===== KẾ THỪA: Book kế thừa Item =====
open class Book(id: Int, title: String, val author: String) : Item(id, title) {

    // ĐÓNG GÓI: bên ngoài chỉ đọc được, muốn đổi phải gọi lend() / takeBack()
    var borrowedBy by mutableStateOf<Student?>(null)
        private set

    val isBorrowed get() = borrowedBy != null

    fun lend(s: Student): Boolean {          // cho mượn, thất bại nếu đã có người mượn
        if (isBorrowed) return false
        borrowedBy = s
        return true
    }

    fun takeBack() { borrowedBy = null }

    // ĐA HÌNH: ghi đè info()
    override fun info() = "$title - Tác giả: $author"
}

// EBook kế thừa Book và ghi đè info() theo cách riêng
class EBook(id: Int, title: String, author: String, val sizeMb: Double) : Book(id, title, author) {
    override fun info() = super.info() + " (EBook ${sizeMb}MB)"
}

// ===== TRỪU TƯỢNG: Person =====
abstract class Person(name: String) {
    // ĐÓNG GÓI: chỉ đổi tên qua rename()
    var name by mutableStateOf(name)
        private set

    fun rename(newName: String) { if (newName.isNotBlank()) name = newName }
    abstract fun role(): String
}

// KẾ THỪA: Student kế thừa Person
class Student(name: String, val code: String) : Person(name) {
    private val _borrowed = mutableStateListOf<Book>()           // ĐÓNG GÓI: danh sách private
    val borrowed: List<Book> get() = _borrowed                   // bên ngoài chỉ được xem

    fun borrow(b: Book): Boolean {
        if (b.lend(this)) { _borrowed.add(b); return true }
        return false
    }

    fun giveBack(b: Book) {
        if (b.borrowedBy == this) { b.takeBack(); _borrowed.remove(b) }
    }

    fun hasBorrowed(b: Book) = b in _borrowed

    override fun role() = "Sinh viên"                            // ĐA HÌNH
}

class Librarian(name: String) : Person(name) {
    override fun role() = "Thủ thư"
}

// Lớp quản lý chính
class Library {
    val books = mutableStateListOf<Book>(
        Book(1, "Sách 01", "Nguyễn A"),
        EBook(2, "Sách 02", "Trần B", 2.5),
        Book(3, "Sách 03", "Lê C")
    )
    val students = mutableStateListOf(
        Student("Nguyen Van A", "SV01"),
        Student("Tran Thi B", "SV02")
    )
    var current by mutableStateOf(students[0])      // sinh viên đang được chọn

    fun addBook() {
        val n = books.size + 1
        books.add(Book(n, "Sách %02d".format(n), "Tác giả $n"))
    }

    fun addStudent() {
        val n = students.size + 1
        students.add(Student("Sinh viên $n", "SV%02d".format(n)))
    }
}