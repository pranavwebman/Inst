package com.instaeducate.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.instaeducate.app.ui.theme.GlassBorderDark
import com.instaeducate.app.ui.theme.GlassBorderLight

@Composable
fun Modifier.glassmorphicCard(
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    isDark: Boolean = true
): Modifier {
    val backgroundColor = if (isDark) {
        Color(0x261E293B) // Dark slate with low alpha
    } else {
        Color(0x66FFFFFF) // Pure white with high transparency
    }
    
    val borderColor = if (isDark) GlassBorderDark else GlassBorderLight
    
    return this
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(cornerRadius),
            clip = false,
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.2f)
        )
        .clip(RoundedCornerShape(cornerRadius))
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    backgroundColor,
                    backgroundColor.copy(alpha = 0.08f)
                )
            )
        )
        .border(
            width = borderWidth,
            brush = Brush.verticalGradient(
                colors = listOf(
                    borderColor,
                    borderColor.copy(alpha = 0.02f)
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
}
