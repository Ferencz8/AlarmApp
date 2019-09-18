package countingsheep.alarm.ui.roasts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import countingsheep.alarm.R;

public class LockedFeatureDialog {
    private Context context;

    public LockedFeatureDialog(Context context) {
        this.context = context;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_locked_feature, null);

        final ImageView like = view.findViewById(R.id.like);
        final ImageView dislike = view.findViewById(R.id.dislike);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).setCancelable(true);

        AlertDialog alert = builder.create();

        like.setOnClickListener(v -> {
            alert.dismiss();
            //TODO send to server event
        });

        dislike.setOnClickListener(v -> {
            alert.dismiss();
            //TODO send to server event
        });

        Window window = alert.getWindow();
        if (window != null) window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }
}
