import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): List<Char> = first().toCharArray().asList()

fun List<Char>.isUnique(): Boolean = mutableSetOf<Char>().let { it.addAll(this); it.size == this.size }

fun List<List<Char>>.findStart(): Long {
    var idx = 0;
    var list = this[idx]
    while (!list.isUnique()) {
        idx++
        list = this[idx]
    }
    return (idx + this.first().size).toLong()
}

fun solutionPart1(input: List<Char>): Long = input.windowed(4, 1, false).findStart()

fun solutionPart2(input: List<Char>): Long = input.windowed(14, 1, false).findStart()
