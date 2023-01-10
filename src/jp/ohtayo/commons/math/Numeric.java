package jp.ohtayo.commons.math;

/**
 * 数値計算メソッドを提供するクラスです。<br>
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class Numeric {
	
	/**
	 * 正規分布の値を計算する<br>
	 * 1/√(2πσ^2) * exp(-(x-mu)^2 / 2σ^2)
	 * @param x 正規分布の入力
	 * @param mu 平均
	 * @param sigma 分散
	 * @return 正規分布の値
	 */
	public static double normalDistribution(double x, double mu, double sigma)
	{
		return 1/Math.sqrt(2 * Math.PI * sigma * sigma) * Math.exp( -((x-mu)*(x-mu)) / (2*sigma*sigma) );
	}
	
	/**
	* double型の値の二乗を返します。<br>
	* @param value 二乗したい値(double)
	* @return 二乗値
	*/
	public static double square(double value)
	{
		return value*value;
	}
	
	/**
	* 値の異常値処理をします。<br>
	* 値が最大値・最小値を超えていたら、値を最大値・最小値に変更して返します。<br>
	* @param data 異常値処理をしたい値
	* @param max 値の最大値
	* @param min 値の最小値
	* @return 異常値処理をした値
	*/
	public static double limit(double data, double max, double min)
	{
		if (data > max)
		{
			data = max;
		}
		else if (data < min)
		{
			data = min;
		}
		
		return data;
	}
	
	/**
	* 値の異常値処理をします。<br>
	* 値が最大値・最小値を超えていたら、値を最大値・最小値に変更して返します。<br>
	* @param data 異常値処理をしたい値
	* @param max 値の最大値
	* @param min 値の最小値
	* @return 異常値処理をした値
	*/
	public static double[] limit(double[] data, double max, double min)
	{
		for(int i=0; i<data.length; i++)
			data[i] = limit(data[i], max, min);
		
		return data;
	}
	
	/**
	 * dataに非ゼロ要素が含まれるかどうか
	 * @param data チェックしたい配列
	 * @return 非ゼロ要素がある場合true
	 */
	public static boolean any(double[] data)
	{
		Vector temp = new Vector(data);
		if(temp.sum() != 0)
			return true;
		else
			return false;
	}

	/**
	 * dataに非ゼロ要素が含まれるかどうか
	 * @param data チェックしたい行列
	 * @return 非ゼロ要素がある場合true
	 */
	public static boolean[] any(double[][] data)
	{
		Matrix temp = new Matrix(data);
		boolean[] result = new boolean[temp.columnLength()];
		for(int i=0; i<temp.columnLength(); i++)
			result[i] = any(temp.getColumn(i).get());
		return result;
	}
	
	/**
	 * dataのすべての要素が非ゼロであるかどうか
	 * @param data チェックしたい配列
	 * @return ゼロが1つでもあったらfalse
	 */
	public static boolean all(double[] data)
	{
		for(int i=0; i<data.length; i++){
			if(data[i] == 0)	return false;
		}
		return true;
	}
	public static boolean[] all(double[][] data)
	{
		Matrix temp = new Matrix(data);
		boolean[] result = new boolean[temp.columnLength()];
		for(int i=0; i<temp.columnLength(); i++)
			result[i] = all(temp.getColumn(i).get());
		return result;
	}

	/**
	 * 任意の単位で丸める
	 * @param in 丸めたい数
	 * @param unit 丸める単位
	 * @return 丸められた数
	 */
	public static double round(double in, double unit)
	{
		return (double)Math.round( in/unit ) * unit;
	}

	/**
	 * 正規化する
	 * @param in 正規化したい数
	 * @param max 正規化の最大値
	 * @param min 正規化の最小値
	 */
	public static double normalize(double in, double max, double min)
	{
		double diff = max-min;
		return (in-min) / diff;
	}

	/**
	 * 非正規化する
	 * @param in 非正規化したい数
	 * @param max 非正規化の最大値
	 * @param min 非正規化の最小値
	 */
	public static double denormalize(double in, double max, double min)
	{
		double diff = max-min;
		return in * diff + min;
	}

}
//file end.-------------------------------------------------------------------//
