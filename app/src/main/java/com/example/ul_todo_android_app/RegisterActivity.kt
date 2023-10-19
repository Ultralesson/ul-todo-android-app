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
import com.example.ul_todo_android_app.constants.SessionStoreTexts
import com.example.ul_todo_android_app.constants.StaticTexts
import com.example.ul_todo_android_app.database.crud.UsersCRUDRepo
import com.example.ul_todo_android_app.database.entities.UsersEntity
import com.example.ul_todo_android_app.utilities.SessionData
import com.example.ul_todo_android_app.utilities.StringUtilities
import com.example.ul_todo_android_app.utilities.ViewUtilities
import com.example.ul_todo_android_app.utilities.ViewUtilities.Companion.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener {

    // common variable declaration
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    // locator variable declaration
    private var btnRegister: Button? = null
    private var toolbarRegister: Toolbar? = null
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private var txtConfirmPassword: EditText? = null
    private var imgCloseEmail: ImageView? = null
    private var imgClosePassword: ImageView? = null
    private var imgCloseConfirmPassword: ImageView? = null
    private var txtEmailError: TextView? = null
    private var txtPasswordError: TextView? = null
    private var txtConfirmPasswordError: TextView? = null
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // locator assignment
        btnRegister = findViewById(R.id.btnRegister)
        toolbarRegister = findViewById(R.id.toolbarRegister)
        txtEmail = findViewById(R.id.etvEmail)
        txtPassword = findViewById(R.id.etvPassword)
        txtConfirmPassword = findViewById(R.id.etvConfirmPassword)

        imgCloseEmail = findViewById(R.id.imgCloseEmail)
        imgClosePassword = findViewById(R.id.imgClosePassword)
        imgCloseConfirmPassword = findViewById(R.id.imgCloseConfirmPassword)

        txtEmailError = findViewById(R.id.txtErrorEmail)
        txtPasswordError = findViewById(R.id.txtErrorPassword)
        txtConfirmPasswordError = findViewById(R.id.txtErrorConfirmPassword)

        progressBar = findViewById(R.id.progressBar)

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
        imgCloseConfirmPassword?.setOnClickListener(this)
        btnRegister?.setOnClickListener(this)

        // 2. focus change listeners
        txtEmail?.onFocusChangeListener = this
        txtPassword?.onFocusChangeListener = this
        txtConfirmPassword?.onFocusChangeListener = this

    }

    // CUSTOM ACTION FUNCTIONS
    private fun actionBar() {
        setSupportActionBar(toolbarRegister)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // defining action bar properties
        actionBar?.title = StaticTexts.REGISTER

        // navigating back functionality
        toolbarRegister?.setNavigationOnClickListener {
            registerDismissDialog()
        }
    }

    private fun getEmailAndPasswordFromTextFields(): List<String?> {
        val emailValue: String? =
            txtEmail?.let { viewElement -> ViewUtilities.getTextFromTextFields(viewElement) }
        val passwordValue: String? = txtPassword?.let { viewElement ->
            ViewUtilities.getTextFromTextFields(
                viewElement
            )
        }
        return listOf(emailValue, passwordValue)
    }

    private fun checkEmptyEmailPasswordAndConfirmPasswordErrors(): Boolean {
        val emailValue = getEmailAndPasswordFromTextFields()[0]
        val passwordValue = getEmailAndPasswordFromTextFields()[1]

        val confirmPasswordValue: String? =
            txtConfirmPassword?.let { viewElement -> ViewUtilities.getTextFromTextFields(viewElement) }

        // Setting the error messages for incorrect input fields
        if (emailValue!!.isEmpty() && passwordValue!!.isEmpty() && confirmPasswordValue!!.isEmpty()) {
            txtEmailError?.text = StaticTexts.EMAIL_EMPTY_ERROR_MESSAGE
            txtPasswordError?.text = StaticTexts.PASSWORD_EMPTY_ERROR_MESSAGE
            txtConfirmPasswordError?.text = StaticTexts.CONFIRM_PASSWORD_EMPTY_ERROR_MESSAGE

            txtEmailError?.visibility = View.VISIBLE
            txtPasswordError?.visibility = View.VISIBLE
            txtConfirmPasswordError?.visibility = View.VISIBLE
            return false
        } else if (emailValue!!.isEmpty()) {
            txtEmailError?.text = StaticTexts.EMAIL_EMPTY_ERROR_MESSAGE
            txtEmailError?.visibility = View.VISIBLE
            return false
        } else if (passwordValue!!.isEmpty()) {
            txtPasswordError?.text = StaticTexts.PASSWORD_EMPTY_ERROR_MESSAGE
            txtPasswordError?.visibility = View.VISIBLE
            return false
        } else if (confirmPasswordValue!!.isEmpty()) {
            txtConfirmPasswordError?.text = StaticTexts.CONFIRM_PASSWORD_EMPTY_ERROR_MESSAGE
            txtConfirmPasswordError?.visibility = View.VISIBLE
            return false
        } else if (confirmPasswordValue!!.isEmpty() && passwordValue!!.isEmpty()) {
            txtConfirmPasswordError?.text = StaticTexts.CONFIRM_PASSWORD_EMPTY_ERROR_MESSAGE
            txtPasswordError?.text = StaticTexts.PASSWORD_EMPTY_ERROR_MESSAGE
            txtConfirmPasswordError?.visibility = View.VISIBLE
            txtPasswordError?.visibility = View.VISIBLE
            return false
        } else if (emailValue!!.isEmpty() && passwordValue!!.isEmpty()) {
            txtEmailError?.text = StaticTexts.EMAIL_EMPTY_ERROR_MESSAGE
            txtPasswordError?.text = StaticTexts.PASSWORD_EMPTY_ERROR_MESSAGE
            txtEmailError?.visibility = View.VISIBLE
            txtPasswordError?.visibility = View.VISIBLE
            return false
        } else if (emailValue!!.isEmpty() && confirmPasswordValue!!.isEmpty()) {
            txtEmailError?.text = StaticTexts.EMAIL_EMPTY_ERROR_MESSAGE
            txtConfirmPasswordError?.text = StaticTexts.CONFIRM_PASSWORD_EMPTY_ERROR_MESSAGE
            txtEmailError?.visibility = View.VISIBLE
            txtConfirmPasswordError?.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun areRegisterCredentialsMatched(): Boolean {
        val emailValue = getEmailAndPasswordFromTextFields()[0]
        val passwordValue = getEmailAndPasswordFromTextFields()[1]

        val confirmPasswordValue: String? =
            txtConfirmPassword?.let { viewElement -> ViewUtilities.getTextFromTextFields(viewElement) }

        return StringUtilities.areStringsMatched(passwordValue, confirmPasswordValue)
    }

    private fun registerDismissDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_login_quit_dialog)

        // locator defining
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtQuitMsg: TextView = dialog.findViewById(R.id.txtQuitMsg)

        txtQuitMsg.text = StaticTexts.REGISTER_DIALOG_ERROR_MESSAGE

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

        txtConfirmPassword?.let { viewElement ->
            txtConfirmPasswordError?.let { relativeElement ->
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

            R.id.imgCloseConfirmPassword -> {
                txtConfirmPassword?.let { viewElement -> ViewUtilities.clearEditFields(viewElement) }
                txtConfirmPassword?.clearFocus()
                imgCloseConfirmPassword?.visibility = View.GONE
                view.hideKeyboard()
            }

            R.id.btnRegister -> {
                view.hideKeyboard()

                val emailValue = getEmailAndPasswordFromTextFields()[0]
                val passwordValue = getEmailAndPasswordFromTextFields()[1]

                if (checkEmptyEmailPasswordAndConfirmPasswordErrors()) {
                    UsersCRUDRepo(applicationContext).insertUser(
                        UsersEntity(
                            userEmail = emailValue!!, password = passwordValue!!
                        )
                    ) { insertStatus ->
                        if (insertStatus) {
                            SessionData(applicationContext).saveToSession(
                                SessionStoreTexts.LOGGED_IN, "true"
                            )
                            SessionData(applicationContext).saveToSession(
                                SessionStoreTexts.LOGGED_IN_EMAIL, emailValue
                            )
                            if (!areRegisterCredentialsMatched()) {
                                Snackbar.make(
                                    view,
                                    "Password and confirm password are not matched",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } else {
                                val intent = Intent(this, LoadingActivity::class.java)
                                intent.putExtra("targetActivityClass", ToDoActivity::class.java)
                                activityResultLauncher.launch(intent)
                            }
                        } else {
                            Snackbar.make(
                                view, "Email is already registered", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    // ON FOCUS CHANGE OVERRIDING
    override fun onFocusChange(view: View?, p1: Boolean) {
        when (view?.id) {
            R.id.etvEmail -> imgCloseEmail?.visibility = if (p1) View.VISIBLE else View.GONE
            R.id.etvPassword -> imgClosePassword?.visibility = if (p1) View.VISIBLE else View.GONE
            R.id.etvConfirmPassword -> imgCloseConfirmPassword?.visibility =
                if (p1) View.VISIBLE else View.GONE
        }
    }
}