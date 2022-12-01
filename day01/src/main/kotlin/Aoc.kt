import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): MutableList<MutableList<Int>> = this.fold(mutableListOf()) { acc, s ->
    if (s.isBlank() || acc.isEmpty()) {
        acc.add(mutableListOf())
        acc
    } else {
        acc.last().add(s.toInt())
        acc
    }
}

fun solutionPart1(input: MutableList<MutableList<Int>>): Long =
    input.maxOf { it.sum() }.toLong()

fun solutionPart2(input: MutableList<MutableList<Int>>): Long = input.map { it.sum() }.sortedDescending().subList(0, 3).sum().toLong()
