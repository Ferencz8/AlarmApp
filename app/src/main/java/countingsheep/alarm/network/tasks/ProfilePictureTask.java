package countingsheep.alarm.network.tasks;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import countingsheep.alarm.R;

public class ProfilePictureTask extends AsyncTask<String, Void, String> {

    private boolean enforce = false;
    private ProfilePictureTaskResponse listener;
    private String fileName = "photo.jpg";
    private String sdPath;
    private String url;

    /** Constructor */
    public ProfilePictureTask(@NonNull Context context, @NonNull String url, @NonNull ProfilePictureTaskResponse listener) {
        this.sdPath = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/";
        File countingSheepFolder =  new File(context.getResources().getString(R.string.app_name));
        if (! countingSheepFolder.exists()) {
            countingSheepFolder.mkdirs();
        }
        this.listener = listener;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {



        /* setup destination directory */
        File directory = new File(this.sdPath + "temp");
        if (! directory.exists()) {
            directory.mkdirs();
        }

        /* setup file name */
//        String[] parts = this.url.split("/");
//        this.fileName = parts[parts.length - 1];
    }

    @Override
    protected String doInBackground(String... arguments) {
        File file = new File(this.sdPath + "temp", this.fileName);
        if(file.exists() && this.enforce) {file.delete();}
        if (! file.exists()) {
            try {
                URLConnection conn = new URL(this.url).openConnection();
                conn.connect();
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(file);
                byte[] b = new byte[1024]; int c;
                while ((c = in.read(b)) != -1) {out.write(b, 0, c);}
                out.close();
                in.close();
            } catch (IOException e) {
                Log.e("ProfileImageTask", e.getMessage());
            }
        }

        return file.getAbsolutePath();
    }

    @Override
    protected void onPostExecute(String imagePath) {
        if (listener != null && imagePath != null) {
            this.listener.OnImageAvailable(imagePath);
        }
    }
}
