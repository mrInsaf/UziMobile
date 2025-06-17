package com.mrinsaf.profile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    activeColor: Color = MaterialTheme.colorScheme.secondary,
    inactiveColor: Color = activeColor.copy(alpha = 0.3f),
    indicatorWidth: Dp = 8.dp,
    inactiveIndicatorWidth: Dp = 6.dp,
    spacing: Dp = 8.dp,
    onDotClick: ((page: Int) -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (page in 0 until pageCount) {
            // анимируем цвет
            val color by animateColorAsState(
                targetValue = if (page == currentPage) activeColor else inactiveColor
            )
            // анимируем размер
            val size by animateDpAsState(
                targetValue = if (page == currentPage) indicatorWidth else inactiveIndicatorWidth
            )

            Spacer(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
                    .then(
                        if (onDotClick != null) {
                            Modifier.clickable { onDotClick(page) }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}