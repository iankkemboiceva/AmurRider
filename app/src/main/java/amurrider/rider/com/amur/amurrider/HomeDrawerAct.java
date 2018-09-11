package amurrider.rider.com.amur.amurrider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.MyPendingOrders;
import adapter.MyTripsAdapter;
import adapter.NavDrawerItemSignIn;
import adapter.TripDetails;
import butterknife.OnClick;
import notifications.MyHandler;
import notifications.NotificationSettings;
import notifications.RegistrationIntentService;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeDrawerAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, FragmentDrawer.FragmentDrawerListener,GoogleMap.OnMarkerClickListener, View.OnClickListener {
    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(-1.262009, 36.804031);
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ProgressDialog pDialog;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private FragmentDrawer drawerFragment;
    LinearLayout layoutBottomSheet,lymapsfrag,lymapsview,lyorderview;
    BottomSheetBehavior sheetBehavior;
    SessionManagement session;
    RelativeLayout rlinst;
    String txtcust,txmobno,txshipaddr,fntxtorderid;
    TextView txarea,txcust,txmobnumb,txamo;
    ActionBarDrawerToggle toggle;
    Button btncall,btndeliver;
    String waypoint = "&waypoints=";
   String cust ,txnamt ,shippadr, mobnumb,finlatit,finlongit,finlat ;
   ImageView imgvw;
    List<TripDetails> tripDetails = new ArrayList<TripDetails>();
    MyPendingOrders aAdpt;
    ImageView ivclose;
    ListView lv;

    View v1,v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);
        session = new SessionManagement(getApplicationContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        lymapsview = (LinearLayout) findViewById(R.id.lymapsvw);
        lyorderview = (LinearLayout) findViewById(R.id.lyorder);
        rlinst = (RelativeLayout) findViewById(R.id.rllistinterv);
        v1 = (View) findViewById(R.id.v1);
        v2 = (View) findViewById(R.id.v2);
        lymapsfrag = (LinearLayout) findViewById(R.id.lymaps);
        lymapsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlinst.setVisibility(View.GONE);
                lymapsfrag.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
            }
        });

        lyorderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlinst.setVisibility(View.VISIBLE);
                lymapsfrag.setVisibility(View.GONE);

                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
            }
        });
        ivclose = (ImageView) findViewById(R.id.close);


        txarea = (TextView) findViewById(R.id.txarea);
        txcust = (TextView) findViewById(R.id.txtcust);
        txmobnumb = (TextView) findViewById(R.id.mobnumb);
        txamo = (TextView) findViewById(R.id.txamo);






        lv = (ListView) findViewById(R.id.lv);
        btncall = (Button) findViewById(R.id.callbutt);
        btndeliver = (Button) findViewById(R.id.deliverbutt);
        imgvw = (ImageView) findViewById(R.id.imgvw);
        btndeliver.setOnClickListener(this);
        btncall.setOnClickListener(this);
        imgvw.setOnClickListener(this);
       // txarea.setText("Changed Text");
       sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                      //  btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
