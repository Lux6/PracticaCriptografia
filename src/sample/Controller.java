package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextArea taEncriptar;
    @FXML private TextArea taEncriptat;
    @FXML private TextArea taDesencriptar;
    @FXML private Button btnEncriptar;
    @FXML private TextField tfContrasenya;
    @FXML private Button btnDesencriptar;

    private String strEncriptar;
    private char[] strKey;

    private KeyStore keyStore;
    private Cipher cipher;

    @FXML
    public void btnEncriptar(ActionEvent actionEvent ) {
        strEncriptar = taEncriptar.getText().toString();

        try {
            cipher = Cipher.getInstance("RSA");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            /** ENCRIPTAR **/
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] plainText = strEncriptar.getBytes("UTF-8");
            //Mostra Bytes text normal
            byte[] encriptedText = cipher.doFinal(plainText);

            taEncriptat.setText( new String (encriptedText ) );

            //Desa els Bytes encryptats a un document
            FileOutputStream fos = new FileOutputStream("./encrypt.txt");
            fos.write( encriptedText );

            //System.out.println( "Bytes: " + Arrays.toString(Files.readAllBytes(new File("./encrypt.txt").toPath()) ));

            //Guarda Clau Privada a la KeyStore
            char[] key1 = "keey".toCharArray();
            keyStore.setKeyEntry( "key1" , keyPair.getPrivate(), key1, new Certificate[] { null } );

        } catch ( Exception e ) {
            System.out.println(
                    "Encryp\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    public void btnDesencriptar( ActionEvent actionEvent ) {
        strKey = tfContrasenya.getText().toCharArray();

        try {
            /** DESECRIPTAR **/
            //Clau privadad
            PrivateKey pkey = (PrivateKey) keyStore.getKey( "key1" , strKey );

            cipher.init(Cipher.DECRYPT_MODE, pkey);
            byte[] decodedText = new byte[50];
            decodedText = cipher.doFinal(Files.readAllBytes(new File("./encrypt.txt").toPath()) );

            taDesencriptar.setText( new String( decodedText ) );
        } catch ( Exception e ) {
            System.out.println(
                    "Decrypt\n"
                            + e.getMessage()
            );
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] pswd = "passwd".toCharArray();
            keyStore.load( null , pswd );

        } catch ( Exception e ) {
            System.out.println(
                    "KeyStore"
                    + e.getMessage()
            );
        }
    }
}
