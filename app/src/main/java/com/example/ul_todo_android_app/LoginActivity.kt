package com.example.ul_todo_android_app

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.ul_todo_android_app.constants.SessionStoreTexts
import com.example.ul_todo_android_app.constants.StaticTexts
import com.example.ul_todo_android_app.database.crud.UsersCRUDRepo
import com.example.ul_todo_android_app.utilities.SessionData
import com.example.ul_todo_android_app.utilities.ViewUtilities
import com.example.ul_todo_android_app.utilities.ViewUtilities.Companion.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener {

    // common variable declaration
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    // locator variable declaration
    private var toolBar: Toolbar? = null
    private var btnSubmit: Button? = null
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private var imgCloseEmail: ImageView? = null
    private var imgClosePassword: ImageView? = null
    private var txtEmailError: TextView? = null
    private var txtPasswordError: TextView? = null
    private lateinit var progressBar: ProgressBar

    // session storage variables
    private var todoSession: SessionData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // locator assignment
        toolBar = findViewById(R.id.toolbar)
        btnSubmit = findViewById(R.id.btnSubmit)
        txtEmail = findViewById(R.id.etvEmail)
        txtPassword = findViewById(R.id.etvPassword)
        imgCloseEmail = findViewById(R.id.imgCloseEmail)
        imgClosePassword = findViewById(R.id.imgClosePassword)
        txtEmailError = findViewById(R.id.txtErrorEmail)
        txtPasswordError = findViewById(R.id.txtErrorPassword)
        progressBar = findViewById(R.id.progressBar)

        toolBar?.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.whiteSmoke))

        // session storage initialization
        todoSession = SessionData(applicationContext)

        // ++++++++++++++++++++++++++
        // CUSTOM ACTION METHOD CALLS
        // ++++++++++++++++++++++++++
        actionBar()
        onEditTextChangeListener()

        // ++++++++++++++++++++++++++++++++
        // OVERRIDDEN ACTION LISTENERS CALL
        // ++++++++++++++++++++++++++++++++

        // 1. click listeners
        imgCloseEmail?.setOnClickListener(this)
        imgClosePassword?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        // 2. focus change listeners
        txtEmail?.onFocusChangeListener = this
        txtPassword?.onFocusChangeListener = this
    }

    // CUSTOM ACTION FUNCTIONS
    private fun actionBar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // defining action bar properties
        actionBar?.title = StaticTexts.LOGIN

        // navigating back functionality
        toolBar?.setNavigationOnClickListener {
            loginDismissDialog()
        }
    }

    private fun checkEmptyEmailAndPasswordErrors(): Boolean {
        val emailValue: String? =
            txtEmail?.let { viewElement -> ViewUtilities.getTextFromTextFields(viewElement) }
        val passwordValue: String? = txtPassword?.let { viewElement ->
            ViewUtilities.getTextFromTextFields(
                viewElement
            )
        }

        // Setting the error messages for incorrect input fields
        if (emailValue!!.isEmpty() && passwordValue!!.isEmpty()) {
            txtEmailError?.text = StaticTexts.EMAIL_EMPTY_ERROR_MESSAGE
            txtPasswordError?.text = StaticTexts.PASSWORD_EMPTY_ERROR_MESSAGE
            txtEmailError?.visibility = View.VISIBLE
            txtPasswordError?.visibility = View.VISIBLE
            return false
        } else if (emailValue!!.isEmpty()) {
            txtEmailError?.text = "Email cannot be empty"
            txtEmailError?.visibility = View.VISIBLE
            return false
        } else if (passwordValue!!.isEmpty()) {
            txtPasswordError?.text = "Password cannot be empty"
            txtPasswordError?.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun areEmailAndPasswordErrorMessagesDisplayed(): Boolean {
        return txtEmailError!!.isVisible || txtPasswordError!!.isVisible
    }

    // CUSTOM DIALOG FUNCTIONALITY
    private fun loginDismissDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_login_quit_dialog)

        // locator defining
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtQuitMsg: TextView = dialog.findViewById(R.id.txtQuitMsg)

        txtQuitMsg.text = StaticTexts.LOGIN_DIALOG_BOX_MSG

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(applicationContext, MainActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun onEditTextChangeListener() {
        txtEmail?.let { viewElement ->
            txtEmailError?.let { relativeElement ->
                ViewUtilities.textWatcher(
                    viewElement, relativeElement, "resetText"
                )
            }
        }

        txtPassword?.let { viewElement ->
            txtPasswordError?.let { relativeElement ->
                ViewUtilities.textWatcher(viewElement, relativeElement, "resetText")
            }
        }
    }

    // ON CLICK OVERRIDING
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgCloseEmail -> {
                txtEmail?.let { viewElement ->
                    ViewUtilities.clearEditFields(viewElement)
                }
                txtEmail?.clearFocus()
                imgCloseEmail?.visibility = View.GONE
                view.hideKeyboard()
            }

            R.id.imgClosePassword -> {
                txtPassword?.let { viewElement -> ViewUtilities.clearEditFields(viewElement) }
                txtPassword?.clearFocus()
                imgClosePassword?.visibility = View.GONE
                view.hideKeyboard()
            }

            R.id.btnSubmit -> {
                view.hideKeyboard()

                if (checkEmptyEmailAndPasswordErrors()) {
                    if (!areEmailAndPasswordErrorMessagesDisplayed()) {

                        UsersCRUDRepo(applicationContext).getUserByEmail(txtEmail?.text.toString()) { user ->
                            if (user?.userEmail != txtEmail?.text.toString()) {

                                // Here we are navigating back to register screen if the email is not registered
                                val snackbar = Snackbar.make(
                                    view, "Email is not registered", Snackbar.LENGTH_LONG
                                )

                                snackbar.addCallback(object : Snackbar.Callback() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_ACTION) {
                                            // Navigating to register screen if email is not registered
                                            val intent =
                                                Intent(
                                                    applicationContext,
                                                    RegisterActivity::class.java
                                                )
                                            activityResultLauncher.launch(intent)
                                        }
                                    }
                                })
                                snackbar.show()
                            } else {
                                if (user.password != txtPassword?.text.toString()) {
                                    Snackbar.make(
                                        view, "Password is incorrect", Snackbar.LENGTH_LONG
                                    ).show()
                                } else {
                                    // storing the login session
                                    SessionData(applicationContext).saveToSession(
                                        SessionStoreTexts.LOGGED_IN, "true"
                                    )
                                    SessionData(applicationContext).saveToSession(
                                        SessionStoreTexts.LOGGED_IN_EMAIL, user.userEmail
                                    )

                                    val intent = Intent(this, LoadingActivity::class.java)
                                    intent.putExtra("targetActivityClass", ToDoActivity::class.java)
                                    activityResultLauncher.launch(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ON FOCUS CHANGE OVERRIDING
    override fun onFocusChange(view: View?, p1: Boolean) {
        when (view?.id) {
            R.id.etvEmail -> {
                imgCloseEmail?.visibility = View.VISIBLE
            }

            R.id.etvPassword -> {
                imgClosePassword?.visibility = View.VISIBLE
            }
        }
    }
}