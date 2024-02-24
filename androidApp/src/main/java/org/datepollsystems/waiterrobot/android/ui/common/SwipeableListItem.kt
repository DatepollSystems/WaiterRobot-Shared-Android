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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableListItem(
    modifier: Modifier = Modifier,
    swipeAdd: (() -> Unit)? = null,
    swipeRemove: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> swipeAdd?.invoke()
                SwipeToDismissBoxValue.EndToStart -> swipeRemove?.invoke()
                else -> Unit
            }
            false // Always return false, as action is repeatable
        }
    )

    val modifiers = remember(onClick, onLongClick) {
        when {
            onLongClick != null -> {
                modifier.combinedClickable(onClick = onClick ?: {}, onLongClick = onLongClick)
            }

            onClick != null -> modifier.clickable(onClick = onClick)
            else -> modifier
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifiers,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            if (direction != SwipeToDismissBoxValue.Settled) {
                val color by animateColorAsState(
                    when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF2ECC71)
                        SwipeToDismissBoxValue.EndToStart -> Color(0xFFE74C3C)
                        SwipeToDismissBoxValue.Settled -> error("Invalid direction")
                    }.let {
                        if (dismissState.targetValue != SwipeToDismissBoxValue.Settled) it else it.copy(
                            0.6f
                        )
                    },
                    label = "DismissColor"
                )
                val alignment = remember(direction) {
                    when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        SwipeToDismissBoxValue.Settled -> error("Invalid direction")
                    }
                }
                val icon = remember(direction) {
                    when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Add
                        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Remove
                        SwipeToDismissBoxValue.Settled -> error("Invalid direction")
                    }
                }
                val description = remember(direction) {
                    when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> "Increase"
                        SwipeToDismissBoxValue.EndToStart -> "Decrease"
                        SwipeToDismissBoxValue.Settled -> error("Invalid direction")
                    }
                }
                val scale by animateFloatAsState(
                    if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.6f else 1f,
                    label = "DismissScale"
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
        },
        enableDismissFromEndToStart = swipeRemove != null,
        enableDismissFromStartToEnd = swipeAdd != null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}
