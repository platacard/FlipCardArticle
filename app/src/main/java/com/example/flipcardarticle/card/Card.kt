package com.example.flipcardarticle.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private const val BankCardAspectRatio = 1.5819f

@Composable
internal fun Card(
    rotationAngle: Float,
    modifier: Modifier = Modifier,
) {
    val sideModifier =
        modifier
            .widthIn(min = 240.dp)
            .aspectRatio(BankCardAspectRatio)
            .graphicsLayer {
                rotationY = rotationAngle
                cameraDistance = 12.dp.toPx()
            }
            .clip(shape = RoundedCornerShape(20.dp))

    val normalizedAngle = abs(rotationAngle % 360f)
    val needRenderBackSide = normalizedAngle in 90f..270f

    Box {
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide) 0f else 1f
                }
                .background(Color.Red),
        )
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide) 1f else 0f
                    rotationY = 180f
                }
                .background(Color.Blue),
        )
    }
}
