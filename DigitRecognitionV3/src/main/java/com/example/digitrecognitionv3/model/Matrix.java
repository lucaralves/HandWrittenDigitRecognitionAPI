package com.example.digitrecognitionv3.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Matrix implements Serializable {

    public double[][] matrix;
    public int numRows;
    public int numCols;

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.numRows = getNumberOfRowsAndColumns()[0];
        this.numCols = getNumberOfRowsAndColumns()[1];
    }

    public Matrix(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = arrayListToArray2D(matrix);
        this.numRows = getNumberOfRowsAndColumns()[0];
        this.numCols = getNumberOfRowsAndColumns()[1];
    }

    public Matrix() {
    }

    // Função responsável por obter o número de linhas e colunas de uma matriz, em forma de Array.
    public int[] getNumberOfRowsAndColumns() {

        int[] output = new int[2];

        for (int i = 0; i < this.matrix.length; i++) {
            if (i == 0) {
                double[] row = this.matrix[i];
                output[1] = row.length;
            }
            output[0] = output[0] + 1;
        }

        return output;
    }

    // Função responsável por obter o número de linhas e colunas de uma matriz,
    // em forma de ArrayList<Double>.
    public static int[] getNumberOfRowsAndColumns1(ArrayList<ArrayList<Double>> matrix) {

        int[] output = new int[2];

        for (int i = 0; i < matrix.size(); i++) {
            if (i == 0) {
                ArrayList<Double> row = matrix.get(i);
                output[1] = row.size();
            }
            output[0] = output[0] + 1;
        }

        return output;
    }

    // Função que converte um ArrayList<ArrayList<Double>> num array simples.
    public double[][] arrayListToArray2D(ArrayList<ArrayList<Double>> arrayList) {

        int[] numOfRowAndCol = getNumberOfRowsAndColumns1(arrayList);
        double[][] array = new double[numOfRowAndCol[0]][numOfRowAndCol[1]];

        for (int i = 0; i < numOfRowAndCol[0]; i++) {
            for (int j = 0; j < numOfRowAndCol[1]; j++) {
                array[i][j] = arrayList.get(i).get(j);
            }
        }

        return array;
    }

    // Função responsável por multiplicar duas matrizes do tipo array simples.
    public Matrix multiplyTwoMatrixType1(Matrix matrixB) {

        double[][] result = new double[this.numRows][matrixB.numCols];

        if (this.numCols == matrixB.numRows) {
            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < matrixB.numCols; j++) {
                    double element = 0;
                    for (int k = 0; k < this.numCols; k++) {
                        element = element + this.matrix[i][k] * matrixB.matrix[k][j];
                    }
                    result[i][j] = element;
                }
            }
        }
        else
            return null;

        Matrix out = new Matrix(result);

        return out;
    }

    // Função responsável por multiplicar duas matrizes do tipo array simples.
    public Matrix multiplyTwoMatrixType2(Matrix matrixB) {

        double[][] result = new double[this.numRows][this.numCols];

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                result[i][j] = this.matrix[i][j] * matrixB.matrix[i][j];
            }
        }

        Matrix out = new Matrix(result);

        return out;
    }

    // Função que multiplica um número por uma matriz.
    public Matrix multiplyNumByMatrix(double number) {

        double[][] result = new double[this.numRows][this.numCols];

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                result[i][j] = number * this.matrix[i][j];
            }
        }

        Matrix out = new Matrix(result);

        return out;
    }

    // Função responsável por somar duas matrizes do tipo array simples.
    public Matrix sumTwoMatrix(Matrix matrixB) {

        double[][] result = new double[this.numRows][this.numCols];

        if ((this.numRows == matrixB.numRows) &&
                (this.numCols == matrixB.numCols)) {
            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    result[i][j] = this.matrix[i][j] + matrixB.matrix[i][j];
                }
            }
        }
        else
            return null;

        Matrix out = new Matrix(result);

        return out;
    }

    // Função responsável por subtrair duas matrizes do tipo array simples.
    public Matrix subtractTwoMatrix(Matrix matrixB) {

        double[][] result = new double[this.numRows][this.numCols];

        if ((this.numRows == matrixB.numRows) &&
                (this.numCols == matrixB.numCols)) {
            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    result[i][j] = this.matrix[i][j] - matrixB.matrix[i][j];
                }
            }
        }
        else
            return null;

        Matrix out = new Matrix(result);

        return out;
    }

    // Função responsável por transpor uma matriz do tipo array simples.
    public Matrix transpose() {

        double[][] transpose = new double[this.numCols][this.numRows];

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                transpose[j][i] = this.matrix[i][j];
            }
        }

        Matrix out = new Matrix(transpose);

        return out;
    }

    // Função responsável por retornar uma fração da matriz passada por parâmetro.
    public Matrix shapeMatrix(int startRow, int endRow, int starCol, int endCol) {

        double[][] result = new double[endRow-startRow+1][endCol-starCol+1];
        int k = 0;
        int w = 0;

        for (int i = startRow; i <= endRow; i++) {
            for (int j = starCol; j <= endCol; j++) {
                result[k][w] = this.matrix[i][j];
                w++;
            }
            w = 0;
            k++;
        }

        Matrix out = new Matrix(result);

        return out;
    }

    // Retorna um array simples com a soma de todas as linhas de uma matriz passada por
    // parâmetro.
    public Matrix sumLineByLine() {

        double[][] matrix = new double[this.numRows][this.numCols];

        for (int i = 0; i < this.numRows; i++) {
            double element = 0;
            for (int j = 0; j < this.numCols; j++) {
                element = element + this.matrix[i][j];
            }
            matrix[i][0] = element;
        }

        for (int i = 1; i < this.numCols; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = matrix[j][i-1];
            }
        }

        Matrix out = new Matrix(matrix);

        return out;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }
}