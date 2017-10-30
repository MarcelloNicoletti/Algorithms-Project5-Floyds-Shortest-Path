import java.util.Scanner;

public class Main {
    private static Scanner stdIn = new Scanner(System.in);

    public static void main (String[] args) {
        MemoMatrix<Double> weights = getWeights();
        int points = weights.getNumCols();

        MemoMatrix<FloydCell> memo = new MemoMatrix<>(points);
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
