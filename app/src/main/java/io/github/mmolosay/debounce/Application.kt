package io.github.mmolosay.debounce

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mmolosay.debounce.lib.debounced
import java.text.SimpleDateFormat
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
    val locale = LocalConfiguration.current.locales[0]
    val dateFormatter = remember(locale) { SimpleDateFormat("HH:mm:ss:SSS", locale) }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button { wasOnClickExecuted ->
            events += ClickEvent(
                time = dateFormatter.format(Date()),
                wasExecuted = wasOnClickExecuted,
                background = inferClickEventBackground(wasOnClickExecuted),
            )
        }
        ClickEvents(
            items = events.reversed(),
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
                onInvoke = { wasExecuted -> afterClick(wasExecuted) },
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
        key = { it.time },
    ) {
        ClickEventItem(item = it)
    }
}

@Composable
private fun ClickEventItem(item: ClickEvent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(item.background)
            .padding(4.dp),
    ) {
        Text(text = "Invokation time: ${item.time}")
        Text(text = "Was executed: ${item.wasExecuted}")
    }
}

private fun onButtonClick(
    context: Context,
) =
    Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()

@Stable
private data class ClickEvent(
    val time: String,
    val wasExecuted: Boolean,
    val background: Color,
)

private fun inferClickEventBackground(wasOnClickExecuted: Boolean): Color =
    if (wasOnClickExecuted) Color.Green.copy(alpha = 0.2f)
    else Color.Red.copy(alpha = 0.2f)