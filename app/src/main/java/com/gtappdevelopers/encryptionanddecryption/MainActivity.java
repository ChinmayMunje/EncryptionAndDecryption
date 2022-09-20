package com.gtappdevelopers.encryptionanddecryption;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button encBtn, decBtn;
    File myDir;
    String key = "1857048598604859";
    String spec_key = "75hfj684kdjtkgi6";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.idIVimage);
        encBtn = findViewById(R.id.idBtnEncrypt);
        decBtn = findViewById(R.id.idBtnDecrypt);

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 100);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
        myDir = new File(Environment.getExternalStorageDirectory().toString() + "/Android/");

        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.chaitanyaimage);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                InputStream is = new ByteArrayInputStream(stream.toByteArray());

                File outputStream = new File(myDir, "Android");
                try {
                    Encryptor.encryptFile(key, spec_key, is, new FileOutputStream(outputStream));
                    Toast.makeText(MainActivity.this, "FIle encrypted..", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "File encrypted..");
                } catch (Exception e) {
                    Log.e("TAG", "Exception is : " + e);
                    e.printStackTrace();
                }
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File outputFileDec = new File(myDir, "chaitanyadec");
//                File encFile = new File(myDir, "Medimg");
//                try {
//                    Encryptor.decryptFile(key, spec_key, new FileInputStream(encFile), new FileOutputStream(outputFileDec));
//                    imageView.setImageURI(Uri.fromFile(outputFileDec));
//                    Log.e("TAG", "File Decrypted..");
//                    Toast.makeText(MainActivity.this, "Decrypted..", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("TAG", "Error is : " + e);
//                }
                try {
                    decryptFile("/storage/emulated/0/download/enc1.zip", "1234567890");
                } catch (Exception e) {
                    Log.e("TAG", "Error is : " + e);
                    e.printStackTrace();
                }

            }
        });


    }

    static void encrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        File extStore = Environment.getExternalStorageDirectory();
        Log.e("TAG", "FIle path is : " + extStore);

        FileInputStream fis = new FileInputStream(extStore + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/Medimg.jpg");
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        FileOutputStream fos = new FileOutputStream(extStore + "/Android/media/encrypted");

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    static void decrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        File extStore = Environment.getExternalStorageDirectory();
        FileInputStream fis = new FileInputStream(extStore + "/download/enc1.zip");
        FileInputStream fi = new FileInputStream("/storage/emulated/0/download/enc1.zip");
        FileOutputStream fos = new FileOutputStream(extStore + "/download");
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fi, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    public void decryptFile(String path, String Pass) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(path.replace(".crypt", ""));
        byte[] key = (Pass).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        Toast.makeText(MainActivity.this, "File decrypted..", Toast.LENGTH_SHORT).show();
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Read Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Read Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Write Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Write Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}