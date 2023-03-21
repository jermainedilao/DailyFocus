package com.jermaine.dailyfocus.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.ui.composables.Headline6Text
import com.jermaine.dailyfocus.ui.composables.OverlineText
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.Primary
import com.jermaine.dailyfocus.ui.theme.White
import com.jermaine.dailyfocus.ui.theme.grids
import com.jermaine.dailyfocus.util.DATETIME_FORMATTER_DAY_MONTH_YEAR
import java.time.LocalDate

typealias AddTaskClick = () -> Unit

@ExperimentalMaterial3Api
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = White,
                    ),
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // TODO: Insert LazyColumn here
            HomeEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.grids.grid40)
                    .offset(y = (-48).dp)
            ) {
                // TODO: Open add task screen
            }
        }
    }
}

@Composable
private fun HomeEmptyState(
    modifier: Modifier = Modifier,
    onAddTaskClick: AddTaskClick = {}
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
            text = DATETIME_FORMATTER_DAY_MONTH_YEAR.format(LocalDate.now()),
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

@Preview(showBackground = true)
@Composable
private fun HomeEmptyStatePreview() {
    DailyFocusTheme {
        HomeEmptyState(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.grids.grid40)
        )
    }
}

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    DailyFocusTheme {
        HomeScreen()
    }
}
