package edu.fsu.cs.goodtiming.Timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Locale;
import edu.fsu.cs.goodtiming.R;


public class BreakDialog extends DialogFragment {
    private TextView breakTime;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 300000;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_break, container, false);

        breakTime=rootView.findViewById(R.id.txtBreakTimer);
        startTimer();

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById

        return rootView;
    }

    // Starts the Timer on the Session fragment
    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning=false;
            }
        }.start();
        timerRunning = true;
    }

    // Update the text on the timer
    private void updateCountDownText(){
        int minutes = (int)(timeLeftInMillis/1000)/60;
        int seconds = (int)(timeLeftInMillis/1000)%60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);
        breakTime.setText(timeLeftFormatted);
    }

}
