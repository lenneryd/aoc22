import java.io.File
import java.lang.IllegalArgumentException

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

sealed class Operation(open val durationLeft: Int) {
    data class ADDX(val value: Int, override val durationLeft: Int) : Operation(durationLeft)
    data class NOOP(override val durationLeft: Int) : Operation(durationLeft)
}

fun String.toOperation() = if (this.startsWith("noop")) Operation.NOOP(1) else Operation.ADDX(this.split(" ")[1].toInt(), 2)

fun List<String>.mapInput(): List<Operation> = map { it.toOperation() }

fun List<Operation>.runCPU(cycles: Int, print: Boolean = false): Int {
    val queue = this.toMutableList()
    var x = 1
    var current: Operation = queue.removeFirst()

    for (i in 1 until cycles) {
        val c = current
        current = when (c) {
            is Operation.ADDX -> c.copy(durationLeft = c.durationLeft - 1)
            is Operation.NOOP -> c.copy(durationLeft = c.durationLeft - 1)
        }

        if (print) {
            val crtPos = (i-1) % 40
            if (crtPos == 0) {
                println()
            }

            val spritePos = listOf(x - 1, x, x + 1)
            print(if (spritePos.contains(crtPos)) "#" else " ")
        }

        val updated = current
        if (updated.durationLeft == 0) {
            when (updated) {
                is Operation.ADDX -> x += updated.value
                is Operation.NOOP -> {}
            }
            current = queue.removeFirstOrNull() ?: break
        }
    }
    return x
}

fun solutionPart1(input: List<Operation>): Long = listOf(20, 60, 100, 140, 180, 220).sumOf { it * input.runCPU(it) }.toLong()

fun solutionPart2(input: List<Operation>) {
    input.runCPU(20000, false)
}
