package com.example.digitrecognitionv3.model;

import java.io.Serializable;
import java.util.Random;

public class NeuralNetwork implements Serializable {

    private Matrix  w1;
    private Matrix  b1;
    private Matrix  w2;
    private Matrix  b2;
    private double learningRate;

    public NeuralNetwork(int numImages) {
        this.w1 = new Matrix(new double[10][784]);
        this.b1 = new Matrix(new double[10][numImages]);
        this.w2 = new Matrix(new double[10][10]);
        this.b2 =  new Matrix(new double[10][numImages]);
        this.learningRate = 0.6;

        initParams(numImages);
    }

    public NeuralNetwork() {

    }

    // Função responsável por inicializar os parâmetros de rede.
    public void initParams(int numImages) {

        Random random = new Random();

        // Inicialização de w1.
        for (int i = 0; i < this.w1.matrix.length; i++) {
            for (int j = 0; j < 784; j++) {
                this.w1.matrix[i][j] = random.nextGaussian();
            }
        }
        // Inicialização de b1.
        for (int i = 0; i < numImages; i++) {
            for (int j = 0; j < this.b1.matrix.length; j++) {
                if (i == 0) {
                    this.b1.matrix[j][i] = random.nextGaussian();
                }
                else
                    this.b1.matrix[j][i] = this.b1.matrix[j][i-1];
            }
        }
        // Inicialização de w2.
        for (int i = 0; i < this.w2.matrix.length; i++) {
            for (int j = 0; j < 10; j++) {
                this.w2.matrix[i][j] = random.nextGaussian();
            }
        }
        // Inicialização de b2.
        for (int i = 0; i < numImages; i++) {
            for (int j = 0; j < this.b2.matrix.length; j++) {
                if (i == 0) {
                    this.b2.matrix[j][i] = random.nextGaussian();
                }
                else
                    this.b2.matrix[j][i] = this.b2.matrix[j][i-1];
            }
        }
    }

    // Função responsável por treinar a rede neural.
    public void gradientDescendent(Matrix x, Matrix labels, int iterations) {

        for (int i = 0; i < iterations; i++) {
            double[][][] forwardProp = forwardProp(x);
            backwardProp(new Matrix(forwardProp[0]), new Matrix(forwardProp[1]),
                    new Matrix(forwardProp[2]), new Matrix(forwardProp[3]), labels, x);
            if (i % 10 == 0) {
                System.out.println("Iteration: " + i);
                Matrix a2Decode = oneHotLabelDecode(new Matrix(forwardProp[3]));
                System.out.println("Accuracy: " + getAccuracy(a2Decode,labels) + "\n");
                if (getAccuracy(a2Decode, labels) > 0.99)
                    break;
            }
        }
    }

    // Função que calcula os erros associados aos diferentes pesos.
    public void backwardProp(Matrix z1, Matrix a1, Matrix z2, Matrix a2, Matrix labels, Matrix x) {

        Matrix oneHotLabels = oneHotLabelsEncode(labels);

        Matrix dZ2 = a2.subtractTwoMatrix(oneHotLabels);
        Matrix dW2 = dZ2.multiplyTwoMatrixType1(a1.transpose()).
                multiplyNumByMatrix(1.0/x.numCols);
        Matrix dB2 = dZ2.sumLineByLine().multiplyNumByMatrix(1.0/x.numCols);
        Matrix dZ1 = this.w2.transpose().multiplyTwoMatrixType1(dZ2).
                multiplyTwoMatrixType2(reLuDeriv(z1));
        Matrix dW1 = dZ1.multiplyTwoMatrixType1(x.transpose()).
                multiplyNumByMatrix(1.0/x.numCols);
        Matrix dB1 = dZ1.sumLineByLine().multiplyNumByMatrix(1.0/x.numCols);

        updateParams(dW1, dB1, dW2, dB2);
    }

    // Função que fornece o conjunto de saídas da rede, em resposta a um certo conjunto de entradas.
    public double[][][] forwardProp(Matrix entries) {

        double[][][] out = new double[4][10][entries.numCols];

        Matrix z1 = this.w1.multiplyTwoMatrixType1(entries).
                sumTwoMatrix(this.b1.
                        shapeMatrix(0, this.b1.numRows - 1,
                                0, entries.numCols - 1));

        Matrix a1 =reLu(z1);

        Matrix z2 = this.w2.multiplyTwoMatrixType1(a1).
                sumTwoMatrix(this.b2.
                        shapeMatrix(0, this.b2.numRows - 1,
                                0, entries.numCols - 1));

        Matrix a2 = softMax(z2);

        out[0] = z1.matrix;
        out[1] = a1.matrix;
        out[2] = z2.matrix;
        out[3] = a2.matrix;

        return out;
    }

    // Função que atualiza os parâmetros de rede.
    public void updateParams(Matrix dW1, Matrix dB1, Matrix dW2, Matrix dB2) {

        this.w1 = dW1.multiplyNumByMatrix(-this.learningRate).sumTwoMatrix(this.w1);
        this.b1 = dB1.multiplyNumByMatrix(-this.learningRate).sumTwoMatrix(this.b1);
        this.w2 = dW2.multiplyNumByMatrix(-this.learningRate).sumTwoMatrix(this.w2);
        this.b2 = dB2.multiplyNumByMatrix(-this.learningRate).sumTwoMatrix(this.b2);
    }

