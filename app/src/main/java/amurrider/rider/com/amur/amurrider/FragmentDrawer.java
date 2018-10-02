package amurrider.rider.com.amur.amurrider;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.NavDrawerItemSignIn;
import adapter.NavDrawerPOJO;
import adapter.NavigationDrawerAdapter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class FragmentDrawer extends Fragment implements View.OnClickListener {
    File finalFile;

    TextView tv,home,tvmobno,tvlastl;

    Button lyhomeid,lysignout;
    RelativeLayout header;
    //  private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    static public List<NavDrawerPOJO> planetsList = new ArrayList<NavDrawerPOJO>();
    private RecyclerView recyclerView;
    ProgressDialog prgDialog,prgDialog2;
    public static TypedArray navMenuIcons,navIcpad;
    ImageView ivclose;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    ImageView imgclose;
    SessionManagement session;
    RelativeLayout rltransf,rlperf,rlccare,rlvsshop;
    private  static  String[] titles = null;
    private  static int [] dricons = null;
    private static ArrayList<String> vd = new ArrayList<String>();
    protected static ArrayList<Integer> vimges = new ArrayList<Integer>();
    protected static ArrayList<Integer> vicpad = new ArrayList<Integer>();
    private static Integer[] icons = null;
    private static Integer[] iconspad = null;
    String uploadFileName = null,userChoosenTask;
    int SELECT_FILE = 345;
    int REQUEST_CAMERA =3293;
    TextView txcust;
    private FragmentDrawerListener drawerListener;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItemSignIn> getData() {
        List<NavDrawerItemSignIn> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < vd.size(); i++) {
            NavDrawerItemSignIn navItem = new NavDrawerItemSignIn(vd.get(i),vimges.get(i),vicpad.get(i));

            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManagement(getActivity());
        vd.clear();
        vd.add("MY TRIPS");
      /*  vd.add("Deposit");
        vd.add("Transfers");

        vd.add("Cash Withdrawal");
        vd.add("Pay Bills");
        vd.add("Airtime Top Up");
        vd.add("Open Account");*/

        vd.add("CHANGE PIN");
        vd.add("LOG OUT");









        vimges.clear();
        vimges.add(0);
        vimges.add(0);
        vimges.add(0);




        vicpad.clear();
        vicpad.add(0);
       /* vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);*/
        vicpad.add(0);
        vicpad.add(0);


        //   CheckServicesBool();

//CheckServicesBool();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.drawer, container, false);


        //recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        //This is the code to provide a sectioned list
        header = (RelativeLayout) layout.findViewById(R.id.nav_header_container);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        txcust = (TextView) layout.findViewById(R.id.custname);
        imgclose = (ImageView) layout.findViewById(R.id.imgclose);
        txcust.setText(session.getString("FIRSTNAME")+" "+session.getString("LASTNAME"));
        recyclerView.setAdapter(adapter);
        //recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        imgclose.setOnClickListener(this);
        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //  invalidateOptionsMenu();

                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
    public void showDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");


        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                        } else if (which == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        }
                    }
                });
        builderSingle.show();
    }
    public Dialog imageAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity()).setAdapter(new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_list_item_1,
                        new String[] { "Camera", "Gallery" }),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                        } else if (which == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        }
                    }
                });

        return builder.create();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result) {
                        boolean camresult= Utility.checkCameraPermission(getActivity());
                        if(camresult) {
                            cameraIntent();
                        }

                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo")) {

                        cameraIntent();

                    }
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }





        //    iv.setImageBitmap(bm);




    @Override
    public void onClick(View view) {

      /*  if(view.getId() == R.id.rl4y){

            drawerListener.onDrawerItemSelected(view, 3);
            mDrawerLayout.closeDrawer(containerView);
        }*/
        if(view.getId() == R.id.imgclose){


            mDrawerLayout.closeDrawer(containerView);
        }


    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }





}
