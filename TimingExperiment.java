package timing;

import java.util.Arrays;

/**
 * Abstract base class for running timing experiments.
 *
 * @author CS 2420 course staff
 * @version September 4, 2026
 */
public abstract class TimingExperiment {
	// These attributes control the problem size of and number of times to iterate
	// each experiment.
	protected String problemSizeDescription;
	protected int problemSizeMin;
	protected int problemSizeCount;
	protected int problemSizeStep;
	protected int experimentIterationCount;

	/**
	 * Constructs a general timing experiment.
	 * 
	 * @param problemSizeDescription   - description of the problem size for the experiment
	 * @param problemSizeMin           - minimum array size
	 * @param problemSizeCount         - number of array sizes to use in the experiment
	 * @param problemSizeStep          - step size between consecutive array sizes
	 * @param experimentIterationCount - number of times to run computation for a given array size
	 */
	public TimingExperiment(String problemSizeDescription, int problemSizeMin, int problemSizeCount,
			int problemSizeStep, int experimentIterationCount) {
		this.problemSizeDescription = problemSizeDescription;
		this.problemSizeMin = problemSizeMin;
		this.problemSizeCount = problemSizeCount;
		this.problemSizeStep = problemSizeStep;
		this.experimentIterationCount = experimentIterationCount;
	}

	/**
	 * Runs the timing experiment and prints the results.
	 */
	public void printResults() {
		System.out.println(problemSizeDescription + "\ttime (ns)");
		int size = problemSizeMin;
		for(int i = 0; i < problemSizeCount; i++) {
			long medianElapsedTime = computeMedianElapsedTime(size);
			System.out.println(size + "\t\t" + medianElapsedTime);
			size += problemSizeStep;
		}
	}

	/**
	 * Computes the median time elapsed to run the computation for a given problem
	 * size.
	 * 
	 * @param problemSize - the problem size for one experiment
	 * @return the median elapsed time of the experiment iterations
	 */
	protected long computeMedianElapsedTime(int problemSize) {
		long[] elapsedTimes = new long[experimentIterationCount];
		for(int i = 0; i < experimentIterationCount; i++) 
			elapsedTimes[i] = computeElapsedTime(problemSize);
		Arrays.sort(elapsedTimes);
		return elapsedTimes[experimentIterationCount / 2];
	}

	/**
	 * Computes the time elapsed to run the computation once for a given problem
	 * size.
	 * 
	 * @param problemSize - the problem size for one experiment
	 * @return the time elapsed
	 */
	protected long computeElapsedTime(int problemSize) {
		setupExperiment(problemSize);
		long startTime = System.nanoTime();
		runComputation();
		long endTime = System.nanoTime();
		return endTime - startTime;
	}

	/**
	 * Abstract method for setting up the infrastructure for the experiment for a
	 * given problem size.
	 * 
	 * @param problemSize - the problem size for one experiment
	 */
	protected abstract void setupExperiment(int problemSize);

	/**
	 * Abstract method to run the computation to be timed.
	 */
	protected abstract void runComputation();
}