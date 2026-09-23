package assign04;

import java.util.Comparator;

public class InsertionSortBestCaseTimingExperiment extends TimingExperiment {

    private Integer[] array;

    public InsertionSortBestCaseTimingExperiment() {
        super("array size", 1000, 20, 1000, 100);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        array = ArrayGenerator.generateNearlyAscendingArray(problemSize);
    }

    @Override
    protected void runComputation() {
        IntegerStringUtility.insertionSort(array, Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        InsertionSortBestCaseTimingExperiment experiment =
                new InsertionSortBestCaseTimingExperiment();

        experiment.printResults();
    }
}