package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeableListItem(
    modifier: Modifier = Modifier,
    swipeAdd: (() -> Unit)? = null,
    swipeRemove: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.DismissedToEnd -> swipeAdd?.invoke()
                DismissValue.DismissedToStart -> swipeRemove?.invoke()
                else -> Unit
            }
            false // Always return false, as action is repeatable
        }
    )

    val directions = remember(swipeRemove, swipeAdd) {
        when {
            swipeRemove != null && swipeAdd != null -> {
                setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd)
            }
            swipeRemove != null -> setOf(DismissDirection.EndToStart)
            swipeAdd != null -> setOf(DismissDirection.StartToEnd)
            else -> emptySet()
        }
    }

    val modifiers = remember(onClick, onLongClick) {
        when {
            onLongClick != null -> {
                modifier.combinedClickable(onClick = onClick ?: {}, onLongClick = onLongClick)
            }
            onClick != null -> modifier.clickable(onClick = onClick)
            else -> modifier
        }
    }

    SwipeToDismiss(
        state = dismissState,
        modifier = modifiers,
        directions = directions,
        dismissThresholds = { FractionalThreshold(0.16f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

            val color by animateColorAsState(
                when (direction) {
                    DismissDirection.StartToEnd -> Color(0xFF2ECC71)
                    DismissDirection.EndToStart -> Color(0xFFE74C3C)
                }.let {
                    if (dismissState.targetValue != DismissValue.Default) it else it.copy(0.6f)
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Add
                DismissDirection.EndToStart -> Icons.Default.Remove
            }
            val description = when (direction) {
                DismissDirection.StartToEnd -> "Increase"
                DismissDirection.EndToStart -> "Decrease"
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.6f else 1f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = description,
                    modifier = Modifier.scale(scale)
                )
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}
