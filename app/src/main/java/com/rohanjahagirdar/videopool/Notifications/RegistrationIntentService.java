package com.rohanjahagirdar.videopool.Notifications;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rohanjahagirdar.videopool.R;

import java.io.IOException;

/**
 * Created by Rohan Jahagirdar
 */
// abbreviated tag name
public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Toast.makeText(RegistrationIntentService.this, token, Toast.LENGTH_LONG).show();
            String label = "Video Pool App registration ID";
            copyRegistrationIdToClipboard(label, token);
        } catch (IOException e) {
            e.printStackTrace();
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(GCM_TOKEN, token).apply();
        //        putBoolean(SENT_TOKEN_TO_SERVER, true).commit();
    }

    /**
     * Copy the Registration ID token for notification sto be sent to the app.
     * The token is copied to the clipboard. The user has to share it with the local server.
     * The token for push notification is to be sent to the local server for storage by the user.
     * If server is available then the developer can send it directly to the server.
     *
     *
     * @param label The label for the label. The token will be stored with the label in the clipboard.
     * @param token The token received from Google Cloud Messaging.
     */

    private void copyRegistrationIdToClipboard(String label, String token) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, token);
        clipboard.setPrimaryClip(clip);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(GCM_TOKEN, token).
                putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }
}