package io.github.mmolosay.debounce

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mmolosay.debounce.lib.debounced
import java.util.Date
import kotlin.time.Duration.Companion.seconds

// region Previews

@Preview
@Composable
private fun ApplicationPreview() {
    MaterialTheme {
        Application()
    }
}

// endregion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Application() {
    Scaffold { padding ->
        MainContent(
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
) {
    val events = remember { mutableStateListOf<ClickEvent>() }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            afterClick = { events.add(ClickEvent(date = Date(), wasInvoked = it)) },
        )
        ClickEvents(
            items = events,
        )
    }
}

private fun LazyListScope.Button(
    afterClick: (Boolean) -> Unit,
) {
    item(key = "button") {
        val context = LocalContext.current
        val onClick = remember {
            debounced(
                timeout = 3.seconds,
                onInvoke = { afterClick(it) },
                action = { onButtonClick(context) },
            )
        }
        Button(
            onClick = onClick,
        ) {
            Text(text = "Click me")
        }
    }
}

private fun LazyListScope.ClickEvents(
    items: List<ClickEvent>,
) {
    items(
        items = items,
        key = { it.date.time },
    ) {
        Column {
            Text(text = "Date: ${it.date}")
            Text(text = "Was invoked: ${it.wasInvoked}")
        }
    }
}

private fun onButtonClick(
    context: Context,
) =
    Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()

private data class ClickEvent(
    val date: Date,
    val wasInvoked: Boolean,
)