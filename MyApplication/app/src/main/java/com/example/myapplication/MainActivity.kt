package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LibraryApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Book(
    val title: String,
    val author: String,
    val publisher: String,
    val pubYear: String,
    val isbn: String = ""
)

// 여기에 책 데이터를 추가하세요!
val sampleBooks = listOf(
    Book("코틀린 인 액션", "드미트리 제메로프, 스베트라나 이사코바", "에이콘출판", "2017", "9788960779006"),
    Book("클린 코드", "로버트 C. 마틴", "인사이트", "2013", "9788966260959"),
    Book("이펙티브 자바", "조슈아 블로크", "인사이트", "2018", "9788966262281"),
    Book("헤드 퍼스트 디자인 패턴", "에릭 프리먼, 엘리자베스 롭슨", "한빛미디어", "2022", "9791162245453"),
    Book("리팩터링", "마틴 파울러", "한빛미디어", "2020", "9791162242742"),
    Book("실용주의 프로그래머", "데이비드 토머스, 앤드류 헌트", "인사이트", "2014", "9788966261031"),
    Book("알고리즘 도감", "이시다 모리테루", "제이펍", "2017", "9791185890845"),
    Book("자바의 정석", "남궁성", "도우출판", "2016", "9788994492032"),
    Book("모던 자바 인 액션", "라울-게이브리얼 우르마", "한빛미디어", "2019", "9791162242025"),
    Book("오브젝트", "조영호", "위키북스", "2019", "9791158391409"),
    Book("IT 엔지니어를 위한 네트워크", "야마자키 야스시", "제이펍", "2020", "9791190665070"),
    Book("혼자 공부하는 파이썬", "윤인성", "한빛미디어", "2019", "9791162241882"),
    Book("Do it! 점프 투 파이썬", "박응용", "이지스퍼블리싱", "2019", "9791163030911"),
    Book("클린 아키텍처", "로버트 C. 마틴", "인사이트", "2019", "9788966262472"),
    Book("이것이 자바다", "신용권", "한빛미디어", "2015", "9788968481475")
)

@Composable
fun LibraryApp(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val myBooks = remember { mutableStateListOf<Book>() }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("책 검색") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("내 서재 (${myBooks.size})") }
            )
        }

        when (selectedTab) {
            0 -> SearchTab(
                myBooks = myBooks,
                onAddBook = { book ->
                    if (!myBooks.any { it.isbn == book.isbn && book.isbn.isNotEmpty() }) {
                        myBooks.add(book)
                    }
                }
            )
            1 -> MyLibraryTab(
                myBooks = myBooks,
                onRemoveBook = { book -> myBooks.remove(book) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTab(
    myBooks: List<Book>,
    onAddBook: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredBooks = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            sampleBooks
        } else {
            sampleBooks.filter { book ->
                book.title.contains(searchQuery, ignoreCase = true) ||
                        book.author.contains(searchQuery, ignoreCase = true) ||
                        book.publisher.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("책 제목, 저자, 출판사를 입력하세요") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "검색")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "검색 결과: ${filteredBooks.size}권",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredBooks) { book ->
                val isAdded = myBooks.any {
                    it.isbn == book.isbn && book.isbn.isNotEmpty()
                }
                SearchBookItem(
                    book = book,
                    isAdded = isAdded,
                    onAddClick = { onAddBook(book) }
                )
            }
        }

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "검색 결과가 없습니다",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SearchBookItem(
    book: Book,
    isAdded: Boolean,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "저자: ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${book.publisher} | ${book.pubYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onAddClick,
                enabled = !isAdded
            ) {
                Icon(
                    imageVector = if (isAdded) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (isAdded) "추가됨" else "내 서재에 추가",
                    tint = if (isAdded)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MyLibraryTab(
    myBooks: List<Book>,
    onRemoveBook: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (myBooks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "저장된 책이 없습니다\n책 검색 탭에서 책을 추가해보세요!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = "총 ${myBooks.size}권의 책",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myBooks) { book ->
                    MyBookItem(
                        book = book,
                        onRemoveClick = { onRemoveBook(book) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyBookItem(
    book: Book,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "저자: ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${book.publisher} | ${book.pubYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (book.isbn.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ISBN: ${book.isbn}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

