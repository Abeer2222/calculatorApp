package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var plusMinus: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        plusMinus = findViewById(R.id.plusMinus)
        plusMinus.setOnClickListener {
           // PlusMinus()
        }
    }

    /*fun PlusMinus(passedList: MutableList<Any>): Float {
        var result = passedList[0]
        var minus = "+/-"
        var toF= result.toString().toFloat()
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == minus)
                    result == "-$nextDigit"

                if (operator == '-')
                    result = nextDigit
            }
        }
    }*/

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal = false
            } else
                workingsTV.append(view.text)

            canAddOperation = true
        }
    }


    fun operatorAction(view: View) {
        if (view is Button && canAddOperation) {
            workingsTV.append(view.text)
        }
    }


    fun allClearAction(view: View) {
        workingsTV.text = ""
        resultsTV.text = ""
    }


    fun backSpaceAction(view: View) {
        val length = workingsTV.length()
        if (length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }


    /* fun PlusMinus(){
         if(operator==' '){
             num1 = if(num1.startsWith("-")){
                 num1.substring(1, num1.length)
             } else{
                 "-$num1"
             }
             displayText = num1
         }else{
             num2 = if(num2.startsWith("-")){
                 num2.substring(1, num2.length)
             } else{
                 "-$num2"
             }
             val text = num1 + operator + num2
             displayText = text
         }
     }*/

    fun equalsAction(view: View) {
        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)//before Add
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }


    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)//one time division
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        if (nextDigit != 0f) {
                            newList.add(prevDigit / nextDigit)
                            restartIndex = i + 1
                        } else {
                            Toast.makeText(this, "You can't divide by Zero!!", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }


    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in workingsTV.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }


}