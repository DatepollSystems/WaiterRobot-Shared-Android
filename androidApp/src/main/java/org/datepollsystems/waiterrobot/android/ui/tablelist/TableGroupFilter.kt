package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

@Composable
fun TableGroupFilter(
    selectedGroups: Set<TableGroup>,
    unselectedGroups: Set<TableGroup>,
    onToggle: (TableGroup) -> Unit
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
                onClick = { onToggle(group) },
            ) {
                Text(group.name)
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
        onToggle = {}
    )
}
