package amurrider.rider.com.amur.amurrider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
SignaturePad mSignaturePad;
String orderid,flname,amo,shipadr,txtcust;
    private Button mClearButton;
    private Button mSaveButton;
    SessionManagement session;
    File filename;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);
        session = new SessionManagement(getApplicationContext());
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
         //       Toast.makeText(getApplicationContext(),"Finally signed",Toast.LENGTH_LONG);
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
         //       Toast.makeText(getApplicationContext(),"Finally cleared",Toast.LENGTH_LONG);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
mClearButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                Toast.makeText(getApplicationContext(),"Inside clear button",Toast.LENGTH_LONG);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*  Toast.makeText(getApplicationContext(),"Inside save button",Toast.LENGTH_LONG);
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                 //   Toast.makeText(MainActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }*/

                String params = orderid+"/123344";
                Log.v("Conf Params",params);
                ConfirmDelivery(params);
               /*

                Intent mIntent = new Intent(getApplicationContext(), ConfirmDelivery.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("params", params);
                mBundle.putString("amo", amo);
                mBundle.putString("shipaddr", shipadr);
                mBundle.putString("cust", txtcust);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);*/
            }
        });


        if(getIntent().getExtras() != null) {

            orderid = getIntent().getExtras().getString("orderid");
            amo = getIntent().getExtras().getString("amo");
            shipadr = getIntent().getExtras().getString("shipaddr");
            txtcust = getIntent().getExtras().getString("cust");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();

        UploadSignature();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {

String riderid = session.getString("RIDERID");
flname = String.format(riderid+"Signature_%d", System.currentTimeMillis());
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format(riderid+"Signature_%d.jpg", System.currentTimeMillis()));
            filename = photo;
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        MainActivity.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onClick(View view) {
if(view.getId() == R.id.clear_button){
    Toast.makeText(getApplicationContext(),"Inside clear button",Toast.LENGTH_LONG);
}
    }

    public void UploadSignature(){

        pDialog.show();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

// Change base URL to your upload server URL.
        ApiInterface apiService = new Retrofit.Builder()

                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://104.42.234.123/").client(client).build().create(ApiInterface.class);



        File file = filename;

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "name");

        retrofit2.Call<String> req = apiService.postImage(body, name);
        req.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Do Something
                try {
               /* Log.v("response..:", response.body());
                Log.v("Success","Success");

                    JSONObject obj = new JSONObject(response.body());
                    String restatus = obj.optString("status");
                    String resmess = obj.optString("respmess");
                    if(restatus.equals("1")){*/
                        Toast.makeText(MainActivity.this, "Successfully saved Signature", Toast.LENGTH_SHORT).show();
                        String params = orderid+"/"+flname;
                        Log.v("Conf Params",params);
                        ConfirmDelivery(params);

                    Intent mIntent = new Intent(getApplicationContext(), ConfirmDelivery.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("params", params);
                    mBundle.putString("amo", amo);
                    mBundle.putString("shipaddr", shipadr);
                    mBundle.putString("cust", txtcust);
mIntent.putExtras(mBundle);
                    startActivity(mIntent);

/*
                    }else if(restatus.equals("0")){
                        Toast.makeText(MainActivity.this, resmess, Toast.LENGTH_SHORT).show();
                    }*/
                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Log.v("Fail","Fail");
                pDialog.dismiss();
            }
        });

    }



    private void ConfirmDelivery(final String params) {

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


                           /* Intent mIntent = new Intent(getApplicationContext(), ResetPasswordOTP.class);

                            startActivity(mIntent);*/

                            Intent mIntent = new Intent(getApplicationContext(), ConfirmDelivery.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("params", params);
                            mBundle.putString("amo", amo);
                            mBundle.putString("shipaddr", shipadr);
                            mBundle.putString("cust", txtcust);
                            mIntent.putExtras(mBundle);
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
