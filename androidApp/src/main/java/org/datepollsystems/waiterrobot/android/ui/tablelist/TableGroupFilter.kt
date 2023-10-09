package org.datepollsystems.waiterrobot.android.ui.tablelist

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
import androidx.compose.material.icons.outlined.FilterListOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

@Composable
fun TableGroupFilter(
    groups: List<TableGroup>,
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
            items(
                items = groups,
                key = TableGroup::id
            ) { group ->

                if (group.isFiltered) {
                    Button(
                        onClick = { onToggle(group) },
                    ) {
                        Text(group.name)
                    }
                } else {
                    OutlinedButton(
                        onClick = { onToggle(group) },
                    ) {
                        Text(group.name)
                    }
                }
            }
        }

        Box {
            IconButton(
                onClick = clearFilter,
                enabled = groups.any { it.isFiltered }
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
        groups = listOf(
            TableGroup(1, "Group 1", 1, 1, null, false, emptyList()),
            TableGroup(3, "Group 2", 1, 2, null, false, emptyList()),
            TableGroup(2, "Group 3", 1, 3, null, false, emptyList()),
            TableGroup(4, "Group 4", 1, 4, null, false, emptyList()),
            TableGroup(5, "Group 5", 1, 5, null, false, emptyList()),
            TableGroup(6, "Group 6", 1, 6, null, false, emptyList())
        ),
        onToggle = {},
        clearFilter = {}
    )
}
