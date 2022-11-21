package com.example.digitrecognitionv3.controller;

import com.example.digitrecognitionv3.data.Data;
import com.example.digitrecognitionv3.dto.DTO;
import com.example.digitrecognitionv3.helper.Helper;
import com.example.digitrecognitionv3.model.Matrix;
import com.example.digitrecognitionv3.model.NeuralNetwork;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@RestController
@RequestMapping("/api")
public class ModelController {

    @RequestMapping(value = "/postImage",
            method = RequestMethod.POST)
    @ResponseBody
    public DTO postImage(@RequestBody String base64Image) throws IOException {

        try {
            NeuralNetwork neuralNetwork = new NeuralNetwork(1);
            Data.readStateFromDb(neuralNetwork);

            base64Image = Helper.shapeBase64(base64Image);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
        /*
        File f = new File("C:\\Users\\TECRA\\Desktop\\Projetos" +
                "\\DigitRecognitionV3\\carregamentos\\carregamento.png");

        ImageIO.write(image, "png", f);
         */

            int[] imageRGB =
                    image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                            null, 0, image.getWidth());

            int[] blueS = new int[imageRGB.length];
            int[] greenS = new int[imageRGB.length];
            int[] redS = new int[imageRGB.length];
            int[] alphaS = new int[imageRGB.length];
            for (int i = 0; i < imageRGB.length; i++) {

                // Components will be in the range of 0..255:
                int blue = imageRGB[i] & 0xff;
                int green = (imageRGB[i] & 0xff00) >> 8;
                int red = (imageRGB[i] & 0xff0000) >> 16;
                int alpha = (imageRGB[i] & 0xff000000) >>> 24;

                blueS[i] = blue;
                greenS[i] = green;
                redS[i] = red;
                alphaS[i] = alpha;
            }

            double[][] mAplhaS = new double[1][imageRGB.length];
            for (int i = 0; i < imageRGB.length; i++) {
                mAplhaS[0][i] = (double) alphaS[i];
            }
            Matrix matrixAlphaS = new Matrix(mAplhaS);
            Matrix pixelsMatrixT = matrixAlphaS.transpose().
                    multiplyNumByMatrix(1.0 / 255.0);


            double[][][] forwardPropOut = neuralNetwork.forwardProp(pixelsMatrixT);
            Matrix a2Decode = neuralNetwork.
                    oneHotLabelDecode(new Matrix(forwardPropOut[3]));

            return new DTO(String.valueOf(a2Decode.matrix[0][0]));
        } catch (Exception e) {
            System.out.println(e.toString());
            return new DTO(e.toString());
        }
    }
}
