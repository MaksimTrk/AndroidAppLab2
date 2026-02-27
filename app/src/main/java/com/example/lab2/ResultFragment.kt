package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private val viewModel: ShapeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvResult  = view.findViewById<TextView>(R.id.tvResult)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                tvResult.text = buildResultText(state)
            }
        }

        btnCancel.setOnClickListener {
            viewModel.cancel()
            parentFragmentManager.popBackStack()
        }
    }

    private fun buildResultText(state: ShapeUiState): String {
        val sb = StringBuilder()
        sb.appendLine("Фігура: ${state.selectedShape.title}")
        sb.appendLine()

        val what = mutableListOf<String>()
        if (state.showArea)      what.add("площа")
        if (state.showPerimeter) what.add("периметр")
        sb.appendLine("Вивести: ${what.joinToString(" та ")}")
        sb.appendLine()

        // Формули залежно від фігури та вибраних прапорців
        when (state.selectedShape) {
            Shape.CIRCLE -> {
                if (state.showArea)      sb.appendLine("Площа = π × r²")
                if (state.showPerimeter) sb.appendLine("Периметр (довжина кола) = 2 × π × r")
            }
            Shape.RECTANGLE -> {
                if (state.showArea)      sb.appendLine("Площа = a × b")
                if (state.showPerimeter) sb.appendLine("Периметр = 2 × (a + b)")
            }
            Shape.TRIANGLE -> {
                if (state.showArea)      sb.appendLine("Площа = (основа × висота) / 2")
                if (state.showPerimeter) sb.appendLine("Периметр = a + b + c")
            }
        }

        return sb.toString().trimEnd()
    }
}
