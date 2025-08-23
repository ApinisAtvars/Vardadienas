package com.example.vardadienas.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun AboutScreen() {
    Column {
        Text(text = "Par šo aplikāciju:", style = MaterialTheme.typography.headlineLarge)
        HorizontalDivider(Modifier.fillMaxWidth().padding(vertical = 16.dp))
        Text(text = buildAnnotatedString {
            append("Viss kods ir pieejams manā GitHub repozitorijā (")
            withLink(
                LinkAnnotation.Url(
                    "https://github.com/ApinisAtvars/Vardadienas",
                    TextLinkStyles(style = SpanStyle(color = Color.LightGray))
                )
            ) {
                append("šeit")
            }
            append(").")
        })

        Text(text = "Vai kaut kas nestrādā?", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))
        // TODO: Create an issue tracking board, and add a link to it here.
//        Text(text = buildAnnotatedString {
//            append("Izskaidro kļūdu manā GitHub dēlī: ")
//            withLink(
//                LinkAnnotation.Url(
//
//                )
//            )
//        })

        Spacer(Modifier.padding(top = 16.dp))
        Text(text = "Dati:", style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider(Modifier.fillMaxWidth().padding(vertical = 16.dp))
        Text(text = buildAnnotatedString {
            append("Personvārdu dati (Skaits, Skaidrojums) iegūti no ")
            withLink(
                LinkAnnotation.Url(
                    "https://personvardi.pmlp.gov.lv/",
                    TextLinkStyles(style = SpanStyle(color = Color.LightGray))
                )
            ) {
                append("PMLP personvārdu datu bāzes")
            }
            append(".")
        },
            style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(buildAnnotatedString {
            append("Vārdadienu datumi iegūti no")
            withLink(
                LinkAnnotation.Url(
                    "https://gist.github.com/laacz/5cccb056a533dffb2165",
                    TextLinkStyles(style = SpanStyle(color = Color.LightGray))
                )
            ) {
                append(" laacz")
            }
            append(" GitHub Gist.")
        }, style = MaterialTheme.typography.bodyLarge)
    }
}