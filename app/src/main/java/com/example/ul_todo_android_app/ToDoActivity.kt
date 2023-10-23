package com.example.ul_todo_android_app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ul_todo_android_app.adapters.*
import com.example.ul_todo_android_app.constants.SessionStoreTexts
import com.example.ul_todo_android_app.constants.StaticTexts
import com.example.ul_todo_android_app.database.crud.TasksCRUDRepo
import com.example.ul_todo_android_app.database.crud.UsersCRUDRepo
import com.example.ul_todo_android_app.database.entities.TasksEntity
import com.example.ul_todo_android_app.utilities.CustomToastUtility
import com.example.ul_todo_android_app.utilities.NotificationUtil
import com.example.ul_todo_android_app.utilities.SessionData
import com.example.ul_todo_android_app.utilities.ViewUtilities
import com.example.ul_todo_android_app.utilities.ViewUtilities.Companion.hideKeyboard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.util.*

data class FilterCriteria(
    val name: String,
    var isSelected: Boolean = false
)

class ToDoActivity : AppCompatActivity(), OnClickListener, OnFocusChangeListener,
    TasksAdapter.OnClickListener, TagsAdapter.OnClickListener,
    TasksAdapter.OnItemLongClickListener {

    // common variable declaration
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tagsRecyclerView: RecyclerView
    private lateinit var dialog: Dialog

    // locator variable declaration
    private lateinit var btnAddTodoFloating: FloatingActionButton
    private lateinit var etvDialog: EditText
    private lateinit var imgCloseDialog: ImageView
    private lateinit var txtErrorDialog: TextView
    private lateinit var eleDrawable: DrawerLayout
    private lateinit var eleNavView: NavigationView
    private lateinit var eleToolBar: Toolbar
    private lateinit var drawableToggle: ActionBarDrawerToggle
    private lateinit var txtTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var updatePassword: EditText
    private lateinit var updatePasswordError: TextView
    private lateinit var imgCloseUpdatePassword: ImageView
    private lateinit var updateButton: Button
    private lateinit var filterButton: Button
    private lateinit var filterOutButton: Button
    private lateinit var clearFiltersButton: Button

    // dynamic cache variables
    private var txtUserInfo: TextView? = null
    private var txtSelectDate: TextView? = null
    private var txtSetDate: EditText? = null
    private var etvNewTask: EditText? = null
    private var etvDescription: EditText? = null
    private var imgCloseNewTask: ImageView? = null
    private var imgCloseDescription: ImageView? = null
    private var txtErrorNewTask: TextView? = null
    private var txtErrorDescription: TextView? = null
    private var txtErrorDate: TextView? = null
    private var btnAddTask: Button? = null
    private var btnAddTag: LinearLayout? = null
    private var txtTitleOnBottomSheet: TextView? = null
    private var yetToDoCheckbox: CheckBox? = null
    private var inProgressCheckbox: CheckBox? = null
    private var doneCheckbox: CheckBox? = null

    // session storage session
    private var todoSession: SessionData? = null

    // dynamic variables
    var tags: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.setHasFixedSize(true)

        // locator assignment
        eleToolBar = findViewById(R.id.eleToolBar)
        btnAddTodoFloating = findViewById(R.id.btnAddTodoFloating)
        eleDrawable = findViewById(R.id.eleDrawable)
        eleNavView = findViewById(R.id.eleNavView)
        txtTitle = findViewById(R.id.txtTitle)
        progressBar = findViewById(R.id.progressBar)
        filterButton = findViewById(R.id.btnFilter)
        clearFiltersButton = findViewById(R.id.btnClearFilters)

        // Session storage assignments
        todoSession = SessionData(applicationContext)

        // ++++++++++++++++++++++++++++++++
        // OVERRIDDEN ACTION LISTENERS CALL
        // ++++++++++++++++++++++++++++++++

        // 1. click listeners
        btnAddTodoFloating?.setOnClickListener(this)
        filterButton?.setOnClickListener {
            showSideSheet()
        }

        // ++++++++++++++++++++++++++
        // CUSTOM ACTION METHOD CALLS
        // ++++++++++++++++++++++++++
        shouldTitleBeDisplayed()
        drawableComponent()
        loadTasksOnRecyclerView()
    }

    private fun shouldTitleBeDisplayed() {
        TasksCRUDRepo(applicationContext).getTasksForUser(
            SessionData(applicationContext).getFromSession(
                SessionStoreTexts.LOGGED_IN_EMAIL
            )
        ) {
            if (it[0].tasks.isEmpty()) {
                txtTitle?.visibility = View.VISIBLE
                filterButton?.visibility = View.GONE
                txtTitle?.text = "What's on your To-Do list?"
            } else {
                txtTitle?.visibility = View.GONE
                filterButton.visibility = View.VISIBLE
            }
        }
    }

    private fun drawableComponent() {
        setSupportActionBar(eleToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Your To Do List"
        drawableToggle = ActionBarDrawerToggle(
            this, eleDrawable, R.string.drawerOpen, R.string.drawerClose
        )
        eleDrawable.addDrawerListener(drawableToggle)
        drawableToggle.syncState()

        eleToolBar.navigationContentDescription = "Open Drawer Navigation Menu"

        eleToolBar?.setNavigationOnClickListener {
            eleDrawable.openDrawer(GravityCompat.START)
        }

        eleNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnLogout -> {
                    logoutDialogBox()
                }

                R.id.btnDelete -> {
                    deleteAccountDialogBox()
                }

                R.id.btnUpdateUserInfo -> {
                    updateDialogBottomSheet()
                }

                R.id.btnWebview -> {
                    navigateToSearchOnlineActivity()
                }

                R.id.btnGuide -> {
                    navigateToGoodAndBadHabitTabActivity()
                }

                else -> {
                }
            }
            eleDrawable.closeDrawer(GravityCompat.START)
            true
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showSideSheet() {
        val sideSheetDialog = SideSheetDialog(this)

        val filterCriteriaList = listOf(
            FilterCriteria("YET_TO_DO", false),
            FilterCriteria("IN_PROGRESS", false),
            FilterCriteria("DONE", false)
        )

        sideSheetDialog.behavior.addCallback(object : SideSheetCallback() {
            override fun onStateChanged(sheet: View, newState: Int) {
                if (newState == SideSheetBehavior.STATE_DRAGGING) {
                    sideSheetDialog.behavior.state = SideSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(sheet: View, slideOffset: Float) {
            }
        })

        val inflater = layoutInflater.inflate(R.layout.activity_side_sheet_filter, null)
        val btnClose = inflater.findViewById<ImageButton>(R.id.btnClose)

        yetToDoCheckbox = inflater.findViewById(R.id.chkYetToDo)
        inProgressCheckbox = inflater.findViewById(R.id.chkInProgress)
        doneCheckbox = inflater.findViewById(R.id.chkDone)
        filterOutButton = inflater.findViewById(R.id.btnFilterOut)

        // Create a single OnClickListener for all checkboxes
        val checkBoxClickListener = View.OnClickListener { view ->
            if (view is CheckBox) {
                val value = view.text.toString()
                val filterCriteria =
                    filterCriteriaList.find { it.name == value.uppercase().replace(" ", "_") }
                filterCriteria?.isSelected = view.isChecked
            }
        }

        // Set the same OnClickListener for all checkboxes
        yetToDoCheckbox?.setOnClickListener(checkBoxClickListener)
        inProgressCheckbox?.setOnClickListener(checkBoxClickListener)
        doneCheckbox?.setOnClickListener(checkBoxClickListener)

        btnClose.setOnClickListener {
            sideSheetDialog.dismiss()
        }

        filterOutButton?.setOnClickListener {
            var filteredTasks: List<TasksEntity>?
            TasksCRUDRepo(applicationContext).getTasksForUser(
                SessionData(applicationContext).getFromSession(
                    SessionStoreTexts.LOGGED_IN_EMAIL
                )
            ) {
                filteredTasks = it[0].tasks.filter { task ->
                    filterCriteriaList.any { criteria ->
                        when {
                            criteria.name == "YET_TO_DO" -> criteria.isSelected && !task.done && !task.inProgress
                            criteria.name == "IN_PROGRESS" -> criteria.isSelected && task.inProgress && !task.done
                            criteria.name == "DONE" -> criteria.isSelected && task.done
                            else -> false // Handle invalid criteria
                        }
                    }
                }
                val tasksAdapter = TasksAdapter(filteredTasks!!, applicationContext, this, this)
                tasksRecyclerView.adapter = tasksAdapter
                val dragAndDropCallback = DragAndDropTasksCallback(tasksAdapter)
                val itemTouchHelper = ItemTouchHelper(dragAndDropCallback)
                itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

                if (filteredTasks!!.isEmpty()) {
                    txtTitle?.visibility = View.VISIBLE
                    txtTitle?.text = "No matching tasks found"
                } else {
                    txtTitle?.visibility = View.GONE
                }
                clearFiltersButton?.visibility = View.VISIBLE
                sideSheetDialog.dismiss()
            }
        }

        clearFiltersButton.setOnClickListener {
            loadTasksOnRecyclerView()
            clearFiltersButton?.visibility = View.GONE
        }

        sideSheetDialog.setCancelable(false)
        sideSheetDialog.setCanceledOnTouchOutside(true)
        sideSheetDialog.setContentView(inflater)
        sideSheetDialog.show()
    }

    private fun loadTasksOnRecyclerView() {
        TasksCRUDRepo(applicationContext).getTasksForUser(
            SessionData(applicationContext).getFromSession(
                SessionStoreTexts.LOGGED_IN_EMAIL
            )
        ) {
            val tasksAdapter = TasksAdapter(it[0].tasks, applicationContext, this, this)
            tasksRecyclerView.adapter = tasksAdapter

            // Create an instance of the custom DragAndDropCallback and attach it to the RecyclerView
            val dragAndDropCallback = DragAndDropTasksCallback(tasksAdapter)
            val itemTouchHelper = ItemTouchHelper(dragAndDropCallback)
            itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
        }
        shouldTitleBeDisplayed()
    }

    private fun loadTagDataOnRecyclerView(tags: List<String> = listOf()) {
        if (tags.isEmpty()) {
            tagsRecyclerView.visibility = View.VISIBLE
            tagsRecyclerView.adapter = TagsAdapter(tags, applicationContext, this)
            return
        } else {
            if (tags.isNotEmpty()) {
                tagsRecyclerView.visibility = View.VISIBLE
                tagsRecyclerView.adapter = tags?.let { TagsAdapter(it, applicationContext, this) }
            } else {
                tagsRecyclerView.visibility = View.GONE
            }
            return
        }
    }

    private fun logoutDialogBox() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_login_quit_dialog)

        // locator defining
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtQuitMsg: TextView = dialog.findViewById(R.id.txtQuitMsg)

        txtQuitMsg.text = StaticTexts.DELETE_DIALOG_MESSAGE
        btnYes.text = "Logout"
        btnNo.text = "Cancel"

        btnYes.setOnClickListener {
            SessionData(applicationContext).saveToSession(SessionStoreTexts.LOGGED_IN, "false")
            dialog.dismiss()

            ViewUtilities.simulateProgressBar(progressBar)

            val intent = Intent(applicationContext, MainActivity::class.java)
            activityResultLauncher.launch(intent)

            CustomToastUtility(this).showToast(
                R.layout.activity_custom_toast,
                "Logged out"
            )
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun deleteAccountDialogBox() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_login_quit_dialog)

        // locator defining
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtQuitMsg: TextView = dialog.findViewById(R.id.txtQuitMsg)

        txtQuitMsg.text = StaticTexts.DELETE_ACCOUNT_MESSAGE
        btnYes.text = "Delete"
        btnNo.text = "Cancel"

        btnYes.setOnClickListener {
            SessionData(applicationContext).saveToSession(SessionStoreTexts.LOGGED_IN, "false")
            dialog.dismiss()

            // Deleting the user from DB
            UsersCRUDRepo(applicationContext).deleteUserByEmail(
                SessionData(
                    applicationContext
                ).getFromSession(SessionStoreTexts.LOGGED_IN_EMAIL)
            )

            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("targetActivityClass", MainActivity::class.java)
            intent.putExtra("dynamicMessage", "Account is permanently deleted")
            activityResultLauncher.launch(intent)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun taskAdditionOrUpdateBottomSheet(
        update: Boolean = false, taskEntity: TasksEntity? = null
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_add_task_bottom_sheet)

        txtSelectDate = dialog.findViewById(R.id.txtSelectDate)
        txtSetDate = dialog.findViewById(R.id.etvSetDate)
        etvNewTask = dialog.findViewById(R.id.etvNewTask)
        etvDescription = dialog.findViewById(R.id.etvDescription)
        imgCloseNewTask = dialog.findViewById(R.id.imgCloseNewTask)
        imgCloseDescription = dialog.findViewById(R.id.imgCloseDescription)
        txtErrorNewTask = dialog.findViewById(R.id.txtErrorNewTask)
        txtErrorDescription = dialog.findViewById(R.id.txtErrorDescription)
        txtErrorDate = dialog.findViewById(R.id.txtErrorDate)
        btnAddTask = dialog.findViewById(R.id.btnAddTask)
        btnAddTag = dialog.findViewById(R.id.btnAddTag)
        txtTitleOnBottomSheet = dialog.findViewById(R.id.txtTitleOnBottomSheet)

        tagsRecyclerView = dialog.findViewById(R.id.tagRecyclerView)
        tagsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        tagsRecyclerView.setHasFixedSize(true)

        if (update) {
            if (taskEntity != null) {
                TasksCRUDRepo(applicationContext).getTask(
                    SessionData(applicationContext).getFromSession(
                        SessionStoreTexts.LOGGED_IN_EMAIL
                    ), taskEntity.task
                ) { taskEntity ->
                    txtTitleOnBottomSheet?.text = "Update the task"
                    etvNewTask?.setText(taskEntity.task)
                    etvDescription?.setText(taskEntity.description)
                    txtSetDate?.setText(taskEntity.completionDate)
                    tags = taskEntity.tags as MutableList<String>

                    btnAddTask?.text = "Update"

                    loadTagDataOnRecyclerView(tags)
                }
            }
        }

        txtSelectDate?.setOnClickListener(this)
        imgCloseNewTask?.setOnClickListener(this)
        imgCloseDescription?.setOnClickListener(this)
        btnAddTask?.setOnClickListener(this)
        btnAddTag?.setOnClickListener(this)

        etvNewTask?.onFocusChangeListener = this
        etvNewTask?.let { viewElement ->
            txtErrorNewTask?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        etvDescription?.onFocusChangeListener = this
        etvDescription?.let { viewElement ->
            txtErrorDescription?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        txtSetDate?.onFocusChangeListener = this
        txtSetDate?.let { viewElement ->
            txtErrorDate?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        btnAddTask?.setOnClickListener {
            if (checkEmptyTaskDescriptionAndDate()) {
                val task = etvNewTask?.text.toString()
                val description = etvDescription?.text.toString()
                val completionDate = txtSetDate?.text.toString()
                val tags = tags
                val email = SessionData(applicationContext).getFromSession(
                    SessionStoreTexts.LOGGED_IN_EMAIL
                )

                if (!update) {
                    TasksCRUDRepo(applicationContext).insertTask(
                        TasksEntity(
                            task = task,
                            description = description,
                            completionDate = completionDate,
                            tags = tags,
                            userRefEmail = email,
                            inProgress = false,
                            done = false
                        )
                    )
                    dialog.dismiss()
                    CustomToastUtility(this).showToast(
                        R.layout.activity_custom_toast,
                        "Task successfully added"
                    )
                } else {

                    Log.i("inProgress", taskEntity.toString())
                    var updatedTask: TasksEntity? = null
                    if (taskEntity != null) {
                        updatedTask = TasksEntity(
                            task = task,
                            description = description,
                            completionDate = completionDate,
                            tags = tags,
                            userRefEmail = email,
                            inProgress = taskEntity.inProgress,
                            done = taskEntity.done
                        )
                        TasksCRUDRepo(applicationContext).updateTaskByEmail(
                            updatedTask, taskEntity?.taskId!!
                        )
                    }

                    this.tags = mutableListOf() // empty the list once the task is added to the DB
                    Log.i("tags", tags.toString())

                    dialog.dismiss()
                    CustomToastUtility(this).showToast(
                        R.layout.activity_custom_toast,
                        "Task updated successfully"
                    )
                }

                loadTasksOnRecyclerView()
            }
        }

        dialog.show()
        dialog.window?.setLayout(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.bottomSheetAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun checkEmptyTaskDescriptionAndDate(): Boolean {
        val taskValue: String = etvNewTask?.text.toString()
        val descriptionValue: String = etvDescription?.text.toString()
        val dateValue: String = txtSetDate?.text.toString()

        val fields = arrayOf(
            Pair(taskValue, txtErrorNewTask),
            Pair(descriptionValue, txtErrorDescription),
            Pair(dateValue, txtErrorDate)
        )

        fields.forEach { (textValue, errorView) ->
            errorView?.let { view ->
                if (textValue.isEmpty()) {
                    view.text = when (view.id) {
                        R.id.txtErrorNewTask -> "Task field cannot be empty"
                        R.id.txtErrorDescription -> "Description field cannot be empty"
                        else -> "Date field cannot be empty"
                    }
                    view.visibility = View.VISIBLE
                }
            }
        }

        return fields.all { it.first != null && it.first!!.isNotEmpty() }
    }

    private fun updateDialogBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_update_bottom_sheet)

        // locator defining
        updatePassword = dialog.findViewById(R.id.etvUpdatePassword)
        updatePasswordError = dialog.findViewById(R.id.txtErrorUpdatePassword)
        imgCloseUpdatePassword = dialog.findViewById(R.id.imgCloseUpdatePassword)

        updateButton = dialog.findViewById(R.id.btnUpdateOnBottomSheet)

        imgCloseUpdatePassword.setOnClickListener(this)
        updatePassword.onFocusChangeListener = this

        updatePassword?.let { viewElement ->
            updatePasswordError?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        updateButton.setOnClickListener {
            imgCloseUpdatePassword.hideKeyboard()

            if (updatePassword.text.isEmpty()) {
                updatePasswordError.text = StaticTexts.UPDATE_PASSWORD_ERROR_MESSAGE
                updatePasswordError.visibility = View.VISIBLE
            } else {

                // updating the password
                UsersCRUDRepo(applicationContext).updatePassword(
                    SessionData(applicationContext).getFromSession(SessionStoreTexts.LOGGED_IN_EMAIL),
                    updatePassword.text.toString()
                )


                CustomToastUtility(this).showToast(
                    R.layout.activity_custom_toast,
                    "Successfully updated the password"
                )
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.window?.setLayout(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.bottomSheetAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun navigateToSearchOnlineActivity() {
        val intent = Intent(applicationContext, SearchOnlineActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private fun navigateToGoodAndBadHabitTabActivity() {
        val intent = Intent(applicationContext, GuideActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private fun tagAdditionDialogBox() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_to_do_dialog)

        // locator defining
        val btnDialog: Button = dialog.findViewById(R.id.btnDialog)
        btnDialog.text = "Add"
        txtErrorDialog = dialog.findViewById(R.id.txtErrorDialog)
        imgCloseDialog = dialog.findViewById(R.id.imgCloseDialog)

        etvDialog = dialog.findViewById(R.id.etvDialog)

        etvDialog.hint = "Tag"
        btnDialog.text = "Add tag"

        // onclick listeners
        imgCloseDialog.setOnClickListener(this)

        // focus change listeners
        etvDialog.onFocusChangeListener = this

        etvDialog?.let { viewElement ->
            txtErrorDialog?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        btnDialog.setOnClickListener {
            val tag = etvDialog.text.toString()
            if (tag.isEmpty()) {
                txtErrorDialog.text = "Tag field is not empty"
                txtErrorDialog.visibility = View.VISIBLE
                etvDialog.hideKeyboard()
            } else {
                if (tag in tags) {
                    Snackbar.make(it, "Tag is already added", Snackbar.LENGTH_LONG).show()
                    etvDialog.hideKeyboard()
                    dialog.dismiss()
                } else {
                    tags?.let {
                        it.add(tag)
                    }
                    etvDialog.hideKeyboard()
                    dialog.dismiss()
                    loadTagDataOnRecyclerView(tags)
                }
            }
        }

        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun deleteDialogBox() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_login_quit_dialog)

        // locator defining
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtQuitMsg: TextView = dialog.findViewById(R.id.txtQuitMsg)

        txtQuitMsg.text = StaticTexts.DELETE_DIALOG_MESSAGE
        btnYes.text = "Delete"
        btnNo.text = "Cancel"

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            loadTasksOnRecyclerView()
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    // ON CLICK OVERRIDING
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnAddTodoFloating -> {
                taskAdditionOrUpdateBottomSheet()
                tags = mutableListOf() // empty the list once the task is added to the DB
            }

            R.id.imgCloseDialog -> {
                etvDialog?.let { viewElement -> ViewUtilities.clearEditFields(viewElement) }
                etvDialog?.clearFocus()
                imgCloseDialog?.visibility = View.GONE
                view.hideKeyboard()
            }

            R.id.imgCloseUpdatePassword -> {
                updatePassword?.let { viewElement ->
                    ViewUtilities.clearEditFields(viewElement)
                }
                updatePassword?.clearFocus()
                imgCloseUpdatePassword?.visibility = View.GONE
                imgCloseUpdatePassword.hideKeyboard()
            }

            R.id.etvNewTask -> {
                etvNewTask?.let { viewElement ->
                    ViewUtilities.clearEditFields(viewElement)
                }
                etvNewTask?.clearFocus()
                imgCloseNewTask?.visibility = View.GONE
                imgCloseNewTask?.hideKeyboard()
            }

            R.id.etvDescription -> {
                etvDescription?.let { viewElement ->
                    ViewUtilities.clearEditFields(viewElement)
                }
                etvDescription?.clearFocus()
                imgCloseDescription?.visibility = View.GONE
                imgCloseDescription?.hideKeyboard()
            }

            R.id.txtSelectDate -> {
                val myCalendar = Calendar.getInstance()
                val year = myCalendar.get(Calendar.YEAR)
                val month = myCalendar.get(Calendar.MONTH)
                val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)

                myCalendar.set(year, month, dayOfMonth)

                val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // setting in UI
                        val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        txtSetDate?.setText(selectedDate)
                    },
                    year,
                    month,
                    dayOfMonth
                )
                datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
                datePickerDialog.show()
            }

            R.id.btnAddTag -> {
                tagAdditionDialogBox()
            }
        }
    }

    // ON FOCUS CHANGE OVERRIDING
    override fun onFocusChange(view: View?, p1: Boolean) {
        when (view?.id) {
            R.id.etvDialog -> {
                imgCloseDialog?.visibility = View.VISIBLE
            }

            R.id.etvUpdatePassword -> {
                imgCloseUpdatePassword?.visibility = View.VISIBLE
            }
        }
    }

    override fun onProgressButtonClick(position: Int, taskValue: TasksEntity) {
        val updatedInProgress = TasksEntity(
            task = taskValue.task,
            description = taskValue.description,
            completionDate = taskValue.completionDate,
            tags = tags,
            userRefEmail = SessionData(applicationContext).getFromSession(
                SessionStoreTexts.LOGGED_IN_EMAIL
            ),
            inProgress = true,
            done = taskValue.done
        )
        TasksCRUDRepo(applicationContext).updateTaskByEmail(
            updatedInProgress, taskValue?.taskId!!
        )
    }

    override fun onDoneButtonClick(position: Int) {
        NotificationUtil.showNotification(
            applicationContext, "To Do App", "Task completed"
        )
        loadTasksOnRecyclerView()
    }

    override fun onEditButtonClick(position: Int, taskEntity: TasksEntity) {
        taskAdditionOrUpdateBottomSheet(update = true, taskEntity = taskEntity)
    }

    override fun onDeleteButtonClick(position: Int) {
        deleteDialogBox()
    }

    override fun onCloseTagButtonClick(position: Int) {
        tags.removeAt(position)
        loadTagDataOnRecyclerView(tags)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawableToggle.onOptionsItemSelected(item)) {
            txtUserInfo = findViewById(R.id.txtUserInfo)
            txtUserInfo?.text =
                "Hi, ${SessionData(applicationContext).getFromSession(SessionStoreTexts.LOGGED_IN_EMAIL)}"

            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (eleDrawable.isDrawerOpen(GravityCompat.START)) {
            eleDrawable.closeDrawer(GravityCompat.START)
        } else {
            finishAffinity()
        }
    }

    override fun onItemLongClick(position: Int, currentTask: TasksEntity) {
        val alertDialog = AlertDialog.Builder(this).setTitle("Confirm")
            .setMessage("Are you sure to delete the task?")
            .setPositiveButton("Yes") { dialog, which ->
                TasksCRUDRepo(applicationContext).deleteTask(
                    email = currentTask.userRefEmail, task = currentTask.task
                )
                loadTasksOnRecyclerView()
            }.setNegativeButton("No") { dialog, which ->
                // Handle Delete action
                // You can perform delete operation here
            }.setCancelable(true).show()
    }
}