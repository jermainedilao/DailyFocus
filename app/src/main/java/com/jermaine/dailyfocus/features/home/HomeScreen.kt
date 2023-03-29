package com.jermaine.dailyfocus.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.ui.composables.*
import com.jermaine.dailyfocus.ui.theme.*
import com.jermaine.dailyfocus.util.DATETIME_FORMATTER_DAY_MONTH_YEAR
import com.jermaine.dailyfocus.util.observeLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate

typealias AddTaskClick = () -> Unit

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: HomeModel = hiltViewModel(),
    onAddTaskClick: AddTaskClick = {}
) {
    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by remember(key1 = LocalDate.now()) {
        mutableStateOf(DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()))
    }
    val currentOnAddTaskClick by rememberUpdatedState(newValue = onAddTaskClick)

    Scaffold(
        topBar = { HomeScreenTopAppBar(state.todoList, state.isLoading, date) }
    ) { padding ->
        HomeScreenContent(padding, state, date, currentOnAddTaskClick)
    }
}

@Composable
private fun HomeScreenContent(
    padding: PaddingValues,
    state: HomeUiState,
    date: String,
    currentOnAddTaskClick: AddTaskClick
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        ContentLinearProgressIndicator(isLoading = state.isLoading)
        if (state.todoList.isEmpty() && !state.isLoading && !state.isFirstOpen) {
            HomeEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.grids.grid40)
                    .offset(y = (-48).dp),
                date = date,
            ) {
                currentOnAddTaskClick()
            }
        } else {
            TodoList(
                modifier = Modifier.fillMaxSize(),
                items = state.todoList
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun HomeScreenTopAppBar(
    todos: List<TodoUiModel>,
    isLoading: Boolean,
    date: String
) {
    Surface(shadowElevation = 4.dp) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = White,
            ),
            title = {
                Column(modifier = Modifier.wrapContentSize()) {
                    if (todos.isNotEmpty() || isLoading) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary
                        )
                    }
                    Text(
                        text = stringResource(R.string.title_things_to_do),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            },
        )
    }
}

@Composable
private fun TodoList(
    modifier: Modifier,
    items: List<TodoUiModel>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.grids.grid16),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.grids.grid4,
                        Alignment.CenterVertically
                    )
                ) {
                    Body1Text(text = item.title, color = Dark)
                    Body2Text(text = item.dueDisplayText, color = Primary)
                }
            }
        }
    }
}

@Composable
private fun HomeEmptyState(
    modifier: Modifier = Modifier,
    date: String,
    onAddTaskClick: AddTaskClick = {},
) {
    val currentOnAddTaskClick by rememberUpdatedState(onAddTaskClick)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.grids.grid16,
            alignment = Alignment.CenterVertically
        ),
    ) {
        Image(
            painter = painterResource(R.drawable.image_home_empty),
            contentDescription = "Illustration for home empty state"
        )
        OverlineText(
            text = date,
            color = Primary
        )
        Headline6Text(
            text = stringResource(id = R.string.title_home_empty),
            textAlign = TextAlign.Center
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = White,
            ),
            shape = RoundedCornerShape(50.dp),
            onClick = { currentOnAddTaskClick() }
        ) {
            Text(
                text = stringResource(id = R.string.action_add_a_task).uppercase(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeEmptyStatePreview() {
    DailyFocusTheme {
        HomeEmptyState(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.grids.grid40),
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now())
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun TopAppBarEmptyPreview() {
    DailyFocusTheme {
        HomeScreenTopAppBar(
            todos = emptyList(),
            isLoading = false,
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now())
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    DailyFocusTheme {
        HomeScreenTopAppBar(
            todos = previewData(),
            isLoading = false,
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now())
        )
    }
}

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    DailyFocusTheme {
        TodoList(modifier = Modifier.fillMaxSize(), items = previewData())
    }
}

private fun previewData() = listOf(
    TodoUiModel(
        id = 1,
        title = "Wash the dishes",
        dueDisplayText = "9:00 AM"
    ),
    TodoUiModel(
        id = 2,
        title = "Do laundry",
        dueDisplayText = "9:00 AM"
    ),
    TodoUiModel(
        id = 3,
        title = "Walk the dog",
        dueDisplayText = "9:00 AM"
    )
)
