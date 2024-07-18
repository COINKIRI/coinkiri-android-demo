package com.cokiri.coinkiri.presentation.screens.main

import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.presentation.screens.login.LoginUiState
import com.cokiri.coinkiri.presentation.screens.login.LoginViewModel
import com.cokiri.coinkiri.presentation.screens.post.PostViewModel
import com.cokiri.coinkiri.ui.navigation.LogInNavGraph
import com.cokiri.coinkiri.ui.navigation.MainGraph
import com.cokiri.coinkiri.ui.theme.CoinkiriandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val result = if (uri != null) arrayOf(uri) else null
        filePathCallback?.onReceiveValue(result)
        filePathCallback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinkiriandroidTheme {

                val loginUiState by loginViewModel.loginUiState.collectAsState()
                val navController = rememberNavController()

                when (loginUiState) {
                    is LoginUiState.LogInSuccess -> MainGraph(navController, loginViewModel, postViewModel)
                    else -> LogInNavGraph(navController, loginViewModel)
                }
            }
        }
    }
    fun openFileChooser(callback: ValueCallback<Array<Uri>>?) {
        filePathCallback = callback
        getContent.launch("image/*")
    }
}
