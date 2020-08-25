package com.katevu.caculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    //Var to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    private val result = MutableLiveData<Double>()
    val stringResult: LiveData<String>
        get() = Transformations.map(result) { it.toString() }
    private val newNumber = MutableLiveData<String>()
    val stringNewNumber: LiveData<String>
        get() = newNumber
    private val operator = MutableLiveData<String>()
    val stringOperator: LiveData<String>
        get() = operator


    fun digitPressed(caption: String) {
        if (newNumber.value != null) {
            newNumber.value = newNumber.value + caption
        } else {
            newNumber.value = caption
        }
    }

    fun operandPressed(op: String) {
        try {
            val value = newNumber.value?.toDouble()
            if (value != null) {
                performOperator(value, op)
            }
        } catch (e: NumberFormatException) {
            newNumber.value = ""
        }
        pendingOperation = op
        operator.value = pendingOperation
    }

    fun negPressed() {
        val value = newNumber.value
        if (value == null || value.isEmpty()) {
            newNumber.value = "-"
        } else {
            try {
                var doubleValue = value.toDouble()
                doubleValue *= -1
                newNumber.value = doubleValue.toString()
            } catch (e: java.lang.NumberFormatException) {
                newNumber.value = ""
            }
        }

    }
    private fun performOperator(value: Double, operator: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operator
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.value = operand1
        newNumber.value = ("")
    }

}