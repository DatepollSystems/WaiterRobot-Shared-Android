package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

@Composable
fun ColumnScope.TableGroupFilter(
    tableGroups: List<TableGroup>?,
    onToggle: (TableGroup) -> Unit,
    showAll: () -> Unit,
    hideAll: () -> Unit,
) {
    if (tableGroups == null) {
        // Should not happen as open filter is only shown when there are groups
        CenteredText(text = "Table groups not loaded...", scrollAble = false)
    } else {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tischgruppen", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.weight(1f))

            // TODO replace with SegmentedButton when available for compose
            //  (https://m3.material.io/components/segmented-buttons/overview)
            val allGroupsShown = tableGroups.none { it.hidden }
            IconToggleButton(
                checked = allGroupsShown,
                enabled = !allGroupsShown,
                onCheckedChange = { showAll() }
            ) {
                Icon(
                    Icons.Filled.DoneAll,
                    contentDescription = "Select all groups"
                )
            }
            val allGroupsHidden = tableGroups.all { it.hidden }
            IconToggleButton(
                checked = allGroupsHidden,
                enabled = !allGroupsHidden,
                onCheckedChange = { hideAll() }
            ) {
                Icon(
                    Icons.Filled.RemoveDone,
                    contentDescription = "Unselect all groups"
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(tableGroups, key = TableGroup::id) { group ->
                TableGroupFilter(group, onToggle = { onToggle(group) })
            }
        }
    }
}

@Composable
private fun TableGroupFilter(tableGroup: TableGroup, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = tableGroup.color?.let { Color(it.toColorInt()) } ?: Color.Transparent,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = tableGroup.name)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = !tableGroup.hidden,
            onCheckedChange = { onToggle() }
        )
    }
}

@Composable
@PreviewLightDark
private fun TableGroupFilterPreview() = Preview {
    Column {
        TableGroupFilter(
            tableGroup = TableGroup(1, "Group 1", 1, 1, "#ff00ff", false, emptyList()),
            onToggle = {},
        )
    }
}

@Composable
@Preview
private fun TableGroupFiltersPreview() = Preview {
    Column {
        TableGroupFilter(
            tableGroups = listOf(
                TableGroup(1, "Group 1", 1, 1, "#ff00ff", false, emptyList()),
                TableGroup(3, "Group 2", 1, 2, "#00ffff", false, emptyList()),
                TableGroup(2, "Group 3", 1, 3, null, false, emptyList()),
                TableGroup(4, "Group 4", 1, 4, null, true, emptyList()),
                TableGroup(5, "Group 5", 1, 5, "#ffff00", false, emptyList()),
                TableGroup(6, "Group 6", 1, 6, null, false, emptyList())
            ),
            onToggle = {},
            showAll = {},
            hideAll = {}
        )
    }
}
