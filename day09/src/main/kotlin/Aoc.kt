import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): List<Pair<String, Int>> = this.map { op -> op.split(" ").let { (direction, steps) -> direction to steps.toInt() } }

private fun moveHead(direction: String, head: Point): Point =
    Direction.valueOf(direction).let { head.pointDelta(it.deltaX, it.deltaY) }

private fun moveTail(rope: Rope): ArrayDeque<Point> = rope.tail
    .fold(ArrayDeque(rope.tail.size)) { tail, knot ->
        val head = tail.lastOrNull() ?: rope.head
        tail.add(motion(head.pointDelta(knot), knot))
        tail
    }

private fun motion(delta: Point, tail: Point): Point =
    // We can leave tail untouched.
    if (delta.x.absoluteValue != 2 && delta.y.absoluteValue != 2) {
        tail
    // We have to move the segment.
    } else tail.pointDelta(
        x = delta.x.coerceIn(-1, 1) * -1,
        y = delta.y.coerceIn(-1, 1) * -1
    )

enum class Direction(val deltaX: Int, val deltaY: Int) {
    U(0, 1),
    R(1, 0),
    D(0, -1),
    L(-1, 0),
}

data class Point(val x: Int = 0, val y: Int = 0) {

    fun pointDelta(x: Int, y: Int) = pointDelta(Point(x, y))

    fun pointDelta(d: Point): Point = Point(x - d.x, y - d.y)
}

data class Rope(var head: Point, var tail: ArrayDeque<Point>, val visited: HashSet<Point>)

private fun List<Pair<String, Int>>.physics(initial: Rope) = fold(initial) { rope, (direction, steps) ->
    rope.apply {
        repeat(steps) {
            head = moveHead(direction, head)
            tail = moveTail(this)
            visited.add(tail.last())
        }
    }
}

fun solutionPart1(input: List<Pair<String, Int>>): Long = input.physics(Rope(Point(), ArrayDeque(List(1) { Point() }), HashSet())).visited.size.toLong()

fun solutionPart2(input: List<Pair<String, Int>>): Long = input.physics(Rope(Point(), ArrayDeque(List(9) { Point() }), HashSet())).visited.size.toLong()
