package assign04;

import java.util.Comparator;

public class InsertionSortAverageCaseTimingExperiment extends TimingExperiment {

    private Integer[] array;

    public InsertionSortAverageCaseTimingExperiment() {
        super("array size", 1000, 20, 1000, 100);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        array = ArrayGenerator.generatePermutedArray(problemSize);
    }

    @Override
    protected void runComputation() {
        IntegerStringUtility.insertionSort(array, Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        InsertionSortAverageCaseTimingExperiment experiment =
                new InsertionSortAverageCaseTimingExperiment();

        experiment.printResults();
    }
}