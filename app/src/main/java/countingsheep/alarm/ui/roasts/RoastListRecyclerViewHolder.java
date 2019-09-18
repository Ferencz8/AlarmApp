package countingsheep.alarm.ui.roasts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import countingsheep.alarm.R;

public class RoastListRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView roastHour;
    private TextView roastDate;
    private TextView contentView = null;
    private ImageView playVoiceoverImageView = null;
    private ConstraintLayout roastLayout = null;
    private ImageView shareImageView = null;
    private ImageView downloadImageView = null;

    private boolean isClicked = true;

    public RoastListRecyclerViewHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            roastHour = (TextView)itemView.findViewById(R.id.roastHour);
            roastDate = (TextView)itemView.findViewById(R.id.roastDate);
            contentView = (TextView)itemView.findViewById(R.id.roastContentId);
            playVoiceoverImageView = (ImageView) itemView.findViewById(R.id.playRoast);
            roastLayout = (ConstraintLayout) itemView.findViewById(R.id.roastItemLayout);
            shareImageView = (ImageView) itemView.findViewById(R.id.shareRoast);
            downloadImageView = (ImageView) itemView.findViewById(R.id.downloadRoast);
        }
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public ConstraintLayout getRoastLayout() {
        return roastLayout;
    }

    public void setRoastLayout(ConstraintLayout roastLayout) {
        this.roastLayout = roastLayout;
    }

    public TextView getRoastHour() {
        return roastHour;
    }

    public void setRoastHour(TextView roastHour) {
        this.roastHour = roastHour;
    }

    public TextView getRoastDate() {
        return roastDate;
    }

    public void setRoastDate(TextView roastDate) {
        this.roastDate = roastDate;
    }

    public TextView getContentView() {
        return contentView;
    }

    public void setContentView(TextView contentView) {
        this.contentView = contentView;
    }

    public ImageView getPlayVoiceoverImageView() {
        return playVoiceoverImageView;
    }

    public void setPlayVoiceoverImageView(ImageView playVoiceoverImageView) {
        this.playVoiceoverImageView = playVoiceoverImageView;
    }

    public ImageView getShareImageView() {
        return shareImageView;
    }

    public void setShareImageView(ImageView shareImageView) {
        this.shareImageView = shareImageView;
    }

    public ImageView getDownloadImageView() {
        return downloadImageView;
    }

    public void setDownloadImageView(ImageView downloadImageView) {
        this.downloadImageView = downloadImageView;
    }
}
