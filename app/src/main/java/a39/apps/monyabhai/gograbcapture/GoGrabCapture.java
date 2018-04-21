package a39.apps.monyabhai.gograbcapture;


import android.app.Application;
import android.support.multidex.MultiDex;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class GoGrabCapture
        extends Application
{
    public void onCreate()
    {
        super.onCreate();
        MultiDex.install(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Object localObject = new Picasso.Builder(this);
        ((Picasso.Builder)localObject).downloader(new OkHttp3Downloader(this, 2147483647L));
        localObject = ((Picasso.Builder)localObject).build();
        ((Picasso)localObject).setIndicatorsEnabled(true);
        ((Picasso)localObject).setLoggingEnabled(true);
        Picasso.setSingletonInstance((Picasso)localObject);
    }
}