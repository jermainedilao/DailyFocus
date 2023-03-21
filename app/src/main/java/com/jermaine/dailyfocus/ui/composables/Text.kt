package com.jermaine.dailyfocus.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.Dark
import com.jermaine.dailyfocus.ui.theme.Dark60

@Composable
fun Headline6Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Dark,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun Body2Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Dark60,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun ButtonText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Dark60,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun OverlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Dark60,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Preview(showBackground = true)
@Composable
private fun Headline6Preview() {
    DailyFocusTheme {
        Headline6Text(text = "Headline6")
    }
}

@Preview(showBackground = true)
@Composable
private fun Body2Preview() {
    DailyFocusTheme {
        Body2Text(text = "Body2")
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    DailyFocusTheme {
        ButtonText(text = "Button")
    }
}

@Preview(showBackground = true)
@Composable
private fun OverlinePreview() {
    DailyFocusTheme {
        OverlineText(text = "Overline")
    }
}