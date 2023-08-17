package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun LinkText(modifier: Modifier = Modifier, text: String, url: String) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = text.count() // end is exclusive
        )

        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = text.count() // end is exclusive
        )
    }

    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = modifier,
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
