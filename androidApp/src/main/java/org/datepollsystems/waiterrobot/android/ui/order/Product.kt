package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.order.models.Product

@Composable
fun Product(
    product: Product,
    onSelect: () -> Unit,
) {
    OutlinedButton(
        onClick = onSelect,
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
