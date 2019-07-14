package jp.ohtayo.commons.io;

import java.lang.reflect.Field;

import jp.ohtayo.commons.log.Logging;

/**
 * コンフィグ設定をXMLで外部に保存・読込するための基本となるクラスです。<br>
 * 本クラスを継承したクラスのフィールドをXMLで書込・読込する関数を提供します。<br>
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 * @see jp.ohtayo.commons.io.Xml
 */
public class ConfigBase {
	/**
	 * コンフィグをXMLから読込<br>
	 * @param filename 読み込むXMLファイル名
	 * @return 読み込みが成功したらtrue、失敗したらfalse
	 */
	public boolean read(String filename)
	{		
		Class<?> cls = this.getClass();				//このクラスを取得
		Field[] fields = cls.getDeclaredFields();	//このクラスの変数を取得
		String[] keys = new String[fields.length];	
		for (int i=0; i<fields.length; i++)
			keys[i] = fields[i].getName();			//変数の名前を文字列で格納
		
		String[] values = Xml.read(filename, keys);	//コンフィグファイルを読み込み
		if(values == null)
			return false;
		try{
			for(int i=0; i<fields.length; i++)
			{
				fields[i].set(this, values[i]);
			}
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		
		return true;
	}
	
	/**
	 * コンフィグをXMLで書込<br>
	 * @param filename 書き込むXMLファイル名
	 * @return 書込が成功したらtrue、失敗したらfalse
	 */
	public boolean write(String filename)
	{		
		Class<?> cls = this.getClass();				//このクラスを取得
		Field[] fields = cls.getDeclaredFields();	//このクラスの変数を取得
		String[] keys = new String[fields.length];	
		for (int i=0; i<fields.length; i++)
			keys[i] = fields[i].getName();			//変数の名前を文字列で格納
		
		String[] values = new String[fields.length];
		try {
			for (int i=0; i<fields.length; i++)
				values[i] = (String) fields[i].get(this);	//変数の値を格納
		} catch (Exception e) {
			Logging.logger.severe(e.getMessage());
			return false;
		}
		
		//コンフィグファイル書き込み
		if(	Xml.write(filename, keys, values) == -1 )
			return false;
		
		return true;
	}

}
