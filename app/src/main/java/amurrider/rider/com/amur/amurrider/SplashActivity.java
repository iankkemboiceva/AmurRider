package amurrider.rider.com.amur.amurrider;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {
    SessionManagement session;
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManagement(this);




        if(!(Utility.isEmulator())) {
            if(!(Utility.isRooted())) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        Intent i = null;
                        //      session.logoutUser();


                        String chlog = session.getString("LOGGEDIN");
                        // boolean checktpref = session.isReg();


                            if (Utility.isNotNull(chlog)&& chlog.equals("Y")){
                                i = new Intent(SplashActivity.this, HomeDrawerAct.class);
                            }else{
                                i = new Intent(SplashActivity.this, LoginActivity.class);
                            }




                        startActivity(i);
                        finish();

                        //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }


                }, SPLASH_TIME_OUT);
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "You have currently rooted your device hence cant access this app"
                        , Toast.LENGTH_LONG).show();
                finish();
            }
        }else{
            Toast.makeText(
                    getApplicationContext(),
                    "You are currently using a mobile Emulator which is not acceptable."
                    , Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
