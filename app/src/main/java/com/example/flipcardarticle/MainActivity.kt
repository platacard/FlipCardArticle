package com.example.flipcardarticle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flipcardarticle.card.FlippableCardContainer
import com.example.flipcardarticle.ui.theme.FlipCardArticleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipCardArticleTheme {
                FlippableCardContainer()
            }
        }
    }
}
