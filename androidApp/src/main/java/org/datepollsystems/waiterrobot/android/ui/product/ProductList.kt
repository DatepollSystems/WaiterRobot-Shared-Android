package org.datepollsystems.waiterrobot.android.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.sectionHeader
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.view.LoadingView
import org.datepollsystems.waiterrobot.android.util.desaturateOnDarkMode
import org.datepollsystems.waiterrobot.android.util.getContentColor
import org.datepollsystems.waiterrobot.android.util.toColor
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.GroupedProducts
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.Product
import org.datepollsystems.waiterrobot.shared.features.product.presentation.list.ProductListState
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.euro

@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    state: ProductListState,
    onSelect: (Product) -> Unit,
    refresh: () -> Unit,
) {
    Column(modifier) {
        val productGroupsResource = state.productGroups
        if (productGroupsResource is Resource.Loading && productGroupsResource.data == null) {
            LoadingView()
        } else {
            Column {
                if (productGroupsResource is Resource.Error) {
                    ErrorBar(message = productGroupsResource.userMessage, retryAction = refresh)
                }
                val productGroups = productGroupsResource.data
                if (productGroups.isNullOrEmpty()) {
                    CenteredText(
                        text = MR.strings.productSearch_noProductFound(),
                        scrollAble = false
                    )
                } else {
                    val coScope = rememberCoroutineScope()
                    val pagerState = rememberPagerState {
                        productGroups.size + 1 // One additional "all" page
                    }

                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        edgePadding = 0.dp,
                        divider = {} // Add divider externally as otherwise it does not span the whole width
                    ) {
                        Tab(
                            selected = pagerState.currentPage == 0,
                            onClick = { coScope.launch { pagerState.scrollToPage(0) } },
                            text = { Text(MR.strings.productSearch_groups_all()) }
                        )
                        productGroups.forEachIndexed { index, productGroup ->
                            val backgroundColor =
                                productGroup.color.toColor()?.desaturateOnDarkMode()
                            val textColor = backgroundColor?.getContentColor() ?: Color.Unspecified
                            Tab(
                                modifier = Modifier.background(
                                    backgroundColor ?: Color.Unspecified
                                ),
                                selected = pagerState.currentPage == index + 1,
                                onClick = { coScope.launch { pagerState.scrollToPage(index + 1) } },
                                text = { Text(text = productGroup.name, color = textColor) }
                            )
                        }
                    }

                    HorizontalDivider()

                    HorizontalPager(pagerState) { pageIndex ->
                        if (pageIndex == 0) {
                            if (productGroups.all { it.products.isEmpty() }) {
                                CenteredText(
                                    text = MR.strings.productSearch_noProductFound(),
                                    scrollAble = false
                                )
                            } else {
                                ProductLazyVerticalGrid {
                                    productGroups.forEach { productGroup ->
                                        if (productGroup.products.isNotEmpty()) {
                                            sectionHeader(
                                                key = "group-${productGroup.id}",
                                                title = productGroup.name
                                            )
                                            items(
                                                productGroup.products,
                                                key = Product::id
                                            ) { product ->
                                                Product(
                                                    product = product,
                                                    groupColor = productGroup.color.toColor(),
                                                    onSelect = { onSelect(product) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (productGroups[pageIndex - 1].products.isEmpty()) {
                                CenteredText(
                                    text = MR.strings.productSearch_noProductFound(),
                                    scrollAble = false
                                )
                            } else {
                                ProductLazyVerticalGrid {
                                    val productGroup = productGroups[pageIndex - 1]
                                    items(productGroup.products, key = Product::id) { product ->
                                        Product(
                                            product = product,
                                            groupColor = productGroup.color.toColor(),
                                            onSelect = { onSelect(product) }
                                        )
                                    }
                                }
                            }
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
    contentPadding = PaddingValues(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    content = content
)

@Preview
@Composable
private fun ProductListPreview() = org.datepollsystems.waiterrobot.android.ui.core.preview.Preview {
    ProductList(
        state = ProductListState(
            productGroups = Resource.Loading(
                data = (0..15).map {
                    GroupedProducts(
                        id = it.toLong(),
                        name = "Group 1",
                        color = "#FF0000",
                        position = it,
                        products = listOf(
                            Product(
                                id = it * 100L + 1,
                                name = "Product 1",
                                price = 1.euro,
                                soldOut = false,
                                color = "#FF0000",
                                position = 1,
                                allergens = emptyList()
                            ),
                            Product(
                                id = it * 100L + 2,
                                name = "Product 2",
                                price = 2.euro,
                                soldOut = false,
                                color = "#FF0000",
                                position = 2,
                                allergens = emptyList()
                            )
                        )
                    )
                }
            ),
            filter = ""
        ),
        onSelect = {},
        refresh = {},
    )
}
