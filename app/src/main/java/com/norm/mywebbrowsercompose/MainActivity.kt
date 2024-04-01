@file:OptIn(ExperimentalMaterial3Api::class)

package com.norm.mywebbrowsercompose

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.norm.mywebbrowsercompose.ui.theme.MyWebBrowserComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWebBrowserComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun WebScreen(
    url: String,
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            return@AndroidView WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
            }
        },
        update = { webview ->
            webview.loadUrl(url)
        },
    )
}

@Composable
fun MainScreen() {
    val pages = remember {
        mutableListOf("https://www.google.com/", "https://normss.github.io/normlinks/")
    }
    var currentIndex by remember {
        mutableStateOf(0)
    }
    var url by remember {
        mutableStateOf(pages[currentIndex])
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                TopBar(
                    url = url,
                    goBack = {
                        if (currentIndex > 0) {
                            currentIndex--
                            url = pages[currentIndex]
                        }
                    },
                    goForward = {
                        if (currentIndex < pages.size - 1) {
                            currentIndex++
                            url = pages[currentIndex]
                        }
                    },
                    search = { newUrl ->
                        if (pages[currentIndex] != newUrl) {
                            pages.add(newUrl)
                            currentIndex = pages.size - 1
                            url = pages[currentIndex]
                        }
                    }
                )
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(bottom = padding.calculateBottomPadding()),
        ) {
            WebScreen(url = url)
        }
    }
}


@Composable
fun TopBar(
    url: String,
    goBack: () -> Unit,
    goForward: () -> Unit,
    search: (String) -> Unit,
) {
    var _url by remember {
        mutableStateOf(url)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { goBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back Page"
            )
        }
        IconButton(
            onClick = { goForward() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go Forward Page"
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = true,
            value = _url,
            onValueChange = {
                _url = it
            }
        )
        IconButton(
            onClick = { search(_url) }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}