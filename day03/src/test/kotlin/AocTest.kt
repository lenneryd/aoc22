import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "vJrwpWtwJgWrhcsFMMfFFhFp",
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
        "PmmdzqPrVvPwwTWBwg",
        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
        "ttgJtRGJQctTZtZT",
        "CrZsJsPPZsGzwwsLwLmpwMDw"
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(157, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(70, solutionPart2(list.mapInput()))
    }
}
