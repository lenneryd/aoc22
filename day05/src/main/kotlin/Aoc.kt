import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Operation(val count: Int, val from: Int, val to: Int)
data class Config(val initial: List<MutableList<Char>>, val operations: List<Operation>)

fun List<String>.toInitialConfig(): List<MutableList<Char>> {
    val piles = Regex("[1-9]+").findAll(this.last()).count()
    return this.reversed().drop(1).let { list ->
        buildList {
            for (i in 0 until piles) {
                add(mutableListOf())
            }

            list.map { line ->
                Regex("([A-Z])|([ ]{4})").findAll(line).toList().mapIndexed { idx, result ->
                    val value = result.groups[1]?.value?.takeUnless { it.isBlank() }
                    value?.let {
                        this[idx].add(it.first())
                    }
                }
            }
        }
    }
}

fun List<String>.mapInput(): Config = Config(
    this.subList(0, this.indexOfFirst { line -> line.isEmpty() }).toInitialConfig(),
    this.subList(this.indexOfFirst { line -> line.isEmpty() } + 1, this.size).map { operation ->
        Regex("move ([0-9]+) from ([1-9]) to ([1-9])").find(operation)?.groupValues?.let { Operation(it[1].toInt(), it[2].toInt() - 1, it[3].toInt() - 1) }
            ?: throw IllegalArgumentException("Can't match on $operation")
    }
)

fun Config.perform(operations: List<Operation>): Config {
    operations.forEach { op ->
        for (i in 0 until op.count) {
            this.initial[op.from].removeLast().let { this.initial[op.to].add(it) }
        }

    }
    return this
}

fun Config.performMultipleMove(operations: List<Operation>): Config {
    operations.forEach { op ->
        val moved = this.initial[op.from].takeLast(op.count)
        val from = this.initial[op.from].dropLast(op.count)
        val to = this.initial[op.to] + moved
        this.initial[op.from].clear()
        this.initial[op.from].addAll(from)
        this.initial[op.to].clear()
        this.initial[op.to].addAll(to)
    }
    return this
}

fun solutionPart1(input: Config): String = input.perform(input.operations).let { it.initial.joinToString(separator = "") { c -> c.last().toString() } }

fun solutionPart2(input: Config): String =
    input.performMultipleMove(input.operations).let { it.initial.joinToString(separator = "") { c -> c.last().toString() } }
