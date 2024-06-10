

fun main() {
    val calculator = Calculator()
    val input = readln().replace(" ", "")

    val result = calculator.doIt(input)
    println(result)
}

class Calculator {
    private val add = AddOperation()
    private val subtract = SubtractOperation()
    private val multiply = MultiplyOperation()
    private val divide = DivideOperation()

    fun doIt(str: String): Float {
        return if(!containsBracket(str)) {
            doCalculate(str)
        } else {
            println("Calculate: $str")
            val brackets = findBrackets(str)
            var result = str

            for(ele in brackets) {
                var removedBracket = ele.substring(1, ele.length-1)
                println("In bracket: $removedBracket")
                result = result.replace(
                    ele,
                    doIt(removedBracket).toString()
                )
            }

            println("result: $result")
            println("-----------------------------")

            return doIt(result)
        }
    }

    private fun doCalculate(str: String): Float {
        val numbers = ArrayList(str
            .replace("[^₩.0-9]".toRegex(), " ")
            .split(" ")
            .map {
                it.toFloat()
            }
        )
        val operators = str.replace("[₩.0-9]".toRegex(), " ").split(" ").filter {
            it.isNotEmpty()
        }

        //println("numbers(${numbers.size}): ${numbers.joinToString(" ")}")
        //println("operators(${operators.size}): ${operators.joinToString(",")}")

        //곱셈, 나눗셈 먼저 계산
        operators.forEachIndexed { index, it ->
            if (it == "*") {
                val n = multiply.doOperation(numbers[index], numbers[index+1])
                numbers[index] = n
                numbers[index + 1] = -1F
            } else if(it == "%") {
                val n = divide.doOperation(numbers[index], numbers[index+1])
                numbers[index] = n
                numbers[index + 1] = -1F
            }
        }

        var result = numbers.first()
        for((index, ele) in numbers.drop(1).withIndex()) {
            if(ele == -1F) continue

            result = when(operators[index]) {
                "+" -> add.doOperation(result, ele)
                "-" -> subtract.doOperation(result, ele)
                else -> throw Exception("* and % is not filtered.")
            }
        }

        return result
    }
    private fun containsBracket(str: String): Boolean = str.contains("(")
    private fun findBrackets(str: String): List<String> {
        var numOfOpen = 0
        var numOfClose = 0
        val bracketList = mutableListOf<String>()
        var firstOpenIndex = 0

        str.forEachIndexed { i, ele ->
            if(ele == '(') {
                numOfOpen++
                if(numOfOpen == 1) firstOpenIndex = i
            }
            else if(ele == ')') {
                numOfClose++

                if(numOfOpen == numOfClose) {
                    bracketList.add(str.substring(firstOpenIndex, i+1))
                    numOfClose = 0
                    numOfOpen = 0
                }
            }
        }

        return bracketList
    }

}

interface AbstractOperation {
    fun doOperation(a: Float, b: Float): Float
}

class AddOperation: AbstractOperation {
    override fun doOperation(a: Float, b: Float): Float = a + b
}
class SubtractOperation: AbstractOperation {
    override fun doOperation(a: Float, b: Float): Float = a - b
}
class MultiplyOperation: AbstractOperation {
    override fun doOperation(a: Float, b: Float): Float = a * b
}
class DivideOperation: AbstractOperation {
    override fun doOperation(a: Float, b: Float): Float = a/b + a%b
}