package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Arrays;

public class Controller {

    @FXML private TextArea taEncriptar;
    @FXML private Button btnEncriptar;
    private String strEncriptar;

    @FXML
    public void btnEncriptar(ActionEvent actionEvent ) {
        strEncriptar = taEncriptar.getText().toString();

        try {
            Cipher cipher = Cipher.getInstance("RSA");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] plainText = strEncriptar.getBytes("UTF-8");
            System.out.println("Plain text: " + new String(plainText) + "\n\tbytes: " + Arrays.toString(plainText));
            byte[] encriptedText = cipher.doFinal(plainText);

            System.out.println("Ecripted Text: " + new String(encriptedText) + "\n\tbytes: " + Arrays.toString(encriptedText));


            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decodedText = new byte[50];
            decodedText = cipher.doFinal(encriptedText);

            System.out.println("Deciphered Text: " + new String(decodedText) + "\n\tbytes: " + Arrays.toString(decodedText));
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( strEncriptar );
    }

}
