package org.datepollsystems.waiterrobot.shared.features.table.presentation.filter

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.objCArray
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.TableGroup
import kotlin.native.HiddenFromObjC
import kotlin.native.ObjCName

data class TableGroupFilterState(
    @HiddenFromObjC
    val groups: Resource<List<TableGroup>> = Resource.Loading()
) : ViewModelState {
    @ObjCName("groups")
    val groupsArray: Resource<Array<TableGroup>> by groups.objCArray()
}
