package org.datepollsystems.waiterrobot.android.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.GroupedProducts
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.Product
import org.datepollsystems.waiterrobot.shared.features.product.presentation.list.ProductListState
import org.datepollsystems.waiterrobot.shared.features.product.presentation.list.ProductListViewModel
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.cent
import org.datepollsystems.waiterrobot.shared.utils.euro
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductSearchScreen(
    vm: ProductListViewModel = koinViewModel(),
    onSelect: (Product) -> Unit,
    close: () -> Unit,
) {
    val state by vm.collectAsState()

    ProductSearch(
        state = state,
        onSelect = onSelect,
        onFilter = vm::filterProducts,
        refresh = vm::refreshProducts,
        close = close
    )
}

@Composable
private fun ProductSearch(
    state: ProductListState,
    onSelect: (Product) -> Unit,
    onFilter: (String) -> Unit,
    refresh: () -> Unit,
    close: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = close) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
            }

            // TODO replace with Searchbar when available (https://m3.material.io/components/search/overview)
            OutlinedTextField(
                value = state.filter,
                onValueChange = onFilter,
                label = { Text(MR.strings.productSearch_title()) },
                placeholder = { Text(MR.strings.productSearch_placeholder()) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, "Search product")
                },
                trailingIcon = {
                    IconButton(
                        enabled = state.filter.isNotEmpty(),
                        onClick = { onFilter("") }
                    ) {
                        Icon(imageVector = Icons.Outlined.Clear, "Clear search")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                modifier = Modifier
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
            )
        }

        ProductList(
            state = state,
            onSelect = onSelect,
            refresh = refresh,
        )
    }
}

@Preview
@Composable
private fun ProductSearchPreview() = Preview {
    ProductSearch(
        ProductListState(
            productGroups = Resource.Success(
                (0..3).map { group ->
                    GroupedProducts(
                        group.toLong(),
                        "Group $group",
                        group,
                        null,
                        products = (0..4).map {
                            Product(
                                group * 100L + it,
                                "Product $it",
                                it.euro + (it.cent / 10 * 10),
                                false,
                                null,
                                emptyList(),
                                it
                            )
                        }
                    )
                }
            )
        ),
        { _ -> },
        { _ -> },
        {},
        {}
    )
}