    // Codificador OneHot.
    public Matrix oneHotLabelsEncode(Matrix labels) {

        double[][] oneHotLabels = new double[10][labels.numCols];
        Matrix oneHotLabelsMatrix = new Matrix(oneHotLabels);

        for (int i = 0; i < labels.numCols; i++) {
            double label = labels.matrix[0][i];
            switch (String.valueOf(label)) {
                case "0.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 0)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "1.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 1)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "2.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 2)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "3.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 3)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "4.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 4)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "5.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 5)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "6.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 6)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "7.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 7)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "8.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 8)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }
                    break;
                case "9.0" :
                    for (int j = 0; j < 10; j++) {
                        if (j == 9)
                            oneHotLabelsMatrix.matrix[j][i] = 1.0;
                        else
                            oneHotLabelsMatrix.matrix[j][i] = 0.0;
                    }


            }
        }

        return oneHotLabelsMatrix;
    }

    // Descodificador OneHot
    public Matrix oneHotLabelDecode(Matrix oneHotLabel) {

        Matrix oneHotLabelDecode = new Matrix(new double[1][oneHotLabel.numCols]);
        int k = 0;

        for (int i = 0; i < oneHotLabel.numCols; i++) {
            int indice = 0;
            for (int j = 1; j < oneHotLabel.numRows; j++) {
                if (oneHotLabel.matrix[j][i] > oneHotLabel.matrix[indice][i])
                    indice = j;
            }
            switch (String.valueOf(indice)) {
                case "0" :
                    oneHotLabelDecode.matrix[0][k] = 0.0;
                    break;
                case "1" :
                    oneHotLabelDecode.matrix[0][k] = 1.0;
                    break;
                case "2" :
                    oneHotLabelDecode.matrix[0][k] = 2.0;
                    break;
                case "3" :
                    oneHotLabelDecode.matrix[0][k] = 3.0;
                    break;
                case "4" :
                    oneHotLabelDecode.matrix[0][k] = 4.0;
                    break;
                case "5" :
                    oneHotLabelDecode.matrix[0][k] = 5.0;
                    break;
                case "6" :
                    oneHotLabelDecode.matrix[0][k] = 6.0;
                    break;
                case "7" :
                    oneHotLabelDecode.matrix[0][k] = 7.0;
                    break;
                case "8" :
                    oneHotLabelDecode.matrix[0][k] = 8.0;
                    break;
                case "9" :
                    oneHotLabelDecode.matrix[0][k] = 9.0;
            }
            k++;

        }

        return oneHotLabelDecode;
    }

    // Função de ativação "ReLu".
    public Matrix reLu(Matrix z1) {

        double[][] a1 = new double[z1.numRows][z1.numCols];

        for (int i = 0; i < z1.numCols; i++) {
            for (int j = 0; j < z1.numRows; j++) {
                if (z1.matrix[j][i] > 0)
                    a1[j][i] = z1.matrix[j][i];
                else
                    a1[j][i] = 0;
            }
        }

        Matrix out = new Matrix(a1);

        return out;
    }

    // Derivada da função de ativação "ReLu".
    public Matrix reLuDeriv(Matrix z1) {

        double[][] derivZ1 = new double[z1.numRows][z1.numCols];

        for (int i = 0; i < z1.numCols; i++) {
            for (int j = 0; j < z1.numRows; j++) {
                if (z1.matrix[j][i] > 0)
                    derivZ1[j][i] = 1;
                else
                    derivZ1[j][i] = 0;
            }
        }

        Matrix out = new Matrix(derivZ1);

        return out;
    }

    // Função de ativação "softmax".
    public Matrix softMax(Matrix z2) {

        double[][] a2 = new double[z2.numRows][z2.numCols];

        for (int i = 0; i < z2.numCols; i++) {
            double sum = 0;
            for (int j = 0; j < z2.numRows; j++) {
                sum = sum + Math.exp(z2.matrix[j][i]);
                if (j == z2.numRows - 1) {
                    for (int k = 0; k < z2.numRows; k++) {
                        a2[k][i] = Math.exp(z2.matrix[k][i])/sum;
                    }
                }
            }
        }

        Matrix out = new Matrix(a2);

        return out;
    }

    // Função que retorna o acerto da rede para um certo conjunto de entradas.
    public double getAccuracy(Matrix a2Decoded, Matrix labels) {

        double cont = 0;
        double accuracy;

        for (int i = 0; i < labels.numCols; i++) {
            if (a2Decoded.matrix[0][i] == labels.matrix[0][i])
                cont++;
        }

        accuracy = cont / (double) labels.numCols;

        return accuracy;
    }

    public Matrix getW1() {
        return w1;
    }

    public void setW1(Matrix w1) {
        this.w1 = w1;
    }

    public Matrix getB1() {
        return b1;
    }

    public void setB1(Matrix b1) {
        this.b1 = b1;
    }

    public Matrix getW2() {
        return w2;
    }

    public void setW2(Matrix w2) {
        this.w2 = w2;
    }

    public Matrix getB2() {
        return b2;
    }

    public void setB2(Matrix b2) {
        this.b2 = b2;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
}
