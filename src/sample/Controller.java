package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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

    @FXML private TextArea taEncriptar; //Text Base
    @FXML private TextArea taEncriptat; //Text Encriptat
    @FXML private TextArea taDesencriptar; //Text Desencriptat
    @FXML private PasswordField pfContrasenya; //Constrasenya
    @FXML private TextField tfRuta; //Ruta Arxiu Text Encriptat
    @FXML private TextField tfRutaSave; //Ruta Guardar Arxiu
    @FXML private Button btnEncriptar;
    @FXML private Button btnDesencriptar;

    private String strEncriptar;
    private String strRutaEncrypt;
    private String strRutaSave;
    private char[] strKey;

    private KeyStore keyStore;
    private Cipher cipher;

    @FXML
    public void btnEncriptar(ActionEvent actionEvent ) {
        strEncriptar = taEncriptar.getText().toString();
        strRutaSave = tfRutaSave.getText().toString();

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
            FileOutputStream fos = new FileOutputStream(strRutaSave);
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
        strKey = pfContrasenya.getText().toCharArray();
        strRutaEncrypt = tfRuta.getText().toString();

        try {
            /** DESECRIPTAR **/
            //Clau privadad
            PrivateKey pkey = (PrivateKey) keyStore.getKey( "key1" , strKey );

            cipher.init(Cipher.DECRYPT_MODE, pkey);
            byte[] decodedText = new byte[50];
            decodedText = cipher.doFinal(Files.readAllBytes(new File(strRutaEncrypt).toPath()) );

            //Mostra Text Desencriptat
            taDesencriptar.setText( new String( decodedText ) );
        } catch ( Exception e ) {
            //Mostra l'error
            taDesencriptar.setText( e.getMessage() );
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            //Deshabilita l'edició als TextArea
            taEncriptat.setEditable( false );
            taDesencriptar.setEditable( false );

            //Crea la KeyStore
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
