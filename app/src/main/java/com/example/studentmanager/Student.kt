package com.example.studentmanager

data class Student(val fullName: String, val studentId: String) {
    override fun toString(): String = "$fullName \nID: $studentId"
}