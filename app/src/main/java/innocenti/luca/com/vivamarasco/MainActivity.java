package innocenti.luca.com.vivamarasco;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import static android.webkit.WebSettings.RenderPriority.HIGH;

public class MainActivity extends AppCompatActivity {

    private WebView wv;
    boolean doubleBackToExitPressedOnce = false;

    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        wv = (WebView) findViewById(R.id.webview1);
        //wv.loadUrl("file:///android_asset/www/index.html");
        //wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setRenderPriority(HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        final GestureDetector gd = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gl = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        };
        wv.setOnTouchListener(gl);



        if (savedInstanceState != null)
            wv.restoreState(savedInstanceState);
        else
            wv.loadUrl("file:///android_asset/www/index.html");


        // fa apparire le istruzioni solo al primo avvio
        // a seconda di come e' impostata la variabile silent
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);

        if (!(silent)){
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("silentMode", true);
                editor.commit();
                Log.d("preferenze","non ci sono");
        Toast.makeText(this, "Back per tornare alla discografia.\nClicca due volte BACK per uscire", Toast.LENGTH_LONG).show();

        }
        else
        {
            Log.d("preferenze","non ci sono");

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        wv.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        wv.loadUrl("file:///android_asset/www/index.html");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("", "DoubleTapEvent");
            wv.loadUrl("file:///android_asset/www/index.html");
            return true;
        }
    }
}
