// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameMenuScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GameMenuScreen(modifier: Modifier = Modifier) {
    var selectedGame by remember { mutableStateOf<String?>(null) }

    if (selectedGame == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎮 게임 모음",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            val games = listOf(
                "숫자 맞추기" to "🔢",
                "두더지 잡기" to "🔨",
                "카드 짝맞추기" to "🃏",
                "틱택토" to "⭕",
                "벽돌깨기" to "🧱",
                "뱀 게임" to "🐍"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { (name, emoji) ->
                    GameMenuItem(
                        name = name,
                        emoji = emoji,
                        onClick = { selectedGame = name }
                    )
                }
            }
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            Button(
                onClick = { selectedGame = null },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("← 메뉴로")
            }

            when (selectedGame) {
                "숫자 맞추기" -> NumberGuessingGame()
                "두더지 잡기" -> WhackAMoleGame()
                "카드 짝맞추기" -> MemoryCardGame()
                "틱택토" -> TicTacToeGame()
                "벽돌깨기" -> BrickBreakerGame()
                "뱀 게임" -> SnakeGame()
            }
        }
    }
}

@Composable
fun GameMenuItem(name: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 1. 숫자 맞추기 게임
@Composable
fun NumberGuessingGame() {
    var targetNumber by remember { mutableStateOf(Random.nextInt(1, 101)) }
    var guess by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("1부터 100 사이의 숫자를 맞춰보세요!") }
    var attempts by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔢 숫자 맞추기", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("시도 횟수: $attempts", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Text(message, fontSize = 20.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = guess,
            onValueChange = { guess = it },
            label = { Text("숫자 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val guessNum = guess.toIntOrNull()
                if (guessNum != null) {
                    val newAttempts = attempts + 1
                    attempts = newAttempts
                    message = when {
                        guessNum < targetNumber -> "UP! ⬆️ 더 큰 숫자입니다."
                        guessNum > targetNumber -> "DOWN! ⬇️ 더 작은 숫자입니다."
                        else -> "🎉 정답! ${newAttempts}번 만에 맞췄습니다! 축하합니다!"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("확인", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                targetNumber = Random.nextInt(1, 101)
                guess = ""
                message = "새 게임이 시작되었습니다!"
                attempts = 0
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("새 게임")
        }
    }
}

// 2. 두더지 잡기 게임
@Composable
fun WhackAMoleGame() {
    var score by remember { mutableIntStateOf(0) }
    var activeMole by remember { mutableIntStateOf(-1) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var isPlaying by remember { mutableStateOf(false) }
    var gameKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(gameKey) {
        if (isPlaying) {
            var currentTime = timeLeft
            while (currentTime > 0) {
                delay(1000)
                currentTime = currentTime - 1
                timeLeft = currentTime
                activeMole = Random.nextInt(9)
                delay(600)
                activeMole = -1
            }
            isPlaying = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔨 두더지 잡기", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("점수: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("시간: ${timeLeft}초", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(9) { index ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            if (index == activeMole) Color(0xFF8B4513) else Color(0xFFD2691E),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isPlaying && index == activeMole) {
                                score = score + 1
                                activeMole = -1
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (index == activeMole) {
                        Text("🐹", fontSize = 40.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isPlaying) {
                    score = 0
                    timeLeft = 30
                    isPlaying = true
                    gameKey = gameKey + 1
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isPlaying
        ) {
            Text(if (isPlaying) "게임 중..." else "시작", fontSize = 18.sp)
        }
    }
}

// 3. 카드 짝맞추기 게임
@Composable
fun MemoryCardGame() {
    val emojis = listOf("🍎", "🍌", "🍇", "🍊", "🍓", "🍉", "🍒", "🥝")
    var cards by remember { mutableStateOf((emojis + emojis).shuffled()) }
    var flippedIndices by remember { mutableStateOf(listOf<Int>()) }
    var matchedIndices by remember { mutableStateOf(setOf<Int>()) }
    var moves by remember { mutableIntStateOf(0) }
    var checkKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(checkKey) {
        if (flippedIndices.size == 2) {
            val newMoves = moves + 1
            moves = newMoves
            delay(1000)
            val first = flippedIndices[0]
            val second = flippedIndices[1]
            if (cards[first] == cards[second]) {
                matchedIndices = matchedIndices + first + second
            }
            flippedIndices = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🃏 카드 짝맞추기", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("이동 횟수: ${moves}", fontSize = 18.sp)

        if (matchedIndices.size == cards.size) {
            Text("🎉 완성! ${moves}번 만에 성공!", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(cards.size) { index ->
                val isFlipped = index in flippedIndices || index in matchedIndices
                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            if (flippedIndices.size < 2 && index !in flippedIndices && index !in matchedIndices) {
                                flippedIndices = flippedIndices + index
                                if (flippedIndices.size == 2) {
                                    checkKey = checkKey + 1
                                }
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) Color(0xFFFFF3E0) else Color(0xFF1976D2)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (isFlipped) {
                            Text(cards[index], fontSize = 32.sp)
                        } else {
                            Text("?", fontSize = 32.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                cards = (emojis + emojis).shuffled()
                flippedIndices = emptyList()
                matchedIndices = emptySet()
                moves = 0
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("새 게임")
        }
    }
}

// 4. 틱택토 게임
@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("O") }
    var winner by remember { mutableStateOf<String?>(null) }

    fun checkWinner(): String? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            if (board[line[0]] != "" &&
                board[line[0]] == board[line[1]] &&
                board[line[1]] == board[line[2]]) {
                return board[line[0]]
            }
        }
        if (board.all { it != "" }) return "무승부"
        return null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("⭕ 틱택토", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (winner != null) {
            Text(
                if (winner == "무승부") "무승부!" else "$winner 승리!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text("현재 차례: $currentPlayer", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(9) { index ->
                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            if (board[index] == "" && winner == null) {
                                board = board.toMutableList().apply { this[index] = currentPlayer }
                                winner = checkWinner()
                                if (winner == null) {
                                    currentPlayer = if (currentPlayer == "O") "X" else "O"
                                }
                            }
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            board[index],
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (board[index] == "O") Color.Blue else Color.Red
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                board = List(9) { "" }
                currentPlayer = "O"
                winner = null
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("새 게임")
        }
    }
}

// 5. 벽돌깨기 게임
@Composable
fun BrickBreakerGame() {
    var score by remember { mutableIntStateOf(0) }
    var bricks by remember { mutableStateOf((0 until 20).toList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🧱 벽돌깨기", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("점수: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (bricks.isEmpty()) {
            Text("🎉 클리어!", fontSize = 24.sp, modifier = Modifier.padding(32.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(20) { index ->
                if (index in bricks) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1.5f)
                            .background(
                                Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                bricks = bricks - index
                                score = score + 10
                            }
                    )
                }
            }
        }

        Button(
            onClick = {
                bricks = (0 until 20).toList()
                score = 0
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("새 게임")
        }
    }
}

// 6. 뱀 게임
@Composable
fun SnakeGame() {
    var snake by remember { mutableStateOf(listOf(Pair(5, 5))) }
    var food by remember { mutableStateOf(Pair(10, 10)) }
    var direction by remember { mutableStateOf(Pair(1, 0)) }
    var score by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var gameKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(gameKey) {
        while (isPlaying) {
            delay(200)
            val head = snake.first()
            val newHead = Pair(head.first + direction.first, head.second + direction.second)

            if (newHead.first < 0 || newHead.first >= 15 || newHead.second < 0 || newHead.second >= 15 || newHead in snake) {
                isPlaying = false
                break
            }

            if (newHead == food) {
                score = score + 1
                food = Pair(Random.nextInt(15), Random.nextInt(15))
                snake = listOf(newHead) + snake
            } else {
                snake = listOf(newHead) + snake.dropLast(1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🐍 뱀 게임", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("점수: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(15),
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            items(225) { index ->
                val x = index % 15
                val y = index / 15
                val pos = Pair(x, y)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            when {
                                pos == snake.first() -> Color.Green
                                pos in snake -> Color(0xFF90EE90)
                                pos == food -> Color.Red
                                else -> Color(0xFFE0E0E0)
                            }
                        )
                        .border(0.5.dp, Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { direction = Pair(0, -1) }) { Text("↑") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { direction = Pair(-1, 0) }) { Text("←") }
            Button(onClick = { direction = Pair(1, 0) }) { Text("→") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { direction = Pair(0, 1) }) { Text("↓") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isPlaying) {
                    snake = listOf(Pair(5, 5))
                    food = Pair(10, 10)
                    direction = Pair(1, 0)
                    score = 0
                    isPlaying = true
                    gameKey = gameKey + 1
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isPlaying
        ) {
            Text(if (isPlaying) "게임 중..." else "시작")
        }
    }
}