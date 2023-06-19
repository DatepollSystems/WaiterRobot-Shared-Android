package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.common.sectionHeader
import org.datepollsystems.waiterrobot.android.ui.core.CenteredText
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroupWithProducts
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.allGroups
import org.datepollsystems.waiterrobot.shared.generated.localization.noProductFound
import org.datepollsystems.waiterrobot.shared.generated.localization.placeholder
import org.datepollsystems.waiterrobot.shared.generated.localization.title

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductSearch(
    productGroups: List<ProductGroupWithProducts>,
    onSelect: (Product) -> Unit,
    onFilter: (String) -> Unit,
    close: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = close) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
            }

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    onFilter(it)
                },
                label = { Text(L.productSearch.title()) },
                placeholder = { Text(L.productSearch.placeholder()) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, "Search product")
                },
                trailingIcon = {
                    IconButton(
                        enabled = text.isNotEmpty(),
                        onClick = {
                            text = ""
                            onFilter("")
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Clear, "Clear search")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                ),
                modifier = Modifier
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
            )
        }

        if (productGroups.isEmpty()) {
            CenteredText(text = L.productSearch.noProductFound(), scrollAble = false)
        } else {
            val coScope = rememberCoroutineScope()
            val pagerState =
                rememberPagerState { productGroups.size + 1 } // One additional "all" page

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.surface,
                edgePadding = 0.dp,
                divider = {} // Add divider externally as otherwise it does not span the whole width
            ) {
                Tab(selected = pagerState.currentPage == 0,
                    onClick = { coScope.launch { pagerState.scrollToPage(0) } },
                    text = { Text(L.productSearch.allGroups()) })
                productGroups.forEachIndexed { index, productGroupWithProducts ->
                    Tab(
                        selected = pagerState.currentPage == index + 1,
                        onClick = { coScope.launch { pagerState.scrollToPage(index + 1) } },
                        text = { Text(productGroupWithProducts.group.name) })
                }
            }

            TabRowDefaults.Divider(modifier = Modifier.fillMaxWidth())

            HorizontalPager(pagerState) { pageIndex ->
                if (pageIndex == 0) {
                    ProductLazyVerticalGrid {
                        productGroups.forEach { (group: ProductGroup, products: List<Product>) ->
                            if (products.isNotEmpty()) {
                                sectionHeader(key = "group-${group.id}", title = group.name)
                                items(products, key = Product::id) { product ->
                                    Product(product = product, onSelect = { onSelect(product) })
                                }
                            }
                        }
                    }
                } else {
                    ProductLazyVerticalGrid {
                        items(productGroups[pageIndex - 1].products, key = Product::id) { product ->
                            Product(product = product, onSelect = { onSelect(product) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductLazyVerticalGrid(
    content: LazyGridScope.() -> Unit
) = LazyVerticalGrid(
    modifier = Modifier.fillMaxSize(),
    columns = GridCells.Adaptive(100.dp),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    content = content
)
