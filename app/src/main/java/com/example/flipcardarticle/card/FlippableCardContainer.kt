package com.example.flipcardarticle.card

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.flipcardarticle.utils.normalizeAngle
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlin.math.abs
import kotlin.math.sign

private val CardHorizontalPadding = 40.dp

@Composable
internal fun FlippableCardContainer() {
    val density = LocalDensity.current

    val cardInteractionSource = remember { MutableInteractionSource() }

    var targetAngle by remember { mutableStateOf(0f) }

    var dragInProgress by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = spring(
            stiffness = if (dragInProgress) Spring.StiffnessHigh else Spring.StiffnessLow,
        ),
    )

    val rotationAngleState = remember {
        derivedStateOf { rotation }
    }

    val frontSideIsShowing by remember {
        derivedStateOf {
            abs(rotationAngleState.value.normalizeAngle()) !in 90f..270f
        }
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = screenWidth.dp - CardHorizontalPadding * 2

    val diff = 180f / cardWidth.value

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
        val draggableState = rememberDraggableState(
            onDelta = { offsetX ->
                val calculatedAngle = calculateAngle(offsetX, density, diff)
                targetAngle += calculatedAngle
            },
        )
        Calendar(
            modifier = Modifier
                .padding(horizontal = 80.dp)
                .aspectRatio(0.7f)
                .offset(y = 150.dp)
                .then(
                    remember {
                        Modifier.draggable(
                            orientation = Orientation.Vertical,
                            onDragStarted = {
                                dragInProgress = true
                            },
                            onDragStopped = { lastVelocity ->
                                dragInProgress = false
                                targetAngle = targetAngle.findNearAngle(velocity = lastVelocity)
                            },
                            state = draggableState,
                        )
                    },
                ),
            rotationAngleState = rotationAngleState,
        )
    }
}

private fun Float.findNextAngle(spinClockwise: Boolean): Float {
    return if (spinClockwise) this - 180f else this + 180f
}

private fun Float.findNearAngle(velocity: Float): Float {
    val velocityAddition = if (abs(velocity) > 1000) {
        90f * velocity.sign
    } else {
        0f
    }
    val normalizedAngle = this.normalizeAngle() + velocityAddition
    val minimalAngle = (this / 360f).toInt() * 360f
    return when {
        normalizedAngle in -90f..90f -> minimalAngle
        abs(normalizedAngle) >= 270f -> minimalAngle + 360f * this.sign
        abs(normalizedAngle) >= 90f -> minimalAngle + 180f * this.sign
        else -> 0f
    }
}

private fun calculateAngle(offsetX: Float, density: Density, diff: Float): Float {
    val offsetInDp = with(density) { offsetX.toDp() }
    return offsetInDp.value * diff
}
