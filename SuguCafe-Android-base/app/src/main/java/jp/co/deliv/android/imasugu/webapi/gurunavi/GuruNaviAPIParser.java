package jp.co.deliv.android.imasugu.webapi.gurunavi;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;


/**
 * ぐるナビWEBサービスのレストラン検索APIをコールし、取得したXMLデータをパースする。
 * パースした結果は、ShopInfoクラスのリストとして返却する。
 * BRタグが含まれる場合は、改行に置換する。
 * @author Atsushi
 */
public class GuruNaviAPIParser implements ShopInfoAPIPaser {  

	private static final String RESULT_TAG = "response";
	private static final String SHOP_TAG = "rest";
	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	
	private static final String CATEGORY_TAG = "category";
	
	private static final String LATITUDE_TAG = "latitude";
	private static final String LONGITUDE_TAG = "longitude";

	private static final String SHOP_IMAGE_1_TAG = "shop_image1";
	private static final String SHOP_IMAGE_2_TAG = "shop_image2";
		
	private static final String ADDRESS_TAG = "address";
	
	private static final String TEL_TAG = "tel";
	private static final String FAX_TAG = "fax";
	
	private static final String OPEN_TIME_TAG = "opentime";
	private static final String HOLIDAY_TAG = "holiday";
	
	private static final String PR_SHORT_TAG = "pr_short";
	private static final String PR_LONG_TAG = "pr_long";
	
	private static final String BUDGET_TAG = "budget";
	//private static final String PC_COUPON_TAG = "pc_coupon";
	
	private static final String PC_URL_TAG = "url";
	private static final String MOBILE_URL_TAG = "url_mobile";
	//private static final String MOBILE_SITE_TAG = "mobile_site";
	private static final String MOBILE_COUPON_TAG = "mobile_coupon";
	
    private String urlStr;  
  
    public GuruNaviAPIParser(String urlStr) {  
        this.urlStr = urlStr;  
    }  
	
    private String getText(XmlPullParser paser) throws XmlPullParserException, IOException {
    	String text = paser.nextText();
    	text = text.replace("<BR>", "\n");
    	text = text.replace("<br>", "\n");
    	text = text.replace("<BR />", "\n");
    	text = text.replace("<br />", "\n");
    	return text;
    }
    
	public List<ShopInfo> parse() {
		
        List<ShopInfo> list = null;
        Stack<String> tagStack = new Stack<String>();
        XmlPullParser parser = Xml.newPullParser();  
        
        try {
        	/*
        	 * URLにアクセスしてXMLデータをbyte配列で取得する
        	 */
        	byte[] data = HttpClient.getByteArrayFromURL(urlStr);
        	
        	// データを取得できなかったときはnullを返す
        	if(data == null) {
        		return null;
        	}
        	
        	// XmlPullParserにデータをセットする
        	parser.setInput(new StringReader(new String(data, "UTF-8")));
        	int eventType = parser.getEventType();
        	ShopInfo currentShop = null;
        	boolean isFinished = false;

        	while (eventType != XmlPullParser.END_DOCUMENT && !isFinished) {
        		
	            String name = null;
	            //int depth = 0;
	            
	            switch (eventType) {
	            
	            case XmlPullParser.START_DOCUMENT:  
	                list = new ArrayList<ShopInfo>();  
	                break;  
	            
	            case XmlPullParser.START_TAG:

	            	// 開始タグを取得
	                name = parser.getName();
	                
	                // 階層を取得
	                //depth = parser.getDepth();
	                
	                // タグスタックに積む
	                tagStack.push(name);
	                
	                /*
	                 * タグ名に応じた処理
	                 */
	                if (name.equalsIgnoreCase(SHOP_TAG)) {
	                	// shopタグが開始されたらShopInfoクラスのインスタンスを作る
	                    currentShop = new ShopInfo();  
	                } else if (currentShop != null) {
	                    if (name.equalsIgnoreCase(ID_TAG)) {  
	                        currentShop.setId(getText(parser));
	                    } else if (name.equalsIgnoreCase(NAME_TAG)) {  
	                        currentShop.setName(getText(parser));  
	                    } else if (name.equals(ADDRESS_TAG)) {
	                    	currentShop.setAddress(getText(parser));
	                    } else if (name.equals(LATITUDE_TAG)) {
	                    	currentShop.setLatitude(Double.parseDouble(getText(parser)));
	                    } else if (name.equals(LONGITUDE_TAG)) {
	                    	currentShop.setLongitude(Double.parseDouble(getText(parser)));
	                    } else if (name.equals(PR_LONG_TAG)) {
	                    	currentShop.setPrShort(getText(parser));
	                    } else if (name.equals(PR_SHORT_TAG)) {
	                    	currentShop.setPrLong(getText(parser));
	                    } else if (name.equals(OPEN_TIME_TAG)) {
	                    	currentShop.setOpenHour(getText(parser));
	                    } else if (name.equals(ADDRESS_TAG)) {
	                    	currentShop.setAddress(getText(parser));
	                    } else if(name.equalsIgnoreCase(BUDGET_TAG)) {
                    		currentShop.setBudget(getText(parser));
	                    } else if(name.equalsIgnoreCase(SHOP_IMAGE_1_TAG)) {
	                    	currentShop.setShopImage1Url(getText(parser));
	                    } else if(name.equalsIgnoreCase(SHOP_IMAGE_2_TAG)) {
	                    	currentShop.setShopImage2Url(getText(parser));	                    	
	                    } else if(name.equalsIgnoreCase(PC_URL_TAG)) {
                    		currentShop.setShopUrlPC(getText(parser));
	                    } else if(name.equalsIgnoreCase(MOBILE_URL_TAG)) {
                    		currentShop.setShopUrlMobile(getText(parser));
	                    } else if(name.equalsIgnoreCase(MOBILE_COUPON_TAG)) {
	                    	currentShop.setMobileCouponFlg(getText(parser));
	                    } else if(name.equalsIgnoreCase(TEL_TAG)) {
	                    	currentShop.setTel(getText(parser));
	                    } else if(name.equalsIgnoreCase(FAX_TAG)) {
	                    	currentShop.setFax(getText(parser));
	                    } else if(name.equalsIgnoreCase(HOLIDAY_TAG)) {
	                    	currentShop.setHoliday(getText(parser));
	                    } else if(name.equalsIgnoreCase(CATEGORY_TAG)) {
	                    	currentShop.setCategory(getText(parser));
	                    }
	                }  
	                break;  
	            
	            case XmlPullParser.END_TAG:
	            	
	            	// 終了タグ名を取得
	                name = parser.getName();
	                
	                if (name.equalsIgnoreCase(SHOP_TAG) && currentShop != null) {  
	                	// shopタグの終わりの場合は、リストにShopInfoを追加する
	                    list.add(currentShop);
	                    currentShop = null;
	                } else if (name.equalsIgnoreCase(RESULT_TAG)) {
	                	// ルートノードの終わりの場合は、検索処理を終了する
	                    isFinished = true;
	                    
	                    // タグスタックは空になっているはず
	                    if(tagStack.empty()) {
	                    	Log.e("GNaviAPIParser", "ぐるナビWEBサービスレストラン検索APIのXML解析がおかしい。要調査。");
	                    }
	                }  
	                break;  
	            }  
	            eventType = parser.next();  
        	}  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	    return list;  
	}  
}
