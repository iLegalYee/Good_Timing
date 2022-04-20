package edu.fsu.cs.goodtiming.Timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.fsu.cs.goodtiming.MediaPlayerService;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.Timer.BreakDialog;

public class SessionFragment extends Fragment {
    public static final String CHANNEL_TIMER = "channelTimer";
    private OnSessionFragmentInteractionListener mListener;
    private Bundle mBundle;
    private static final long START_TIME_IN_MILLIS = 6000;
    private TextView textViewTime;
    private Button btnStartPause;
    private Button btnReset;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;
    private Button btnBreak;
    private ImageButton btnPlay;
    private ImageButton btnStop;
    private boolean musicPlaying;
    private Intent serviceIntent;







    public SessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_session, container, false);


        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById
        textViewTime = rootView.findViewById(R.id.textviewCountDown);
        btnStartPause = rootView.findViewById(R.id.btnStartPause);
        btnReset = rootView.findViewById(R.id.btnReset);
        btnBreak = rootView.findViewById(R.id.btnBreak);
        btnPlay = rootView.findViewById(R.id.btnPlay);
        

        serviceIntent = new Intent(getActivity(), MediaPlayerService.class);




        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning){
                    pauseTimer();
                } else {
                    startTimer();

                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        btnBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                BreakDialog breakDialog = new BreakDialog();
                breakDialog.show(getChildFragmentManager(),"BreakDialog");

            }
        });


        updatetimer2();


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!musicPlaying)){
                   playAudio();
                    btnPlay.setBackgroundResource(R.drawable.stopbtn);
                    musicPlaying = true;
                }else{
                    stopPlayService();
                    btnPlay.setBackgroundResource(R.drawable.playbtn);
                    musicPlaying = false;
                }
            }
        });


        return rootView;
    }

    private void stopPlayService() {
        try {
            requireActivity().stopService(serviceIntent);
        }catch (SecurityException e){
            Toast.makeText(getActivity(),"Error :" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() {
        try {
            requireActivity().startService(serviceIntent);
        }catch (SecurityException e){
            Toast.makeText(getActivity(),"Error :" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    // Initializes mListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionFragmentInteractionListener) {
            mListener = (OnSessionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSessionFragmentInteractionListener {
        void ShowEventFragment(Bundle bundle);
        void ShowCalendarFragment(Bundle bundle);
        void ShowUserFragment(Bundle bundle);
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updatetimer2();
                    callNotification();

            }

            @Override
            public void onFinish() {
                    timerRunning = false;
                    btnStartPause.setText("Start");
                    btnStartPause.setVisibility(View.INVISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                    btnBreak.setVisibility(View.INVISIBLE);
                    callNotification();
            }
        }.start();
        timerRunning = true;
        btnStartPause.setText("Pause");
        btnReset.setVisibility(View.INVISIBLE);
        btnBreak.setVisibility(View.VISIBLE);

    }
    private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning=false;
        btnStartPause.setText("Start");
        btnReset.setVisibility(View.VISIBLE);

    }

    private void resetTimer(){
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updatetimer2();
        btnReset.setVisibility(View.INVISIBLE);
        btnBreak.setVisibility(View.INVISIBLE);
        btnStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText(){
        int hours = (int)(timeLeftInMillis/1000)/3600;
        int minutes = (int)(timeLeftInMillis/1000)/60;
        int seconds = (int)(timeLeftInMillis/1000)%60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes,seconds);
        textViewTime.setText(timeLeftFormatted);
    }

    private void updatetimer2(){
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));
        textViewTime.setText(hms);
    }

    private void callNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "NotificationTimer");
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));
        textViewTime.setText(hms);
        builder.setContentTitle("Timer");
        builder.setContentText("Event" + hms);
        builder.setSmallIcon(R.drawable.ic_time);
        builder.setAutoCancel(true);
       if (hms.equals("00:00:01")){
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
        managerCompat.notify(1,builder.build());

    }

    





}