toggleBottomSheet();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("RIDER");
        toolbar.setNavigationIcon(R.drawable.hamburger);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.hamburger);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.hamburger);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //   drawerFragment.setArguments(bundle);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int clickCount, long l) {
        cust = tripDetails.get(clickCount).getCust();
        txnamt = tripDetails.get(clickCount).getAmo();
        shippadr = tripDetails.get(clickCount).getShippAdr();
        mobnumb = tripDetails.get(clickCount).getMobno();
        fntxtorderid = tripDetails.get(clickCount).getOrderId();
        finlatit = tripDetails.get(clickCount).getOrderId();
        finlat = tripDetails.get(clickCount).getLatit();
        finlongit = tripDetails.get(clickCount).getLongt();
        txmobno = mobnumb;
        InitDelivery(fntxtorderid);
    }
});
        pDialog.setCancelable(false);

        String riderid = session.getString("RIDERID");
        FetchRiders(riderid);


        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, MyHandler.class);
        registerWithNotificationHubs();
       // ShowRoute();
    }
    public void registerWithNotificationHubs()
    {
        Log.i("Registering", " Registering with Notification Hubs");

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                Toast.makeText(
                        getApplicationContext(),
                        "This device is not supported by Google Play Services.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;


            case android.R.id.home:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                    drawer.openDrawer(drawer);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(HomeDrawerAct.this, MyTrips.class);
                startActivity(intent);
                break;
            case 1:
                Intent intentb = new Intent(HomeDrawerAct.this, ChangePassword.class);
                startActivity(intentb);
                break;
            case 2:
                session.setString("LOGGEDIN","N");
                Intent intenta = new Intent(getApplicationContext(), LoginActivity.class);
                intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intenta);
                break;
            default:
                break;
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
          //      {"STATUS":"SUCCESS","RIDER_ORDER_JSON":[{"ORDER_ASSI_REF_ID":"RIORDCDFD8C821806","RIDER_ID":"RIDER525DC1455300","ORDER_ID":"ORDID95098448426694","ORDER_TYPE":"1","TXN_AMT":230,"SHIPPING_ADDRESS":"taarifa suites nairobi","CUSTOMER_NAME":"Meshack Kipyegon","MOBILE_NUMBER":"1234","EMAIL_ID":"ravi@cevaltd.com","STATUS":"Shipped","LAT":"-1.30109","LONGT":"36.81695222222223"},{"ORDER_ASSI_REF_ID":"RIORD54A4E6C52575","RIDER_ID":"RIDER525DC1455300","ORDER_ID":"ORDID8957465C474804","ORDER_TYPE":"1","TXN_AMT":760,"SHIPPING_ADDRESS":"9west westlands nairobi","CUSTOMER_NAME":"Meshack Kipyegon","MOBILE_NUMBER":"1234","EMAIL_ID":"ravi@cevaltd.com","STATUS":"Shipped","LAT":"-1.30109","LONGT":"36.81695222222223"}]}
                Integer clickCount = (Integer) marker.getTag();
                if (clickCount != null) {

                    cust = tripDetails.get(clickCount).getCust();
                    txnamt = tripDetails.get(clickCount).getAmo();
                    shippadr = tripDetails.get(clickCount).getShippAdr();
                     mobnumb = tripDetails.get(clickCount).getMobno();
                   fntxtorderid = tripDetails.get(clickCount).getOrderId();
                    finlatit = tripDetails.get(clickCount).getOrderId();
                    finlat = tripDetails.get(clickCount).getLatit();
                    finlongit = tripDetails.get(clickCount).getLongt();
                    txmobno = mobnumb;
                    txarea.setText(shippadr.toUpperCase());
                    txcust.setText("DELIVER TO: "+cust.toUpperCase());
                    txamo.setText(txnamt+"KSHS");
                    txmobnumb.setText("MOBILE NUMBER: "+mobnumb);
                    toggleBottomSheet();
Log.v("Click Count Number",Integer.toString(clickCount));
                }
                return false;
            }
        });
        // Add a marker in Sydney, Australia, and move the camera.

    }
    private void FetchRiders(String params) {
pDialog.show();

tripDetails.clear();
        String endpoint= "fetchpendingassignedorders/";

        String urlparams = "";
        try {
            urlparams = endpoint+params;
           Log.v("cbcurl",urlparams);

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
String finallongt = "";
String finallat = "";

                                JSONObject json_data = null;
                                int jslength = js.length()-1;
                                for (int i = 0; i < js.length(); i++) {
                                    try {

                                        json_data = js.getJSONObject(i);
                                        String lat = json_data.optString("lattitude");
                                        String orderid = json_data.optString("orderAssiRefId");
                                        String shworderid = json_data.optString("orderId");
                                        String longt = json_data.optString("longitute");
                                        String custname = json_data.optString("customerName");
                                        txshipaddr = json_data.optString("shippingAddress");
                                        String mobnumb = json_data.optString("mobileNumber");
                                        String txnAmt = json_data.optString("txnAmt");
                                        String txntimest = json_data.optString("txnDate");
                                        tripDetails.add(new TripDetails(custname,txshipaddr,txnAmt,mobnumb,orderid,longt,lat,shworderid,txntimest));


                                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(longt))).title(custname).icon(BitmapDescriptorFactory.fromResource(R.drawable.markerone))).setTag(i);
                                       if(i < jslength && i <8) {
                                           waypoint += "via:" + lat + "%2C" + longt + "%7C";
                                       }
                                       if(i == jslength){
                                           finallat = lat;
                                           finallongt = longt;
                                       }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                                aAdpt = new MyPendingOrders(tripDetails, HomeDrawerAct.this);


                                lv.setAdapter(aAdpt);
                                LatLng from = new LatLng(Double.parseDouble(finallat),Double.parseDouble(finallongt));
                                LatLng origin = new LatLng(-1.261457,36.803493);
                                String url = getDirectionsUrl(origin, from);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);
                            } else {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "There are no orders assigned to you at this moment",
                                        Toast.LENGTH_LONG).show();


                            }

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



    @Override
    public boolean onMarkerClick(Marker marker) {
        toggleBottomSheet();

        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.callbutt){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", txmobno, null));
            startActivity(intent);
        }
        if(view.getId() == R.id.deliverbutt){
           /* Intent intent = new Intent(HomeDrawerAct.this, PickUpActivity.class);
            startActivity(intent);*/
           InitDelivery(fntxtorderid);
        }
        if(view.getId() == R.id.imgvw){
            String riderid = session.getString("RIDERID");
            FetchRiders(riderid);
        }
    }



    private void InitDelivery(String params) {
        pDialog.show();


        String endpoint= "initiatedelivery/";

        String urlparams = "";
        try {
            urlparams = endpoint+params;
            Log.v("init del url",urlparams);

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
                          //  String cust ,txnamt ,shippadr, mobnumb ;
                            Intent intent = new Intent(HomeDrawerAct.this, PickUpActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("cust", cust);
                            mBundle.putString("txamo", txnamt);
                            mBundle.putString("shippadr", shippadr);
                            mBundle.putString("mobnumb", mobnumb);
                            mBundle.putString("orderid", fntxtorderid);
                            mBundle.putString("lat", finlat);
                            mBundle.putString("long", finlongit);
                            intent.putExtras(mBundle);
                            startActivity(intent);

                        } else {

                            Toast.makeText(
                                    getApplicationContext(), responsemessage,
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


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           ParserTask parserTask = new ParserTask();
            Log.d("Result Gotten", result);

            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            try {
                mMap.addPolyline(lineOptions);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
      //  String waypoint = "&waypoints=via:36.787635%2C-1.255098%7Cvia:36.791272%2C-1.250722";
        // Output format


        String output = "json";
waypoint = waypoint.substring(0,waypoint.length()-3);
Log.v("Final Waypoint URl",waypoint);
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+waypoint;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
  /*  public  void ShowRoute(){
        GoogleDirection.withServerKey("AIzaSyCfyushkdxfQh4ixcIcpbEMyPR7cxkl7kY")

                .from(new LatLng(36.803493, -1.261457))
                .to(new LatLng(36.805499 ,-1.264686))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            // Do something
                        } else {
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }*/

}
