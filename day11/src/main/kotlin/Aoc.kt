import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.floor

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

val multi: (a: () -> Long, b: () -> Long) -> Long = { a, b ->
    a() * b()
}
val add: (a: () -> Long, b: () -> Long) -> Long = { a, b ->
    a() + b()
}

class Operation(val a: () -> Long, val b: () -> Long, val perform: (a: () -> Long, b: () -> Long) -> Long)

fun String.findIdx(str: String) = this.indexOf(str) + str.length
class Monkey(
    private val input: List<String>,
    private val divideWorry: Boolean,
    private val modulo: Int? = null,
    private val throwTo: (Pair<Long, Int>) -> Unit
) {
    val id: Int = input[0].substring(7, 8).toInt()
    val items: MutableList<Long> = input[1].substring(input[1].findIdx("items: ")).split(", ").map { it.trim().toLong() }.toMutableList()

    private val leftHand: () -> Long
    private val rightHand: () -> Long
    private var monkeyBusiness = 0L
    val operation = input[2].substring(input[2].findIdx("Operation: ")).let { str ->
        val m = str.split(" * ")
        val a = str.split(" + ")
        val values = if (m.size == 2) m else a

        leftHand = when {
            values[0].contains("old") -> {
                { items.first() }
            }
            else -> {
                { values[0].toLong() }
            }
        }

        rightHand = when {
            values[1].contains("old") -> {
                { items.first() }
            }
            else -> {
                { values[1].toLong() }
            }
        }

        if (m.size == 2) Operation(leftHand, rightHand, multi) else Operation(leftHand, rightHand, add)
    }

    private val test: (Long) -> Boolean =
        { value -> value % input[3].substring(input[3].findIdx("divisible by ")).toLong() == 0L }

    private val testResult: (Boolean) -> Int = { result ->
        if (result) {
            input[4].substring(input[4].findIdx("monkey ")).toInt()
        } else {
            input[5].substring(input[5].findIdx("monkey ")).toInt()
        }
    }

    fun catch(item: Long) {
        items.add(item)
    }

    fun throwItem(): Long = items.removeFirst()

    fun inspect(): Long = operation.perform(operation.a, operation.b).let { worry ->
        print(" increasing it to $worry")
        monkeyBusiness++

        if (divideWorry) {
            floor(worry.toDouble() / 3.0).toLong().also {
                print(" then reducing it to: $it. ")
            }
        } else if (modulo != null) {
            worry % modulo
        } else {
            throw IllegalArgumentException("No Modulo when not dividing worry.")
        }
    }

    fun Long.decideTarget(): Pair<Long, Int> = this.let { worry ->
        val target = testResult(test(worry))
        worry to target
    }

    fun performTurn() {
        while (items.isNotEmpty()) {
            print(" inspects [${items.first()}]")
            inspect().decideTarget().let { (worry, target) ->
                throwItem()
                print(" Monkey $id throws ")
                throwTo(worry to target)
            }
        }
    }

    fun getMonkeyBusiness() = monkeyBusiness

}

fun List<String>.mapInput(): List<List<String>> = this.filter { it.isNotEmpty() }.chunked(6)

fun print(str: String, print: Boolean = false) {
    if (print) {
        kotlin.io.print(str)
    }
}

fun println(str: String, print: Boolean = false) {
    if (print) {
        kotlin.io.println(str)
    }
}

fun solutionPart1(input: List<List<String>>): Long {
    val monkeys: MutableList<Monkey> = mutableListOf()

    input.map {
        monkeys.add(Monkey(it, divideWorry = true) { (worry, target) ->
            println("[$worry] => Monkey $target")
            monkeys[target].catch(worry)
        })
    }

    repeat(20) { round ->
        println("START Round: $round")
        monkeys.forEach {
            println("Monkey ${it.id} starts turn with ${it.items.joinToString { item -> item.toString() }}")
            it.performTurn()
        }

        println("END Round: $round")
    }

    return monkeys.sortedByDescending { it.getMonkeyBusiness() }.let {
        it[0].getMonkeyBusiness() * it[1].getMonkeyBusiness()
    }.toLong()
}

fun solutionPart2(input: List<List<String>>): Long {
    val monkeys: MutableList<Monkey> = mutableListOf()

    input.map {
        monkeys.add(Monkey(it, divideWorry = false,
            modulo = input.flatten().filter { it.contains("divisible by ") }.map { it.substring(it.findIdx("divisible by ")).toInt() }.fold(1) { acc, div ->
                acc * div
            }
        ) { (worry, target) ->
            println("[$worry] => Monkey $target")
            monkeys[target].catch(worry)
        })
    }

    repeat(10000) { round ->
        println("START Round: $round")
        monkeys.forEach {
            println("Monkey ${it.id} starts turn with ${it.items.joinToString { item -> item.toString() }}")
            it.performTurn()
        }

        println("END Round: $round")
    }

    return monkeys.sortedByDescending { it.getMonkeyBusiness() }.let {
        it[0].getMonkeyBusiness() * it[1].getMonkeyBusiness()
    }.toLong()
}
