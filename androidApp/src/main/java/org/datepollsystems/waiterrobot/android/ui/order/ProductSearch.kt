package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.placeholder
import org.datepollsystems.waiterrobot.shared.generated.localization.title

@Composable
fun ProductSearch(
    products: List<Product>,
    onSelect: (Product) -> Unit,
    onFilter: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column {
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
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
        )

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(100.dp),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products, key = Product::id) { product ->
                OutlinedButton(
                    onClick = { onSelect(product) },
                    enabled = !product.soldOut,
                    shape = RoundedCornerShape(10),
                    contentPadding = PaddingValues(5.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.onSurface)
                    // elevation = ButtonDefaults.elevation() // TODO yes/no?
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            textDecoration = if (product.soldOut) TextDecoration.LineThrough else null
                        )
                        Text(
                            text = product.allergens.joinToString(", ") { it.shortName }
                                .ifEmpty { "-" },
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                        Text(
                            text = product.price.toString(),
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
