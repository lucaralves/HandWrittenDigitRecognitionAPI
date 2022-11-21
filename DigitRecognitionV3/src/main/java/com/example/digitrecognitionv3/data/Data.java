package com.example.digitrecognitionv3.data;

import com.example.digitrecognitionv3.model.NeuralNetwork;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Data implements Serializable {

    // Função responsável pela importação do mnist dataset de treino.
    public static ArrayList<ArrayList<Double>> importTrainDataSet() {

        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();
        try {
            BufferedReader lerCsv = new BufferedReader(new FileReader("mnist_train.csv"));
            lerCsv.readLine();
            String linha;
            while ((linha = lerCsv.readLine()) != null) {
                ArrayList<Double> row = new ArrayList<>();
                String[] conteudo = linha.split(",");
                for (int i = 0; i < 785; i++) {
                    row.add(Double.parseDouble(conteudo[i]));
                }
                dataSet.add(row);
            }
            lerCsv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    // Função responsável pela importação do mnist dataset de operação.
    public static ArrayList<ArrayList<Double>> importTestDataSet() {

        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();
        try {
            BufferedReader lerCsv = new BufferedReader(new FileReader("mnist_test.csv"));
            lerCsv.readLine();
            String linha;
            while ((linha = lerCsv.readLine()) != null) {
                ArrayList<Double> row = new ArrayList<>();
                String[] conteudo = linha.split(",");
                for (int i = 0; i < 785; i++) {
                    row.add(Double.parseDouble(conteudo[i]));
                }
                dataSet.add(row);
            }
            lerCsv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public static void writeState(NeuralNetwork neuralNetwork) {

        try {
            FileOutputStream f = new FileOutputStream(new File("persistencia.bin"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write trained neural network to a file
            o.writeObject(neuralNetwork);
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public static NeuralNetwork readState() {

        NeuralNetwork neuralNetwork = null;

        try {
            FileInputStream fi = new FileInputStream("persistencia.bin");
            ObjectInputStream oi = new ObjectInputStream(fi);

            neuralNetwork = (NeuralNetwork) oi.readObject();

            oi.close();
            fi.close();

        } catch (IOException e) {
            System.out.println("File Not Found");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }

        return neuralNetwork;
    }

    public static void createW1table() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "create table w1 (";

            for (int i = 0; i < 784; i++) {
                if (i != 783)
                    query = query + "w" + i + " double, ";
                else
                    query = query + "w" + i + " double";
            }

            query = query + ")";

            stat.executeUpdate(query);
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void createW2table() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "create table w2 (";

            for (int i = 0; i < 10; i++) {
                if (i != 9)
                    query = query + "w" + i + " double, ";
                else
                    query = query + "w" + i + " double";
            }

            query = query + ")";

            stat.executeUpdate(query);
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void createB1table() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "create table b1 (b double)";

            stat.executeUpdate(query);
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void createB2table() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "create table b2 (b double)";

            stat.executeUpdate(query);
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeW1ToDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            for (int k = 0; k < 10; k++) {
                String sql = "insert into w1 (";

                for (int i = 0; i < 784; i++) {
                    if (i != 783)
                        sql = sql + "w" + i + ", ";
                    else
                        sql = sql + "w" + i + ") ";
                }
                sql = sql + "values (";
                for (int i = 0; i < 784; i++) {
                    if (i != 783)
                        sql = sql + "?, ";
                    else
                        sql = sql + "?)";
                }

                PreparedStatement stat = connection.prepareStatement(sql);

                for (int j = 0; j < 784; j++) {
                    stat.setDouble(j + 1, neuralNetwork.getW1().matrix[k][j]);
                }
                stat.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeW1toDb1(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "";

            for (int i = 0; i < 10; i++) {
                query = "insert into w1 (";
                for (int j = 0; j < 784; j++) {
                    if (j != 783)
                        query = query + "w" + j + ", ";
                    else
                        query = query + "w" + j + ") ";
                }
                query = query + "values(";
                for (int j = 0; j < 784; j++) {
                    if (j != 783)
                        query = query + neuralNetwork.getW1().matrix[i][j] + ", ";
                    else
                        query = query + neuralNetwork.getW1().matrix[i][j] + ")";
                }
                stat.executeUpdate(query);
            }

            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeW2ToDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            for (int k = 0; k < 10; k++) {
                String sql = "insert into w2 (";

                for (int i = 0; i < 10; i++) {
                    if (i != 9)
                        sql = sql + "w" + i + ", ";
                    else
                        sql = sql + "w" + i + ") ";
                }
                sql = sql + "values (";
                for (int i = 0; i < 10; i++) {
                    if (i != 9)
                        sql = sql + "?, ";
                    else
                        sql = sql + "?)";
                }

                PreparedStatement stat = connection.prepareStatement(sql);

                for (int j = 0; j < 10; j++) {
                    stat.setDouble(j + 1, neuralNetwork.getW2().matrix[k][j]);
                }
                stat.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeW2toDb1(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "";

            for (int i = 0; i < 10; i++) {
                query = "insert into w2 (";
                for (int j = 0; j < 10; j++) {
                    if (j != 9)
                        query = query + "w" + j + ", ";
                    else
                        query = query + "w" + j + ") ";
                }
                query = query + "values(";
                for (int j = 0; j < 10; j++) {
                    if (j != 9)
                        query = query + neuralNetwork.getW2().matrix[i][j] + ", ";
                    else
                        query = query + neuralNetwork.getW2().matrix[i][j] + ")";
                }
                stat.executeUpdate(query);
            }

            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeB1ToDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            for (int k = 0; k < 10; k++) {
                String sql = "insert into b1 (b) " +
                        "values (?)";

                PreparedStatement stat = connection.prepareStatement(sql);

                stat.setDouble(1, neuralNetwork.getB1().matrix[k][0]);

                stat.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeB1toDb1(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "";

            for (int i = 0; i < 10; i++) {
                query = "insert into b1 (b) values(" + neuralNetwork.getB1().matrix[i][0] +
                ")";
                stat.executeUpdate(query);
            }

            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeB2ToDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            for (int k = 0; k < 10; k++) {
                String sql = "insert into b2 (b) " +
                        "values (?)";

                PreparedStatement stat = connection.prepareStatement(sql);

                stat.setDouble(1, neuralNetwork.getB2().matrix[k][0]);

                stat.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void writeB2toDb1(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "";

            for (int i = 0; i < 10; i++) {
                query = "insert into b2 (b) values(" + neuralNetwork.getB2().matrix[i][0] +
                        ")";
                stat.executeUpdate(query);
            }

            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void readW1fromDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "select * from w1";

            ResultSet rs = stat.executeQuery(query);

            for (int i = 0; i < 10; i++) {
                if (rs.next()) {
                    for (int j = 0; j < 784; j++) {
                        neuralNetwork.getW1().matrix[i][j] = rs.getDouble(j + 1);
                    }
                }
            }
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void readW2fromDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "select * from w2";

            ResultSet rs = stat.executeQuery(query);

            for (int i = 0; i < 10; i++) {
                if (rs.next()) {
                    for (int j = 0; j < 10; j++) {
                        neuralNetwork.getW2().matrix[i][j] = rs.getDouble(j + 1);
                    }
                }
            }
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void readB1fromDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "select * from b1";

            ResultSet rs = stat.executeQuery(query);

            for (int i = 0; i < 10; i++) {
                if (rs.next()) {
                    double b = rs.getDouble(1);
                    for (int j = 0; j < neuralNetwork.getB2().numCols; j++) {
                        neuralNetwork.getB1().matrix[i][j] = b;
                    }
                }
            }
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void readB2fromDb(NeuralNetwork neuralNetwork) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/digitrecognition";
        String username = "";
        String password = "";

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement stat = connection.createStatement();

            String query = "select * from b2";

            ResultSet rs = stat.executeQuery(query);

            for (int i = 0; i < 10; i++) {
                if (rs.next()) {
                    double b = rs.getDouble(1);
                    for (int j = 0; j < neuralNetwork.getB2().numCols; j++) {
                        neuralNetwork.getB2().matrix[i][j] = b;
                    }
                }
            }
            connection.close();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static void readStateFromDb(NeuralNetwork neuralNetwork) {
        readW1fromDb(neuralNetwork);
        readW2fromDb(neuralNetwork);
        readB1fromDb(neuralNetwork);
        readB2fromDb(neuralNetwork);
    }

    public static void writeStateToDb(NeuralNetwork neuralNetwork) {
        writeW1ToDb(neuralNetwork);
        writeW2ToDb(neuralNetwork);
        writeB1ToDb(neuralNetwork);
        writeB2ToDb(neuralNetwork);
    }

    public static void writeStateToDb1(NeuralNetwork neuralNetwork) {
        writeW1toDb1(neuralNetwork);
        writeW2toDb1(neuralNetwork);
        writeB1toDb1(neuralNetwork);
        writeB2toDb1(neuralNetwork);
    }

    public static void createDb() {
        createW1table();
        createW2table();
        createB1table();
        createB2table();
    }
}
