package jp.co.deliv.android.cache;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

/**
 * イメージ画像のメモリキャッシュクラス。HashMapにURLをキーにBitmapイメージを格納しているだけの
 * シンプルなキャッシュ機構。キャッシュのクリアを入れていないため、あまりに大きくなりすぎると
 * メモリを逼迫してしまうため、時間によるキャッシュのクリアなど考慮が必要。
 * 
 * TODO: キャッシュをクリアする機構を備える
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
