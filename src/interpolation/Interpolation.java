// https://github.com/matanof2/Numeric_Analysis.git
package interpolation;
import java.util.Scanner;

public class Interpolation {

    public static double[] getInput(int choice) {
        Scanner scanner = new Scanner(System.in);
        double[] points = new double[(choice == 1) ? 4 : 6];
        for (int i = 0; i < points.length; i += 2) {
            System.out.print("Enter X value: ");
            points[i] = scanner.nextDouble();
            System.out.print("Enter Y value: ");
            points[i + 1] = scanner.nextDouble();
        }
        return points;
    }

    public static double linearInterpolation(double[] points, double x) {
        double x0 = points[0], y0 = points[1];
        double x1 = points[2], y1 = points[3];

        if (x1 - x0 == 0) {
            throw new IllegalArgumentException("The two points have the same x value, resulting in a vertical line, which cannot be represented as a linear equation in the form y = mx + b.");
        }

        double m = (y1 - y0) / (x1 - x0);
        double b = y0 - m * x0;
        return m * x + b;
    }

    public static double polynomialInterpolation(double[] points, double x) {
        double x0 = points[0], y0 = points[1];
        double x1 = points[2], y1 = points[3];
        double x2 = points[4], y2 = points[5];

        double[][] A = {
                {x0 * x0, x0, 1},
                {x1 * x1, x1, 1},
                {x2 * x2, x2, 1}
        };
        double[] B = {y0, y1, y2};

        double[] coeffs = solveSystem(A, B);
        double a = coeffs[0], b = coeffs[1], c = coeffs[2];
        return a * x * x + b * x + c;
    }

    public static double lagrangeInterpolation(double[] points, double x) {
        double x0 = points[0], y0 = points[1];
        double x1 = points[2], y1 = points[3];
        double x2 = points[4], y2 = points[5];

        double L0 = ((x - x1) * (x - x2)) / ((x0 - x1) * (x0 - x2));
        double L1 = ((x - x0) * (x - x2)) / ((x1 - x0) * (x1 - x2));
        double L2 = ((x - x0) * (x - x1)) / ((x2 - x0) * (x2 - x1));

        return y0 * L0 + y1 * L1 + y2 * L2;
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose the following interpolation functions");
            System.out.println("1. Linear interpolation");
            System.out.println("2. Polynomial interpolation");
            System.out.println("3. Lagrange interpolation");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            if (choice == 4) {
                System.out.println("Exiting...");
                break;
            }
            double[] points = getInput(choice);
            System.out.print("Enter the x value for which you want to find the interpolated y value: ");
            double x = scanner.nextDouble();
            double y = 0;
            switch (choice) {
                case 1:
                    y = linearInterpolation(points, x);
                    break;
                case 2:
                    y = polynomialInterpolation(points, x);
                    break;
                case 3:
                    y = lagrangeInterpolation(points, x);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }
            System.out.println("The interpolated value at x = " + x + " is y = " + y);
        }
        scanner.close();
    }

    public static double[] solveSystem(double[][] A, double[] B) {
        int n = B.length;
        double[] x = new double[n];
        System.arraycopy(B, 0, x, 0, n);
        for (int i = 0; i < n; i++) {
            double max = Math.abs(A[i][i]);
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > max) {
                    max = Math.abs(A[k][i]);
                    maxRow = k;
                }
            }
            double[] temp = A[i];
            A[i] = A[maxRow];
            A[maxRow] = temp;

            double t = x[i];
            x[i] = x[maxRow];
            x[maxRow] = t;

            for (int k = i + 1; k < n; k++) {
                double factor = A[k][i] / A[i][i];
                x[k] -= factor * x[i];
                for (int j = i; j < n; j++) {
                    A[k][j] -= factor * A[i][j];
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            x[i] /= A[i][i];
            for (int k = i - 1; k >= 0; k--) {
                x[k] -= A[k][i] * x[i];
            }
        }
        return x;
    }

    public static void main(String[] args) {
        menu();
    }
}