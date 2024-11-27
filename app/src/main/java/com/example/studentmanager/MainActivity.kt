package com.example.studentmanager

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf(
        Student("Le Thi Thao Van", "SV001"),
        Student("Le Minh Hung", "SV002"),
        Student("Phan Linh Nga", "SV003")
    )
    private lateinit var adapter: ArrayAdapter<Student>

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val fullName = result.data?.getStringExtra("fullName") ?: return@registerForActivityResult
            val studentId = result.data?.getStringExtra("studentId") ?: return@registerForActivityResult
            val originalId = result.data?.getStringExtra("originalId") ?: return@registerForActivityResult

            val updatedStudent = Student(fullName, studentId)

            // If the originalId exists, update that student; otherwise, it's a new student
            val existingIndex = students.indexOfFirst { it.studentId == originalId }
            if (existingIndex != -1) {
                // Update the existing student if the ID matches
                students[existingIndex] = updatedStudent
            } else {
                // Avoid duplicate entries and add the new student
                if (students.none { it.studentId == studentId }) {
                    students.add(updatedStudent)
                } else {
                    Toast.makeText(this, "A student with ID $studentId already exists.", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, students)
        listView.adapter = adapter
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        val student = students[info.position]

        when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, AddStudentActivity::class.java).apply {
                    putExtra("fullName", student.fullName)
                    putExtra("studentId", student.studentId)
                    putExtra("originalId", student.studentId) // Pass the original ID for reference
                }
                launcher.launch(intent)
                return true
            }
            R.id.action_remove -> {
                students.removeAt(info.position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "${student.fullName} removed.", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            val intent = Intent(this, AddStudentActivity::class.java)
            launcher.launch(intent) // Launch the AddStudentActivity for new student
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}