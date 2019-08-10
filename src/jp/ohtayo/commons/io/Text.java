package jp.ohtayo.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jp.ohtayo.commons.log.Logging;

/**
 * 文字列を読み書きするクラスです。<br>
 * UTF-16非対応、UTF-8 BOM有無両対応<br>
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class Text {

	private StringBuilder string;	//内部テキスト

	/**
	 * コンストラクタ<br>
	 * デフォルトでは内部テキストの初期化のみ。
	 */
	public Text()
	{
		string = new StringBuilder();
	}
	/**
	 * コンストラクタ<br>
	 * 引数を内部テキストに格納。<br>
	 * @param in 引数
	 */
	public Text(StringBuilder in)
	{
		set(in);
	}
	/**
	 * コンストラクタ<br>
	 * 引数を内部テキストに格納。<br>
	 * @param in 引数
	 */
	public Text(String in)
	{
		set(in);
	}
	/**
	 * コンストラクタ<br>
	 * 引数を内部テキストに格納。<br>
	 * @param in 引数
	 */
	public Text(String[] in)
	{
		set(in);
	}

	/**
	 * テキストファイルを読み込みます。<br>
	 * @param fileName テキストファイル名
	 * @param characterSet 文字コード
	 * @return テキスト文字列
	 */
	public Text read(String fileName, String characterSet)
	{
		int readData;

		try{
			File file = new File(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),characterSet));

			//utf-8のBOMチェック
			if(characterSet.toUpperCase().equals("UTF-8")) {
				int buf = reader.read();
				if( buf!=(int)0xfeff){
					string.append((char)buf);// BOMでない場合はそのまま書き込み
				}
			}
			//残りを読み込み
			while((readData = reader.read()) != -1){
				string.append((char)readData);
			}
			reader.close();

		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
		return this;
	}

	/**
	 * テキストファイルをデフォルトの文字コード(UTF-8)で読み込みます。<br>
	 * @param fileName テキストファイル名
	 * @return テキスト文字列
	 */
	public Text read(String fileName)
	{
		return read(fileName, "utf-8");
	}

	/**
	 * 保持するテキストをStringで返します。
	 * @return 読み込んだString型のテキスト
	 */
	public String getString()
	{
		return string.toString();
	}

	/**
	 * 保持するテキストをStringで返します。
	 * @return 読み込んだString型のテキスト
	 */
	public String toString()
	{
		return getString();
	}

	/**
	 * 内部テキストを返します。
	 * @return 内部テキスト(StringBuilder)
	 */
	public StringBuilder get()
	{
		return string;
	}

	/**
	 * 保持するテキストを行ごとの配列で返します。
	 * @return 行ごとのString配列
	 */
	public String[] getStringArray()
	{
		return string.toString().split("\n");
	}

	/**
	 * 内部テキストにStringBuilderをセットします。
	 * @param in セットしたいStringBuilderオブジェクト
	 * @return セットしたテキスト
	 */
	public Text set(StringBuilder in)
	{
		string = in;
		return this;
	}

	/**
	 * 内部テキストにStringをセットします。
	 * @param in セットしたいString文字列
	 * @return セットしたテキスト
	 */
	public Text set(String in)
	{
		string = new StringBuilder(in);
		return this;
	}
	/**
	 * 内部テキストにString[]をセットします。
	 * @param in セットしたいString文字列
	 * @return セットしたテキスト
	 */
	public Text set(String[] in)
	{
		string = new StringBuilder();
		for(int s=0; s<in.length; s++)
		{
			string.append(in[s]).append("\n");
		}
		return this;
	}

	/**
	 * テキストファイルを作成して書込します。<br>
	 * @param fileName テキストファイル名
	 * @param append true:追記、false:上書き
	 * @return 成功:0、失敗:-1
	 */
	private int write(String fileName, boolean append, String characterSet, boolean BOM )
	{
		try{
			File file = new File(fileName);
			if(!file.exists())	//ファイルが存在しない場合、作成する
			{
				file.createNewFile();
				append = false;	//追記はしないのでfalseにする
			}
			if(file.isFile() && file.canWrite())	//ファイルが存在し、書き込み可能なら書き込む
			{
				PrintWriter pw = null;
				if(append) {	//追記の場合文字コードに関係なくappendで開く
					pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), characterSet));
				}else{	//新規作成の場合falseで開き、UTF-8 BOM付きの場合だけBOMを書く
					pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), characterSet));
					if(characterSet.toUpperCase().equals("UTF-8") & BOM) {	//新規作成で、BOMありの場合、BOMを書き込む
						char[] buf = {(char)0xfeff};
						pw.write(buf);
					}
				}
				pw.write(this.toString());
				if (pw != null) {
					pw.close();
				}
			}
			else
			{
				Logging.logger.severe("ファイルに書込みできません。");
				return -1;
			}
		//例外が発生した場合nullを返す。
		}catch(Exception e){
			Logging.logger.severe(e.getMessage());
			return -1;
		}

		return 0;
	}

	/**
	 * テキストファイルに追加書込します。<br>
	 * CSVファイルを作るときは必ずBOMを入れる必要があるため、これを呼び出します。<br>
	 * @param fileName テキストファイル名
	 * @param characterSet 文字コード
	 * @param BOM BOM有無true/false
	 * @return 成功:0、失敗:-1
	 */
	public int append(String fileName, String characterSet, boolean BOM)
	{
		return write(fileName,true, characterSet, BOM);
	}
	/**
	 * テキストファイルに追加書込します。<br>
	 * utf-8でもBOMは付けません<br>
	 * @param fileName テキストファイル名
	 * @param characterSet 文字コード
	 * @return 成功:0、失敗:-1
	 */
	public int append(String fileName, String characterSet)
	{
		return append(fileName, characterSet, false);
	}
	/**
	 * テキストファイルにBOMなしutf-8で追加書込します。<br>
	 * @param fileName テキストファイル名
	 * @return 成功:0、失敗:-1
	 */
	public int append(String fileName)
	{
		return append(fileName, "utf-8", false);
	}

	/**
	 * テキストファイルを新規作成して書込します。<br>
	 * @param fileName テキストファイル名
	 * @param characterSet 文字コード
	 * @param BOM BOM有無true/false
	 * @return 成功:0、失敗:-1
	 */
	public int write(String fileName, String characterSet, boolean BOM)
	{
		return write(fileName, false, characterSet, BOM);
	}

	/**
	 * テキストファイルを新規作成して書込します。<br>
	 * UTF-8でもBOMは付きません。<br>
	 * @param fileName テキストファイル名
	 * @param characterSet 文字コード
	 * @return 成功:0、失敗:-1
	 */
	public int write(String fileName, String characterSet)
	{
		return write(fileName, false, characterSet, false);
	}

	/**
	 * テキストファイルを BOMなしutf-8で新規作成して書込します。<br>
	 * @param fileName テキストファイル名
	 * @return 成功:0、失敗:-1
	 */
	public int write(String fileName)
	{
		return write(fileName, false, "UTF-8", false);
	}

}
