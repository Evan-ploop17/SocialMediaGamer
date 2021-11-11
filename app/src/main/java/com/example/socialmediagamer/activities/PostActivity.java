package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediagamer.R;
import com.example.socialmediagamer.models.Post;
import com.example.socialmediagamer.providers.AuthProvider;
import com.example.socialmediagamer.providers.ImageProvider;
import com.example.socialmediagamer.providers.PostProvider;
import com.example.socialmediagamer.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mBtnPost;
    ImageProvider mImageProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    ImageView mImageViewPC;
    ImageView mImageViewPlay;
    ImageView mImageViewXbox;
    ImageView mImageViewNintendo;
    TextView mTextViewCategory;
    String mCategory= "";
    PostProvider mPostProvider;
    String mTitle = "";
    String mDescription = "";
    AlertDialog mDialog;
    AlertDialog.Builder mDialogBuilder;
    CharSequence options[];

    // Foto1
    String mAbsolutePhotoPhat;
    String mPhotoPath;
    File mPhotoFile;

    // Foto 2
    String mAbsolutePhotoPhat2;
    String mPhotoPath2;
    File mPhotoFile2;

    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2  = 2;
    private final int PHOTO_REQUEST_CODE  = 3;
    private final int PHOTO_REQUEST_CODE_2  = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();
        // Mensaje
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage(R.string.wait).build();
        mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setTitle("Choose an option");

        options = new CharSequence[] { "Imagen de galeria",  "Camara"};

        mTextInputTitle = findViewById(R.id.textInputNameGame);
        mTextInputDescription = findViewById(R.id.textInputDescription);
        mImageViewPC = findViewById(R.id.imageViewPC);
        mImageViewPlay = findViewById(R.id.imageViewPlay);
        mImageViewXbox = findViewById(R.id.imageViewXbox);
        mImageViewNintendo = findViewById(R.id.imageViewNintendo);
        mTextViewCategory = findViewById(R.id.textViewCategory);

        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(GALLERY_REQUEST_CODE);
                //openGallery(GALLERY_REQUEST_CODE);
            }
        });

        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(GALLERY_REQUEST_CODE_2);
                //openGallery(GALLERY_REQUEST_CODE_2);
            }
        });

        mBtnPost = findViewById(R.id.btnPost);
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickPost(); 
            }
        });

        mImageViewPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "PC";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "PS4";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "XBOX";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewNintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "NINTENDO";
                mTextViewCategory.setText(mCategory);
            }
        });
    }



    private void selectOptionImage(int numberImage) {
        mDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if( i == 0 ){
                    if(numberImage == 1){
                        openGallery(GALLERY_REQUEST_CODE);
                    }
                    else if(numberImage == 2){
                        openGallery(GALLERY_REQUEST_CODE_2);
                    }
                }
                else if(i == 1){
                    if(numberImage == 1){
                        takePhoto(PHOTO_REQUEST_CODE);
                    }
                    else if(numberImage == 2){
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });
        mDialogBuilder.show();
    }

    private void takePhoto(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Toast.makeText(PostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile( PostActivity.this, "com.example.hanut", photoFile );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);

            }
        }
    }
    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if(requestCode == PHOTO_REQUEST_CODE){
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPhat = photoFile.getAbsolutePath();
        }else if(requestCode == PHOTO_REQUEST_CODE_2){
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPhat2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void clickPost() {
        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescription.getText().toString();
        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            // Selecciono ambas imágenes de la galeria
            if(mImageFile != null && mImageFile2 != null){
                saveImage(mImageFile, mImageFile2);
            }
            // Tomo las dos fotos de la camara
            else if(mPhotoFile != null && mPhotoFile2 != null ){
                saveImage(mPhotoFile, mPhotoFile2);
            }
            // Primera galeria y segunda foto
            else if(mImageFile != null && mPhotoFile2 != null ){
                saveImage(mImageFile, mPhotoFile2);
            }
            // Primera foto y segunda galeria
            else if(mPhotoFile != null && mImageFile2 != null ){
                saveImage(mPhotoFile, mImageFile2);
            }
            else{
                Toast.makeText(PostActivity.this,"Choose an Image", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(PostActivity.this,"Fill in the fields", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(File imageFile, File imageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this, imageFile ).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            mImageProvider.save(PostActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());

                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "Info saved", Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            Toast.makeText(PostActivity.this, "Info unsaved", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Image 2 unsaved", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Image unsaved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.upload_image);
        mImageViewPost2.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mCategory = "";
        mDescription = "";
        mImageFile = null;
        mImageFile2 = null;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && requestCode != RESULT_OK){
            try{
                mPhotoFile = null;
                // Transformar la URI en archivo
                mImageFile = FileUtil.from(this, data.getData());
                // Para que la imágen seleccionada aparezca en la pantalla
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch(Exception e){
                Log.d("ERROR ","Se produjo el error: " + e.getMessage());
                Toast.makeText(PostActivity.this, "Se produjo un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == GALLERY_REQUEST_CODE_2 && requestCode != RESULT_OK){
            try{
                mPhotoFile2 = null;
                // Transformar la URI en archivo
                mImageFile2 = FileUtil.from(this, data.getData());
                // Para que la imágen seleccionada aparezca en la pantalla
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            }catch(Exception e){
                Log.d("ERROR ","There is an error: " + e.getMessage());
                Toast.makeText(PostActivity.this, "There is an error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        // SELECCIÓN DE FOTOGRAFÍA
        // foto 1
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPhat);
            Picasso.with(PostActivity.this).load(mPhotoPath).into(mImageViewPost1);
        }
        // foto 2
        if(requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK){
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPhat2);
            Picasso.with(PostActivity.this).load(mPhotoPath2).into(mImageViewPost2);
        }
    }



    // FInal de la clase
}