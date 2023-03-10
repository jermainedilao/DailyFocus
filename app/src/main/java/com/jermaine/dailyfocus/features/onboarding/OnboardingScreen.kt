package com.jermaine.dailyfocus.features.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.ui.composables.Body2
import com.jermaine.dailyfocus.ui.composables.Headline6
import com.jermaine.dailyfocus.ui.theme.*
import com.jermaine.dailyfocus.util.ONBOARDING_PAGE_COUNT
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()
            HorizontalPager(
                count = ONBOARDING_PAGE_COUNT,
                state = pagerState,
                modifier = Modifier.fillMaxHeight(.9f)
            ) { page ->
                OnboardingPage(page = page)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(MaterialTheme.grids.grid16)
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage > 0) {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    enabled = pagerState.currentPage > 0
                ) {
                    Text(
                        text = stringResource(id = R.string.back).uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (pagerState.currentPage > 0) {
                            Primary
                        } else {
                            Primary200
                        },
                    )
                }
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage < ONBOARDING_PAGE_COUNT - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                ) {
                    Text(
                        text = if (pagerState.currentPage == ONBOARDING_PAGE_COUNT - 1) {
                            stringResource(id = R.string.get_started).uppercase()
                        } else {
                            stringResource(id = R.string.next).uppercase()
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    page: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.grids.grid16)
    ) {
        Image(
            painter = painterResource(
                id = when (page) {
                    0 -> R.drawable.image_onboarding_1
                    1 -> R.drawable.image_onboarding_2
                    2 -> R.drawable.image_onboarding_3
                    else -> {
                        throw IllegalArgumentException("Invalid $page!")
                    }
                }
            ),
            contentDescription = "Illustration for $page"
        )
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid24))
        PageIndicators(page = page)
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid32))
        Headline6(
            text = when (page) {
                0 -> stringResource(id = R.string.onboarding_page1_title)
                1 -> stringResource(id = R.string.onboarding_page2_title)
                2 -> stringResource(id = R.string.onboarding_page3_title)
                else -> {
                    throw IllegalArgumentException("Invalid $page!")
                }
            },
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid8))
        Body2(
            text = when (page) {
                0 -> stringResource(id = R.string.onboarding_page1_description)
                else -> ""
            },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PageIndicators(page: Int) {
    Row {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (page == 0) {
                        Primary
                    } else {
                        Primary100
                    }
                )
        )
        Spacer(modifier = Modifier.size(MaterialTheme.grids.grid8))
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (page == 1) {
                        Primary
                    } else {
                        Primary100
                    }
                )
        )
        Spacer(modifier = Modifier.size(MaterialTheme.grids.grid8))
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (page == 2) {
                        Primary
                    } else {
                        Primary100
                    }
                )
        )
    }
}

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DailyFocusTheme {
        OnboardingScreen()
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
    DailyFocusTheme {
        OnboardingPage(
            page = 0,
        )
    }

}