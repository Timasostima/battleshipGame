import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
@Preview
fun App() {

    val arr by remember { mutableStateOf(createArray()) }
    val arrOpponent by remember { mutableStateOf(createArray()) }

    val comic = Font(
        resource = "fontC.ttf",
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 900.dp)
                .fillMaxSize() // Take all the space available
                .background(Color.Black) // background black
        ) {
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    gridCall(arr, comic)
                    Text("Your field", color = Color.White, fontFamily = comic.toFontFamily(), fontSize = 40.sp)
                }
                Spacer(modifier = Modifier.size(100.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    gridCallOpponent(arrOpponent, comic)
                    Text("Enemy field", color = Color.White, fontFamily = comic.toFontFamily(), fontSize = 40.sp)
                }
            }
        }
    }
}

@Composable
fun gridCall(arr: Array<IntArray>, comic: Font, modifier: Modifier = Modifier) {
    val listOfChars = listOf(' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(11)
    ) {
        items(121) {    //Friendly grid
            val x = it % 11
            val y = (it - 1) / 11
            var colorVar by remember {
                mutableStateOf(
                    // If arr[x][y] == 1, paint green
                    if (x > 0 && y > 0 && arr[x - 1][y - 1] == 1) Color(0xFF006605)
                    // Use the default color
                    else Color.LightGray
                )
            }

            Box(
                modifier = Modifier
                    .defaultMinSize(10.dp, 10.dp)
                    .padding(1.dp) // Reduce padding to make items smaller
                    .aspectRatio(1f) // Adjust aspect ratio to make the items smaller
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        when (it) {
                            0 -> Color.Black // Paint the box in the position (0, 0) black
                            else -> colorVar
                        }
                    )
                    .clickable {
                        if (x == 0 || y == 0) {
                            return@clickable
                        }
                        val arrPos = arr[x - 1][y - 1]
                        colorVar = when (colorVar) {
                            Color.Gray -> {
                                when (arrPos) {
                                    1 -> Color(0xFF006605)
                                    0 -> Color.Cyan
                                    else -> Color.Gray
                                }
                            }

                            else -> colorVar
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (x == 0) Text(text = "${y + 1}", fontFamily = comic.toFontFamily())
                else if (y == 0) Text(text = "${listOfChars[x]}", fontFamily = comic.toFontFamily())
            }
        }
    }
}

@Composable
fun gridCallOpponent(arrOpponent: Array<IntArray>, comic: Font, modifier: Modifier = Modifier) {
    val listOfChars = listOf(' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(11)
    ) {
        items(121) {    //Enemy grid
            var colorVar by remember { mutableStateOf(Color.Gray) } //When it's a hit
            val x = it % 11
            val y = (it - 1) / 11
            Box(
                modifier = Modifier
                    .defaultMinSize(10.dp, 10.dp)
                    .padding(1.dp) // Reduce padding to make items smaller
                    .aspectRatio(1f) // Adjust aspect ratio to make the items smaller
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        when (it) {
                            0 -> Color.Black // Paint the box in the position (0, 0) black
                            else -> colorVar
                        }
                    )
                    .clickable {
                        if (x == 0 || y == 0) {
                            return@clickable
                        }
                        val arrPos = arrOpponent[x - 1][y - 1]
                        colorVar = when (colorVar) {
                            Color.Gray -> {
                                when (arrPos) {
                                    1 -> Color.Red
                                    0 -> Color.Cyan
                                    else -> Color.Gray
                                }
                            }

                            else -> colorVar
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (x == 0) Text(text = "${y + 1}", fontFamily = comic.toFontFamily())
                else if (y == 0) Text(text = "${listOfChars[x]}", fontFamily = comic.toFontFamily())
            }
        }

    }
}

fun createArray(): Array<IntArray> {
    val arr = Array(10) { IntArray(10) }
    var zeros = 100
    var ones = 0

    // Create a list with all the positions posibles in the array (0 .. 99)
    val positions = mutableListOf<Pair<Int, Int>>()
    for (y in arr.indices) {
        for (x in arr[y].indices) {
            positions.add(Pair(y, x))
        }
    }

    // Shuffle the list to obtain the random positions
    positions.shuffle()

    // Put the ones in the first 20 positions random
    for (i in 0 until 20) {
        val (y, x) = positions[i]
        arr[y][x] = 1
        ones++
    }

    // El resto de posiciones se queda en 0, así que no hace falta cambiarlas
    zeros = 100 - ones

    println("Fist call\n0 catches $zeros")
    println("1 catches $ones")
    return arr
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1200.dp, 650.dp))  // Specify  the inicial size of the window
    ) {
        App()
    }
}
