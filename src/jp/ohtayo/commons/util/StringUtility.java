package jp.ohtayo.commons.util;

/**
 * 文字列操作に関するユーティリティクラスです。<br>
 * 将来的にorg.apache.commons.lang.StringUtilsに代替します．<br>
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
 */
public class StringUtility {
	/**
	 * 文字列がnullもしくは空文字であればtrueを返します。
	 * @param string nullか空文字かを確かめたい文字列
	 * @return nullか空文字ならtrue その他はfalse
	 */
	 public static boolean isNullOrEmpty(String string) {
	    return ( string == null || string.isEmpty() );
	  }

}
