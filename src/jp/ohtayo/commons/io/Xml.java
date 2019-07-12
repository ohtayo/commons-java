package jp.ohtayo.commons.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import jp.ohtayo.commons.log.Logging;

/**
 * XMLファイルの読み書きをするクラスです。<br>
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
 */
public class Xml {
	
	/**
	 * キーを用いてXMLからプロパティを読み込みます。<br>
	 * @param filename	XMLファイル名
	 * @param keys		キーの文字列配列
	 * @return			読み込んだプロパティ文字列配列
	 */
	public static String[] read(String filename, String[] keys)
	{
		
		String[] values;
		Properties properties = new Properties();
		
		try{
			InputStream inputStream = new FileInputStream(new File(filename));
			
			properties.loadFromXML(inputStream);

			values = new String[properties.size()];
			for (int i=0; i<properties.size(); i++){
				values[i] = properties.getProperty(keys[i]);
			}
		
		//例外が発生した場合nullを返す。
		}catch(Exception e){
			Logging.logger.severe(e.toString());
			values = null;
		}
		
		return values;
	}
	
	/**
	 * キーを用いてXMLにプロパティを書き込みます。<br>
	 * キー配列とプロパティ配列は同じ長さでなければなりません。<br>
	 * @param filename	XMLファイル名
	 * @param keys		キーの文字列配列
	 * @param values	書き込むプロパティの配列
	 * @return			成功：0, 失敗：-1
	 */
	public static int write(String filename, String[] keys, String[] values)
	{
		if(keys.length != values.length){
			Logging.logger.severe("illegal keys or properties length");
			return -1;
		}
		
		Properties properties = new Properties();
		
		try{
			OutputStream outputStream = new FileOutputStream(new File(filename));
			
			for (int i=0; i<keys.length; i++){
				properties.setProperty(keys[i], values[i]);
			}
			
			properties.storeToXML(outputStream, "comment");
			
		//例外が発生した場合-1を返す。
		}catch(Exception e){
			Logging.logger.severe(e.getMessage());
			return -1;
		}
		
		return 0;
	}
	
}
