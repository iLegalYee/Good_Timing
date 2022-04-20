package edu.fsu.cs.goodtiming;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;

import android.provider.MediaStore;
import android.widget.Toast;

public class MediaPlayerService extends Service implements  MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{


    private static final int NOTIFICATION_ID_PLAYING = 10;

    static private MediaPlayer player;




    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnInfoListener(this);
        player.setOnBufferingUpdateListener(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.reset();
        if(!player.isPlaying()){
            try{
                player = MediaPlayer.create(getApplication(),R.raw.chill);
                player.start();
                //player.setDataSource(getApplication(),Uri.parse("android.resource://" + getApplication().getPackageName() + "/res/raw/wanderingthoughts.mp3"));
                //player.prepareAsync();
            }
            catch (Exception e){
                Toast.makeText(this, "Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null){
            if(player.isPlaying()){
                player.stop();
            }
            player.release();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        stopSelf();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {

        switch (what){
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }



}