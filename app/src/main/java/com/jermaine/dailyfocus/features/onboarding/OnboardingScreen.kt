package com.jermaine.dailyfocus.features.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.ui.composables.Body2
import com.jermaine.dailyfocus.ui.composables.Headline6
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.Primary
import com.jermaine.dailyfocus.ui.theme.Primary200
import com.jermaine.dailyfocus.ui.theme.grids

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
            OnboardingPage(
                title = "Hello",
                description = "helloasdfdas",
                page = OnboardingPage.Page1,
                modifier = Modifier
                    .fillMaxHeight(.9f)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(MaterialTheme.grids.grid16)
            ) {
                TextButton(
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(id = R.string.back).uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary200,
                    )
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(id = R.string.next).uppercase(),
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
    title: String,
    description: String,
    page: OnboardingPage,
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
            painter = painterResource(id = R.drawable.image_onboarding_1),
            contentDescription = "Illustration for ${page.name}"
        )
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid24))
        PageIndicators(page = page)
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid32))
        Headline6(text = title)
        Spacer(modifier = Modifier.height(MaterialTheme.grids.grid8))
        Body2(text = description)
    }
}

@Composable
private fun PageIndicators(page: OnboardingPage) {
    Row {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.size(MaterialTheme.grids.grid8))
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.size(MaterialTheme.grids.grid8))
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

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
            title = "Hello",
            description = "hola asdfasdf",
            page = OnboardingPage.Page1,
        )
    }
}

private enum class OnboardingPage {
    Page1,
    Page2,
    Page3
}