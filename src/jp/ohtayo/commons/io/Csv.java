package jp.ohtayo.commons.io;

import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.math.Vector;

/**
 * .csv(カンマ区切り)のファイルを読み書きするクラスです。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class Csv {

	/**
	 * CSVファイルを読み込み、double[][]型で返します。<br>
	 * @param fileName 読み込むCSVファイル名
	 * @return CSVのデータ(double[][])
	 */
	public static double[][] read(String fileName)
	{
		return  read(fileName, 0, 0);
	}

	/**
	 * CSVファイルをヘッダを除いて読み込み、double[][]型で返します。<br>
	 * @param fileName 読み込むCSVファイル名
	 * @param row ヘッダの行数
	 * @param column ヘッダの列数
	 * @return CSVのデータ(double[][])
	 */
	public static double[][] read(String fileName, int row, int column)
	{
		String[][] string = readString(fileName, row, column);
		double[][] matrix = Cast.stringToDouble(string);

		return matrix;
	}

	/**
	 * CSVファイルをヘッダ行数を除いて読み込み、String[][]型で返します。<br>
	 * 歯抜けファイルにも対応<br>
	 * @param fileName 読み込むCSVファイル名
	 * @param row ヘッダの行数
	 * @param column ヘッダの列数
	 * @return CSVのデータ(String[][])
	 */
	public static String[][] readString(String fileName, int row, int column)
	{
		//ファイルの中身をStringで全て読み出す
		String readString = new Text().read(fileName, "utf-8").toString();

		//改行コードを確認
		String regex = null;
		String lf = "\n";
		String cr = "\r";
		String crlf = cr+lf;
		if( readString.contains(crlf))
			regex = crlf;
		else if(readString.contains(cr))
			regex = cr;
		else if(readString.contains(lf))
			regex = lf;

		String[] array;
		if(regex == null){
			array = new String[1];
			array[0] = readString;
		}else{
			//改行で区切る
			array = readString.split(regex);
		}

		//列の最大数を求める。
		String[] tmp;
		int maxColumn = 0;
		for(int i=0; i<array.length; i++){
			tmp = array[i].split(",");
			if(tmp.length > maxColumn) maxColumn = tmp.length;
		}

		//row, columnが大きすぎる場合nullを返す
		if(array.length <= row)	return null;
		if(maxColumn <= column) return null;

		//読み込み結果のString[][]型を用意
		String[][] result = new String[array.length-row][maxColumn-column];

		//ヘッダを除いて結果を格納
		for(int i=row; i<array.length; i++)
		{
			tmp = array[i].split(",");
			for(int j=column; j<tmp.length; j++)
			{
				result[i-row][j-column] = tmp[j];
			}
		}

		return result;
	}
	/**
	 * double[][]配列をCSVファイルに書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param matrix 書き込むdouble[][]配列
	 * @param header ヘッダ
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, double[][] matrix, String header)
	{
		return write(fileName, Cast.doubleToString(matrix), header);
	}
	/**
	 * double[]配列をCSVファイルに書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param vector 書き込むdouble[]配列
	 * @param header ヘッダ
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, double[] vector, String header)
	{
		Matrix matrix = new Matrix(vector.length, 1);
		matrix.setColumn(0, new Vector(vector));
		return write(fileName, Cast.doubleToString(matrix.get()), header);
	}

	/**
	 * double[][]型の値をCSVファイルに書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param matrix 書き込む値(double[][])
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, double[][] matrix)
	{
		return write(fileName, matrix, "");
	}
	/**
	 * double[]型の値をCSVファイルに書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param vector 書き込む値(double[])
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, double[] vector)
	{
		return write(fileName, vector, "");
	}

	/**
	 * String[][]型の値をCSVファイルにヘッダ付きで書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param matrix 書き込む値(String[][])
	 * @param header ヘッダ文字列
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, String[][] matrix, String header)
	{
		
		return write(fileName, matrix, header, "UTF-8", true);
	}
	
	/**
	 * String[][]型の値をCSVファイルにヘッダ付きで書き込みます。<br>
	 * @param fileName 書き込むCSVファイル名
	 * @param matrix 書き込む値(String[][])
	 * @param header ヘッダ文字列
	 * @param encode 文字エンコーディング
	 * @param BOM BOM付きか否か
	 * @return 成功：0、失敗：-1
	 */
	public static int write(String fileName, String[][] matrix, String header, String encode, boolean BOM)
	{
		StringBuilder buffer = new StringBuilder();

		//ヘッダがあればヘッダ文字列を追加
		if(header.isEmpty() == false)
		{
			buffer.append(header + "\r\n");
		}

		//行列の値をバッファに追加
		for (int i = 0; i<matrix.length; i++){
			for (int j = 0; j<matrix[i].length; j++){
				buffer.append(matrix[i][j]+",");
			}
			buffer.append("\r\n");
		}

		//書込
		return new Text(buffer.toString()).write(fileName, encode, BOM);	//UTF-8BOMアリで書き込み
	}
}
