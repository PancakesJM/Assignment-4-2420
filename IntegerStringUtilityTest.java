package assign04;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

public class IntegerStringUtilityTest {

    /*
     * Tests StringNumericalValueComparator with normal integer strings.
     * @author Ava Murphy & Yujia Zhao
     * @version September 23, 2026
     */
    @Test
    public void testStringNumericalValueComparatorBasic() {
        Comparator<String> comparator =
                new IntegerStringUtility.StringNumericalValueComparator();

        assertTrue(comparator.compare("1234", "1233") > 0);
        assertTrue(comparator.compare("987", "1231") < 0);
        assertEquals(0, comparator.compare("1234", "1234"));
    }

    /*
     * Tests StringNumericalValueComparator with leading zeros.
     */
    @Test
    public void testStringNumericalValueComparatorLeadingZeros() {
        Comparator<String> comparator =
                new IntegerStringUtility.StringNumericalValueComparator();

        assertTrue(comparator.compare("13", "012") > 0);
        assertEquals(0, comparator.compare("00123", "123"));
        assertTrue(comparator.compare("00012", "13") < 0);
    }

    /*
     * Tests StringNumericalValueComparator with values too large
     * to be represented by long.
     */
    @Test
    public void testStringNumericalValueComparatorLargeNumbers() {
        Comparator<String> comparator =
                new IntegerStringUtility.StringNumericalValueComparator();

        assertTrue(comparator.compare(
                "9223372036854775808",
                "9223372036854775809") < 0);

        assertTrue(comparator.compare(
                "999999999999999999999999999999",
                "1000000000000000000000000000000") < 0);
    }

    /*
     * Tests StringSimilarityComparator with similar strings.
     */
    @Test
    public void testStringSimilarityComparatorSimilar() {
        Comparator<String> comparator =
                new IntegerStringUtility.StringSimilarityComparator();

        assertEquals(0, comparator.compare("1234", "3421"));
        assertEquals(0, comparator.compare("987", "789"));
        assertEquals(0, comparator.compare("1123", "3211"));
    }

    /*
     * Tests StringSimilarityComparator with strings that are not similar.
     */
    @Test
    public void testStringSimilarityComparatorNotSimilar() {
        Comparator<String> comparator =
                new IntegerStringUtility.StringSimilarityComparator();

        assertTrue(comparator.compare("1235", "3421") > 0);
        assertTrue(comparator.compare("3421", "11111") < 0);
        assertTrue(comparator.compare("1234", "1235") < 0);
    }

    /*
     * Tests StringSimilarityGroupComparator when group sizes differ.
     */
    @Test
    public void testStringSimilarityGroupComparatorDifferentSizes() {
        Comparator<String[]> comparator =
                new IntegerStringUtility.StringSimilarityGroupComparator();

        String[] group1 = {"3421", "1234", "4321"};
        String[] group2 = {"987", "789"};

        assertTrue(comparator.compare(group1, group2) > 0);
        assertTrue(comparator.compare(group2, group1) < 0);
    }

    /*
     * Tests StringSimilarityGroupComparator when group sizes are equal.
     */
    @Test
    public void testStringSimilarityGroupComparatorSameSize() {
        Comparator<String[]> comparator =
                new IntegerStringUtility.StringSimilarityGroupComparator();

        String[] group1 = {"3421", "1234", "4321"};
        String[] group2 = {"1235", "5321", "2153"};

        assertTrue(comparator.compare(group1, group2) < 0);
        assertTrue(comparator.compare(group2, group1) > 0);
    }

    /*
     * Tests StringSimilarityGroupComparator with empty groups.
     */
    @Test
    public void testStringSimilarityGroupComparatorEmpty() {
        Comparator<String[]> comparator =
                new IntegerStringUtility.StringSimilarityGroupComparator();

        assertEquals(0,
                comparator.compare(new String[0], new String[0]));
    }

    /*
     * Tests insertionSort using Character natural ordering.
     */
    @Test
    public void testInsertionSortCharacters() {
        Character[] digits = {'8', '6', '1', '0', '4'};

        IntegerStringUtility.insertionSort(
                digits,
                (char1, char2) -> char1.compareTo(char2));

        Character[] expected = {'0', '1', '4', '6', '8'};

        assertArrayEquals(expected, digits);
    }

