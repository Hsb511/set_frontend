package com.team23.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.team23.ui.theming.LocalSpacings
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun VerticalTableWithHeader(
    header: VerticalTableRow,
    rows: List<VerticalTableRow>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(all = LocalSpacings.current.small),
        modifier = modifier,
    ) {
        stickyHeader {
            TableHeader(header, backgroundColor)
        }
        items(rows) { row ->
            TableRow(row)
        }
    }
}

data class VerticalTableRow(
    val cells: List<Cell>,
    val onClick: () -> Unit = {},
) {
    data class Cell(
        val content: Content,
        val weight: Float,
    ) {
        sealed interface Content {
            data class Text(val text: String) : Content
            data class Icon(
                val icon: ImageVector,
                val contentDescription: String,
            ) : Content
        }
    }
}

@Composable
private fun TableHeader(header: VerticalTableRow, backgroundColor: Color) {
    Column(modifier = Modifier.background(backgroundColor)) {
        Row {
            header.cells.forEach { cell ->
                TableCellContent(
                    content = cell.content,
                    modifier = Modifier.weight(cell.weight),
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TableRow(row: VerticalTableRow) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { row.onClick() }
                .padding(
                    vertical = LocalSpacings.current.medium,
                    horizontal = LocalSpacings.current.small
                ),
        ) {
            row.cells.forEach { cell ->
                TableCellContent(
                    content = cell.content,
                    modifier = Modifier.weight(cell.weight),
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun TableCellContent(
    content: VerticalTableRow.Cell.Content,
    modifier: Modifier = Modifier,
) {
    when (content) {
        is VerticalTableRow.Cell.Content.Icon -> Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier,
        ) {
            Icon(
                imageVector = content.icon,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = content.contentDescription,
            )
        }
        is VerticalTableRow.Cell.Content.Text -> Text(
            text = content.text,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier,
        )
    }
}