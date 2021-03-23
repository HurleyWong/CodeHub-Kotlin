package ac.hurley.codehub_kotlin.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringResource
import ac.hurley.codehub_kotlin.R
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics

@Composable
fun CollectButton(
    isCollected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel =
        if (isCollected) {
            stringResource(R.string.cancel_collect)
        } else {
            stringResource(R.string.collect)
        }
    IconToggleButton(checked = isCollected, onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            this.onClick(label = clickLabel, action = null)
        }
    ) {
        Icon(
            if (isCollected) painterResource(id = R.drawable.ic_baseline_collect)
            else painterResource(id = R.drawable.ic_baseline_collect_border),
            contentDescription = null
        )
    }
}