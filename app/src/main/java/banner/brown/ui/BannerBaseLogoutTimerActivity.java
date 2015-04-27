package banner.brown.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import banner.brown.BannerApplication;
import banner.brown.ui.R;

public class BannerBaseLogoutTimerActivity extends ActionBarActivity {

    LogoutCountdownTimer mCountdownTimer;

    public static long startTime=20*60*1000; // 60 sec IDLE TIME
    private final long interval = 5 * 1000; //5 second toasts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BannerApplication.curCookie == "") {
            logUserOut();
            finish();
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean shouldLogOut = BannerApplication.getShouldLogOut();
        if (shouldLogOut) {
            logUserOut();
            finish();
        }
        mCountdownTimer = new LogoutCountdownTimer(startTime, interval);
        mCountdownTimer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BannerApplication.updateLastActive();
        mCountdownTimer.cancel();

    }

    @Override
    public void onUserInteraction(){

        super.onUserInteraction();

        //Reset the timer on user interaction...
        mCountdownTimer.cancel();
        mCountdownTimer.start();
    }

    public class LogoutCountdownTimer extends CountDownTimer {
        public LogoutCountdownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            logUserOut();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished < 20000) {
                CharSequence text = "You will be logged out in " + (millisUntilFinished / 1000) + " seconds";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(BannerBaseLogoutTimerActivity.this, text, duration);
                toast.show();
            }
        }
    }

    protected void logUserOut(){
        BannerApplication.removeUserCookie();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

}
