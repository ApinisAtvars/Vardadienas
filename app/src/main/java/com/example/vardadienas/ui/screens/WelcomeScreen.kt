package com.example.vardadienas.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    "Lielākoties strādā."
    )
@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Vārdadienas", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        FadingText(texts = descriptionFunnies, periodMillis = 5000, animationDurationMillis = 1000)
    }
    // TODO: Add name searching functionality + Today's namedays
}

@Composable
fun FadingText(
    texts: List<String>,
    periodMillis: Long = 3000,
    animationDurationMillis: Int = 750
) {
    // 1. Create a shuffled version of the list that is remembered across recompositions.
    // The `shuffled()` function will only be called ONCE when this composable
    // is first added to the composition.
    val shuffledTexts = remember { texts.shuffled() }

    // 2. The state that holds the current index in the *shuffled* list.
    // We can safely start from 0 now.
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(periodMillis)
            // Update the index, using the size of our shuffled list.
            currentIndex = (currentIndex + 1) % shuffledTexts.size
        }
    }

    Crossfade(
        targetState = currentIndex,
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "FadingTextAnimation"
    ) { index ->
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                // 3. Use the shuffled list to get the text.
                text = shuffledTexts[index],
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}