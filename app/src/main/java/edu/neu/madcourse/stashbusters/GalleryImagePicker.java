package edu.neu.madcourse.stashbusters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;


public class GalleryImagePicker extends Activity {
    private Bitmap photoBitmap;
    private int REQUEST_CODE;
    private Uri photoUri;

    public GalleryImagePicker(int REQUEST_CODE) {
        this.REQUEST_CODE = REQUEST_CODE;
    }

    public Bitmap getBitmap() {
        return photoBitmap;
    }

    public int getRequestCode() {
        return REQUEST_CODE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        photoBitmap = selectedImage;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                photoUri = selectedImage;

                                photoBitmap = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private Uri fileToUri(String path) {
        Uri file = Uri.fromFile(new File(path));
        return file;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

}
