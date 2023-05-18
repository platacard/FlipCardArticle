package com.example.flipcardarticle.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.flipcardarticle.utils.normalizeAngle
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlin.math.abs

private val CardHorizontalPadding = 40.dp

@Composable
internal fun FlippableCardContainer() {
    val density = LocalDensity.current

    val cardInteractionSource = remember { MutableInteractionSource() }

    var targetAngle by remember { mutableStateOf(0f) }

    val rotation by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(1000),
    )

    val frontSideIsShowing = abs(rotation.normalizeAngle()) !in 90f..270f

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = screenWidth.dp - CardHorizontalPadding * 2

    LaunchedEffect(frontSideIsShowing) {
        cardInteractionSource.interactions
            .filterIsInstance<PressInteraction.Release>()
            .map {
                val offsetInDp = with(density) {
                    it.press.pressPosition.x.toDp()
                }
                val offsetXForContainer =
                    if (frontSideIsShowing) {
                        offsetInDp
                    } else {
                        cardWidth - offsetInDp
                    }
                cardWidth / offsetXForContainer > 2
            }
            .collect { spinClockwise ->
                targetAngle = targetAngle.findNextAngle(spinClockwise)
            }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CardHorizontalPadding),
            rotationAngle = rotation,
            interactionSource = cardInteractionSource,
        )
    }
}

internal fun Float.findNextAngle(spinClockwise: Boolean): Float {
    return if (spinClockwise) this - 180f else this + 180f
}
