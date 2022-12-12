import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Node(val x: Int, val y: Int, val h: Char) : Comparable<Node> {
    val neighbours: MutableList<Node> = mutableListOf()
    override fun compareTo(other: Node) = this.h.toHeight().compareTo(other.h.toHeight())
}

data class Graph(val grid: List<List<Node>>, val start: Node, val end: Node)

fun Char.toHeight() = when (this) {
    'S' -> 'a'
    'E' -> 'z'
    else -> this
}.code

fun List<String>.mapInput(): Graph = mapIndexed { row, str ->
    str.mapIndexed { col, c ->
        Node(row, col, c)
    }
}.let { grid ->
    var start: Node? = null
    var end: Node? = null
    for (x in 0..grid.lastIndex) {
        for (y in 0..grid.first().lastIndex) {
            val node = grid[x][y]

            if (node.h == 'S') {
                start = node
            } else if (node.h == 'E') {
                end = node
            }

            val neighbours = listOfNotNull(
                grid.getOrNull(x - 1)?.getOrNull(y),
                grid.getOrNull(x)?.getOrNull(y - 1),
                grid.getOrNull(x + 1)?.getOrNull(y),
                grid.getOrNull(x)?.getOrNull(y + 1)
            )

            neighbours.forEach {
                // Allowed Diff is +1 height.
                val diff = node.h.toHeight() + 1 - it.h.toHeight()
                if (diff >= 0) {
                    node.neighbours.add(it)
                }
            }
        }
    }

    Graph(grid, start!!, end!!)
}

fun Node.toPos() = x to y
data class Queued(val node: Node, val dist: Long)

fun Map<Pair<Int, Int>, Long>.getDist(pos: Pair<Int, Int>) = this[pos]!!

fun Graph.djikstra2(): List<Node> = this.let { graph ->
    val dist = graph.grid.flatten().map { it.x to it.y }.associateWith { Integer.MAX_VALUE.toLong() }.toMutableMap()
    dist[graph.start.toPos()] = 0

    val adjacent = mutableMapOf<Node, Node>()
    val comparator = compareBy<Queued> { it.dist }
    val queue = PriorityQueue(comparator)
    queue.add(Queued(graph.start, 0))
    val inQueue: MutableSet<Node> = graph.grid.flatten().toSet().toMutableSet()
    val settled: MutableSet<Node> = mutableSetOf()

    var prev: Node? = null

    while (settled.size < graph.grid.size * graph.grid.first().size) {
        val current = try {
            queue.remove().node
        } catch (e: Exception) {
            //println("Failed to remove from queue: ${queue.joinToString { "(${it.node.x}, ${it.node.y})" }}. Prev: (${prev?.x}, ${prev?.y})")
            break
        }

        try {
            inQueue.remove(current)
        } catch (e: Exception) {
            //println("Failed to  remove (${current.x}, ${current.y}) from inQueue: ${inQueue.joinToString { "(${it.x}, ${it.y})" }}")
            break
        }

        if (!settled.contains(current)) {
            current.neighbours.filter { !settled.contains(it) }.forEach { nb ->
                val alt = dist.getDist(current.toPos()) + nb.h.toHeight()
                if (alt < dist.getDist(nb.toPos())) {
                    dist[nb.toPos()] = alt
                    adjacent[nb] = current
                }

                queue.add(Queued(nb, dist.getDist(nb.toPos())))
                inQueue.add(nb)
            }
            settled.add(current)
        }

        prev = current
    }

    val path = mutableListOf(end)
    while (adjacent.containsKey(path.last())) {
        path += adjacent[path.last()]!!
    }

    path
}


fun solutionPart1(input: Graph): Long = input.djikstra2().size.toLong() - 1

// FIXME: This gave the correct result, but really shouldn't. It gave the same result as part one, which is apparently correct. It fails in the djikstra.
fun solutionPart2(input: Graph): Long = input.grid.flatten().filter {
    (it.x == 0 || it.x == input.grid.lastIndex || it.y == 0 || it.y == input.grid.first().lastIndex) && it.h == 'a'
}.let { startPositions ->
    startPositions.minOf { start ->
        val path = Graph(input.grid, start, input.end).djikstra2().size.toLong() -1
        if(path != 0L) path else Long.MAX_VALUE
    }
}
