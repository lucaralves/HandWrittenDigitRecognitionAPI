# HandWrittenDigitRecognitionAPI
REST API - built in java (SpringMVC) - with the purpose of recognizing hand written digits. The client side is based in a html page, where the user can draw the digit and send it to the API.

----------------------------------------------------------------------------------------------------------------------------------------------------------------------

The API has a built in neural network with 784 entries, 10 neurons on the hidden layer and 10 neurons on the out layer. The neural network was trained with the MNIST dataset. All the math and array manipulation behind the machine learning was built from scratch. The client side can be accessed at this uri: http://lucaralves.santa.pt:8080/digitrecognition/api/getWebPage.

On the training phase, during all the seasons through the train dataset, the weights and bias were saved on a binary file. After the training, all the weights and bias were saved in different tables, accordingly the layer where They belong. On the operation phase, the program import all the data (weights and bias) through sql queries.

The mnist dataset adapted to csv file can be downloaded here: https://www.kaggle.com/datasets/oddrationale/mnist-in-csv.
