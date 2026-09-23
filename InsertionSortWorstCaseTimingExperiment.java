package assign04;

import java.util.Comparator;

public class InsertionSortWorstCaseTimingExperiment extends TimingExperiment {

    private Integer[] array;

    public InsertionSortWorstCaseTimingExperiment() {
        super("array size", 1000, 20, 1000, 100);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        array = ArrayGenerator.generateDescendingArray(problemSize);
    }

    @Override
    protected void runComputation() {
        IntegerStringUtility.insertionSort(array, Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        InsertionSortWorstCaseTimingExperiment experiment =
                new InsertionSortWorstCaseTimingExperiment();

        experiment.printResults();
    }
}