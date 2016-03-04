package jp.co.deliv.android.geocoding;

/**
 * 緯度経度などを用いたジオコーディングにおけるユーティリティクラス。
 * ２点の緯度経度から、２点間の距離を求める、緯度経度から住所に変換する等の便利メソッドが定義されている。
 * @author Atsushi
 */
public class GeoCodingUtils {

    // 長半径(WGS84)
    private static final double a = 6378137D;

    // 扁平率(WGS84)
    private static final double f = 1D / 298.257222101D;
    
	/**
	 * ヒュベニの公式を使って２点間の距離を求める
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return ２点間の距離（単位：メートル）
	 * 
	 * @see http://www.kashmir3d.com/kash/manual-e/std_siki.htm
	 * @see http://vldb.gsi.go.jp/sokuchi/surveycalc/algorithm/ellipse/ellipse.htm
	 */
	public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
		
        //緯度経度をラジアンに変換
        double radLatStart = latitude1 * Math.PI/180D;
        double radLonStart = longitude1 * Math.PI/180D;
        double radLatEnd = latitude2 * Math.PI/180D;
        double radLonEnd = longitude2 * Math.PI/180D;
        
        //二点間の平均緯度（ラジアン）
        double avgLat = (radLatStart + radLatEnd) / 2D;
        
        //扁平率の逆数
        double F = 1D / f;
        
        //第一離心率
        double e = (Math.sqrt(2 * F - 1)) / F;
        double W = Math.sqrt(1 - Math.pow(e, 2) * Math.pow(Math.sin(avgLat), 2));
        
        //子午線曲率半径
        double M = (a * (1 - Math.pow(e, 2))) / Math.pow(W, 3);

        //卯酉線曲率半径
        double N = a / W;
        
        //2点間の緯度差(ラジアン)    
        double dLat = radLatStart - radLatEnd;
        
        //2点間の経度差(ラジアン)
        double dLon = radLonStart - radLonEnd;
        
        //2点間の距離（メートル）
        double d = Math.sqrt(Math.pow(M * dLat, 2) + Math.pow(N * Math.cos(avgLat) * dLon, 2));
        
        return d;
	}
}
