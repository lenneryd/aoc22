import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "    [D]    ",
        "[N] [C]    ",
        "[Z] [M] [P]",
        " 1   2   3 ",
        "",
        "move 1 from 2 to 1",
        "move 3 from 1 to 3",
        "move 2 from 2 to 1",
        "move 1 from 1 to 2",
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals("CMZ", solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals("MCD", solutionPart2(list.mapInput()))
    }
}
