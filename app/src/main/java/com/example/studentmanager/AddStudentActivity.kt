package com.example.studentmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val fullNameEditText = findViewById<EditText>(R.id.edit_hoten)
        val studentIdEditText = findViewById<EditText>(R.id.edit_mssv)
        val saveButton = findViewById<Button>(R.id.button_ok)

        // Get the data passed from MainActivity for editing
        val originalId = intent.getStringExtra("studentId") // The original student ID
        if (originalId != null) {
            fullNameEditText.setText(intent.getStringExtra("fullName"))
            studentIdEditText.setText(intent.getStringExtra("studentId"))
        }

        saveButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val studentId = studentIdEditText.text.toString()

            if (fullName.isNotEmpty() && studentId.isNotEmpty()) {
                val intent = intent.apply {
                    putExtra("fullName", fullName)
                    putExtra("studentId", studentId)
                    putExtra("originalId", originalId ?: studentId) // If editing, pass the original ID
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter both name and student ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}