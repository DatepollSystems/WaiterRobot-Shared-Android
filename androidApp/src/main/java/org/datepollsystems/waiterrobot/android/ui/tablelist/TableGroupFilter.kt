package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableGroupFilter(
    selectedGroups: Set<TableGroup>,
    unselectedGroups: Set<TableGroup>,
    onToggle: (TableGroup) -> Unit,
    clearFilter: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = selectedGroups.toList(),
                key = TableGroup::id
            ) { group ->
                Button(
                    modifier = Modifier.animateItemPlacement(),
                    onClick = { onToggle(group) },
                ) {
                    Text(group.name)
                }
            }
            items(
                items = unselectedGroups.toList(),
                key = TableGroup::id
            ) { group ->
                OutlinedButton(
                    modifier = Modifier.animateItemPlacement(),
                    onClick = { onToggle(group) },
                ) {
                    Text(group.name)
                }
            }
        }
        Box {
            IconButton(onClick = clearFilter) {
                Icon(Icons.Outlined.Close, contentDescription = "Clear")
            }
        }
    }
}

@Composable
@Preview
private fun TableGroupFilterPreview() {
    TableGroupFilter(
        selectedGroups = setOf(TableGroup(1, "Group 1")),
        unselectedGroups = setOf(TableGroup(2, "Group 2")),
        onToggle = {},
        clearFilter = {}
    )
}
