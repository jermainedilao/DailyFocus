package com.jermaine.dailyfocus.feature.addtask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.ui.composable.Body2Text
import com.jermaine.dailyfocus.ui.composable.ButtonText
import com.jermaine.dailyfocus.ui.composable.Headline6Text
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.grids
import com.jermaine.dailyfocus.util.TIME_FORMATTER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.time.LocalTime
import java.util.UUID

private typealias OnAddTaskCompleteListener = () -> Unit
private typealias OnCloseClickListener = () -> Unit
private typealias OnSaveClickListener = () -> Unit
private typealias OnTimeSetListener = (LocalTime) -> Unit
private typealias OnDismissListener = () -> Unit
private typealias OnTitleChangedListener = (TextFieldValue) -> Unit
private typealias OnCompleteTaskListener = () -> Unit
private typealias OnDeleteTaskListener = () -> Unit

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalMaterial3Api
@Composable
fun AddTaskScreen(
    id: UUID?,
    viewModel: AddTaskViewModel = hiltViewModel(),
    onAddTaskCompleteListener: OnAddTaskCompleteListener,
    onCloseClickListener: OnCloseClickListener,
) {
    val isEdit by remember(id) {
        derivedStateOf { id != null }
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnAddTaskCompleteListener by rememberUpdatedState(onAddTaskCompleteListener)

    var titleState by remember(state.todo?.title) {
        mutableStateOf(state.todo?.title.orEmpty())
    }
    var dueState by remember(state.todo?.due) {
        mutableStateOf(state.todo?.due)
    }

    state.events?.let { events ->
        LaunchedEffect(events) {
            events.firstOrNull {
                it in arrayOf(
                    AddTaskUiEvent.SaveComplete,
                    AddTaskUiEvent.DeleteSuccessful,
                )
            }?.let {
                currentOnAddTaskCompleteListener.invoke()
                viewModel.consumeEvent(it)
            }
        }
    }

    LaunchedEffect(isEdit, id) {
        id?.let(viewModel::loadTodo)
    }

    Scaffold(
        topBar = {
            AddTaskScreenTopAppBar(
                onCloseClickListener = {
                    onCloseClickListener.invoke()
                },
                onSaveClickListener = {
                    val title = titleState
                    val due = dueState

                    if (title.isNotEmpty() && due != null) {
                        viewModel.saveTodo(
                            id = id,
                            title = title,
                            due = due,
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (isEdit) {
                AddTaskScreenBottomAppBar(
                    state = state,
                    onDeleteTaskListener = {
                        id?.let(viewModel::deleteTodo)
                    },
                    onCompleteTask = {
                        id?.let(viewModel::completeTodo)
                    },
                )
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AddTaskContent(
                isEdit = isEdit,
                state = state,
                onTitleChangedListener = {
                    titleState = it.text
                },
                onTimeSetListener = {
                    dueState = it
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AddTaskContent(
    isEdit: Boolean,
    state: AddTaskUiState,
    onTitleChangedListener: OnTitleChangedListener,
    onTimeSetListener: OnTimeSetListener,
) {
    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var due by remember(state.todo?.due) {
        mutableStateOf(state.todo?.due?.format(TIME_FORMATTER).orEmpty())
    }

    val isComplete by remember(state.todo?.isComplete) {
        mutableStateOf(state.todo?.isComplete)
    }

    if (showTimePicker) {
        TimePickerDialog(
            state.todo?.due,
            onDismissListener = {
                showTimePicker = false
            },
            onTimeSetListener = { selectedTime ->
                due = TIME_FORMATTER.format(selectedTime)
                onTimeSetListener.invoke(selectedTime)
                showTimePicker = false
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.grids.grid16),
    ) {
        TitleTextField(
            modifier = Modifier.fillMaxWidth(),
            text = remember(state.todo?.title) {
                state.todo?.title.orEmpty()
            },
            onTitleChanged = onTitleChangedListener,
            isEdit = isEdit,
            isComplete = isComplete ?: false,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    showTimePicker = true
                }
                .padding(
                    top = MaterialTheme.grids.grid8,
                    bottom = MaterialTheme.grids.grid8,
                ),
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.grids.grid16,
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "Set time",
            )
            Body2Text(
                text = due.ifEmpty {
                    stringResource(id = R.string.hint_set_time)
                },
                color = if (due.isEmpty()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                textDecoration = if (isComplete == true) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TimePickerDialog(
    due: LocalTime?,
    onDismissListener: OnDismissListener,
    onTimeSetListener: OnTimeSetListener,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = due?.hour ?: LocalTime.now().hour,
        initialMinute = due?.minute ?: 0,
        is24Hour = false,
    )

    Dialog(
        onDismissRequest = {
            onDismissListener.invoke()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(MaterialTheme.grids.grid24),
        ) {
            TimePicker(state = timePickerState)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.grids.grid8,
                    Alignment.End,
                ),
            ) {
                TextButton(
                    onClick = {
                        onDismissListener.invoke()
                    },
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.action_cancel),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                TextButton(
                    onClick = {
                        onTimeSetListener.invoke(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute,
                            ),
                        )
                    },
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.action_set),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TitleTextField(
    isEdit: Boolean,
    text: String,
    isComplete: Boolean,
    modifier: Modifier,
    onTitleChanged: (TextFieldValue) -> Unit,
) {
    var titleState by remember(text) {
        mutableStateOf(TextFieldValue(text))
    }
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(isEdit) {
        if (!isEdit) {
            focusRequester.requestFocus()
        }
    }

    TextField(
        modifier = modifier
            .offset(
                x = -MaterialTheme.grids.grid16,
                y = -MaterialTheme.grids.grid8,
            )
            .focusRequester(focusRequester),
        value = titleState,
        onValueChange = {
            titleState = it
            onTitleChanged.invoke(it)
        },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            textDecoration = if (isComplete) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
        ),
        singleLine = true,
        placeholder = {
            Headline6Text(
                text = stringResource(id = R.string.hint_task_title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
        ),
    )
}

@ExperimentalMaterial3Api
@Composable
private fun AddTaskScreenTopAppBar(
    onCloseClickListener: OnCloseClickListener,
    onSaveClickListener: OnSaveClickListener,
) {
    Surface {
        TopAppBar(
            title = {
                // no-op
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onCloseClickListener.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = { onSaveClickListener.invoke() },
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.action_save),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            },
        )
    }
}

@Composable
fun AddTaskScreenBottomAppBar(
    state: AddTaskUiState,
    onDeleteTaskListener: OnDeleteTaskListener,
    onCompleteTask: OnCompleteTaskListener,
) {
    val isComplete by remember(state.todo?.isComplete) {
        mutableStateOf(state.todo?.isComplete)
    }

    BottomAppBar(
        actions = {
            IconButton(
                onClick = {
                    onDeleteTaskListener.invoke()
                },
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    "Delete todo",
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    ButtonText(
                        text = if (isComplete == false) {
                            stringResource(id = R.string.action_mark_as_done)
                        } else {
                            stringResource(id = R.string.action_unmark_as_done)
                        },
                        color = contentColorFor(BottomAppBarDefaults.bottomAppBarFabColor),
                    )
                },
                icon = {
                    Icon(
                        Icons.Outlined.Check,
                        "Complete todo",
                    )
                },
                onClick = { onCompleteTask.invoke() },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            )
        },
    )
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun BottomAppBarPreview() {
    DailyFocusTheme {
        AddTaskScreenBottomAppBar(
            AddTaskUiState(null, null),
            {
            },
            {
            },
        )
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TopAppBarPreview() {
    DailyFocusTheme {
        AddTaskScreenTopAppBar(
            onCloseClickListener = {
            },
            onSaveClickListener = {
            },
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun AddTaskContentPreview() {
    DailyFocusTheme {
        AddTaskContent(
            isEdit = true,
            state = AddTaskUiState(null, null),
            onTitleChangedListener = {
            },
            onTimeSetListener = {
            },
        )
    }
}
