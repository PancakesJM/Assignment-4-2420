package assign04;

import java.util.Comparator;
import timing.TimingExperiment;
import timing.ArrayGenerator;

public class InsertionSortWorstCaseTimingExperiment extends TimingExperiment {

    protected Integer[] array;

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