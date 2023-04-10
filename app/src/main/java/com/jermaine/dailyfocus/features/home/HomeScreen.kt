package com.jermaine.dailyfocus.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jermaine.dailyfocus.ui.composables.Body1Text
import com.jermaine.dailyfocus.ui.composables.Body2Text
import com.jermaine.dailyfocus.ui.composables.ContentLinearProgressIndicator
import com.jermaine.dailyfocus.ui.composables.Headline6Text
import com.jermaine.dailyfocus.ui.composables.OverlineText
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.Dark
import com.jermaine.dailyfocus.ui.theme.Gray80
import com.jermaine.dailyfocus.ui.theme.Primary
import com.jermaine.dailyfocus.ui.theme.White
import com.jermaine.dailyfocus.ui.theme.grids
import com.jermaine.dailyfocus.util.DATETIME_FORMATTER_DAY_MONTH_YEAR
import com.jermaine.dailyfocus.util.observeLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate

private typealias OnAddTaskClickListener = () -> Unit
private typealias OnItemCompleteClickListener = (id: Int) -> Unit

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddTaskClick: OnAddTaskClickListener = {}
) {
    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by remember(key1 = LocalDate.now()) {
        mutableStateOf(DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()))
    }

    Scaffold(
        topBar = { HomeScreenTopAppBar(state.items, state.isLoading, date) }
    ) { padding ->
        HomeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = state,
            date = date,
            onAddTaskClick = onAddTaskClick,
            onItemCompleteClick = viewModel::onItemComplete
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    state: HomeUiState,
    date: String,
    onAddTaskClick: OnAddTaskClickListener,
    onItemCompleteClick: OnItemCompleteClickListener,
) {
    Box(modifier = modifier) {
        ContentLinearProgressIndicator(isLoading = state.isLoading)
        if (state.items.isEmpty() && !state.isLoading && !state.isFirstOpen) {
            HomeEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.grids.grid40)
                    .offset(y = (-48).dp),
                date = date,
            ) {
                onAddTaskClick()
            }
        } else {
            TodoList(
                modifier = Modifier.fillMaxSize(),
                items = state.items,
                onItemCompleteClick = onItemCompleteClick,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TodoList(
    modifier: Modifier,
    items: List<TodoUiModel>,
    onItemCompleteClick: OnItemCompleteClickListener,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            TodoItem(item, onItemCompleteClick)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TodoItem(
    item: TodoUiModel,
    onItemCompleteClick: OnItemCompleteClickListener,
) {
    val completed by remember(item.completed) {
        mutableStateOf(item.completed)
    }

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
        Row(
            modifier = Modifier
                .padding(MaterialTheme.grids.grid16)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
        ) {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                Checkbox(
                    modifier = Modifier
                        .padding(top = 2.dp),
                    checked = completed,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Primary,
                        uncheckedColor = Gray80,
                        checkmarkColor = White,
                    ),
                    onCheckedChange = { onItemCompleteClick(item.id) }
                )
            }
            Column(
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

@Composable
private fun HomeEmptyState(
    modifier: Modifier = Modifier,
    date: String,
    onAddTaskClick: OnAddTaskClickListener = {},
) {
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
            onClick = { onAddTaskClick() }
        ) {
            Text(
                text = stringResource(id = R.string.action_add_a_task).uppercase(),
                style = MaterialTheme.typography.labelLarge,
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
    Surface {
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

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun TodoItemPreview() {
    DailyFocusTheme {
        TodoItem(item = previewData().first()) {

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

@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    DailyFocusTheme {
        TodoList(modifier = Modifier.fillMaxSize(), items = previewData()) {

        }
    }
}

private fun previewData() = listOf(
    TodoUiModel(
        id = 1,
        title = "Wash the dishes",
        dueDisplayText = "9:00 AM",
        completed = true,
    ),
    TodoUiModel(
        id = 2,
        title = "Do laundry",
        dueDisplayText = "9:00 AM",
        completed = false,
    ),
    TodoUiModel(
        id = 3,
        title = "Walk the dog",
        dueDisplayText = "9:00 AM",
        completed = false,
    )
)
