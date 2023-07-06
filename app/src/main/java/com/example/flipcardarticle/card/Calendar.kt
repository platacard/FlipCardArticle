package com.example.flipcardarticle.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flipcardarticle.R
import kotlin.math.abs

@Composable
fun Calendar(
    modifier: Modifier,
    rotationAngleState: State<Float>,
) {
    val sideModifier =
        modifier
            .graphicsLayer {
                rotationX = -rotationAngleState.value
                cameraDistance = 12.dp.toPx()
                transformOrigin = TransformOrigin(pivotFractionY = 0f, pivotFractionX = 0.5f)
            }

    val needRenderBackSide = remember {
        derivedStateOf {
            val normalizedAngle = abs(rotationAngleState.value % 360f)
            normalizedAngle in 90f..270f
        }
    }
    Box {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.third_sep),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Image(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide.value) 1f else 0f
                    rotationX = 180f
                },
            painter = painterResource(id = R.drawable.shuf),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Image(
            modifier = sideModifier
                .graphicsLayer {
                    alpha = if (needRenderBackSide.value) 0f else 1f
                },
            painter = painterResource(id = R.drawable.sec_sep),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
    }
}
