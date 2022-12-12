import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    val list: List<String> = listOf(
        "Sabqponm",
        "abcryxxl",
        "accszExk",
        "acctuvwj",
        "abdefghi",
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(31, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(29, solutionPart2(list.mapInput()))
    }
}
