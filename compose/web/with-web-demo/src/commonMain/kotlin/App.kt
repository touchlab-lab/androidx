package androidx.compose.web.ww.demo

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ww.Box
import org.jetbrains.ui.ww.Modifier
import org.jetbrains.ui.ww.size
import org.jetbrains.ui.ww.background
import androidx.compose.ui.unit.ww.dp
import androidx.core.graphics.ww.Color
import androidx.compose.foundation.layout.ww.Row
import androidx.compose.foundation.layout.ww.Arrangement

@Composable
fun App() {
    Row(
        modifier = Modifier
                        .size(150.dp)
                        .background(Color.Yellow),
        horizontalArrangement = Arrangement.End
    ) {
        Box(Modifier.size(50.dp).background(Color.Red)) { }
        Box(Modifier.size(30.dp).background(Color.Blue)) { }
    }
}