package jp.co.deliv.android.cache;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

/**
 * �C���[�W�摜�̃������L���b�V���N���X�BHashMap��URL���L�[��Bitmap�C���[�W���i�[���Ă��邾����
 * �V���v���ȃL���b�V���@�\�B�L���b�V���̃N���A�����Ă��Ȃ����߁A���܂�ɑ傫���Ȃ肷�����
 * ��������N�����Ă��܂����߁A���Ԃɂ��L���b�V���̃N���A�ȂǍl�����K�v�B
 * あああああああ
 *
 * TODO: �L���b�V�����N���A����@�\�������
 * 
 * @author DLV4002
 */
public class ImageMemoryCache {
	
    private static HashMap<String,Bitmap> cache = new HashMap<String,Bitmap>();  
      
    public static Bitmap getImage(String key) {  
        if (cache.containsKey(key)) {  
            Log.d("cache", "cache hit!");  
            return cache.get(key);  
        }  
        return null;  
    }  
      
    public static void setImage(String key, Bitmap image) {  
        cache.put(key, image);  
    }
}
