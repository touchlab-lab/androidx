import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.elements.A
import androidx.compose.web.elements.Button
import androidx.compose.web.elements.Div
import androidx.compose.web.elements.ElementScope
import androidx.compose.web.elements.H1
import androidx.compose.web.elements.Span
import androidx.compose.web.elements.TagElement
import androidx.compose.web.elements.Text
import androidx.compose.web.renderComposable
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import kotlin.random.Random
import kotlin.random.nextInt


fun main() {

//	state = State(data = buildData(1000))
    renderComposable(document.getElementById("root")!! as HTMLElement) {
        Main()
    }
}

@Composable
fun Jumbotron(dispatch: (Map<String, String>) -> Unit) {
    Div(attrs = {
        classes("jumbotron")
    }) {

        Div(attrs = {
            classes("row")
        }) {
            Div(attrs = {
                classes("col-md-6")
            }) {
                H1 { Text("Compose Benchmark") }
            }
            Div(attrs = {
                classes("col-md-6")
            }) {
                Div(attrs = {
                    classes("row")
                }) {
                    MyButton("run", "Create 1,000 rows") {
                        dispatch(mapOf("type" to "RUN"))
                    }
                    MyButton("runlots", "Create 10,000 rows") {
                        dispatch(mapOf("type" to "RUN_LOTS"))
                    }
                    MyButton("add", "Append 1,000 rows") {
                        dispatch(mapOf("type" to "ADD"))
                    }
                    MyButton("update", "Update every 10th row") {
                        dispatch(mapOf("type" to "UPDATE"))
                    }
                    MyButton("clear", "Clear") {
                        dispatch(mapOf("type" to "CLEAR"))
                    }
                    MyButton("swaprows", "Swap Rows") {
                        dispatch(mapOf("type" to "SWAP_ROWS"))
                    }
                }
            }
        }
    }
}

@Composable
fun MyButton(id: String, title: String, cb: () -> Unit) {
    Div(attrs = {
        classes("col-sm-6", "smallpad")
    }) {
        Button(attrs = {
            id(id)
            onClick { cb() }
            classes("btn", "btn-primary", "btn-block")
        }) {
            Text(title)
        }
    }
}

@Composable
fun Main() {
    val dispatch: (Map<String, String>) -> Unit = {
        val type = it["type"]
        when (type) {
            "RUN" -> {
                state = state.copy(
                    data = buildData(1000),
                    selected = 0
                )
            }
            "RUN_LOTS" -> {
                state = state.copy(
                    data = buildData(10000),
                    selected = 0
                )
            }
            "ADD" -> {
                state = state.copy(
                    data = state.data + buildData(1000)
                )
            }
            "SELECT" -> {
                state = state.copy(
                    selected = it["id"]!!.toInt()
                )
            }
            "UPDATE" -> {
                state = state.copy(
                    data = state.data.map {
                        if ((it.id - 1) % 10 == 0) {
                            it.copy(label = it.label + " !!!")
                        } else {
                            it
                        }
                    }
                )
            }
            "CLEAR" -> {
                state = State()
            }
            "SWAP_ROWS" -> {
                if (state.data.size > 998) {
                    state = state.copy(
                        data = mutableListOf<Data>().apply {
                            add(state.data[0])
                            add(state.data[998])
                            addAll(state.data.subList(2, 998))
                            add(state.data[1])
                            add(state.data[999])
                        }
                    )
                }
            }
            "REMOVE" -> {
                val id = it["id"]!!.toInt()
                val ix = state.data.indexOfFirst { it.id == id }
                state = state.copy(
                    data = state.data.subList(0, ix) + state.data.subList(ix + 1, state.data.size)
                )
            }
        }
    }

    Div(attrs = { classes("container") }) {
        Jumbotron(dispatch = dispatch)
        Content(dispatch = dispatch)
    }
}

