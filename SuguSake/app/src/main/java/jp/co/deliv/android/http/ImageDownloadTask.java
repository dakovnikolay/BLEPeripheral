package jp.co.deliv.android.http;

import jp.co.deliv.android.cache.ImageMemoryCache;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private static final String LOG_TAG = "ImageDownloadTask";
	
    // アイコンを表示するビュー  
    private ImageView imageView = null;  
  
    // イメージの代わりに表示ている代替ビュー
    private View substitudeView = null;
    
    private boolean bResize = false;	// 画像のリサイズフラグ
    private int width = 128;			// リサイズ後の画像の幅
    private int height = 128;			// リサイズ後の画像の高さ

    /**
     * リサイズ後イメージ幅の取得
     * @return
     */
    public int getWidth() {
		return width;
	}

    /**
     * リサイズ後イメージ幅の設定
     * @param width
     */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * リサイズ後イメージ高さの取得
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * リサイズ後イメージ高さの設定
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	// コンストラクタ(リサイズ指定なし)  
    public ImageDownloadTask(ImageView imageView) {  
        this.imageView = imageView;
        this.bResize = false;
    }  

    // コンストラクタ(リサイズ指定あり)  
    public ImageDownloadTask(ImageView imageView, boolean bResize) {  
        this.imageView = imageView;
        this.bResize = bResize;
    }  

    // コンストラクタ(リサイズ指定あり)  
    public ImageDownloadTask(ImageView imageView, View subView, boolean bResize) {  
        this.imageView = imageView;
        this.substitudeView = subView;
        this.bResize = bResize;
    }
    
    
    @Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		/*
		 * ImageViewの代替ビューが設定されていとき、
		 * イメージのダウンロード前に代替ビューを表示してImageViewを消去しておく
		 */
		if(this.substitudeView != null) {
			this.substitudeView.setVisibility(View.VISIBLE);
			this.imageView.setVisibility(View.GONE);
		}
	}

    /**
     * イメージダウンロードタスクのデフォルト画像を作成する
     * @param width
     * @param height
     * @return
     */
    private Bitmap createDefaultImage(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.GRAY);
		return bitmap;
    }
    
	/**
     * バックグラウンドスレッドによりパラメータで渡されるURLのイメージ画像を取得する
     */
    @Override  
    protected Bitmap doInBackground(String... urls) {
        
    	// 引数が空文字列の場合は何もせずnullを返す
        if("".equals(urls[0].trim())) {
        	return createDefaultImage(width, height);
        }
        
        // キャッシュからイメージを取得
        Bitmap image = ImageMemoryCache.getImage(urls[0]);
        if(image == null) {
        	// キャッシュにヒットしなかった場合、URLから画像を取得する
        	image = HttpClient.getImage(urls[0]);
        	
        	// 指定するURLからデータを取得できなかった場合、Color.GRAYの代替画像を作成する
        	if(image == null) {
        		image = createDefaultImage(width, height);
        	}
        	
        	// メモリキャッシュにデータを保存
        	ImageMemoryCache.setImage(urls[0], image);
        }

        /*
         * リサイズ不要な場合はそのままイメージを返却する
         */
        if(bResize == false) {
        	return image;
        }
        
        /*
         * 画像を指定の大きさにリサイズする
         */
    	Bitmap resizeImage = null;
        if(image.getWidth() != width && image.getHeight() != height) {
        	int srcWidth = image.getWidth();
        	int srcHeight = image.getHeight();
        	
        	Log.d(LOG_TAG, "Src image width:" + srcWidth + " Src image height:" + srcHeight);
        	
        	Matrix scaleMat = new Matrix();
        	float scaleX = (float)width / (float)srcWidth;
        	float scaleY = (float)height / (float)srcHeight;
        	
        	Log.d(LOG_TAG, "Scale X:" + scaleX + " Scale Y:" + scaleY);
        	
        	scaleMat.postScale(scaleX, scaleY);
        	
        	// resize
        	resizeImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), scaleMat, true);
        	Log.d(LOG_TAG, "Resized image width:" + resizeImage.getWidth() + " Resized image height:" + resizeImage.getHeight());
        	
        } else {
        	resizeImage = image;
        }
        // イメージを返す
        return resizeImage;  
    }  
  
    /**
     * メインスレッドで実行する処理  
     */
    @Override  
    protected void onPostExecute(Bitmap result) {
    	if(result != null) {
    		this.imageView.setImageBitmap(result);
    	} else {
    		// TODO: このときの処理をどうするか。。。
    	}
    	
		/*
		 * ImageViewの代替ビューが設定されていとき、
		 * 代替ビューを消去してダウンロードしたイメージをセットしたImageViewを表示する
		 */
		if(this.substitudeView != null) {
			this.substitudeView.setVisibility(View.GONE);
			this.imageView.setVisibility(View.VISIBLE);
		}
    }  
}
