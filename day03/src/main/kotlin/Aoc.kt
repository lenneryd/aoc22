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

fun List<String>.mapInput(): List<Sack> = this.map { items ->
    Sack(
        items.substring(0 until (items.length / 2)).map { Item(it, it.toPrio()) },
        items.substring((items.length / 2) until items.length).map { Item(it, it.toPrio()) },
        items
    )
}

fun Char.toPrio() = when (this.code) {
    in 65..90 -> this.code - 38
    in 97..122 -> this.code - 96
    else -> throw IllegalArgumentException(this.toString())
}

data class Item(val letter: Char, val prio: Int)
data class Sack(val first: List<Item>, val second: List<Item>, val fullStr: String)

fun solutionPart1(input: List<Sack>): Long = input.sumOf { sack ->
    sack.first.intersect(sack.second.toSet()).sumOf { it.prio }
}.toLong()

fun List<String>.findItem() = this.fold(this.first().toSet().toMutableSet()) { acc, list ->
    acc.intersect(list.toSet()).let {
        acc.clear()
        acc.addAll(it)
    }
    acc
}

fun solutionPart2(input: List<Sack>): Long = input.chunked(3).map { group ->
    group.map { it.fullStr }.findItem().sumOf { it.toPrio() }
}.sum().toLong()
