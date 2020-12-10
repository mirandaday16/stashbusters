package edu.neu.madcourse.stashbusters.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.views.MyFeedActivity;
import edu.neu.madcourse.stashbusters.views.PublicProfileActivity;
import edu.neu.madcourse.stashbusters.views.SnackPostActivity;

// Documentation: https://firebase.google.com/docs/reference/android/com/google/firebase/messaging/FirebaseMessagingService
// DISCLAIMER: BELOW IS BASED HEAVILY ON PROFESSOR FEINBERG'S DEMO CODE ("Firebase Demo - DemoMessagingService.java")
public class FCMService extends FirebaseMessagingService {

    private String Channel_Id  = "CHANNEL_ID";
    private String Channel_Name  = "CHANNEL_NAME";
    private String Channel_Description  = "CHANNEL_DESCRIPTION";

    // Documentation: Called when a new token for the default Firebase project is generated.
    // This is invoked after app install when a token is first generated, and again if the token changes.
    @Override
    public void onNewToken(String token) {
        Log.e("Token:", token);
    }

    // Managing messages/notifications while the app is in the foreground.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage); // Create a notification!
        }

        if (remoteMessage.getData() != null) {
            showToastMessage(remoteMessage.getData().get("title")); // Show a toast!
        }
    }

    // Create notification to show (for when app is in the foreground).
    private void showNotification(RemoteMessage remoteMessage) {

        String postType = remoteMessage.getData().get("postType");
        Intent intent;

        String postId = remoteMessage.getData().get("postId");
        String userId = remoteMessage.getData().get("userId");
        String senderId = remoteMessage.getData().get("senderId");

        switch (postType) {
            case "snack":
                intent = new Intent(this, SnackPostActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                break;
            case "follow":
                intent = new Intent(this, PublicProfileActivity.class);
                intent.putExtra("userId", senderId);
                break;
            case "commentPanel":
            case "likePanel":
                intent = new Intent(this, StashPanelPost.class);
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                break;
            case "commentSwap":
            case "likeSwap":
                intent = new Intent(this, StashSwapPost.class);
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                break;
            default:
                intent = new Intent(this, MyFeedActivity.class);
                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(Channel_Id,Channel_Name,NotificationManager.IMPORTANCE_HIGH);

            // Set up the notification channel
            notificationChannel.setDescription(Channel_Description);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this,Channel_Id);

        }
        else {
            builder = new NotificationCompat.Builder(this);
        }

        // Get image bitmap for the notification.
        // Adapted from: https://stackoverflow.com/questions/24840282/load-image-from-url-in-notification-android
        Bitmap bitmap = null;
        try {
            InputStream in = new
                    URL(remoteMessage.getData().get("image"))
                    .openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        notification = builder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.drawable.mouse_icon)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(0,notification);

    }

    // Create and show a toast (for when app is in the foreground).
    public void showToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

}