package com.rohanjahagirdar.videopool;


import android.content.Intent;
import android.content.SharedPreferences;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohanjahagirdar.videopool.Networking.OkHttpRequest;
import com.rohanjahagirdar.videopool.Notifications.RegistrationIntentService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * Created by Rohan Jahagirdar
 */

public class MainActivity extends AppCompatActivity {

    private ImageButton addToPlayList;
    private TextInputEditText youtubeLinkEditText;
    private ImageView videoPoolLogo;
    private OkHttpClient client = new OkHttpClient();
    private OkHttpRequest request;
    private SharedPreferences prefs;
    private String PREFS_NAME = "VideoPoolTheApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addToPlayList = (ImageButton) findViewById(R.id.add);
        youtubeLinkEditText = (TextInputEditText) findViewById(R.id.enter_youtube_link);
        videoPoolLogo = (ImageView) findViewById(R.id.video_pool_logo);
        prefs = getSharedPreferences(PREFS_NAME, 0);

        addToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(youtubeLinkEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a link",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String link = youtubeLinkEditText.getText().toString().trim();
                    sendLink(link);
                }
            }
        });

        videoPoolLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsServiceIntent = new Intent(MainActivity.this,
                        RegistrationIntentService.class);
                MainActivity.this.startService(notificationsServiceIntent);
            }
        });
    }


    /**
     * Send the link of the youtube video to the server. The link will be sent to the Database.
     * The server will add the video to the playlist in the Database.
     * The target Desktop that will play the playlist will pick it up from the server and play all the
     * songs.
     *
     *
     * @param link the link that is shared by the Youtube app or Website. eg: https://youtu.be/jgpJVI3tDbY
     */

    private void sendLink(String link) {
        String url = prefs.getString("server", "");
        String user = prefs.getString("user", "");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user", user);
        params.put("link", link);

        request = new OkHttpRequest(client);
        System.out.println(params);
        System.out.println("url: " + url);

        if(!url.isEmpty()) {

            request.POST(url, params, new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        /*
                            System.out.println("Login Error: " + e);
                            Toast.makeText(MainActivity.this, "Link Upload Failure!!",
                                Toast.LENGTH_LONG).show();
                        */
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {}
                    });
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please ask the server to send a URL to send the video link to.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}