package jp.ohtayo.commons.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.imageio.ImageIO;

import jp.ohtayo.commons.log.Logging;

/**
 * 画像処理に必要なツール関数を提供するクラスです。<br>
 * int型ARGBデータのコンバート関数や画像読み書き等のメソッドを含みます。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class ImageUtility{
	
	/**
	 * ARGBデータからアルファ値を抽出します。<br>
	 * @param color ARGBのint型データ
	 * @return α値
	 */
	public static int alpha(int color)
    {
        return color>>>24;
    }

	/**
	 * ARGBデータから赤のデータを抽出します。<br>
	 * @param color ARGBのint型データ
	 * @return 赤のデータ
	 */
    public static int red(int color)
    {
        return color>>>16&0xff;
    }
    
	/**
	 * ARGBデータから緑のデータを抽出します。<br>
	 * @param color ARGBのint型データ
	 * @return 緑のデータ
	 */
    public static int green(int color)
    {
        return color>>>8&0xff;
    }
    
	/**
	 * ARGBデータから青のデータを抽出します。<br>
	 * @param color ARGBのint型データ
	 * @return 青のデータ
	 */
    public static int blue(int color)
    {
        return color&0xff;
    }
    
	/**
	 * 赤、青、緑データからRGBデータを作成します。<br>
	 * @param red	赤データ
	 * @param green	緑データ
	 * @param blue	青データ
	 * @return		RGBデータ
	 */
    public static int rgb(int red,int green,int blue)
    {
        return 0xff000000 | red <<16 | green <<8 | blue;
    }

	/**
	 * α、赤、青、緑データからARGBデータを作成します。<br>
	 * @param alpha	α値
	 * @param red	赤データ
	 * @param green	緑データ
	 * @param blue	青データ
	 * @return		ARGBデータ
	 */
    public static int argb(int alpha,int red,int green,int blue)
    {
        return alpha<<24 | red <<16 | green <<8 | blue;
    }

	/**
	 * ファイルがImageIO.readで読み込める画像かを判断します。
	 * @param fileName ファイル名
	 * @return 読み込める場合true
	 */
	public static boolean isReadableImage(String fileName)
	{
		String extension = fileName.substring(1, fileName.lastIndexOf("."));
		if(	"png".equalsIgnoreCase(extension)  || "jpg".equalsIgnoreCase(extension)  || "jpeg".equalsIgnoreCase(extension) || "gif".equalsIgnoreCase(extension) )
		{
		    return true;
		}else{
			return false;
		}
	}
	



}