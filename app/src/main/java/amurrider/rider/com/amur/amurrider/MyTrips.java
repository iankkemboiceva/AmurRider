package amurrider.rider.com.amur.amurrider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyTripsAdapter;
import adapter.TripDetails;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyTrips extends AppCompatActivity {
    ProgressDialog pDialog;
    ListView lv;
    List<TripDetails> tripDetails = new ArrayList<TripDetails>();
    SessionManagement session;
    MyTripsAdapter aAdpt;
    double amont = 0;
    TextView txo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);

       lv = (ListView) findViewById(R.id.lv);
        txo = (TextView) findViewById(R.id.textView1);
        session = new SessionManagement(getApplicationContext());

        String riderid = session.getString("RIDERID");
        FetchRiders(riderid);

      /*  aAdpt = new InboxListAdapter(planetsList, getActivity());


        lv.setAdapter(aAdpt);*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void FetchRiders(String params) {
        pDialog.show();

amont = 0;
        String endpoint= "fetchassignedorders/";

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
                         /*   {
                                "STATUS":"SUCCESS", "RIDER_ORDER_JSON":[{
                                "ORDER_ASSI_REF_ID":"RIORDCDFD8C821806", "RIDER_ID":
                                "RIDER525DC1455300", "ORDER_ID":"ORDID95098448426694", "ORDER_TYPE":
                                "1", "TXN_AMT":230, "SHIPPING_ADDRESS":
                                "taarifa suites nairobi", "CUSTOMER_NAME":
                                "Meshack Kipyegon", "MOBILE_NUMBER":"1234", "EMAIL_ID":
                                "ravi@cevaltd.com", "STATUS":"Shipped", "LAT":"-1.30109", "LONGT":
                                "36.81695222222223"
                            },{
                                "ORDER_ASSI_REF_ID":"RIORD54A4E6C52575", "RIDER_ID":
                                "RIDER525DC1455300", "ORDER_ID":"ORDID8957465C474804", "ORDER_TYPE":
                                "1", "TXN_AMT":760, "SHIPPING_ADDRESS":
                                "9west westlands nairobi", "CUSTOMER_NAME":
                                "Meshack Kipyegon", "MOBILE_NUMBER":"1234", "EMAIL_ID":
                                "ravi@cevaltd.com", "STATUS":"Shipped", "LAT":"-1.30109", "LONGT":
                                "36.81695222222223"
                            }]}*/
                            JSONArray js = obj.optJSONArray("RIDER_ORDER_JSON");
                            if (js.length() > 0) {


                                JSONObject json_data = null;
                                for (int i = 0; i < js.length(); i++) {
                                    try {

                                        json_data = js.getJSONObject(i);
                                        String lat = json_data.optString("lattitude");
                                        String orderid = json_data.optString("orderAssiRefId");
                                        String shworderid = json_data.optString("orderId");
                                        String longt = json_data.optString("longitute");
                                        String custname = json_data.optString("customerName");
                                        String txshipaddr = json_data.optString("shippingAddress");
                                        String mobnumb = json_data.optString("mobileNumber");
                                        String txnAmt = json_data.optString("txnAmt");
                                        String txntimest = json_data.optString("txnDate");
                                        String paystat = json_data.optString("paymentStatus");
double txnamo = Double.parseDouble(txnAmt);
amont += txnamo;
                                        tripDetails.add(new TripDetails(custname,txshipaddr,txnAmt,mobnumb,orderid,longt,lat,shworderid,txntimest,paystat));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                aAdpt = new MyTripsAdapter(tripDetails, getApplicationContext());


                                lv.setAdapter(aAdpt);

                            } else {

                                Toast.makeText(
                                        getApplicationContext(),
                                        errmess,
                                        Toast.LENGTH_LONG).show();


                            }
txo.setText("Total Trips Amount "+Double.toString(amont)+ " Kshs");
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();


                        }
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
