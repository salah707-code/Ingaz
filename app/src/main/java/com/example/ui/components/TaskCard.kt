package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Priority
import com.example.data.model.ReminderType
import com.example.data.model.RepeatType
import com.example.data.model.TaskEntity
import com.example.data.preferences.CardBorderStyle
import com.example.data.preferences.CardCornerStyle
import com.example.data.preferences.CardLayoutStyle
import com.example.data.preferences.CardShadowStyle
import com.example.data.preferences.IconThemeStyle
import com.example.ui.localization.LocalAppStrings
import com.example.ui.theme.SuccessGreen
import com.example.ui.utils.DateTimeUtils

@Composable
fun TaskCard(
    task: TaskEntity,
    onToggleComplete: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    taskIndex: Int? = null,
    layoutStyle: CardLayoutStyle = CardLayoutStyle.STANDARD,
    shadowStyle: CardShadowStyle = CardShadowStyle.MEDIUM,
    borderStyle: CardBorderStyle = CardBorderStyle.SUBTLE_LINE,
    iconThemeStyle: IconThemeStyle = IconThemeStyle.COLORED_EMOJI,
    cornerStyle: CardCornerStyle = CardCornerStyle.ROUNDED,
    isArabic: Boolean = true
) {
    when (layoutStyle) {
        CardLayoutStyle.NOTES_VIEW -> {
            TaskCardNotesView(
                task = task,
                onToggleComplete = onToggleComplete,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier,
                taskIndex = taskIndex,
                shadowStyle = shadowStyle,
                borderStyle = borderStyle,
                iconThemeStyle = iconThemeStyle,
                cornerStyle = cornerStyle,
                isArabic = isArabic
            )
        }
        CardLayoutStyle.LARGE_GRID -> {
            TaskCardLargeGrid(
                task = task,
                onToggleComplete = onToggleComplete,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier,
                taskIndex = taskIndex,
                shadowStyle = shadowStyle,
                borderStyle = borderStyle,
                iconThemeStyle = iconThemeStyle,
                cornerStyle = cornerStyle,
                isArabic = isArabic
            )
        }
        CardLayoutStyle.COMPACT_LIST -> {
            TaskCardCompactList(
                task = task,
                onToggleComplete = onToggleComplete,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier,
                taskIndex = taskIndex,
                shadowStyle = shadowStyle,
                borderStyle = borderStyle,
                iconThemeStyle = iconThemeStyle,
                cornerStyle = cornerStyle,
                isArabic = isArabic
            )
        }
        CardLayoutStyle.STANDARD -> {
            TaskCardStandard(
                task = task,
                onToggleComplete = onToggleComplete,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier,
                taskIndex = taskIndex,
                shadowStyle = shadowStyle,
                borderStyle = borderStyle,
                iconThemeStyle = iconThemeStyle,
                cornerStyle = cornerStyle,
                isArabic = isArabic
            )
        }
    }
}

private fun copyTaskNoteToClipboard(context: Context, task: TaskEntity, isArabic: Boolean) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val textToCopy = buildString {
        append(task.title)
        if (task.description.isNotBlank()) {
            append("\n")
            append(task.description)
        }
    }
    val clip = ClipData.newPlainText(task.title, textToCopy)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(
        context,
        if (isArabic) "تم نسخ الملاحظة إلى الحافظة 📋" else "Note copied to clipboard 📋",
        Toast.LENGTH_SHORT
    ).show()
}

