package jp.co.deliv.android.imasugu.sugucafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

/**
 * Control tap on map to close info on map view
 * @author 120007HTT
 *
 */
public class TapControlledMapView extends MapView implements OnGestureListener {

	/**
	 * object to control action of mapview
	 */
    private GestureDetector gd;    
    
    /**
     * action on single click
     */
    private OnSingleTapListener singleTapListener;

	public TapControlledMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupGestures();
    }

    public TapControlledMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupGestures();
    }

    public TapControlledMapView(Context context, String apiKey) {
        super(context, apiKey);
        setupGestures();
    }
    
    /**
     * setup on click
     */
    private void setupGestures() {
    	gd = new GestureDetector(this);  
        //set the on Double tap listener  
    	OnDoubleTapListener g = new OnDoubleTapListener() {
			
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (singleTapListener != null) {
					return singleTapListener.onSingleTap(e);
				} else {
					return false;
				}
			}
			
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
			
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		};
        gd.setOnDoubleTapListener(g);
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.gd.onTouchEvent(ev)) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}
	
	public void setOnSingleTapListener(OnSingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}  
}


