package com.example.facebookapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FunctionFBActivity extends AppCompatActivity {

    private EditText edtUrl;
    private Button btnShare;
    private ImageView imgHT;
    private Button btnShareImage;
    private VideoView videoView;
    private Button btnPickVideo;
    private Button btnShareVideo;
    public static int SELECT_IMAGE = 1;
    private Bitmap bitmap;
    public static int PICK_VIDEO = 2;
    private Uri selectVideo;

    private void init() {

        edtUrl = (EditText) findViewById(R.id.edtUrl);
        btnShare = (Button) findViewById(R.id.btnShare);
        imgHT = (ImageView) findViewById(R.id.imgHT);
        btnShareImage = (Button) findViewById(R.id.btnShareImage);
        videoView = (VideoView) findViewById(R.id.videoView);
        btnPickVideo = (Button) findViewById(R.id.btnPickVideo);
        btnShareVideo = (Button) findViewById(R.id.btnShareVideo);

    }

    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_f_b);
        init();

        shareDialog = new ShareDialog(FunctionFBActivity.this);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareDialog.canShow(ShareLinkContent.class)) {
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(edtUrl.getText().toString())).build();
                }
                shareDialog.show(shareLinkContent);
            }
        });

        imgHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });
//        btnShareImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharePhoto photo = new SharePhoto.Builder()
//                        .setImageUrl(Uri.parse("https://live.staticflickr.com/3730/8971069758_843645ff6d.jpg"))
//                        .build();
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(photo)
//                        .build();
//                shareDialog.show(content);
//            }
//        });
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });

        btnPickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, PICK_VIDEO);
            }
        });
        btnShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareVideo shareVideo = null;
                shareVideo = new ShareVideo.Builder()
                        .setLocalUrl(selectVideo)
                        .build();
                ShareVideoContent content = new ShareVideoContent.Builder()
                        .setVideo(shareVideo)
                        .build();
                shareDialog.show(content);
                videoView.stopPlayback();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgHT.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK) {
            selectVideo = data.getData();
            videoView.setVideoURI(selectVideo);
            videoView.start();
        }
    }
}