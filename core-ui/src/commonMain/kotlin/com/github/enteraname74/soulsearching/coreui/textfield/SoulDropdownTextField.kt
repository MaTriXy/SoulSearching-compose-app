package com.github.enteraname74.soulsearching.coreui.textfield

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.github.enteraname74.soulsearching.coreui.image.SoulIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoulDropdownTextField(
    values: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    labelName: String?,
    error: String?,
    isInError: Boolean,
    modifier: Modifier = Modifier,
    colors : SoulTextFieldColors = SoulTextFieldDefaults.secondaryColors(),
    style: SoulTextFieldStyle,
    focusManager: FocusManager
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var isDropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val focusRequester = remember { FocusRequester() }

    SoulTextFieldViewHolder(
        style = style,
        colors = colors,
        isFocused = isFocused
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded && isFocused,
            onExpandedChange = { isExpanded = it }
        ) {
            SoulTextField(
                value = value,
                onValueChange = {
                    isDropdownExpanded = it != value
                    focusRequester.requestFocus()
                    onValueChange(it)
                },
                labelName = labelName,
                isInError = isInError,
                error = error,
                focusManager = focusManager,
                modifier = modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryEditable,
                        enabled = true
                    )
                    .focusRequester(focusRequester),
                style = style,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        isExpanded = false
                    }
                ),
                trailingIcon = {
                    val rotation by animateFloatAsState(targetValue = if (isExpanded && isFocused) 180f else 0f)
                    SoulIcon(
                        icon = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation),
                        tint = colors.contentColor,
                    )
                }
            )
            ExposedDropdownMenu(
                containerColor = colors.containerColor,
                expanded = isDropdownExpanded && values.isNotEmpty(),
                onDismissRequest = {
                    isDropdownExpanded = false
                }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        text = { Text(text = value) },
                        onClick = { onValueChange(value) },
                        colors = MenuDefaults.itemColors(
                            textColor = colors.contentColor,
                        )
                    )
                }
            }
        }
    }
}

class SoulDropdownTextFieldHolderImpl(
    id: String,
    initialValue: String = "",
    isValid: (value: String) -> Boolean = { true },
    getLabel: @Composable () -> String?,
    getError: @Composable () -> String?,
    getColors: @Composable () -> SoulTextFieldColors = { SoulTextFieldDefaults.secondaryColors() },
    private val modifier: Modifier = Modifier,
    private val updateProposedValues: suspend (fieldValue: String) -> List<String>,
    private val style: SoulTextFieldStyle = SoulTextFieldStyle.Unique,
): SoulTextFieldHolder(
    initialValue = initialValue,
    isValid = isValid,
    id = id,
    getLabel = getLabel,
    getColors = getColors,
    getError = getError,
) {
    private var values: List<String> by mutableStateOf(emptyList())
    private var updateJob: Job? = null

    override fun onValueChanged(newValue: String) {
        super.onValueChanged(newValue)
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
        }
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            values = updateProposedValues(newValue)
        }
    }

    @Composable
    override fun TextField(
        focusManager: FocusManager,
    ) {
        SoulDropdownTextField(
            modifier = modifier,
            value = value,
            onValueChange = ::onValueChanged,
            labelName = label,
            focusManager = focusManager,
            values = values,
            style = style,
            colors = colors,
            isInError = isInError,
            error = error,
        )
    }
}