    /*
     * Tests insertionSort with integers.
     */
    @Test
    public void testInsertionSortIntegers() {
        Integer[] numbers = {5, 2, 8, 1, 3};

        IntegerStringUtility.insertionSort(
                numbers,
                (a, b) -> a.compareTo(b));

        Integer[] expected = {1, 2, 3, 5, 8};

        assertArrayEquals(expected, numbers);
    }

    /*
     * Tests findMax and verifies that the original array is unchanged.
     */
    @Test
    public void testFindMax() {
        String[] numbers = {"2341", "2134", "2431", "2143"};
        String[] original = numbers.clone();

        String result = IntegerStringUtility.findMax(
                numbers,
                new IntegerStringUtility.StringNumericalValueComparator());

        assertEquals("2431", result);
        assertArrayEquals(original, numbers);
    }

    /*
     * Tests getSimilarityGroups using the example from the assignment.
     */
    @Test
    public void testGetSimilarityGroups() {
        String[] numbers =
                {"2341", "123", "2134", "2431", "312", "2143"};

        String[][] groups =
                IntegerStringUtility.getSimilarityGroups(numbers);

        assertEquals(2, groups.length);

        assertEquals(2, groups[0].length);
        assertEquals(4, groups[1].length);
    }

    /*
     * Tests that every element within a similarity group is similar.
     */
    @Test
    public void testGetSimilarityGroupsContents() {
        String[] numbers =
                {"2341", "123", "2134", "2431", "312", "2143"};

        String[][] groups =
                IntegerStringUtility.getSimilarityGroups(numbers);

        Comparator<String> comparator =
                new IntegerStringUtility.StringSimilarityComparator();

        for(String[] group : groups) {
            for(int i = 1; i < group.length; i++) {
                assertEquals(0,
                        comparator.compare(group[0], group[i]));
            }
        }
    }

    /*
     * Tests that getSimilarityGroups does not modify its input.
     */
    @Test
    public void testGetSimilarityGroupsDoesNotAlterInput() {
        String[] numbers =
                {"2341", "123", "2134", "2431", "312", "2143"};

        String[] original = numbers.clone();

        IntegerStringUtility.getSimilarityGroups(numbers);

        assertArrayEquals(original, numbers);
    }

    /*
     * Tests getSimilarityGroups with an empty array.
     */
    @Test
    public void testGetSimilarityGroupsEmpty() {
        String[][] groups =
                IntegerStringUtility.getSimilarityGroups(new String[0]);

        assertEquals(0, groups.length);
    }

    /*
     * Tests findMaximumSimilarityGroup using the assignment example.
     */
    @Test
    public void testFindMaximumSimilarityGroup() {
        int[] numbers = {123, 987, 321, 789, 132, 879};

        String[] result =
                IntegerStringUtility.findMaximumSimilarityGroup(numbers);

        assertEquals(3, result.length);

        assertTrue(contains(result, "987"));
        assertTrue(contains(result, "789"));
        assertTrue(contains(result, "879"));
    }

    /*
     * Tests findMaximumSimilarityGroup when two groups have the same size.
     * The group containing the larger maximum integer should be returned.
     */
    @Test
    public void testFindMaximumSimilarityGroupTie() {
        int[] numbers = {123, 321, 789, 987};

        String[] result =
                IntegerStringUtility.findMaximumSimilarityGroup(numbers);

        assertEquals(2, result.length);

        assertTrue(contains(result, "789"));
        assertTrue(contains(result, "987"));
    }

    /*
     * Tests findMaximumSimilarityGroup with an empty input.
     */
    @Test
    public void testFindMaximumSimilarityGroupEmpty() {
        String[] result =
                IntegerStringUtility.findMaximumSimilarityGroup(new int[0]);

        assertEquals(0, result.length);
    }

    /*
     * Helper method for checking whether an array contains a string.
     */
    private boolean contains(String[] array, String target) {
        for(String element : array) {
            if(element.equals(target))
                return true;
        }

        return false;
    }
}