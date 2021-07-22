import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import co.touchlab.compose.Button
import co.touchlab.compose.SimpleButton
import co.touchlab.compose.Text

object ComposeNativeExample {
    @OptIn(ExperimentalUnitApi::class)
    @Composable
    internal fun HelloWorld() {
        var baseFontSize by remember { mutableStateOf(5) }
        LaunchedEffect(baseFontSize) {
            println("BaseFontSize: $baseFontSize")
        }
        Column {

            Row {
                //                SimpleButton(title = "-", onClick = {
                //                    baseFontSize -= 1
                //                    println("Decreasing baseFontSize: $baseFontSize")
                //                })
                //                SimpleButton(title = "+", onClick = {
                //                    baseFontSize += 1
                //                    println("Increasing baseFontSize: $baseFontSize")
                //                })
                Button(onClick = { baseFontSize -= 1 }, modifier = Modifier.padding(10.dp)) {
                    Text("-")
                }
                Text("Hello", modifier = Modifier.padding(20.dp))
                Button(onClick = { baseFontSize += 1 }) {
                    Text("+")
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Text("Yo - $baseFontSize.value")
                Column(modifier = Modifier.align(Alignment.Center)) {
                    repeat(10) { row ->
                        Row {
                            Text(
                                "${row + baseFontSize}: ${(0..baseFontSize).joinToString { 
                                    "$it"
                                }}",
//                                size = TextUnit(
//                                    value = baseFontSize.toFloat() + row.toFloat(),
//                                    type = TextUnitType.Unspecified
//                                )

                            )
//                            repeat(baseFontSize) { column ->
//                                Text(
//                                    "${baseFontSize + row + column} ",
//                                    size = TextUnit(
//                                        baseFontSize.toFloat() + row.toFloat() + column.toFloat(),
//                                        type = TextUnitType.Unspecified
//                                    )
//                                )
//                            }
                        }
                    }
                }
                Text("Okay", modifier = Modifier.align(Alignment.BottomEnd))
//            Column(
////                    modifier = Modifier.align(Alignment.BottomEnd)
//            ) {
//
//            }
            }
        }
//        TooLong()
//         TooShort()
//        SomeConstraints()



        // LazyColumn(items = (0..200).map { "Row: $it" }) {
        //     Box(Modifier.padding(30.dp)) {
        //         Text(it)
        //     }
        // }
    }

    @Composable
    internal fun TooLong() {
        Column {
            repeat(60) {
                Text("Common Row $it")
            }
        }
    }

    @Composable
    internal fun TooShort() {
        Column {
            repeat(10) {
                Text("Common Row $it")
            }
        }
    }

    @Composable
    internal fun SomeConstraints() {
        Column {
            repeat(5) {
                Text(
//                    modifier = Modifier.padding(Dp(10F)),
                    text = "Common Row $it"
                )
            }
        }
    }
}