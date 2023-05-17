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

private const val BankCardAspectRatio = 1.5819f

@Composable
internal fun Card(modifier: Modifier = Modifier) {
    val sideModifier =
        modifier
            .widthIn(min = 240.dp)
            .aspectRatio(BankCardAspectRatio)
            .clip(shape = RoundedCornerShape(20.dp))

    Box {
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = 1f
                }
                .background(Color.Red),
        )
        Box(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = 0f
                    rotationY = 180f
                }
                .background(Color.Blue),
        )
    }
}
