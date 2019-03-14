package util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hsic.qp.R;

import java.util.HashMap;
import java.util.Map;

public class SoundUtil {
	public static SoundPool sp ;
	public static Map<Integer, Integer> suondMap;
	public static Context mContext;
	
	//
	public static void initSoundPool(Context context){
		mContext = context;
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
		suondMap = new HashMap<Integer, Integer>();
		suondMap.put(1, sp.load(context, R.raw.barcodebeep, 1));
	}
	
	public static  void play(){
		AudioManager am = (AudioManager)mContext.getSystemService(mContext.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 返回当前AudioManager对象的最大音量值
		float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);// 返回当前AudioManager对象的音量值
		float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
		
        sp.play(suondMap.get(1), //
        		volumnRatio, //
        		volumnRatio, //
                1, //
                0, //
                1);//
    }
	
	public static  void play(int sound, int number){
		AudioManager am = (AudioManager)mContext.getSystemService(mContext.AUDIO_SERVICE);

        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sp.play(suondMap.get(sound), //
        		audioCurrentVolume, //
        		audioCurrentVolume, //
                1, //
                number, //
                1);//
    }
	public static void pasue() {
		sp.pause(0);
	}
}
