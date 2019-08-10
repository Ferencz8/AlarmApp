package countingsheep.alarm.ui.alarmLaunch;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.crashlytics.android.Crashlytics;

public class AlarmRingtonePlayer {
    private MediaPlayer mPlayer;
    private Context mContext;
    private float playerVolume=0;

    public AlarmRingtonePlayer(Context context) {
        mContext = context;
        initialize();
    }

    public void initialize() {
        try {
            mPlayer = new MediaPlayer();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void cleanup() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Uri toneUri, Integer volume) {
        try {
            if (mPlayer != null && !mPlayer.isPlaying()) {
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });

                if(volume == null) {
                    playerVolume = (float) getSystemAlarmVolume() / 100;
                }
                else{
                    playerVolume = (float) volume / 100;
                }
                mPlayer.setVolume(playerVolume, playerVolume);

                mPlayer.setDataSource(mContext, toneUri);
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.setLooping(true);
                mPlayer.prepareAsync();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private int getSystemAlarmVolume() {
        int systemAlarmVolume = 100;
        try {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            systemAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        }
        catch(Exception e){
            Crashlytics.logException(e);
        }
        return systemAlarmVolume;
    }

    public void setVolumne(int volume){
        if (mPlayer != null){
            float volumeAsFloat = (float)volume/100;
            mPlayer.setVolume(volumeAsFloat,volumeAsFloat);
        }
    }

    public void setVolumne(boolean increase){
        if (mPlayer != null){

            if(increase){
                if(playerVolume <= 1){
                    playerVolume += 0.2;
                }
            }
            else {
                if(playerVolume > 0){
                    playerVolume -= 0.2;
                }
            }

            mPlayer.setVolume(playerVolume,playerVolume);
        }
    }

    public void stop() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
        }
    }
}
