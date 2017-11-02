import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floyd {
    private static Scanner input;
    private static boolean fileInput;

    public static void main (String[] args) {
        processArgs(args);

        MemoMatrix<Double> weights = getWeights();

        MemoMatrix<FloydCell> memo = initializeMemoFromWeights(weights);

        memo.printMatrix();
        System.out.println("D_0");

        doFloyds(weights, memo);
        System.out.println("");

        for (int i = 1; i <= weights.getNumCols(); i++) {
            for (int j = 1; j <= weights.getNumCols(); j++) {
                if (i != j) {
                    System.out.print(i + " -> " + j + " : ");
                    System.out.print(memo.recall(j - 1, i - 1).cost + " : ");
                    System.out.println(getShortestPathString(i, j, memo));
                }
            }
        }
    }

    private static void doFloyds (MemoMatrix<Double> weights,
            MemoMatrix<FloydCell> memo) {
        int numPoints = weights.getNumCols();
        for (int k = 1; k <= numPoints; k++) {
            int checkDim = k - 1;
            for (int row = 0; row < numPoints; row++) {
                double rowWeight = memo.recall(checkDim, row).cost;
                if (row == checkDim || rowWeight == Double.POSITIVE_INFINITY) {
                    continue;
                }
                for (int col = 0; col < numPoints; col++) {
                    double colWeight = memo.recall(col, checkDim).cost;
                    if (col == checkDim || colWeight == Double.POSITIVE_INFINITY) {
                        continue;
                    }

                    FloydCell current = memo.recall(col, row);
                    if (colWeight + rowWeight < current.cost) {
                        current.cost = colWeight + rowWeight;
                        current.next = k;
                    }
                }
            }
            memo.printMatrix();
            System.out.println("D_" + k);
        }
    }

    private static String getShortestPathString (int start, int end,
            MemoMatrix<FloydCell> memo) {
        int next = memo.recall(end - 1, start - 1).next;
        if (next == 0) {
            return start + " -> " + end;
        }

        int next1 = memo.recall(next - 1, start - 1).next;
        int next2 = memo.recall(end - 1, next - 1).next;
        if (next1 == 0) {
            return start + " -> " + getShortestPathString(next, end, memo);
        } else if (next2 == 0) {
            return getShortestPathString(start, next, memo) + " -> " + end;
        } else {
            return getShortestPathString(start, next1, memo) + " -> " + next +
                    " -> " + getShortestPathString(next2, end, memo);
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
        if (!fileInput) {
            System.out.print("How many points are there? > ");
        }
        int numPoints = input.nextInt();

        MemoMatrix<Double> weights = new MemoMatrix<>(numPoints, numPoints);
        for (int row = 0; row < numPoints; row++) {
            for (int col = 0; col < numPoints; col++) {
                if (!fileInput) {
                    System.out.printf("Enter edge weight from %d to %d > ",
                            row + 1, col + 1);
                }
                double weight = input.nextDouble();
                if (weight < 0) {
                    weight = Double.POSITIVE_INFINITY;
                }
                weights.memoize(col, row, weight);
            }
        }
        return weights;
    }

    private static void processArgs (String[] args) {
        if (args.length > 0) {
            try {
                File inputFile = new File(args[0]);
                FileInputStream fis = new FileInputStream(inputFile);
                input = new Scanner(fis);
                fileInput = true;
            } catch (FileNotFoundException e) {
                System.out.println("Error: File " + args[0] + "not found.");
                System.out.println("Using standard input instead.");
                input = new Scanner(System.in);
                fileInput = false;
            }
        } else {
            input = new Scanner(System.in);
            fileInput = false;
        }
    }
}
