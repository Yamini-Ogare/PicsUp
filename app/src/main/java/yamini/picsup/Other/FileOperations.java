package yamini.picsup.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class FileOperations {



    public static abstract class operation{

        public static void addDataToArray(ArrayList<Bitmap> bitmapArrayList){

            byte bytes[] = new byte[16];
            Arrays.fill(bytes,(byte)0);// 128 bits are converted to 16 bytes;



            File[] files = getAllFiles();

            if(files.length>0){

                for(File file : files){
                    byte[] filedata= readData(file);

                    try {

                        byte[] decryptData = FileEncrpt.decrypt(bytes,filedata);


                        byte[] decodedString =GZip.zipper.gzipUncompress(decryptData);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);



                        bitmapArrayList.add(decodedByte);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }


        private static   byte[] readData (File file){

            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return bytes;

        }

        private static File[] getAllFiles(){

            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "PicsUp");

            File f = new File(storageDir.getPath());
            File file[] = f.listFiles();

            if(file!=null)
                return file;

            else
                return new File[0];

        }


        private static File createImageFile(byte[] compressedImg)  {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp;
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "PicsUp");
            File newFile = null;

            if(!storageDir.exists()){

                storageDir.mkdirs();

            }
            try {

                newFile = File.createTempFile(
                        imageFileName,
                        ".txt",
                        storageDir
                );


                byte bytes[] = new byte[16];
                Arrays.fill(bytes,(byte)0);// 128 bits are converted to 16 bytes;
                FileOutputStream fos = new FileOutputStream(newFile);


                fos.write( FileEncrpt.encrypt(bytes,compressedImg));
                fos.flush();
                fos.close();
            }catch (Exception e){

                e.printStackTrace();

            }


            return newFile;

        }


        public static void uploadImage(Bitmap bitmap,ArrayList<Bitmap> bitmapArrayList) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            try {
                createImageFile(GZip.zipper.gzipCompress(byteArray));
                bitmapArrayList.clear();
                FileOperations.operation.addDataToArray(bitmapArrayList);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

}
