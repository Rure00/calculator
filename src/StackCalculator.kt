import java.util.*

fun main() {
    val calculator = StackCalculator()
    //val input = readln().replace(" ", "")
    val input = "2 * ((2 + 1) * 3 + 1)"

    calculator.doCalculate(input)
}

class StackCalculator() {
    fun doCalculate(expression: String): Int {
        val stack = Stack<String>()
        val arr = strToArr(expression)
        var result = ""

        println("strToArr: ${arr.joinToString(" ")}")

        //stack: *

        for(e in arr) {
            when(e) {
                "+", "-", "*", "/" -> {
                    while(!stack.isEmpty() && precedence(stack.peek()) >= precedence(e)) {
                        result += stack.pop() + " "
                    }
                    stack.push(e)
                }
                "(" -> stack.push(e)
                ")" -> {
                    while(stack.peek() != "(") {
                        result += stack.pop() + " "
                    }
                    stack.pop() // "(" 제거
                }
                else -> result += "$e " //숫자
            }
        }

        println("result: $result")

        while(!stack.isEmpty()) {
            result += stack.pop() + " "
        }

        val realResult = finalCalc(result)
        println("결과: $realResult")
        return realResult
    }

    private fun finalCalc(str: String): Int {
        val stack = Stack<String>()
        val calResult = str.split(" ")
        var result = 0

        for(e in calResult) {
            when(e) {
                "+" -> {
                    result = stack.pop().toInt() + stack.pop().toInt()
                    stack.push(result.toString())
                }
                "-" -> {
                    result = -stack.pop().toInt() + stack.pop().toInt()
                    stack.push(result.toString())
                }
                "*" -> {
                    result = stack.pop().toInt() * stack.pop().toInt()
                    stack.push(result.toString())
                }
                "/" -> {
                    val num2 = stack.pop().toInt()
                    val num1 = stack.pop().toInt()
                    result = num1 / num2
                    stack.push(result.toString())
                }
                else -> {
                    stack.push(e)
                }
            }
        }
        return result
    }

    private fun strToArr(str: String): Array<String> {
        var tempStr = str.replace("(", "( ")
        tempStr = tempStr.replace(")", " )")
        return tempStr.split(" ").toTypedArray()
    }
    private fun precedence(operator: String): Int {
        return when(operator) {
            "+", "-" -> 1
            "*", "/" -> 2
            else -> 0
        }
    }
}