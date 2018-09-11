package amurrider.rider.com.amur.amurrider;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by brian on 05/10/2016.
 */
public class ApplicationClass  extends MultiDexApplication {



   // private ObjectGraph mObjectGraph;
    @Override
    public void onCreate() {
        super.onCreate();



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/segoeuiregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        MultiDex.install(this);
      //  initObjectGraph(new FingerprintModule(this));
    }

   /* @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        MultiDex.install(this);
    }*/

    /**
     * Initialize the Dagger module. Passing null or mock modules can be used for testing.
     *
     * @param module for Dagger
     */

}