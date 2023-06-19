package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterListOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableGroupFilter(
    selectedGroups: List<TableGroup>,
    unselectedGroups: List<TableGroup>,
    onToggle: (TableGroup) -> Unit,
    clearFilter: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(
                items = selectedGroups,
                key = { i, group -> "$i-${group.id}-selected" }
            ) { _, group ->
                Button(
                    modifier = Modifier.animateItemPlacement(),
                    onClick = { onToggle(group) },
                ) {
                    Text(group.name)
                }
            }

            itemsIndexed(
                items = unselectedGroups,
                key = { i, group -> "$i-${group.id}-unselected" }
            ) { _, group ->
                OutlinedButton(
                    modifier = Modifier.animateItemPlacement(),
                    onClick = { onToggle(group) },
                ) {
                    Text(group.name)
                }
            }
        }

        Box {
            IconButton(
                onClick = clearFilter,
                enabled = selectedGroups.isNotEmpty()
            ) {
                Icon(Icons.Outlined.FilterListOff, contentDescription = "Clear Filter")
            }
        }
    }
}

@Composable
@Preview
private fun TableGroupFilterPreview() {
    TableGroupFilter(
        selectedGroups = listOf(
            TableGroup(1, "Group 1"),
            TableGroup(3, "Group 3")
        ),
        unselectedGroups = listOf(
            TableGroup(2, "Group 2"),
            TableGroup(4, "Group 4"),
            TableGroup(5, "Group 5"),
            TableGroup(6, "Group 6")
        ),
        onToggle = {},
        clearFilter = {}
    )
}
