package com.example.vardadienas.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.delay

val descriptionFunnies: List<String> = listOf(
    "Tīri tā, lai ērtāk.",
    "Mammai patīk.",
    "Čau kakau!",
    "Katrai dienai ir vārds un daudziem vārdiem ir diena.",
    "Ražots Beļģijā, ražoja latvietis.",
    "Vai vārdadienas raksta kopā vai atsevišķi?",
    "Vispopulārākais vārds Latvijā nav Atvars.",
    "Vispopulārākais vārds Latvijā varbūt ir Jānis.",
    "Java edition",
    "20% vairāk vārdu nekā citās aplikācijās!",
    "7 no 13 lietotājiem ieteica saviem draugiem.",
    "I <3 Latvia",
    "Live, Love Latvia",
    "502 Bad Gateway",
    "Lielākoties strādā.",
    "Mākslīgā intelekta palīgs nāks drīzumā!"
    )
@Composable
fun WelcomeScreen() {
    var visible by remember { mutableStateOf(false) } // For sliding in when the app is started
    val animatedOffsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else (-200).dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow), // Adjust duration as needed
        label = "offsetAnimation"
    )
    val animatedOffsetX by animateDpAsState(
        targetValue = if (visible) 0.dp else (-200).dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessVeryLow), // Start 200ms later
        label = "offsetAnimation2"
    )

    LaunchedEffect(key1 = true) { // Trigger the animation when this composable is composed
        // delay(100)
        visible = true
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Vārdadienas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.offset(y = animatedOffsetY))
        FadingText(texts = descriptionFunnies,
            periodMillis = 5000,
            animationDurationMillis = 1000,
            modifier = Modifier.offset(x = animatedOffsetX))
    }
    // TODO: Add name searching functionality + Today's namedays
}

@Composable
fun FadingText(
    texts: List<String>,
    modifier: Modifier = Modifier, // Accept a modifier
    periodMillis: Long = 3000,
    animationDurationMillis: Int = 750
) {
    val shuffledTexts = remember { texts.shuffled() }
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(periodMillis)
            currentIndex = (currentIndex + 1) % shuffledTexts.size
        }
    }

    Crossfade(
        targetState = currentIndex,
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "FadingTextAnimation",
        modifier = modifier // Apply the passed modifier here
    ) { index ->
        // ... rest of the FadingText composable is the same
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = shuffledTexts[index],
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}