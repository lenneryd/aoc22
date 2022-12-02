import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "A Y",
        "B X",
        "C Z"
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(15, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(12, solutionPart2(list.mapInput2()))
    }
}
