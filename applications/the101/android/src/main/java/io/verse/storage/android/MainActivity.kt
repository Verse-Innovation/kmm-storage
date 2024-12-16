package io.verse.storage.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.tagd.android.app.AppCompatActivity
import io.tagd.arch.access.dao
import io.verse.storage.android.domain.dao.BookDao
import io.verse.storage.android.usage.UsageClient

class MainActivity : AppCompatActivity() {
    
    private lateinit var dao: BookDao

    override fun onCreateView(savedInstanceState: Bundle?) {

    }

    override fun onReady() {
        super.onReady()

        dao = dao<BookDao>()!!

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    GreetingView(text = "Hello, Android")
                    UsageClient(dao = dao)
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
