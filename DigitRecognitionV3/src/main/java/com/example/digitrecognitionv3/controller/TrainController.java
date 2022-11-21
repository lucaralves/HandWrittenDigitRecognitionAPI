package com.example.digitrecognitionv3.controller;

import com.example.digitrecognitionv3.data.Data;
import com.example.digitrecognitionv3.model.Matrix;
import com.example.digitrecognitionv3.model.NeuralNetwork;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class TrainController {

    @GetMapping("/trainNeuralNetwork")
    public void trainNeuralNetwork() {

        NeuralNetwork neuralNetwork = Data.readState();

        // Importação do dataset de treino.
        // Depois de importado o dataset é dividido em labels e pixeis.
        // É calculada a transposta das matrizes de labels e pixeis.
        ArrayList<ArrayList<Double>> trainDataSet = Data.importTrainDataSet();

        Matrix trainDataSetMatrix = new Matrix(trainDataSet);
        Matrix trainLabelsMatrix = trainDataSetMatrix.shapeMatrix(0,
                trainDataSetMatrix.numRows - 1, 0, 0);
        Matrix trainPixelsMatrix = trainDataSetMatrix.shapeMatrix(0,
                trainDataSetMatrix.numRows - 1, 1,
                trainDataSetMatrix.numCols - 1);
        Matrix trainLabelsMatrixT = trainLabelsMatrix.transpose();
        Matrix trainPixelsMatrixT = trainPixelsMatrix.transpose().
                multiplyNumByMatrix(1.0/255.0);

        // Criação da rede neural e inicialização dos pesos.
        neuralNetwork.gradientDescendent(trainPixelsMatrixT,
                trainLabelsMatrixT, 5000);

        // Guarda-se a rede neural treinada num ficheiro binário.
        Data.writeState(neuralNetwork);
    }

    @GetMapping("/operateNeuralNetwork")
    public void operateNeuralNetwork() {

        NeuralNetwork neuralNetwork = Data.readState();

        // Importação do dataset de operação.
        // Depois de importado o dataset é dividido em labels e pixeis.
        // É calculada a transposta das matrizes de labels e pixeis.
        ArrayList<ArrayList<Double>> trainDataSet = Data.importTestDataSet();
        Matrix testDataSetMatrix = new Matrix(trainDataSet);
        Matrix testLabelsMatrix = testDataSetMatrix.shapeMatrix(0,
                testDataSetMatrix.numRows - 1, 0, 0);
        Matrix testPixelsMatrix = testDataSetMatrix.shapeMatrix(0,
                testDataSetMatrix.numRows - 1, 1,
                testDataSetMatrix.numCols - 1);
        Matrix testLabelsMatrixT = testLabelsMatrix.transpose();
        Matrix testPixelsMatrixT = testPixelsMatrix.transpose().
                multiplyNumByMatrix(1.0/255.0);

        double[][][] forwardPropOut = neuralNetwork.forwardProp(testPixelsMatrixT);
        Matrix a2Decode = neuralNetwork.
                oneHotLabelDecode(new Matrix(forwardPropOut[3]));
        System.out.println("\nAccuracy: " + neuralNetwork.getAccuracy(a2Decode,
                testLabelsMatrixT) + "\n");
    }

    @GetMapping("/createWeightTables")
    public void createWeightTables() {

        Data.createDb();
    }

    @GetMapping("/fillWeightTables")
    public void fillWeightTables() {

        NeuralNetwork neuralNetwork = Data.readState();
        Data.writeStateToDb(neuralNetwork);
    }

    @GetMapping("/readStateFromDb")
    public void readStateFromDb() {

        NeuralNetwork neuralNetwork = new NeuralNetwork(60000);
        Data.readStateFromDb(neuralNetwork);
    }

}