@Composable
fun Content(dispatch: (Map<String, String>) -> Unit) {
    Table(attrs = { classes("table", "table-hover", "table-striped", "test-data") }) {
        Tbody(attrs = {}) {
            state.data.forEach {
                Row(item = it, selected = state.selected == it.id, dispatch = dispatch)
            }
        }
    }
}

@Composable
fun Row(item: Data, selected: Boolean, dispatch: (Map<String, String>) -> Unit) {
    Tr(attrs = {
        classes { if (selected) +"danger" }
    }) {
        Td(attrs = { classes("col-md-1") }) {
            Text(item.id.toString())
        }
        Td(attrs = {
            classes("col-md-4")
        }) {
            A(attrs = {
                onClick { dispatch(mapOf("type" to "SELECT", "id" to item.id.toString())) }
            }) {
                Text(item.label)
            }

        }
        Td(attrs = { classes("col-md-1") }) {
            A(attrs = {
                onClick { dispatch(mapOf("type" to "REMOVE", "id" to item.id.toString())) }
            }) {
                Span(attrs = {
                    classes("glyphicon", "glyphicon-remove")
                    attr("aria-hidden", "true")
                }, content = {})
            }
        }
        Td(attrs = { classes("col-md-6") }) {}
    }
}

var state by mutableStateOf(State())

data class Data(val id: Int, val label: String)
data class State(val data: List<Data> = emptyList(), val selected: Int = 0)

private var nextId = 1

fun buildData(count: Int): List<Data> {
    return generateSequence {
        val i1 = Random.nextInt(A.indices)
        val i2 = Random.nextInt(C.indices)
        val i3 = Random.nextInt(N.indices)
        Data(nextId++, "${A[i1]} ${C[i2]} ${N[i3]}")
    }.take(count).toList()
}

val A = arrayOf(
    "pretty",
    "large",
    "big",
    "small",
    "tall",
    "short",
    "long",
    "handsome",
    "plain",
    "quaint",
    "clean",
    "elegant",
    "easy",
    "angry",
    "crazy",
    "helpful",
    "mushy",
    "odd",
    "unsightly",
    "adorable",
    "important",
    "inexpensive",
    "cheap",
    "expensive",
    "fancy"
)
val C = arrayOf(
    "red",
    "yellow",
    "blue",
    "green",
    "pink",
    "brown",
    "purple",
    "brown",
    "white",
    "black",
    "orange"
)
val N = arrayOf(
    "table",
    "chair",
    "house",
    "bbq",
    "desk",
    "car",
    "pony",
    "cookie",
    "sandwich",
    "burger",
    "pizza",
    "mouse",
    "keyboard"
)

@Composable
inline fun Tr(
    crossinline attrs: @DisallowComposableCalls AttrsBuilder<Tag.Div>.() -> Unit,
    content: @Composable ElementScope<HTMLElement>.() -> Unit
) {
    TagElement(
        tagName = "tr",
        applyAttrs = attrs,
        content = content,
        applyStyle = {}
    )
}

@Composable
inline fun Td(
    crossinline attrs: @DisallowComposableCalls AttrsBuilder<Tag.Div>.() -> Unit,
    content: @Composable ElementScope<HTMLElement>.() -> Unit
) {
    TagElement(
        tagName = "td",
        applyAttrs = attrs,
        content = content,
        applyStyle = {}
    )
}

@Composable
inline fun Table(
    crossinline attrs: @DisallowComposableCalls AttrsBuilder<Tag.Div>.() -> Unit,
    content: @Composable ElementScope<HTMLElement>.() -> Unit
) {
    TagElement(
        tagName = "table",
        applyAttrs = attrs,
        content = content,
        applyStyle = {}
    )
}

@Composable
inline fun Tbody(
    crossinline attrs: @DisallowComposableCalls AttrsBuilder<Tag.Div>.() -> Unit,
    content: @Composable ElementScope<HTMLElement>.() -> Unit
) {
    TagElement(
        tagName = "tbody",
        applyAttrs = attrs,
        content = content,
        applyStyle = {}
    )
}