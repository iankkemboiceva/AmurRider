package amurrider.rider.com.amur.amurrider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmDelivery extends AppCompatActivity implements View.OnClickListener {
    LinearLayout btnnext;
    ProgressDialog pDialog;
    String params,amo,shipadr;
    TextView txamo,txshpadr,txcust,txnpaid;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);
        btnnext = (LinearLayout) findViewById(R.id.button2);
        txamo = (TextView) findViewById(R.id.txamo);
        txshpadr = (TextView) findViewById(R.id.txdest);
        txcust = (TextView) findViewById(R.id.txtcust);
        txnpaid = (TextView) findViewById(R.id.ntpaid);
        session = new SessionManagement(getApplicationContext());
        btnnext.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);

        if(getIntent().getExtras() != null) {
            params = getIntent().getExtras().getString("params");
            amo = getIntent().getExtras().getString("amo");
            shipadr = getIntent().getExtras().getString("shipaddr");
           String textcs = getIntent().getExtras().getString("cust");
           txcust.setText(textcs.toUpperCase());
            txamo.setText(amo+"Ksh");
            txshpadr.setText(shipadr);
            String strpd = session.getString("NTPAID");
            txnpaid.setText(strpd);

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button2){
            AlertDialog alertDialog = new AlertDialog.Builder(ConfirmDelivery.this).create();
            alertDialog.setTitle("Confirm Delivery");
            alertDialog.setMessage("Are you sure you wish to confirm delivery?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ConfirmDelivery(params+"/123344");
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        }
    }




    private void ConfirmDelivery(String params) {

        //   http://104.42.234.123:1234/amurcore/amur/rider/confirmdelivery/{orderassignRefId}/{customerSignature}
        pDialog.show();
        String endpoint= "confirmdelivery/";

        String urlparams = "";
        try {
            urlparams = endpoint+params;
            //SecurityLayer.Log("cbcurl",url);
            Log.v("Conf Delivery",urlparams);

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

                            Toast.makeText(
                                    getApplicationContext(),
                                    "You have successfully confirmed and ended the delivery",
                                    Toast.LENGTH_LONG).show();
finish();
                            Intent mIntent = new Intent(getApplicationContext(), HomeDrawerAct.class);

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
