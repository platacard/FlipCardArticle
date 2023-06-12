package com.example.flipcardarticle.card

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private const val BankCardAspectRatio = 1.5819f

@Composable
internal fun Card(
    rotationAngle: State<Float>,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val sideModifier =
        modifier
            .widthIn(min = 240.dp)
            .aspectRatio(BankCardAspectRatio)
            .graphicsLayer {
                rotationY = rotationAngle.value
                cameraDistance = 12.dp.toPx()
            }
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {},
            )

    val needRenderBackSide = remember {
        derivedStateOf {
            val normalizedAngle = abs(rotationAngle.value % 360f)
            normalizedAngle in 90f..270f
        }
    }

    Box {
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide.value) 0f else 1f
                }
                .background(Color.Red),
        )
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide.value) 1f else 0f
                    rotationY = 180f
                }
                .background(Color.Blue),
        )
    }
}
