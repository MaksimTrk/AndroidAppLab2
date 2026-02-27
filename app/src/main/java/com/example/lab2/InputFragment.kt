package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class InputFragment : Fragment() {

    private val viewModel: ShapeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_input, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cbArea       = view.findViewById<CheckBox>(R.id.cbArea)
        val cbPerimeter  = view.findViewById<CheckBox>(R.id.cbPerimeter)
        val rgShape      = view.findViewById<RadioGroup>(R.id.rgShape)
        val rbCircle     = view.findViewById<RadioButton>(R.id.rbCircle)
        val rbRectangle  = view.findViewById<RadioButton>(R.id.rbRectangle)
        val rbTriangle   = view.findViewById<RadioButton>(R.id.rbTriangle)
        val tvError      = view.findViewById<TextView>(R.id.tvError)
        val btnOk        = view.findViewById<Button>(R.id.btnOk)

        // Відновлюємо стан після Cancel
        val initial = viewModel.uiState.value
        cbArea.isChecked      = initial.showArea
        cbPerimeter.isChecked = initial.showPerimeter
        when (initial.selectedShape) {
            Shape.CIRCLE    -> rbCircle.isChecked    = true
            Shape.RECTANGLE -> rbRectangle.isChecked = true
            Shape.TRIANGLE  -> rbTriangle.isChecked  = true
        }

        cbArea.setOnCheckedChangeListener      { _, checked -> viewModel.onAreaChecked(checked) }
        cbPerimeter.setOnCheckedChangeListener { _, checked -> viewModel.onPerimeterChecked(checked) }

        rgShape.setOnCheckedChangeListener { _, checkedId ->
            val shape = when (checkedId) {
                R.id.rbCircle    -> Shape.CIRCLE
                R.id.rbRectangle -> Shape.RECTANGLE
                R.id.rbTriangle  -> Shape.TRIANGLE
                else             -> Shape.CIRCLE
            }
            viewModel.onShapeSelected(shape)
        }

        btnOk.setOnClickListener {
            viewModel.confirm()
            val state = viewModel.uiState.value
            if (state.showResult) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ResultFragment())
                    .addToBackStack("input")
                    .commit()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                tvError.text       = state.error ?: ""
                tvError.visibility = if (state.error != null) View.VISIBLE else View.GONE
            }
        }
    }
}
