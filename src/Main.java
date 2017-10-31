import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static Scanner input;
    private static boolean fileInput;

    public static void main (String[] args) {
        processArgs(args);

        MemoMatrix<Double> weights = getWeights();

        MemoMatrix<FloydCell> memo = initializeMemoFromWeights(weights);

        memo.printMatrix();
        System.out.println("D_0");

        doFloyds(weights, memo);
    }

    private static void doFloyds (MemoMatrix<Double> weights,
            MemoMatrix<FloydCell> memo) {
        int numPoints = weights.getNumCols();
        for (int i = 1; i <= numPoints; i++) {
            int checkDim = i - 1;
            for (int col = 0; col < numPoints; col++) {
                double colWeight = weights.recall(col, checkDim);
                if (colWeight == Double.POSITIVE_INFINITY) {
                    continue;
                }
                for (int row = 0; row < numPoints; row++) {
                    double rowWeight = weights.recall(checkDim, row);
                    if (rowWeight == Double.POSITIVE_INFINITY) {
                        continue;
                    }

                    FloydCell current = memo.recall(col, row);
                    if (colWeight + rowWeight < current.cost) {
                        current.cost = colWeight + rowWeight;
                        current.intermediateVertex = i;
                    }
                }
            }
            memo.printMatrix();
            System.out.println("D_" + i);
        }
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
}
