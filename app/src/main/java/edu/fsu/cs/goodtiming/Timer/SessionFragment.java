package edu.fsu.cs.goodtiming.Timer;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import edu.fsu.cs.goodtiming.MyContentProvider;
import edu.fsu.cs.goodtiming.MediaPlayerService;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;


public class SessionFragment extends Fragment {
    public static final String CHANNEL_TIMER = "channelTimer";
    private OnSessionFragmentInteractionListener mListener;
    private Bundle mBundle;
    private long START_TIME_IN_MILLIS;
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
    private Button btnSet;
    private EditText editTextHrs, editTextMins, editTextSecs;
    private int timePassed;

    public SessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("NotificationTimer", "NotificationTimer", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
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
        editTextHrs = rootView.findViewById(R.id.edittextHrs);
        editTextMins = rootView.findViewById(R.id.edittextMins);
        editTextSecs = rootView.findViewById(R.id.edittextSecs);
        editTextHrs.setFilters(new InputFilter[]{new InputFilterMinMax(1,72)});
        editTextMins.setFilters(new InputFilter[]{new InputFilterMinMax(1,60)});
        editTextSecs.setFilters(new InputFilter[]{new InputFilterMinMax(1,60)});
        btnSet = rootView.findViewById(R.id.btnSet);
        timePassed = 0;

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputHrs = editTextHrs.getText().toString();
                String inputMins = editTextMins.getText().toString();
                String inputSecs = editTextSecs.getText().toString();
                if(inputHrs.length() == 0 && inputMins.length() == 0 && inputSecs.length() ==0 ){
                    Toast.makeText(getActivity(), "Fields can not be empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if((inputHrs.length() == 0) ){
                    inputHrs = "0";
                }
                if ((inputMins.length() == 0) ) {
                    inputMins = "0";
                }
                if (inputSecs.length() == 0){
                    inputSecs = "0";
                }
            long millisInput = (Long.parseLong(inputHrs)*3600000) + (Long.parseLong(inputMins)*60000) + (Long.parseLong(inputSecs)*1000);
                if(millisInput == 0 ){
                    Toast.makeText(getActivity(), "ERROR",Toast.LENGTH_LONG).show();
                    return;
                }
                setTime(millisInput);
                editTextHrs.setText("");
                editTextMins.setText("");
                editTextSecs.setText("");
            }
        });

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
        countDownTimer = new CountDownTimer(timeLeftInMillis, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updatetimer2();
                callNotification();
                timePassed += 1;

            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartPause.setText("Start");
                btnStartPause.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.VISIBLE);
                btnBreak.setVisibility(View.INVISIBLE);
                callNotification();

                // Send how much time passed to the content provider - Will be used in analytics
                ContentValues values = new ContentValues();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String date = format.format(Calendar.getInstance().getTime());
                values.put(MyContentProvider.COLUMN_ANALYTICS_TIME, date);
                values.put(MyContentProvider.COLUMN_ANALYTICS_DATA, timePassed);
                Uri uri = getActivity().getContentResolver().insert(MyContentProvider.ANALYTICS_CONTENT_URI, values);
                timePassed = 0;

            }
        }.start();
        timerRunning = true;
        btnStartPause.setText("Pause");
        btnSet.setVisibility(View.INVISIBLE);
        editTextSecs.setVisibility(View.INVISIBLE);
        editTextMins.setVisibility(View.INVISIBLE);
        editTextHrs.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        btnBreak.setVisibility(View.VISIBLE);

    }

    private void setTime(long milliseconds){
        START_TIME_IN_MILLIS = milliseconds;
        resetTimer();
        closeKeyboard();
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
        editTextSecs.setVisibility(View.VISIBLE);
        editTextMins.setVisibility(View.VISIBLE);
        editTextHrs.setVisibility(View.VISIBLE);
        btnSet.setVisibility(View.VISIBLE);
    }

    private void updatetimer2(){
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));
        textViewTime.setText(hms);
    }

    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void callNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "NotificationTimer");
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));
        textViewTime.setText(hms);
        builder.setContentTitle("Timer");
        builder.setContentText(hms);
        builder.setSmallIcon(R.drawable.ic_time);
        builder.setOnlyAlertOnce(true);
        builder.setAutoCancel(true);
      if (hms.equals("00:00:00")){
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            //builder.setVibrate(new long[]{1000,1000,1000});
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
        managerCompat.notify(1,builder.build());

    }



    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        //inflate menu
        menu.clear();
        inflater.inflate(R.menu.eventmenu, menu);
        //in case we want to hide certain items in the future
        //menu.findItem(R.id.addevent).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();
        if (id == R.id.addevent) {
            //jump here
            NewEventFragment NewEventFragment = new NewEventFragment();
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,NewEventFragment);
            fragmentTransaction.commit();

        }

        return super.onOptionsItemSelected(item);
    }



}
