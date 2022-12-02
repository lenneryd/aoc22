import java.io.File
import java.lang.IllegalArgumentException

fun main() {
    println(
        File("input.txt").readLines()
            .let { input -> if (System.getenv("part") == "part2") solutionPart2(input.mapInput2()) else solutionPart1(input.mapInput()) })
}

data class Round(val hands: List<Play>, val points: Int)
sealed class Play(val points: Int, open val value: String, val hand: String, val wins: String, val draw: String, val lose: String) {
    data class Rock(override val value: String) : Play(1, value, "Rock", "C", "A", "B")
    data class Paper(override val value: String) : Play(2, value, "Paper", "A", "B", "C")
    data class Scissors(override val value: String) : Play(3, value, "Scissors", "B", "C", "A")
}

fun String.toPlay() = when {
    listOf("A", "X").contains(this) -> Play.Rock(this)
    listOf("B", "Y").contains(this) -> Play.Paper(this)
    listOf("C", "Z").contains(this) -> Play.Scissors(this)
    else -> throw IllegalArgumentException(this)
}

fun Play.toPlay2(state: String) = when (state) {
    "X" -> this.wins.toPlay()
    "Y" -> this.draw.toPlay()
    "Z" -> this.lose.toPlay()
    else -> throw IllegalArgumentException(state)
}

fun List<Play>.toPoints(): Int = when {
    this[0] is Play.Rock && this[1] is Play.Paper -> 6
    this[0] is Play.Paper && this[1] is Play.Scissors -> 6
    this[0] is Play.Scissors && this[1] is Play.Rock -> 6
    this[0].hand == this[1].hand -> 3
    else -> 0
}

fun String.toRound() = split(" ").map { it.toPlay() }.let { Round(hands = it, points = it[1].points + it.toPoints()) }

fun List<String>.mapInput(): List<Round> = map { it.toRound() }
fun List<String>.mapInput2(): List<Round> = map { it.split(" ") }.map { plays ->
    plays[0].toPlay().let { first ->
        listOf(first, first.toPlay2(plays[1]))
    }
}.map { Round(it, it[1].points + it.toPoints()) }

fun solutionPart1(input: List<Round>): Long = input.sumOf { it.points }.toLong()

fun solutionPart2(input: List<Round>): Long = input.sumOf { it.points }.toLong()
