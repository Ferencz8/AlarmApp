package countingsheep.alarm.ui.roasts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import countingsheep.alarm.R;
import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.domain.enums.Feature;
import countingsheep.alarm.core.domain.enums.FeatureReaction;
import countingsheep.alarm.core.services.UserServiceImpl;
import countingsheep.alarm.core.services.interfaces.UserService;

public class LockedFeatureDialog {
    private Context context;
    private UserService userService;
    private Feature feature;

    public LockedFeatureDialog(Context context, UserService userService, Feature feature) {
        this.context = context;
        this.userService = userService;
        this.feature = feature;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_locked_feature, null);

        final ImageView like = view.findViewById(R.id.like);
        final ImageView dislike = view.findViewById(R.id.dislike);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).setCancelable(true);

        AlertDialog alert = builder.create();

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                userService.addFeatureReaction(feature, FeatureReaction.NotReacted, new OnResult() {
                    @Override
                    public void onSuccess(Object result) {

                    }
                    @Override
                    public void onFailure(String response){
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        like.setOnClickListener(v -> {
            this.userService.addFeatureReaction(feature, FeatureReaction.Like, new OnResult() {
                @Override
                public void onSuccess(Object result) {

                }
                @Override
                public void onFailure(String response){
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                }
            });
            alert.dismiss();
        });

        dislike.setOnClickListener(v -> {
            this.userService.addFeatureReaction(feature, FeatureReaction.DisLike, new OnResult() {
                @Override
                public void onSuccess(Object result) {

                }
                @Override
                public void onFailure(String response){
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                }
            });
            alert.dismiss();
        });

        Window window = alert.getWindow();
        if (window != null) window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }
}
