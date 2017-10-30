import java.util.Scanner;

public class Main {
    private static Scanner stdIn = new Scanner(System.in);

    public static void main (String[] args) {
        MemoMatrix<Double> weights = getWeights();
        int points = weights.getNumCols();

        MemoMatrix<FloydCell> memo = initializeMemoFromWeights(weights);

        doFloyds(weights, memo);
    }

    private static void doFloyds (MemoMatrix<Double> weights,
            MemoMatrix<FloydCell> memo) {
        int numPoints = weights.getNumCols();
        for (int i = 0; i < numPoints; i++) {
            int checkDim = i - 1;
            for (int col = 0; col < numPoints; col ++) {
                double colWeight = weights.recall(col, checkDim);
                for (int row = 0; row < numPoints; row++) {
                    if (i == 0) {
                        FloydCell fc = new FloydCell(weights.recall(col, row));
                        memo.memoize(col, row, fc);
                    } else {
                        double rowWeight = weights.recall(checkDim, row);
                        if (rowWeight == Double.POSITIVE_INFINITY ||
                                colWeight == Double.POSITIVE_INFINITY) {
                            continue;
                        }

                        FloydCell current = memo.recall(col, row);
                        if (colWeight + rowWeight > current.cost) {
                            current.cost = colWeight + rowWeight;
                            current.intermediateVertex = i;
                        }
                    }
                }
            }
        }
    }

    private static MemoMatrix<FloydCell> initializeMemoFromWeights (
            MemoMatrix<Double> weights) {
        int numPoints = weights.getNumCols();
        MemoMatrix<FloydCell> memo = new MemoMatrix<>(numPoints);

        for (int col = 0; col < numPoints; col++) {
            for (int row = 0; row < numPoints; row++) {
                FloydCell fc = new FloydCell(weights.recall(col, row));
                memo.memoize(col, row, fc);
            }
        }

        return memo;
    }

    private static MemoMatrix<Double> getWeights () {
        System.out.print("How many points are there? > ");
        int numPoints = stdIn.nextInt();

        MemoMatrix<Double> weights = new MemoMatrix<>(numPoints, numPoints);
        for (int row = 0; row < numPoints; row++) {
            for (int col = 0; col < numPoints; col++) {
                System.out.printf("Enter edge weight from %d to %d > ", row + 1,
                        col + 1);
                double weight = stdIn.nextDouble();
                if (weight < 0) {
                    weight = Double.POSITIVE_INFINITY;
                }
                weights.memoize(col, row, weight);
            }
        }
        return weights;
    }
}
