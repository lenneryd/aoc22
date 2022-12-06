import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(7, solutionPart1(listOf("mjqjpqmgbljsphdztnvjfqwrcgsmlb").mapInput()))
        assertEquals(5, solutionPart1(listOf("bvwbjplbgvbhsrlpgdmjqwftvncz").mapInput()))
        assertEquals(6, solutionPart1(listOf("nppdvjthqldpwncqszvftbrmjlhg").mapInput()))
        assertEquals(10, solutionPart1(listOf("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg").mapInput()))
        assertEquals(11, solutionPart1(listOf("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw").mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(19, solutionPart2(listOf("mjqjpqmgbljsphdztnvjfqwrcgsmlb").mapInput()))
        assertEquals(23, solutionPart2(listOf("bvwbjplbgvbhsrlpgdmjqwftvncz").mapInput()))
        assertEquals(23, solutionPart2(listOf("nppdvjthqldpwncqszvftbrmjlhg").mapInput()))
        assertEquals(29, solutionPart2(listOf("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg").mapInput()))
        assertEquals(26, solutionPart2(listOf("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw").mapInput()))
    }
}
