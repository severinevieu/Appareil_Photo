package com.example.appareil_photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //constante
    private static final int RETOUR_PRENDRE_PHOTO =1;

    //propriété d'accés au obj graphique créer
    private Button btnPrendrePhoto;
    private ImageView imgAffichePhoto;
    private String photoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();
    }

    /**
     * Initialisation de l'activity
     */

    private void initActivity() {
        btnPrendrePhoto = (Button) findViewById(R.id.btnPrendrePhoto);
        imgAffichePhoto = (ImageView) findViewById(R.id.imageAffichePhoto);

        createOnClickBtnPrendrePhoto();
    }

    //événement clic sur boutton btnPrendrePhoto

    private void createOnClickBtnPrendrePhoto() {
        btnPrendrePhoto.setOnClickListener(new Button.OnClickListener(){
        @Override
        public void onClick (View v){
            prendreUnePhoto();
        }
    });
}

    //accés à l'appareil photo et mémoire dans un fichier temporaire

    private void prendreUnePhoto(){
        //creer un Intent pour prendre photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // test pour contrôler l'intent
        if(intent.resolveActivity(getPackageManager()) !=null){
            //créer un nom de fichier unique
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo"+time, ".jpg", photoDir);
                //enregistrement du chemin
                photoPath = photoFile.getAbsolutePath();
                // création URI
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName()+".provider",photoFile);
                //transfert Uri vers intent pour enregistrement dans fichier temporaire
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //Ouvrir l'activity par rapport à l'intent
                startActivityForResult(intent, RETOUR_PRENDRE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * retour del'appareil photo (startActivityForResult)
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //vérifie code de retour et état du retour
        if(requestCode ==RETOUR_PRENDRE_PHOTO && resultCode==RESULT_OK){
            //récupération img dans un bitmap
            Bitmap image = BitmapFactory.decodeFile(photoPath);

            imgAffichePhoto.setImageBitmap(image);
        }
    }
}
