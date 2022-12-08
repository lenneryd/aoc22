import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): List<List<Tree>> = this.mapIndexed { row, list ->
    list.split("").filter { it.isNotEmpty() }.mapIndexed { col, c -> Tree(row, col, c.toInt()) }
}

data class Tree(val row: Int, val col: Int, val height: Int)

fun List<List<Tree>>.isVisible(tree: Tree): Boolean {
    // Check Above
    var visibleAbove = true
    for (r in (tree.row - 1) downTo 0) {
        if (tree.height <= this[r][tree.col].height) {
            visibleAbove = false
            break
        }
    }

    // Check Below
    var visibleBelow = true
    for (r in (tree.row + 1)..this.lastIndex) {
        if (tree.height <= this[r][tree.col].height) {
            visibleBelow = false
            break
        }
    }

    // Check Left
    var visibleLeft = true
    for (c in (tree.col - 1) downTo 0) {
        if (tree.height <= this[tree.row][c].height) {
            visibleLeft = false
            break
        }
    }

    // Check Right
    var visibleRight = true
    for (c in (tree.col + 1)..this.first().lastIndex) {
        if (tree.height <= this[tree.row][c].height) {
            visibleRight = false
            break
        }
    }
    return visibleAbove || visibleBelow || visibleLeft || visibleRight
}

fun List<List<Tree>>.viewingScore(tree: Tree): Int {
    // Check Above
    var scoreAbove = 0
    for (r in (tree.row - 1) downTo 0) {
        if (tree.height > this[r][tree.col].height) {
            scoreAbove += 1
        } else {
            scoreAbove += 1
            break
        }
    }

    // Check Below
    var scoreBelow = 0
    for (r in (tree.row + 1)..this.lastIndex) {
        if (tree.height > this[r][tree.col].height) {
            scoreBelow += 1
        } else {
            scoreBelow += 1
            break
        }
    }

    // Check Left
    var scoreLeft = 0
    for (c in (tree.col - 1) downTo 0) {
        if (tree.height > this[tree.row][c].height) {
            scoreLeft += 1
        } else {
            scoreLeft += 1
            break
        }
    }

    // Check Right
    var scoreRight = 0
    for (c in (tree.col + 1)..this.first().lastIndex) {
        if (tree.height > this[tree.row][c].height) {
            scoreRight += 1
        } else {
            scoreRight += 1
            break
        }
    }
    return scoreAbove * scoreBelow * scoreRight * scoreLeft
}

fun solutionPart1(input: List<List<Tree>>): Long {
    val lastRow = input.lastIndex
    val lastCol = input.first().lastIndex
    val lastConsideredRow = lastRow - 1
    val lastConsideredCol = lastCol - 1

    val visibleTrees: MutableSet<Tree> = mutableSetOf()
    visibleTrees.addAll(input.first())
    visibleTrees.addAll(input.last())
    visibleTrees.addAll(input.map { it.last() })
    visibleTrees.addAll(input.map { it.first() })

    for (row in 1..lastConsideredRow) {
        for (col in 1..lastConsideredCol) {
            val tree = input[row][col]
            if (input.isVisible(tree)) {
                visibleTrees.add(tree)
            }
        }
    }

    return visibleTrees.size.toLong()
}

fun solutionPart2(input: List<List<Tree>>): Long {
    val lastRow = input.lastIndex - 1
    val lastCol = input.first().lastIndex - 1

    var maxScore = 0
    for (row in 1..lastRow) {
        for (col in 1..lastCol) {
            val tree = input[row][col]
            val score = input.viewingScore(tree)
            maxScore = score.coerceAtLeast(maxScore)
        }
    }
    return maxScore.toLong()
}
