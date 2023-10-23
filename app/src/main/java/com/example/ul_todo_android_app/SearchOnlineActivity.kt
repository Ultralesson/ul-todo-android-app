package com.example.ul_todo_android_app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ul_todo_android_app.constants.StaticTexts
import com.example.ul_todo_android_app.utilities.CustomToastUtility
import java.util.regex.Pattern

class SearchOnlineActivity : AppCompatActivity() {

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    private var toolBar: androidx.appcompat.widget.Toolbar? = null
    private var searchEngineSpinner: Spinner? = null
    private var urlEditText: EditText? = null
    private var searchButton: Button? = null
    private var webview: WebView? = null
    private var webviewBackButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_online)

        // Enable WebView debugging
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        toolBar = findViewById(R.id.toolbar)
        searchEngineSpinner = findViewById(R.id.searchEngineSpinner)
        urlEditText = findViewById(R.id.urlEditText)
        searchButton = findViewById(R.id.searchButton)

        toolBar?.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.whiteSmoke))

        actionBar()
        setupSearchEngineSpinner();
    }

    private fun actionBar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // defining action bar properties
        actionBar?.title = StaticTexts.SEARCH_ONLINE

        // navigating back functionality
        toolBar?.setNavigationOnClickListener {
            val intent = Intent(applicationContext, ToDoActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun setupSearchEngineSpinner() {
        val searchEngines = arrayOf("Choose your search engine", "Google", "Yahoo", "Bing")
        val searchEngineUrls = mapOf(
            "Google" to "https://www.google.com",
            "Yahoo" to "https://www.yahoo.com",
            "Bing" to "https://www.bing.com"
        )

        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            searchEngines
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchEngineSpinner?.adapter = adapter
        searchEngineSpinner?.prompt = "Choose your search engine"

        // Add an OnItemSelectedListener to the Spinner
        searchEngineSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item text
                val selectedItem = parent?.getItemAtPosition(position)?.toString()

                // Get the corresponding URL from the map
                val selectedUrl = searchEngineUrls[selectedItem]

                // Update the EditText field with the URL if available
                urlEditText?.setText(selectedUrl ?: "")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        searchButton?.setOnClickListener {
            // Get the entered URL from the EditText field
            val enteredUrl = urlEditText?.text.toString().trim()

            // Check if the entered URL is valid
            if (isValidUrl(enteredUrl)) {
                navigateToSearchOnlineActivity(enteredUrl)
            } else {
                val selectedSearchEngine = searchEngineSpinner?.selectedItem.toString()
                val selectedUrl = searchEngineUrls[selectedSearchEngine]

                // Check if a URL is selected from the spinner
                if (selectedUrl != null) {
                    navigateToSearchOnlineActivity(selectedUrl)
                } else {
                    // Handle the case where no URL is selected
                    CustomToastUtility(this).showToast(
                        R.layout.activity_custom_toast,
                        "Please select a search engine or enter a valid URL"
                    )
                }
            }
        }
    }

    // Function to check if a URL is valid
    private fun isValidUrl(url: String): Boolean {
        // Regular expression pattern for a valid URL
        val urlPattern = Pattern.compile(
            "^https?://[-\\w]+(\\.\\w[-\\w]*)+(:\\d+)?(/[^.!,?;<>\"'()\\[\\]{}\\s\\x7F-\\xFF]*)*$",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )

        // Check if the provided URL matches the pattern
        return urlPattern.matcher(url).matches()
    }

    private fun navigateToSearchOnlineActivity(url: String) {
        setContentView(R.layout.activity_webview)
        webview = findViewById(R.id.webview)
        webview?.settings?.javaScriptEnabled = true;
        webviewBackButton = findViewById(R.id.webviewBackButton)
        val webViewClient = WebViewClient()
        webview?.webViewClient = webViewClient
        webview?.settings?.cacheMode = WebSettings.LOAD_NO_CACHE
        webview?.loadUrl(url) // Load the URL passed as a parameter

        webviewBackButton?.setOnClickListener {
            // Show a confirmation dialog when the Back button is clicked
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { dialog, which ->
                // Navigate to ToDoActivity
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra("targetActivityClass", ToDoActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, which ->
                // Do nothing or dismiss the dialog
            }
            .create()

        alertDialog.show()
    }
}