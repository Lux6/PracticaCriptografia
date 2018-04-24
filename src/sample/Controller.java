package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextArea taEncriptar;
    @FXML private Button btnEncriptar;

    private String strEncriptar;
    private KeyStore keyStore;

    @FXML
    public void btnEncriptar(ActionEvent actionEvent ) {
        strEncriptar = taEncriptar.getText().toString();

        try {
            Cipher cipher = Cipher.getInstance("RSA");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            /** ENCRIPTAR **/
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] plainText = strEncriptar.getBytes("UTF-8");
            //Mostra Bytes text normañ
            byte[] encriptedText = cipher.doFinal(plainText);

            //Desa els Bytes encryptats a un document
            FileOutputStream fos = new FileOutputStream("./encrypt.txt");
            fos.write( encriptedText );

            System.out.println( "Bytes: " + Arrays.toString(Files.readAllBytes(new File("./encrypt.txt").toPath()) ));








            /** DESECRIPTAR **/
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decodedText = new byte[50];
            decodedText = cipher.doFinal(Files.readAllBytes(new File("./encrypt.txt").toPath()) );

            System.out.println("Deciphered Text: " + new String(decodedText) + "\n\tbytes: " + Arrays.toString(decodedText));

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char [] keyStorePassword = "123abc" .toCharArray ();

            try (InputStream keyStoreData = new FileInputStream("./keystore.ks")) {
                keyStore.load (keyStoreData, keyStorePassword);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
