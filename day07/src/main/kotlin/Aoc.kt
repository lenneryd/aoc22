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

fun String.isCommand(): Boolean = startsWith("\$ ")
fun String.isDir(): Boolean = startsWith("dir")
fun List<String>.findCommands() = fold(mutableListOf<Command>()) { acc, str ->
    if (str.isCommand()) {
        acc.add(Command(str, mutableListOf()))
    } else {
        acc.last().output.add(str)
    }
    acc
}

fun List<String>.mapInput(): Item.Dir = Item.Dir("/", null, mutableMapOf(), null).let { root ->
    findCommands().fold(
        Structure(root = root, current = root)
    ) { structure, command ->
        when {
            command.command.startsWith("$ ls") -> {
                command.output.forEach { output ->
                    if (output.isDir()) {
                        val name = output.substring(4)
                        if (!structure.current!!.children.contains(name)) {
                            structure.current!!.children[name] = Item.Dir(name, structure.current, mutableMapOf(), null)
                        }
                    } else {
                        val size = output.split(" ")[0].toLong()
                        val name = output.split(" ")[1]
                        if (!structure.current!!.children.contains(name)) {
                            structure.current!!.children[name] = Item.File(name, size)
                        }
                    }
                }
            }
            command.command.startsWith("$ cd ..") -> {
                structure.current = structure.current!!.parent
            }

            command.command.startsWith("$ cd /") -> {
                structure.current = structure.root
            }

            command.command.startsWith("$ cd") -> {
                val name = command.command.substring(5)
                val child = structure.current!!.children[name]
                if (child == null) {
                    val newNode = Item.Dir(name, structure.current,mutableMapOf(), null)
                    structure.current!!.children[name] = newNode
                    structure.current = newNode
                } else {
                    structure.current = child as Item.Dir
                }
            }

            else -> throw IllegalArgumentException("Command: ${command.command} doesn't work")
        }

        structure
    }.root
}

data class Command(val command: String, val output: MutableList<String>)
data class Structure(var root: Item.Dir, var current: Item.Dir?)
sealed class Item(open val name: String) {
    data class Dir(override val name: String, val parent: Dir?, val children: MutableMap<String, Item>, var size: Long?) : Item(name) {
        override fun toString(): String = "dir $name, size: $size"
    }
    data class File(override val name: String, val size: Long) : Item(name) {
        override fun toString(): String = "file $name, size: $size"
    }
}

fun Item.Dir.countSize(): Long = this.let { dir ->
    val size = children.values.sumOf { child ->
        when (child) {
            is Item.Dir -> {
                child.countSize()
            }
            is Item.File -> child.size
        }
    }
    dir.size = size
    size
}

fun Item.Dir.allChildDirs(): List<Item.Dir> = this.children.values.fold(mutableListOf()) { acc, item ->
    when (item) {
        is Item.Dir -> {
            acc.add(item)
            acc.addAll(item.allChildDirs())
            acc
        }
        is Item.File -> {
            acc
        }
    }
}

fun solutionPart1(input: Item.Dir): Long = input.allChildDirs().let { dirs ->
    dirs.forEach { it.countSize() }
    dirs
}.sumOf { item ->
    if (item.size!! < 100000) {
        item.size!!
    } else {
        0
    }
}

fun solutionPart2(input: Item.Dir): Long = input.allChildDirs().let { dirs ->
    input.countSize()
    // 25029079 + 23352670 = 48381749
    val total = input.size!!
    val free = 70000000L - total
    val needed = 30000000L - free
    dirs.minOf { dir ->
        dir.size?.takeIf { it > needed } ?: Long.MAX_VALUE
    }
}
