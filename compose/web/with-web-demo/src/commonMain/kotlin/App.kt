package androidx.compose.web.ww.demo

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ww.Box
import org.jetbrains.ui.ww.Modifier
import org.jetbrains.ui.ww.size
import org.jetbrains.ui.ww.background
import androidx.compose.ui.unit.ww.dp
import androidx.core.graphics.ww.Color
import androidx.compose.foundation.layout.ww.Row
import androidx.compose.foundation.layout.ww.Column
import androidx.compose.foundation.layout.ww.Arrangement
import androidx.compose.ui.ww.Alignment
import androidx.compose.foundation.ww.border

@Composable
fun App() {
    val horizontalArrangements = listOf(Arrangement.End, Arrangement.Start)
    val verticalAlignments = listOf(Alignment.Top, Alignment.CenterVertically, Alignment.Bottom)

    Column() {
        horizontalArrangements.forEach { horizontalArrangement ->
            verticalAlignments.forEach { verticalAlignment ->
                Row(
                    modifier = Modifier
                        .size(150.dp)
                        .border(1.dp, Color(0, 0, 200))
                        .background(Color.Yellow),
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = verticalAlignment
                ) {
                    Box(Modifier.size(50.dp).background(Color.Red)) { }
                    Box(Modifier.size(30.dp).background(Color.Blue)) { }
                }
            }
        }
    }
}