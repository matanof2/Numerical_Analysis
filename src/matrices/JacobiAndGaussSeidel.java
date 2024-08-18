// https://github.com/matanof2/Numeric_Analysis.git
package matrices;
import java.util.Arrays;

public class JacobiAndGaussSeidel {
    private static final double PRECISION = 0.001;

    public static void main(String[] args) {
        double[][] matrix = {
                {4, 2, 0},
                {2, 10, 4},
                {0, 4, 5}
        };
        double[] b = {2, 6, 5};

        double[] jacobiResult = jacobi(matrix, b);
        if (jacobiResult != null) {
            System.out.println("Jacobi [x, y, z]: " + Arrays.toString(jacobiResult));
        }

        double[] gaussSeidelResult = gaussSeidel(matrix, b);
        if (gaussSeidelResult != null) {
            System.out.println("Gauss Seidel [x, y, z]: " + Arrays.toString(gaussSeidelResult));
        }
    }

    public static double[] jacobi(double[][] matrix, double[] b) {
        if (!isDominant(matrix)) {
            System.out.println("The matrix does not have a dominant diagonal!");
            return null;
        }

        double[] x = new double[3];
        double[] prevX = new double[3];
        int i = 0;

        while (true) {
            for (int j = 0; j < 3; j++) {
                prevX[j] = x[j];
            }

            x[0] = (b[0] - matrix[0][1] * prevX[1] - matrix[0][2] * prevX[2]) / matrix[0][0];
            x[1] = (b[1] - matrix[1][0] * prevX[0] - matrix[1][2] * prevX[2]) / matrix[1][1];
            x[2] = (b[2] - matrix[2][0] * prevX[0] - matrix[2][1] * prevX[1]) / matrix[2][2];

            i++;

            if (Math.abs(x[0] - prevX[0]) < PRECISION &&
                    Math.abs(x[1] - prevX[1]) < PRECISION &&
                    Math.abs(x[2] - prevX[2]) < PRECISION) {
                break;
            }
        }

        return x;
    }

    public static double[] gaussSeidel(double[][] matrix, double[] b) {
        if (!isDominant(matrix)) {
            System.out.println("The matrix does not have a dominant diagonal!");
            return null;
        }

        double[] x = new double[3];
        double[] prevX = new double[3];
        int i = 0;

        while (true) {
            for (int j = 0; j < 3; j++) {
                prevX[j] = x[j];
            }

            x[0] = (b[0] - matrix[0][1] * x[1] - matrix[0][2] * x[2]) / matrix[0][0];
            x[1] = (b[1] - matrix[1][0] * x[0] - matrix[1][2] * x[2]) / matrix[1][1];
            x[2] = (b[2] - matrix[2][0] * x[0] - matrix[2][1] * x[1]) / matrix[2][2];

            i++;

            if (Math.abs(x[0] - prevX[0]) < PRECISION &&
                    Math.abs(x[1] - prevX[1]) < PRECISION &&
                    Math.abs(x[2] - prevX[2]) < PRECISION) {
                break;
            }
        }

        return x;
    }

    private static boolean isDominant(double[][] matrix) {
        for (int i = 0; i < 3; i++) {
            double sum = 0;
            for (int j = 0; j < 3; j++) {
                if (i != j) {
                    sum += Math.abs(matrix[i][j]);
                }
            }
            if (Math.abs(matrix[i][i]) <= sum) {
                return false;
            }
        }
        return true;
    }
}