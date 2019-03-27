package yamini.picsup.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import yamini.picsup.Adapter.ListAdapter;
import yamini.picsup.Other.FileOperations;
import yamini.picsup.R;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    RecyclerView recyclerView;
    final int IMG_REQUEST=2;
    final int STORAGE_PERMISSION=5;
    Bitmap bitmap=null;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ListAdapter adapter;





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        if (requestCode ==IMG_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.img2);
        recyclerView= findViewById(R.id.recycle);
        imageView.setImageResource(R.drawable.add);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ListAdapter(this,bitmapArrayList);

        recyclerView.setAdapter(adapter);

        FileOperations.operation.addDataToArray(bitmapArrayList);

        adapter.notifyDataSetChanged();

    }




    public void upload(View view) {

        if(bitmap!=null) {
            FileOperations.operation.uploadImage(bitmap,bitmapArrayList);
            bitmap=null;
            imageView.setImageResource(R.drawable.add);
            adapter.notifyDataSetChanged();

        }else
            Toast.makeText(this,"Add Image to Upload",Toast.LENGTH_LONG).show();
    }

    public void addImg(View view) {

        openGallery(IMG_REQUEST);

    }




    public void openGallery(Integer req){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);

        }else {

            Intent intent = new Intent();
// Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), req);

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if(requestCode==STORAGE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery(IMG_REQUEST);
            }
        }

    }

}