// 1. STANDARD CARD - WITH AUTO-NUMBERING, COLOR HARMONY & NOTE COPYING
@Composable
fun TaskCardStandard(
    task: TaskEntity,
    onToggleComplete: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    taskIndex: Int? = null,
    shadowStyle: CardShadowStyle = CardShadowStyle.MEDIUM,
    borderStyle: CardBorderStyle = CardBorderStyle.SUBTLE_LINE,
    iconThemeStyle: IconThemeStyle = IconThemeStyle.COLORED_EMOJI,
    cornerStyle: CardCornerStyle = CardCornerStyle.ROUNDED,
    isArabic: Boolean = true
) {
    val context = LocalContext.current
    val strings = LocalAppStrings.current
    var isExpanded by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val effectiveColorHex = if (task.cardColorHex != 0L) task.cardColorHex else task.categoryColor
    val cardAccentColor = Color(effectiveColorHex)
    val priorityColor = Color(task.priority.colorHex)

    // Accurate theme-aware container tinting
    val baseSurface = MaterialTheme.colorScheme.surface
    val surfaceBg = when {
        task.isCompleted -> baseSurface.copy(alpha = 0.7f)
        task.cardColorHex != 0L -> {
            // Harmonic blend of selected card color with surface
            cardAccentColor.copy(alpha = 0.12f)
        }
        else -> baseSurface
    }

    val cardShape = RoundedCornerShape(cornerStyle.cornerRadiusDp.dp)

    val cardBorderModifier = when (borderStyle) {
        CardBorderStyle.NONE -> {
            if (task.cardColorHex != 0L && !task.isCompleted) {
                Modifier.border(1.dp, cardAccentColor.copy(alpha = 0.35f), cardShape)
            } else Modifier
        }
        CardBorderStyle.SUBTLE_LINE -> {
            val borderColor = if (task.cardColorHex != 0L && !task.isCompleted) {
                cardAccentColor.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            }
            Modifier.border(0.8.dp, borderColor, cardShape)
        }
        CardBorderStyle.COLORED_BORDER -> Modifier.border(1.5.dp, cardAccentColor.copy(alpha = 0.7f), cardShape)
        CardBorderStyle.GLOW_BORDER -> Modifier.border(
            width = 2.dp,
            brush = Brush.linearGradient(listOf(cardAccentColor, cardAccentColor.copy(alpha = 0.3f))),
            shape = cardShape
        )
    }

    val shadowElevation = shadowStyle.elevationDp.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = shadowElevation, shape = cardShape, spotColor = cardAccentColor.copy(alpha = 0.25f))
            .then(cardBorderModifier)
            .clip(cardShape)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(spring(dampingRatio = 0.8f, stiffness = 400f))
            .testTag("task_card_${task.id}"),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = surfaceBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Auto-numbering + Category Chip & Priority & Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Auto-numbering badge
                    if (taskIndex != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(cardAccentColor.copy(alpha = 0.18f))
                                .border(1.dp, cardAccentColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${if (isArabic) DateTimeUtils.toArabicNumerals(taskIndex) else taskIndex.toString()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = cardAccentColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Category Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardAccentColor.copy(alpha = 0.15f))
                            .border(1.dp, cardAccentColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (iconThemeStyle == IconThemeStyle.COLORED_EMOJI) {
                                Text(
                                    text = CategoryIcons.getEmoji(task.categoryIcon),
                                    fontSize = 12.sp
                                )
                            } else {
                                Icon(
                                    imageVector = CategoryIcons.getIcon(task.categoryIcon),
                                    contentDescription = null,
                                    tint = cardAccentColor,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Text(
                                text = task.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = cardAccentColor
                            )
                        }
                    }

                    // Priority Flag Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(priorityColor.copy(alpha = 0.14f))
                            .border(1.dp, priorityColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Priority",
                                tint = priorityColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (isArabic) task.priority.titleAr else task.priority.titleEn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = priorityColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (task.reminderType != ReminderType.NONE) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(cardAccentColor.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Reminder Active",
                                tint = cardAccentColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    if (task.repeatType != RepeatType.NONE) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repeating",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                // Expand Indicator and Menu
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.description.isNotBlank()) {
                        IconButton(
                            onClick = { copyTaskNoteToClipboard(context, task, isArabic) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Note",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand content",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(28.dp).testTag("task_menu_${task.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            if (task.description.isNotBlank()) {
                                DropdownMenuItem(
                                    text = { Text(if (isArabic) "نسخ الملاحظة" else "Copy Note", color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = {
                                        showMenu = false
                                        copyTaskNoteToClipboard(context, task, isArabic)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = cardAccentColor)
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text(strings.editTask, color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(strings.delete, color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Content Row: Checkbox + Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Interactive Checkbox
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (task.isCompleted) SuccessGreen else Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = if (task.isCompleted) SuccessGreen else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onToggleComplete(!task.isCompleted) },
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and Quick note
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Description / Note Content
                    if (task.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Expanded Rich Content Details
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .padding(12.dp)
                ) {
                    // Full Date & Hijri + Time detail
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = cardAccentColor,
                            modifier = Modifier.size(15.dp)
                        )
                        val dateFull = DateTimeUtils.formatBothDates(task.date, isArabic)
                        val timeFull = if (task.timeHour in 0..23 && task.timeMinute in 0..59) {
                            " • ${DateTimeUtils.formatTimeDisplay(task.timeHour, task.timeMinute, isArabic)}"
                        } else " • ${strings.allDay}"
                        Text(
                            text = "$dateFull$timeFull",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Location if set
                    if (task.locationName.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF06B6D4),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = task.locationName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Reminder Info if set
                    if (task.reminderType != ReminderType.NONE) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = cardAccentColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (isArabic) task.reminderType.titleAr else task.reminderType.titleEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = cardAccentColor
                            )
                        }
                    }

                    // Repeat Info if set
                    if (task.repeatType != RepeatType.NONE) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (isArabic) task.repeatType.titleAr else task.repeatType.titleEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Inline Action Bar (Copy / Edit / Delete)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (task.description.isNotBlank()) {
                            TextButton(
                                onClick = { copyTaskNoteToClipboard(context, task, isArabic) }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = cardAccentColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isArabic) "نسخ الملاحظة" else "Copy Note", color = cardAccentColor, style = MaterialTheme.typography.labelMedium)
                            }

                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        TextButton(
                            onClick = onDelete
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = strings.delete, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedButton(
                            onClick = onEdit
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = cardAccentColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = strings.editTask, color = cardAccentColor, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            // Bottom Collapsed Bar: Date/Time & Status indicator
            if (!isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )

                        val dateText = DateTimeUtils.formatDateDisplay(task.date, isArabic)
                        val timeText = if (task.timeHour in 0..23 && task.timeMinute in 0..59) {
                            " • ${DateTimeUtils.formatTimeDisplay(task.timeHour, task.timeMinute, isArabic)}"
                        } else ""

                        Text(
                            text = "$dateText$timeText",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (task.isCompleted) {
                        Text(
                            text = if (isArabic) "مكتملة ✓" else "Done ✓",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }
            }
        }
    }
}

// 2. NOTES VIEW (نمط الملاحظات - بطاقات ملونة مميزة وتركيز على الملاحظة ونسخها)
@Composable
fun TaskCardNotesView(
    task: TaskEntity,
    onToggleComplete: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    taskIndex: Int? = null,
    shadowStyle: CardShadowStyle = CardShadowStyle.MEDIUM,
    borderStyle: CardBorderStyle = CardBorderStyle.SUBTLE_LINE,
    iconThemeStyle: IconThemeStyle = IconThemeStyle.COLORED_EMOJI,
    cornerStyle: CardCornerStyle = CardCornerStyle.ROUNDED,
    isArabic: Boolean = true
) {
    val context = LocalContext.current
    val strings = LocalAppStrings.current
    var isExpanded by remember { mutableStateOf(false) }

    val effectiveColorHex = if (task.cardColorHex != 0L) task.cardColorHex else task.categoryColor
    val cardAccentColor = Color(effectiveColorHex)
    val cardShape = RoundedCornerShape(cornerStyle.cornerRadiusDp.dp)

    val surfaceBg = if (task.isCompleted) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    } else {
        cardAccentColor.copy(alpha = 0.12f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = shadowStyle.elevationDp.dp, shape = cardShape, spotColor = cardAccentColor.copy(alpha = 0.35f))
            .border(1.5.dp, cardAccentColor.copy(alpha = 0.55f), cardShape)
            .clip(cardShape)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(spring(dampingRatio = 0.8f, stiffness = 400f))
            .testTag("task_card_notes_${task.id}"),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = surfaceBg)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Accent Band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(cardAccentColor)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Header with Auto-Number, Category Chip & Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (taskIndex != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(cardAccentColor.copy(alpha = 0.22f))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "#${if (isArabic) DateTimeUtils.toArabicNumerals(taskIndex) else taskIndex.toString()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = cardAccentColor,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(cardAccentColor.copy(alpha = 0.18f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (iconThemeStyle == IconThemeStyle.COLORED_EMOJI) {
                                    Text(CategoryIcons.getEmoji(task.categoryIcon), fontSize = 12.sp)
                                } else {
                                    Icon(
                                        imageVector = CategoryIcons.getIcon(task.categoryIcon),
                                        contentDescription = null,
                                        tint = cardAccentColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                Text(
                                    text = task.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = cardAccentColor
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Outlined.StickyNote2,
                            contentDescription = "Note",
                            tint = cardAccentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Checkbox
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(if (task.isCompleted) SuccessGreen else Color.Transparent)
                            .border(
                                width = 1.8.dp,
                                color = if (task.isCompleted) SuccessGreen else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .clickable { onToggleComplete(!task.isCompleted) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (task.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                // Note Body
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardAccentColor.copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Date and Action Bar
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateTimeUtils.formatDateDisplay(task.date, isArabic),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (task.description.isNotBlank()) {
                            IconButton(
                                onClick = { copyTaskNoteToClipboard(context, task, isArabic) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = cardAccentColor, modifier = Modifier.size(16.dp))
                            }
                        }
                        IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = cardAccentColor, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// 3. LARGE GRID SQUARE CARD (مربعات كبيرة)
@Composable
fun TaskCardLargeGrid(
    task: TaskEntity,
    onToggleComplete: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    taskIndex: Int? = null,
    shadowStyle: CardShadowStyle = CardShadowStyle.MEDIUM,
    borderStyle: CardBorderStyle = CardBorderStyle.SUBTLE_LINE,
    iconThemeStyle: IconThemeStyle = IconThemeStyle.COLORED_EMOJI,
    cornerStyle: CardCornerStyle = CardCornerStyle.ROUNDED,
    isArabic: Boolean = true
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    val effectiveColorHex = if (task.cardColorHex != 0L) task.cardColorHex else task.categoryColor
    val cardAccentColor = Color(effectiveColorHex)
    val cardShape = RoundedCornerShape(cornerStyle.cornerRadiusDp.dp)

    val surfaceBg = if (task.isCompleted) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    } else if (task.cardColorHex != 0L) {
        cardAccentColor.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = shadowStyle.elevationDp.dp, shape = cardShape, spotColor = cardAccentColor.copy(alpha = 0.35f))
            .border(1.dp, cardAccentColor.copy(alpha = 0.35f), cardShape)
            .clip(cardShape)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(spring(dampingRatio = 0.8f, stiffness = 400f))
            .testTag("task_card_grid_${task.id}"),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = surfaceBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (taskIndex != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(cardAccentColor.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "#${if (isArabic) DateTimeUtils.toArabicNumerals(taskIndex) else taskIndex.toString()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = cardAccentColor,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardAccentColor.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (iconThemeStyle == IconThemeStyle.COLORED_EMOJI) {
                                Text(CategoryIcons.getEmoji(task.categoryIcon), fontSize = 13.sp)
                            } else {
                                Icon(
                                    imageVector = CategoryIcons.getIcon(task.categoryIcon),
                                    contentDescription = null,
                                    tint = cardAccentColor,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Text(
                                text = task.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = cardAccentColor
                            )
                        }
                    }
                }

                // Checkbox Button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (task.isCompleted) SuccessGreen else Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = if (task.isCompleted) SuccessGreen else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onToggleComplete(!task.isCompleted) },
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateTimeUtils.formatDateDisplay(task.date, isArabic),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    if (task.description.isNotBlank()) {
                        IconButton(
                            onClick = { copyTaskNoteToClipboard(context, task, isArabic) },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = cardAccentColor, modifier = Modifier.size(15.dp))
                        }
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = cardAccentColor, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(15.dp))
                    }
                }
            }
        }
    }
}

// 4. COMPACT LIST ITEM CARD (قائمة سريعة ومختصرة)
@Composable
fun TaskCardCompactList(
    task: TaskEntity,
    onToggleComplete: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    taskIndex: Int? = null,
    shadowStyle: CardShadowStyle = CardShadowStyle.MEDIUM,
    borderStyle: CardBorderStyle = CardBorderStyle.SUBTLE_LINE,
    iconThemeStyle: IconThemeStyle = IconThemeStyle.COLORED_EMOJI,
    cornerStyle: CardCornerStyle = CardCornerStyle.ROUNDED,
    isArabic: Boolean = true
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    val effectiveColorHex = if (task.cardColorHex != 0L) task.cardColorHex else task.categoryColor
    val cardAccentColor = Color(effectiveColorHex)
    val cardShape = RoundedCornerShape(cornerStyle.cornerRadiusDp.dp)

    val surfaceBg = if (task.isCompleted) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    } else if (task.cardColorHex != 0L) {
        cardAccentColor.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = shadowStyle.elevationDp.dp, shape = cardShape, spotColor = cardAccentColor.copy(alpha = 0.25f))
            .border(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), cardShape)
            .clip(cardShape)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(spring(dampingRatio = 0.8f, stiffness = 400f))
            .testTag("task_card_compact_${task.id}"),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = surfaceBg)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (taskIndex != null) {
                        Text(
                            text = "#${if (isArabic) DateTimeUtils.toArabicNumerals(taskIndex) else taskIndex.toString()}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = cardAccentColor,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (task.isCompleted) SuccessGreen else Color.Transparent)
                            .border(
                                width = 1.8.dp,
                                color = if (task.isCompleted) SuccessGreen else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .clickable { onToggleComplete(!task.isCompleted) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (task.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "${task.category} • ${DateTimeUtils.formatDateDisplay(task.date, isArabic)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.description.isNotBlank()) {
                        IconButton(
                            onClick = { copyTaskNoteToClipboard(context, task, isArabic) },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = cardAccentColor, modifier = Modifier.size(15.dp))
                        }
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = cardAccentColor, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(15.dp))
                    }
                }
            }

            if (isExpanded && task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 34.dp)
                )
            }
        }
    }
}
