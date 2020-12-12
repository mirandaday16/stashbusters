package edu.neu.madcourse.stashbusters.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Utils
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    // FCM server key
    private static final String SERVER_KEY ="key=AAAAZhAZJQA:APA91bFtBTB_cyXhfzoP2GGiHXAFTQkBpnd_" +
            "bNB9UHfLAcLZ8qytxo436A3pasF269vCbOGY_32O8Vz8hpgpc7HdIyeg3PdLi3wqfWXUu9YGbcrv26OWUHy1h" +
            "BAobynUvkmrklxJ6Ei3";

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * This function is called from public profiles (upon follow button), swap/panel posts (upon
     * comment/like), and snack posts (upon voting). It gets the device token of the recipient and
     * the username of the sender and passes on data to sendNotification.
     *
     * @param postType the type of notification about to be sent (e.g., likePanel, likeSwap,
     *                 commentPanel, commentSwap, snackVote, follow)
     * @param senderId the userId of the user who prompted the notification (e.g., the user that hit
     *                 the comment, like, or follow buttons)
     * @param recipientId the userID of the target of the notification (e.g., the author of the post
     *                    that was liked, commented on, followed, etc.)
     * @param postId the postID is id of the snack/panel/swap vote that was interacted with. However,
     *               in the case of postType == follow, the postId is the id of the user that tapped
     *               the follow button.
     * @param photoURL the url of the post or user to be shown in the notification
     */
    public static void startNotification(final String postType, final String senderId, final String recipientId, final String postId, final String photoURL){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ValueEventListener tokenListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    // Get recipient's token.
                    final String clientToken = (String) dataSnapshot.child("users").child(recipientId).child("deviceToken").getValue();

                    // Get sender's username.
                    final String sender = (String) dataSnapshot.child("users").child(senderId).child("username").getValue();

                    // Send the actual message once the token is retrieved.
                    if (clientToken != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendNotification(clientToken, sender, postType, recipientId, postId, photoURL);
                            }
                        }).start();
                    }
                }
                catch (Exception ignored) {
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        };

        ref.addListenerForSingleValueEvent(tokenListener);
    }

    /**
     * This function is called from the above startNotification function above. It creates and sends
     * the notification to FCM given the token of the recipient. Other given params are later used
     * to determine the target class of the intent as well as the extras to fill that intent. This
     * takes users to the appropriate activity when the notification is tapped.
     */
    // DISCLAIMER: BASED HEAVILY ON PROFESSOR FEINBERG'S DEMO CODE ("Firebase Demo - FCMActivity.java")
    private static void sendNotification(String clientToken, String sender, String postType, String userId, String postId, String imgURL) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            /*
            // Populate notification object
            */
            switch (postType){
                case "snack":
                    jNotification.put("title", "Snack busted!");
                    jNotification.put("body", "Someone voted on your snack bust post.");
                    jData.put("title", "You got a snack vote!");
                    jData.put("userId", userId);
                    jData.put("postId", postId);
                    jData.put("image", imgURL);
                    break;
                case "follow":
                    jNotification.put("title", "Followed!");
                    jNotification.put("body", sender + " started following you.");
                    jData.put("title", "You got a follower!");
                    jData.put("senderId", postId);
                    jData.put("senderUsername", sender);
                    break;
                case "likePanel":
                    jNotification.put("title", "You're inspiring other Stashbusters!");
                    jNotification.put("body", sender + " liked your panel post.");
                    jData.put("title", "You got a like!");
                    jData.put("userId", userId);
                    jData.put("postId", postId);
                    jData.put("image", imgURL);
                    break;
                case "likeSwap":
                    jNotification.put("title", "You're inspiring other Stashbusters!");
                    jNotification.put("body", sender + " liked your swap post.");
                    jData.put("title", "You got a like!");
                    jData.put("userId", userId);
                    jData.put("postId", postId);
                    jData.put("image", imgURL);
                    break;
                case "commentSwap":
                    jNotification.put("title", "Other Stashbusters wanna swap!");
                    jNotification.put("body", sender + " made an offer.");
                    jData.put("title", "You got an offer!");
                    jData.put("userId", userId);
                    jData.put("postId", postId);
                    jData.put("image", imgURL);
                    break;
                case "commentPanel":
                    jNotification.put("title", "Stashbusting time!");
                    jNotification.put("body", sender + " gave you advice on your panel post.");
                    jData.put("title", "You got advice!");
                    jData.put("userId", userId);
                    jData.put("postId", postId);
                    jData.put("image", imgURL);
                    break;
                default:
                    break;
            }

            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("image", imgURL);

            jData.put("postType", postType);

            /*
            // Build payload
            */

            jPayload.put("to", clientToken); // token the user is sending the message to
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            /*
            // Send to Firebase
            */
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send message!
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read response from Firebase Cloud Messaging...
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("Posted?", "run: " + resp);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to parse and convert stream to string.
     */
    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
