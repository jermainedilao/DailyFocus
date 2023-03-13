package com.jermaine.dailyfocus.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.jermaine.dailyfocus.ui.theme.Dark
import com.jermaine.dailyfocus.ui.theme.Dark60

@Composable
fun Headline6(
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
fun Body2(
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