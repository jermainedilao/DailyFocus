package com.jermaine.dailyfocus.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.components.todolist.OnItemClickListener
import com.jermaine.dailyfocus.components.todolist.OnItemCompleteClickListener
import com.jermaine.dailyfocus.components.todolist.TodoList
import com.jermaine.dailyfocus.ui.composable.ContentLinearProgressIndicator
import com.jermaine.dailyfocus.ui.composable.Headline6Text
import com.jermaine.dailyfocus.ui.composable.OverlineText
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.grids
import com.jermaine.dailyfocus.util.DATETIME_FORMATTER_DAY_MONTH_YEAR
import com.jermaine.dailyfocus.util.observeLifecycle
import com.jermaine.dailyfocus.util.todoUiModelPreviewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.util.UUID

private typealias OnAddTaskClickListener = () -> Unit
private typealias OnViewTaskClickListener = (id: UUID) -> Unit
private typealias OnViewArchivesClickListener = () -> Unit

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    onAddTaskClick: OnAddTaskClickListener = {},
    onViewTaskClick: OnViewTaskClickListener = {},
    onViewArchivesClick: OnViewArchivesClickListener = {},
) {
    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val date by remember(key1 = LocalDate.now()) {
        mutableStateOf(DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()))
    }

    Scaffold(
        topBar = { HomeScreenTopAppBar(state.items, state.isLoading, date) },
        bottomBar = {
            HomeScreenBottomAppBar(
                isListNotEmpty = state.items.isNotEmpty(),
                onAddTaskClick = onAddTaskClick,
                onViewArchivesClick = onViewArchivesClick,
            )
        },
    ) { padding ->
        HomeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = state,
            date = date,
            onAddTaskClick = onAddTaskClick,
            onItemCompleteClick = viewModel::onItemComplete,
            onItemClick = {
                onViewTaskClick.invoke(it.id)
            },
        )
    }
}

@Composable
fun HomeScreenBottomAppBar(
    isListNotEmpty: Boolean,
    onAddTaskClick: OnAddTaskClickListener,
    onViewArchivesClick: OnViewArchivesClickListener,
) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = {
                    onViewArchivesClick.invoke()
                },
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_archive),
                    "Archives",
                )
            }
        },
        floatingActionButton = {
            if (isListNotEmpty) {
                FloatingActionButton(
                    onClick = { onAddTaskClick.invoke() },
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Add",
                    )
                }
            }
        },
    )
}

@ExperimentalMaterial3Api
@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    state: HomeUiState,
    date: String,
    onAddTaskClick: OnAddTaskClickListener,
    onItemCompleteClick: OnItemCompleteClickListener,
    onItemClick: OnItemClickListener,
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
                onItemClick = onItemClick,
            )
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
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Image(
            painter = painterResource(R.drawable.image_home_empty),
            contentDescription = "Illustration for home empty state",
        )
        OverlineText(
            text = date,
            color = MaterialTheme.colorScheme.primary,
        )
        Headline6Text(
            text = stringResource(id = R.string.title_home_empty),
            textAlign = TextAlign.Center,
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            shape = RoundedCornerShape(50.dp),
            onClick = { onAddTaskClick() },
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
    date: String,
) {
    Surface {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            title = {
                Column(modifier = Modifier.wrapContentSize()) {
                    if (todos.isNotEmpty() || isLoading) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
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

@Preview(showBackground = true)
@Composable
private fun HomeEmptyStatePreview() {
    DailyFocusTheme {
        HomeEmptyState(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.grids.grid40),
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()),
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
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()),
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    DailyFocusTheme {
        HomeScreenTopAppBar(
            todos = todoUiModelPreviewData(),
            isLoading = false,
            date = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()),
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun BottomAppBarPreview() {
    DailyFocusTheme {
        HomeScreenBottomAppBar(
            isListNotEmpty = true,
            onAddTaskClick = {},
            onViewArchivesClick = {},
        )
    }
}
