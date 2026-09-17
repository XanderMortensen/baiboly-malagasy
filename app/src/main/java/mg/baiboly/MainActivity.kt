package mg.baiboly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { ReaderScreen() } }
    }
}

@Composable
fun ReaderScreen(vm: ReaderViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${Books.NAMES[state.book - 1]} ${state.chapter}") },
                actions = { IconButton(onClick = { vm.setFontSize(state.fontSize + 1f) }) { Text("A+") } }
            )
        },
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(8.dp), Arrangement.SpaceBetween) {
                TextButton(onClick = vm::prevChapter) { Text("Toerana aloha") }
                TextButton(onClick = vm::nextChapter) { Text("Manaraka") }
            }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            items(state.verses) { v ->
                Text(
                    "${v.verse}. ${v.text}",
                    fontSize = state.fontSize.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

object Books {
    val NAMES = listOf(
        "Genesisy", "Eksodosy", "Levitikosy", "Nomery", "Deoteronomia",
        "Josoa", "Mpitsara", "Rota", "1 Samoela", "2 Samoela",
        "1 Mpanjaka", "2 Mpanjaka", "1 Tantara", "2 Tantara", "Ezra",
        "Nehemia", "Estera", "Joba", "Salamo", "Ohabolana",
        "Mpitoriteny", "Tonon-kiran'i Solomona", "Isaia", "Jeremia",
        "Fitomaniana", "Ezekiela", "Daniela", "Hosea", "Joela", "Amosa",
        "Obadia", "Jona", "Mika", "Nahoma", "Habakoka", "Zefania",
        "Hagay", "Zakaria", "Malakia", "Matio", "Marka", "Lioka",
        "Jaona", "Asan'ny Apostoly", "Romana", "1 Korintiana",
        "2 Korintiana", "Galatiana", "Efesiana", "Filipiana", "Kolosiana",
        "1 Tesaloniana", "2 Tesaloniana", "1 Timoty", "2 Timoty", "Titosy",
        "Filemona", "Hebreo", "Jakoba", "1 Petera", "2 Petera", "1 Jaona",
        "2 Jaona", "3 Jaona", "Joda", "Apokalypsy"
    )
}
