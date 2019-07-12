package jp.ohtayo.commons.util;

/**
 * 行列・配列の型をキャストするクラスです。<br>
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
 */
public class Cast {
	
	//int⇔double------------------------------
	/**
	* int配列をdouble配列にキャストします。
	* @param in int配列
	* @return キャストされたdouble配列
	*/
	public static double[] intToDouble(int[] in)
	{
		double[] out = new double[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (double)in[i];
		return out;
	}
	/**
	* int行列をdouble行列にキャストします。
	* @param in int行列
	* @return キャストされたdouble行列
	*/
	public static double[][] intToDouble(int[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		double[][] out = new double[row][column];
		for(int r=0; r<row; r++)
			out[r] = intToDouble(in[r]);
		
		return out;
	}
	
	/**
	* double配列をint配列にキャストします。
	* @param in double配列
	* @return キャストされたint配列
	*/
	public static int[] doubleToInt(double[] in)
	{
		int[] out = new int[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (int)in[i];
		return out;
	}
	/**
	* double行列をint行列にキャストします。
	* @param in double行列
	* @return キャストされたint行列
	*/
	public static int[][] doubleToInt(double[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		int[][] out = new int[row][column];
		for(int r=0; r<row; r++)
			out[r] = doubleToInt(in[r]);
		
		return out;
	}
	

	//float⇔double------------------------------
	/**
	* float配列をdouble配列にキャストします。
	* @param in float配列
	* @return キャストされたdouble配列
	*/
	public static double[] floatToDouble(float[] in)
	{
		double[] out = new double[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (double)in[i];
		return out;
	}
	/**
	* float行列をdouble行列にキャストします。
	* @param in float行列
	* @return キャストされたdouble行列
	*/
	public static double[][] floatToDouble(float[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		double[][] out = new double[row][column];
		for(int r=0; r<row; r++)
			out[r] = floatToDouble(in[r]);
		
		return out;
	}
	
	/**
	* double配列をfloat配列にキャストします。
	* @param in double配列
	* @return キャストされたfloat配列
	*/
	public static float[] doubleToFloat(double[] in)
	{
		float[] out = new float[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (float)in[i];
		return out;
	}
	/**
	* double行列をfloat行列にキャストします。
	* @param in double行列
	* @return キャストされたfloat行列
	*/
	public static float[][] doubleToFloat(double[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		float[][] out = new float[row][column];
		for(int r=0; r<row; r++)
			out[r] = doubleToFloat(in[r]);
		
		return out;
	}
	
	//int⇔byte------------------------------
	/**
	 * int配列をByte配列にキャストします。
	 * @param in int配列
	 * @return byte配列
	 */
	public static byte[] intToByte(int[] in)
	{
		byte[] out = new byte[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (byte)in[i];
		return out;
	}
	/**
	 * int行列をByte行列にキャストします。
	 * @param in int行列
	 * @return byte行列
	 */
	public static byte[][] intToByte(int[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		byte[][] out = new byte[row][column];
		for(int r=0; r<row; r++)
			out[r] = intToByte(in[r]);
		
		return out;
	}
	
	/**
	 * byte配列をint配列にキャストします。
	 * @param in byte配列
	 * @return int配列
	 */
	public static int[] byteToInt(byte[] in)
	{
		int[] out = new int[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = (int)in[i];
		return out;
	}
	/**
	 * byte行列をint行列にキャストします。
	 * @param in byte行列
	 * @return int行列
	 */
	public static int[][] byteToInt(byte[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		int[][] out = new int[row][column];
		for(int r=0; r<row; r++)
			out[r] = byteToInt(in[r]);
		
		return out;
	}
	
	/**
	 * byte(signed)をunsigned intに変換します。
	 * @param in byteデータ
	 * @return unsignedにしたintデータ
	 */
	public static int byteToUnsignedInt(byte in)
	{
		int ret = in;
		if(ret<0)	ret += 256;
		return ret;
	}
	/**
	 * signedのbyte配列をunsignedのint配列にキャストします。
	 * @param in byte配列
	 * @return int配列
	 */
	public static int[] byteToUnsignedInt(byte[] in)
	{
		int[] out = new int[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = byteToUnsignedInt(in[i]);
		return out;
	}
	/**
	 * signedのbyte行列をunsignedのint行列にキャストします。
	 * @param in byte行列
	 * @return int行列
	 */
	public static int[][] byteToUnsignedInt(byte[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		int[][] out = new int[row][column];
		for(int r=0; r<row; r++)
			out[r] = byteToUnsignedInt(in[r]);
		
		return out;
	}
	
	//double⇔String------------------------------
	/**
	* double配列をString配列にキャストします。
	* @param in double配列
	* @return キャストされたString配列
	*/
	public static String[] doubleToString(double[] in)
	{
		String[] out = new String[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = String.valueOf(in[i]);
		return out;
	}
	/**
	* double行列をString行列にキャストします。
	* @param in double行列
	* @return キャストされたString行列
	*/
	public static String[][] doubleToString(double[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		String[][] out = new String[row][column];
		for(int r=0; r<row; r++)
			out[r] = doubleToString(in[r]);
		
		return out;
	}
	
	/**
	* String配列をdouble配列にキャストします。
	* @param in String配列
	* @return キャストされたdouble配列
	*/
	public static double[] stringToDouble(String[] in)
	{
		double[] out = new double[in.length];
		
		for(int i=0; i<in.length; i++){
			try{
				out[i] = Double.parseDouble(in[i]);
			}catch(Exception e){
				out[i] = Double.NaN;
			}
		}
		return out;
	}
	/**
	* String行列をdouble行列にキャストします。
	* @param in String行列
	* @return キャストされたdouble行列
	*/
	public static double[][] stringToDouble(String[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		double[][] out = new double[row][column];
		for(int r=0; r<row; r++)
			out[r] = stringToDouble(in[r]);
		
		return out;
	}
	
	//boolean→int------------------------------
	/**
	 * boolean値をintに変換します。true=1、false=0です。
	 * @param in boolean値
	 * @return int値
	 */
	public static int booleanToInt(boolean in)
	{
		if(in)	return 1;
		else	return 0;
	}
	/**
	* boolean配列をint配列にキャストします。
	* @param in double配列
	* @return キャストされたString配列
	*/
	public static int[] booleanToInt(boolean[] in)
	{
		int[] out = new int[in.length];
		for(int i=0; i<in.length; i++)
			out[i] = booleanToInt(in[i]);
		return out;
	}
	/**
	* boolean行列をint行列にキャストします。
	* @param in double行列
	* @return キャストされたString行列
	*/
	public static int[][] booleanToInt(boolean[][] in)
	{
		int row = in.length;
		int column = in[0].length;
		int[][] out = new int[row][column];
		for(int r=0; r<row; r++)
			out[r] = booleanToInt(in[r]);
		
		return out;
	}
}
