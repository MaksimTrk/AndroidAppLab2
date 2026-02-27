package com.example.lab2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Shape(val title: String) {
    CIRCLE("Коло"),
    RECTANGLE("Прямокутник"),
    TRIANGLE("Трикутник")
}

data class ShapeUiState(
    val selectedShape: Shape = Shape.CIRCLE,
    val showArea: Boolean = false,
    val showPerimeter: Boolean = false,
    val showResult: Boolean = false,
    val error: String? = null
)

class ShapeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShapeUiState())
    val uiState: StateFlow<ShapeUiState> = _uiState.asStateFlow()

    fun onShapeSelected(shape: Shape) {
        _uiState.value = _uiState.value.copy(selectedShape = shape, error = null)
    }

    fun onAreaChecked(checked: Boolean) {
        _uiState.value = _uiState.value.copy(showArea = checked, error = null)
    }

    fun onPerimeterChecked(checked: Boolean) {
        _uiState.value = _uiState.value.copy(showPerimeter = checked, error = null)
    }

    fun confirm() {
        val state = _uiState.value
        if (!state.showArea && !state.showPerimeter) {
            _uiState.value = state.copy(error = "Оберіть хоча б один прапорець (площа або периметр)")
            return
        }
        _uiState.value = state.copy(showResult = true, error = null)
    }

    fun cancel() {
        _uiState.value = ShapeUiState()
    }
}
