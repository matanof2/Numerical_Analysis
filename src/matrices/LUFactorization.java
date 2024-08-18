// https://github.com/matanof2/Numeric_Analysis.git
package matrices;
import java.util.Scanner;

/**
 * This class implements LU matrix factorization and related operations.
 * It allows the user to input a matrix, perform LU decomposition, solve linear systems,
 * compute the inverse of a matrix, and display results interactively through a menu.
 */
public class LUFactorization {

    /**
     * Main method that provides an interactive menu for performing LU matrix operations.
     * Allows the user to enter a matrix, perform LU decomposition, solve linear systems,
     * compute matrix inverses, and display results.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\nMenu for LU matrix factorization:");
            System.out.println("1] Enter a matrix");
            System.out.println("2] close");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter a matrix:");
                    double[][] matrix = matrixOperations.InitializeMatrix(); // Initialize the matrix using a helper method
                    System.out.println("Matrix entered successfully.");
                    System.out.println("Enter a vector:");
                    double[] b = matrixOperations.InitializeVector();// Initialize the vector using a helper method
                    System.out.println("Vector entered successfully.");

                    double[][][] LU = luDecomposition(matrix); // Perform LU decomposition

                    System.out.println("L matrix:");
                    matrixOperations.printMatrix(LU[0]);

                    System.out.println("U matrix:");
                    matrixOperations.printMatrix(LU[1]);

                    double[] x = solveLU(LU[0], LU[1], b); // Solve the linear system using LU decomposition
                    System.out.println("Solution vector x:");
                    matrixOperations.printVector(x);

                    double[][] AInv = inverse(matrix);
                    System.out.println("Inverse of matrix A:");
                    matrixOperations.printMatrix(AInv);
                    break;
                case 2:
                    System.out.println("Goodbye =)");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    /**
     * Performs LU decomposition on a given matrix A.
     *
     * @param A The input matrix to decompose.
     * @return A double array containing two matrices: L (lower triangular matrix) and U (upper triangular matrix).
     */
    public static double[][][] luDecomposition(double[][] A) {
        int n = A.length;
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        for (int i = 0; i < n; i++) {
            L[i][i] = 1.0; // Initialize diagonal of L matrix to 1
        }

        for (int j = 0; j < n; j++) {
            for (int i = 0; i <= j; i++) {
                double sum = 0.0;
                for (int k = 0; k < i; k++) {
                    sum += U[k][j] * L[i][k]; // Compute sum for U matrix calculation
                }
                U[i][j] = A[i][j] - sum; // Calculate elements of U matrix
            }

            for (int i = j + 1; i < n; i++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += U[k][j] * L[i][k]; // Compute sum for L matrix calculation
                }
                L[i][j] = (A[i][j] - sum) / U[j][j]; // Calculate elements of L matrix
            }
        }

        return new double[][][]{L, U}; // Return both L and U matrices
    }

    /**
     * Solves a linear system using LU decomposition.
     *
     * @param L The lower triangular matrix L.
     * @param U The upper triangular matrix U.
     * @param b The vector b in the equation Ax = b.
     * @return The solution vector x.
     */
    public static double[] solveLU(double[][] L, double[][] U, double[] b) {
        int n = L.length;
        double[] y = new double[n];
        double[] x = new double[n];

        // Forward substitution to solve Ly = b
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j]; // Compute sum for forward substitution
            }
            y[i] = b[i] - sum; // Calculate elements of y vector
        }

        // Backward substitution to solve Ux = y
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j]; // Compute sum for backward substitution
            }
            x[i] = (y[i] - sum) / U[i][i]; // Calculate elements of x vector
        }

        return x;
    }

    /**
     * Computes the inverse of a matrix A using LU decomposition.
     *
     * @param A The matrix A to invert.
     * @return The inverse matrix of A.
     */
    public static double[][] inverse(double[][] A) {
        int n = A.length;
        double[][] AInv = new double[n][n];
        double[] b;

        for (int i = 0; i < n; i++) {
            b = new double[n];
            b[i] = 1.0;
            double[] col = solveLU(luDecomposition(A)[0], luDecomposition(A)[1], b);
            for (int j = 0; j < n; j++) {
                AInv[j][i] = col[j];
            }
        }

        return AInv;
    }
}