import java.io.File
import kotlin.math.min
import kotlin.math.max

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): List<Pair<IntRange, IntRange>> = this.map { str -> str.split(",").let { pairs -> pairs[0].toIntRange() to pairs[1].toIntRange() } }

fun String.toIntRange(): IntRange = this.split("-").let { it[0].toInt()..it[1].toInt() }
fun IntRange.intersect(other: IntRange) =
    (min(endInclusive, other.last) + 1 - max(start, other.first))
        .coerceAtLeast(0)

fun solutionPart1(input: List<Pair<IntRange, IntRange>>): Long = input.count { pair ->
    pair.first.intersect(pair.second) == min(pair.first.count(), pair.second.count())
}.toLong()

fun solutionPart2(input: List<Pair<IntRange, IntRange>>): Long = input.count { pair ->
    pair.first.intersect(pair.second) >= 1
}.toLong()
