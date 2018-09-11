package amurrider.rider.com.amur.amurrider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    LinearLayout btnnext;
    ProgressDialog pDialog;
    EditText edus,edpin;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);
        session = new SessionManagement(getApplicationContext());
        btnnext = (LinearLayout) findViewById(R.id.button2);
        edus = (EditText) findViewById(R.id.userid);

        btnnext.setOnClickListener(this);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button2){
           /* finish();
            Intent mIntent = new Intent(getApplicationContext(), HomeDrawerAct.class);

            startActivity(mIntent);*/
            String username = edus.getText().toString();
session.setString(SessionManagement.KEY_MOBILENO,username);
            if(Utility.isNotNull(username)) {

                    String params = username;
                    GenOTP(params);

            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "Please enter a valid value for Username",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void GenOTP(String params) {


        pDialog.show();
        String endpoint= "generateOtp/";

        String urlparams = "";
        try {
            urlparams = endpoint+params;
            //SecurityLayer.Log("cbcurl",url);

        } catch (Exception e) {
            Log.e("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object
                    Log.v("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());

                    Log.v("decrypted_response", obj.toString());
                    //    {"CUST_LAST_NAME":"Dirisam","STATUS":"SUCCESS","RIDER_ID":"RIDER525DC1455300","EMAIL":"ravi0336@gmail.com","CUST_FIRST_NAME":"Pritha","CUST_STATUS":"A"}

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("STATUS");
                    String errmess = obj.optString("ERROR_MSG");


                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if ( Utility.isNotNull(responsemessage)) {
                        Log.v("Response Message", responsemessage);

                        if (responsemessage.equals("SUCCESS")) {


                            Intent mIntent = new Intent(getApplicationContext(), ResetPasswordOTP.class);

                            startActivity(mIntent);
                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    errmess,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    Log.v("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    Log.v("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed

                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }
        });

    }
}
