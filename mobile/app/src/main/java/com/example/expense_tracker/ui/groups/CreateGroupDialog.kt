package com.example.expense_tracker.ui.groups

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.expense_tracker.GroupManagementActivity
import com.example.expense_tracker.R
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.network.requests.CreateGroupRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CreateGroupDialog(
    private val activity: AppCompatActivity,
    private val groupRepository: GroupRepository
) : Dialog(activity) {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var participantsEditText: EditText
    private lateinit var createButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_group)

        nameEditText = findViewById(R.id.editTextGroupName)
        descriptionEditText = findViewById(R.id.editTextGroupDescription)
        participantsEditText = findViewById(R.id.editTextParticipantEmails)
        createButton = findViewById(R.id.buttonCreate)
        cancelButton = findViewById(R.id.buttonCancel)

        createButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val participantEmailsInput = participantsEditText.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(activity, "Group name is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val participantEmails = if (participantEmailsInput.isNotEmpty()) {
                participantEmailsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            } else {
                emptyList()
            }

            createGroup(name, if (description.isEmpty()) null else description, if (participantEmails.isEmpty()) null else participantEmails)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun createGroup(name: String, description: String?, participantEmails: List<String>?) {
        activity.lifecycleScope.launch {
            try {
                val response = groupRepository.createGroup(CreateGroupRequest(name, description, participantEmails))
                Toast.makeText(activity, "Group '${response.name}' created successfully.", Toast.LENGTH_SHORT).show()
                if (activity is GroupManagementActivity) {
                    activity.refreshGroups()
                }
                dismiss()
            } catch (e: IOException) {
                Toast.makeText(activity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Toast.makeText(activity, "Failed to create group. Please check your inputs.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(activity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
