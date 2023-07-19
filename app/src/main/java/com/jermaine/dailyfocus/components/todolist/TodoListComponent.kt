package com.jermaine.dailyfocus.components.todolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jermaine.dailyfocus.feature.home.TodoUiModel
import com.jermaine.dailyfocus.ui.composable.Body1Text
import com.jermaine.dailyfocus.ui.composable.Body2Text
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.grids
import com.jermaine.dailyfocus.util.todoUiModelPreviewData
import java.util.UUID

typealias OnItemCompleteClickListener = (id: UUID) -> Unit
typealias OnItemClickListener = (todo: TodoUiModel) -> Unit

@ExperimentalMaterial3Api
@Composable
fun TodoList(
    modifier: Modifier,
    isArchives: Boolean = false,
    items: List<TodoUiModel>,
    onItemCompleteClick: OnItemCompleteClickListener,
    onItemClick: OnItemClickListener,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items) { item ->
            TodoItem(
                item = item,
                isArchives = isArchives,
                onItemCompleteClick = onItemCompleteClick,
                onItemClick = onItemClick,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TodoItem(
    item: TodoUiModel,
    isArchives: Boolean,
    onItemCompleteClick: OnItemCompleteClickListener,
    onItemClick: OnItemClickListener,
) {
    val completed by remember(item.isComplete) {
        mutableStateOf(item.isComplete)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        enabled = !isArchives,
        onClick = {
            onItemClick.invoke(item)
        },
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.grids.grid16)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    modifier = Modifier
                        .padding(top = 2.dp),
                    checked = completed,
                    enabled = !isArchives,
                    onCheckedChange = { onItemCompleteClick(item.id) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.grids.grid4,
                    Alignment.CenterVertically,
                ),
            ) {
                Body1Text(
                    text = item.title,
                    color = if (item.isComplete) {
                        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = .50f)
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    },
                    textDecoration = if (item.isComplete) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                )
                Body2Text(
                    text = item.dueDisplayText,
                    color = if (item.isComplete) {
                        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = .50f)
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    },
                    textDecoration = if (item.isComplete) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun TodoItemPreview() {
    DailyFocusTheme {
        TodoItem(
            item = todoUiModelPreviewData().first(),
            false,
            {},
            {},
        )
    }
}

@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    DailyFocusTheme {
        TodoList(
            modifier = Modifier.fillMaxSize(),
            items = todoUiModelPreviewData(),
            onItemClick = {},
            onItemCompleteClick = {},
        )
    }
}
