package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.desc
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.sectionHeader
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.BooleanPreviewProvider
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.android.util.toColor
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.GroupedTables
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR

@Composable
fun TableGrid(
    groupsResource: Resource<List<GroupedTables>>,
    isDemoEvent: Boolean,
    onTableClick: (Table) -> Unit,
    refresh: () -> Unit
) {
    val tableGroups = groupsResource.data
    Column {
        if (groupsResource is Resource.Error) {
            ErrorBar(message = groupsResource.userMessage, retryAction = refresh)
        }
        if (isDemoEvent) {
            ErrorBar(message = MR.strings.tableList_demoEventWarning.desc(), initialLines = 1)
        }

        if (tableGroups.isNullOrEmpty()) {
            CenteredText(
                modifier = Modifier.weight(1f),
                text = MR.strings.tableList_noTableFound(),
                scrollAble = true
            )
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                columns = GridCells.Adaptive(80.dp)
            ) {
                tableGroups.forEach { group: GroupedTables ->
                    // TODO use a sticky header
                    //  (Compose currently does not support sticky headers in LazyGrids)
                    sectionHeader(key = "group-${group.id}", title = group.name)
                    items(group.tables, key = Table::id) { table ->
                        Table(
                            table = table,
                            color = group.color.toColor(),
                            onClick = { onTableClick(table) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TableListPreview(
    @PreviewParameter(BooleanPreviewProvider::class) isDemoEvent: Boolean
) = Preview {
    TableGrid(
        groupsResource = Resource.Success(
            listOf(
                GroupedTables(1, "Group 1", 1, "#FF0000", listOf(Table(0, 1, "Group 1"))),
                GroupedTables(2, "Group 2", 1, "#00FF00", listOf(Table(1, 2, "Group 2"))),
            )
        ),
        onTableClick = {},
        isDemoEvent = isDemoEvent,
        refresh = {}
    )
}
