package jp.ohtayo.commons.math;

import jp.ohtayo.commons.random.Random;
import jp.ohtayo.commons.log.Logging;
import jp.ohtayo.commons.util.Cast;

/**
 * 配列クラスです。<br>
 * 配列の操作や数値計算を行うメソッドを含みます。<br>
 * 内部配列はdouble[]です。<br>
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class Vector {

	double[] vector;

	/**
	 * ある長さの配列を作成します。
	 * @param length ベクトル長さ
	 */
	public Vector(int length)
	{
		vector = new Vector(length, 0).get();
	}
	/**
	 * 配列の値を指定して作成します。
	 * @param target 配列の値
	 */
	public Vector(double[] target)
	{
		vector = new double[target.length];
		System.arraycopy(target, 0, vector, 0, target.length);
	}
	/**
	* 要素が全てvalueの配列を作成します。<br>
	* @param length 配列の長さ
	* @param value 各配列要素の値
	*/
	public Vector(int length, double value)
	{
		vector = new double[length];
		for(int i=0; i<length; i++)
			vector[i] = value;
	}
	/**
	* 整数の等差数列を作成します。<br>
	* @param start 等差数列の開始値
	* @param difference 差
	* @param end 数列の最終値
	*/
	public Vector(double start, double difference, double end)
	{
		vector = new double[(int)Math.floor( Math.abs(end-start)/difference )+1];
		for (int i=0; i<vector.length; i=i+1)
		{
			vector[i] = start + difference*i;
		}
	}
	/**
	 * ランダムな配列を作成します。
	 * @param length 配列長さ
	 * @param string "random"。あるとランダム配列作成。
	 */
	public Vector(int length, String string)
	{
		Random random = new Random();
		vector = random.array(length);
	}

	/**
	 * 入力のベクトルと全く同じベクトルをメモリの別の場所にコピーします。
	 * @param value 入力ベクトル
	 */
	public Vector( Vector value )
	{
		vector = new double[value.length()];
		System.arraycopy(value.get(), 0, vector, 0, value.length());
	}

	//------------------------------------------------------------------------//
	// 配列の操作関数                                                         //
	//------------------------------------------------------------------------//
	public static final String DIRECTION_COLUMN = "column";
	public static final String DIRECTION_ROW = "row";
	/**
	* 配列を文字列として返します。<br>
	* @return 変換した文字列
	*/
	public String toString()
	{
		String str = "";

		for(int i=0; i<length(); i++ ){
			str += "  ";
			str += String.valueOf(vector[i]) + " ";
			str += "\r\n";
		}
		return str;
	}
	/**
	* 配列を文字列として返します。axisによって配列の方向を指定します。<br>
	 * @param direction 文字列にする配列の方向
	* @return 変換した文字列
	*/
	public String toString(String direction)
	{
		String str = "";
		// 列方向
		if (direction.equals(DIRECTION_COLUMN)) {
			str = toString();
		}
		// 行方向
		else if(direction.equals(DIRECTION_ROW)) {
			str += "  ";
			for(int i=0; i<length(); i++ ){
				str += String.valueOf(vector[i]) + ", ";
			}
			str += "\r\n";
		}
		return str;
	}
	/**
	 * ベクトルの長さを返します。
	 * @return ベクトル長さ
	 */
	public int length()
	{
		return vector.length;
	}
	/**
	 * ベクトルの中身を返します。
	 * @return ベクトルの中身double[]
	 */
	public double[] get()
	{
		return vector;
	}
	/**
	 * ベクトルの要素を返します。
	 * @param index 要素番号
	 * @return ベクトルの要素
	 */
	public double get(int index)
	{
		return vector[index];
	}
	/**
	* 配列の一部を抽出して返します。<br>
	* @param offset 抽出開始点
	* @param length 抽出長さ
	* @return 抽出した配列
	*/
	public Vector get(int offset, int length)
	{
		double[] result = new double[length];
		for(int i=0; i<length; i++){
			result[i] = vector[i+offset];
		}
		return new Vector(result);
	}
	/**
	 * 配列のうちindexで指定した場所の値をピックアップして返します。
	 * @param index 欲しい値のインデックス
	 * @return ピックアップされた配列
	 */
	public Vector get(int[] index)
	{
		Vector result = new Vector(index.length);
		int count=0;
		for(int i=0; i<index.length; i++)
		{
			result.set(count, get(index[i]));
			count++;
		}
		return result;
	}

	/**
	 * ベクトルの値を設定します。
	 * @param target 設定するベクトルdouble[]
	 */
	public void set(double[] target)
	{
		vector = new double[target.length];
		System.arraycopy(target, 0, vector, 0, target.length);
	}
	/**
	 * ベクトルの要素の値を設定します。
	 * @param index 要素番号
	 * @param target 値
	 */
	public void set(int index, double target)
	{
		vector[index] = target;
	}
	/**
	 * ベクトルのある位置に別のベクトルの値を設定します。
	 * @param index 開始位置
	 * @param target 設定したい値を持つベクトル
	 */
	public void set(int index, Vector target)
	{
		System.arraycopy(target.get(), 0, vector, index, target.length());
	}


	/**
	* 配列を接続します。
	* @param target 後側の配列
	* @return 接続した配列
	*/
	public Vector add(Vector target)
	{
		double[] result = new double[length()+target.length()];
		System.arraycopy(vector, 0, result, 0, length());
		System.arraycopy(target.get(), 0, result, length(), target.length());

		return new Vector(result);
	}

	/**
	* 配列の要素を削除します。<br>
	* @param offset 削除開始点
	* @param length 削除長さ
	* @return 削除した配列
	*/
	public Vector delete(int offset, int length)
	{
		double[] result = new double[length()-length];
		int count=0;

		for(int i=0; i<length(); i++){
			if( (i<offset) || (i>=(offset+length)) ){
				result[count] = vector[i];
				count++;
			}
		}
		return new Vector(result);
	}

	/**
	 * 反転した配列を返します。
	 * @return 反転した配列
	 */
	public Vector invert()
	{
		double[] result = new double[length()];

		for(int i=0; i<length(); i++)
			result[i] = vector[length()-1-i];

		return new Vector(result);
	}

	/**
	 * 配列をランダムに並び替えます。
	 * @return 並び替えられた配列
	 */
	public Vector shuffle()
	{
		Random random = new Random();
		return new Vector(random.shuffle(vector));
	}

	//------------------------------------------------------------------------//
	// 配列の演算関数                                                         //
	//------------------------------------------------------------------------//
	/**
	* 配列の各要素を加算します。<br>
	* @param target 加算される配列
	* @return 加算した配列
	*/
	public Vector plus(Vector target)
	{
		if(length() != target.length()){
			Logging.logger.severe("different vector length");
			return	null;	//長さが異なったらnull返し
		}

		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]+target.get(i);

		return new Vector(result);
	}
	/**
	* 配列の各要素に定数を加算します。
	* @param target 加算される定数
	* @return 加算した配列
	*/
	public Vector plus(double target)
	{
		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]+target;

		return new Vector(result);
	}

	/**
	* 配列の各要素ごとの差を計算します。
	* @param target 引く配列
	* @return 差の配列
	*/
	public Vector minus(Vector target)
	{
		if(length() != target.length()){
			Logging.logger.severe("different vector length");
			return	null;	//長さが異なったらnull返し
		}

		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]-target.get(i);

		return new Vector(result);
	}
	/**
	* 配列の各要素から定数を減算します。
	* @param target 引く定数
	* @return 減算した配列
	*/
	public Vector minus(double target)
	{
		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]-target;

		return new Vector(result);
	}

	/**
	* 2つの配列の各要素ごとの積を計算します。
	* @param target 配列2
	* @return 積の配列
	*/
	public Vector multiply(Vector target)
	{
		if(length() != target.length()){
			Logging.logger.severe("different vector length");
			return	null;	//長さが異なったらnull返し
		}

		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]*target.get(i);

		return new Vector(result);
	}
	/**
	 * 2つのベクトルの内積(各要素ごとの積の和)を返します。
	 * @param target 内積を計算したい対象のベクトル
	 * @return 自分とtargetの内積
	 */
	public double innerProduct(Vector target)
	{
		return multiply(target).sum();
	}
	/**
	* 配列を定数倍します。
	* @param target 定数
	* @return 定数倍された配列
	*/
	public Vector multiply(double target)
	{
		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]*target;

		return new Vector(result);
	}

	/**
	* 配列の各要素ごとに割り算します。
	* @param target 割る配列
	* @return 除算された配列
	*/
	public Vector division(Vector target)
	{
		if(length() != target.length()){
			Logging.logger.severe("different vector length");
			return	null;	//長さが異なったらnull返し
		}

		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]/target.get(i);

		return new Vector(result);
	}

	/**
	* 配列の1/定数を計算します。
	* @param target 定数
	* @return 1/定数にした配列
	*/
	public Vector division(double target)
	{
		double[] result = new double[length()];
		for(int i=0; i<length(); i++)
			result[i] = vector[i]/target;

		return new Vector(result);
	}


	//------------------------------------------------------------------------//
	// 配列の数値計算                                                         //
	//------------------------------------------------------------------------//
	/**
	* 小数点以下を四捨五入した値を返します。<br>
	* @return 四捨五入した配列
	*/
	public Vector round()
	{
		Vector result = new Vector(this.length());
		for(int i=0; i<this.length(); i++)
			result.set(i, Math.round(this.get(i)));

		return result;
	}
	/**
	* 配列の最大値を返します。<br>
	* @return 最大値
	*/
	public double max()
	{
		double max = vector[0];
		for( int i=0; i<length(); i++ ){
			if( max < vector[i] ) max = vector[i];
		}
		return max;
	}
	/**
	* 配列の最大値と最大値の位置を返します。<br>
	* @param index 最大値のインデックス(出力,長さ1のint配列)
	* @return 最大値
	*/
	public double max(int[] index)
	{
		double max = vector[0];
		index[0] = 0;
		for( int i=0; i<length(); i++ ){
			if( max < vector[i] )
			{
				max = vector[i];
				index[0] = i;
			}
		}
		return max;
	}
	/**
	* 配列の最小値を返します。<br>
	* @return 最小値
	*/
	public double min()
	{
		double min = vector[0];
		for( int i=0; i<length(); i++ ){
			if( min > vector[i] ) min = vector[i];
		}
		return min;
	}
	/**
	* 配列の最小値を返します。<br>
	* @param index 最小値のインデックス(出力,長さ1のint配列)
	* @return 最小値
	*/
	public double min(int[] index)
	{
		double min = vector[0];
		index[0] = 0;
		for( int i=0; i<length(); i++ ){
			if( min > vector[i] )
			{
				min = vector[i];
				index[0] = i;
			}
		}
		return min;
	}

	/**
	* 配列の絶対値を返します。<br>
	* @return 絶対値
	*/
	public Vector abs()
	{
		double[] result = new double[length()];
		for(int i=0; i<result.length; i++){
			result[i] = Math.abs(vector[i]);
		}
		return new Vector(result);
	}

	/**
	* 配列の平方根を返します。<br>
	* @return 平方根
	*/
	public Vector sqrt()
	{
		double[] result = new double[length()];
		for(int i=0; i<result.length; i++){
			result[i] = Math.sqrt(vector[i]);
		}
		return new Vector(result);
	}

	/**
	* 配列の個々の要素の二乗を返します。<br>
	* @return 二乗値
	*/
	public Vector square()
	{
		return multiply(this);
	}

	/**
	* double[]型配列の和を返します。<br>
	* @return 配列の和
	*/
	public double sum()
	{
		double sum = 0;
		for( int i=0; i<length(); i++ ){
			sum += vector[i];
		}
		return (sum);
	}

	/**
	* 配列の算術平均値(mean)を返します。<br>
	* @return 平均値
	*/
	public double mean()
	{
		return (sum()/length());
	}
	/**
	 * ベクトルのノルム(√a0^2+a1^2+a2^2+...)を計算します。
	 * @return ノルム値
	 */
	public double norm()
	{
		return Math.sqrt(square().sum());
	}
	/**
	 * ベクトルのRMS(Root Mean Square、二乗平均平方根)を求めます。
	 * @return RMS値
	 */
	public double rootMeanSquare()
	{
		return Math.sqrt(square().mean());
	}
	/**
	 * 配列の中央値(median)を返します。
	 * @return 中央値
	 */
	public double median()
	{
		Vector temp = new Vector(vector);
		temp.sort();
		return temp.get( (int)Math.ceil((double)vector.length/2)-1 );
	}

	/**
	 * レンジ(分布の範囲)を返します。
	 * @return レンジ
	 */
	public double range()
	{
		return max()-min();
	}
	/**
	 * ミッドレンジ(分布の中間の値)を返します。
	 * @return ミッドレンジ
	 */
	public double midRange()
	{
		return ( max()+min() )/2;
	}

	/**
	 * 分散R^2を返します。
	 * @return 分散
	 */
	public double variance()
	{
		return minus(mean()).square().mean();
	}
	/**
	 * 標準偏差Rを返します。
	 * @return 標準偏差
	 */
	public double standardDeviation()
	{
		return Math.sqrt(variance());
	}
	/**
	 * 不偏分散s^2を返します。
	 * @return 不偏分散
	 */
	public double unbiasedVariance()
	{
		return minus(mean()).square().sum()/(length()-1);
	}

	/**
	 * 配列を標準化(平均0標準偏差1)してかえします。
	 * @return 標準化された配列
	 */
	public Vector standardize()
	{
		return new Vector( minus(mean()).division(Math.sqrt(unbiasedVariance())) );
	}
	/**
	 * 偏差値得点(平均50,標準偏差10)を返します。
	 * @return 偏差値
	 */
	public Vector deviationValue()
	{
		return new Vector( standardize().multiply(10).plus(50) );
	}

	/**
	* 配列を正規化して返します。<br>
	* 最大値・最小値を与えると、最大値を1, 最小値を0として変換した配列を返します。<br>
	* @param maxValue 正規化の最大値
	* @param minValue 正規化の最小値
	* @return 正規化された配列
	*/
	public Vector normalize(double maxValue, double minValue)
	{
		//事前に別メモリに配列コピー
		double[] result = new double[length()];
		System.arraycopy(vector, 0, result, 0, length());

		double difference = maxValue - minValue;
		for (int i=0; i<length(); i++)
			result[i] = (result[i] - minValue)/difference;

		return new Vector(result);
	}
	/**
	* 配列を正規化して返します。<br>
	* 配列の最大値・最小値を計算して、最大値を1, 最小値を0として変換した配列を返します。<br>
	* @return 正規化された配列
	*/
	public Vector normalize()
	{
		double maxValue = max();
		double minValue = min();

		return normalize(maxValue, minValue);
	}

	/**
	* 配列を昇順にソートして書き換えます。<br>
	* 返り値はインデックス配列です。<br>
	* 返り値がindexの場合、vector.get(index[x])が元の値になります。<br>
	* @return インデックス配列
	*/
	public int[] sort()
	{
		int[] index = Cast.doubleToInt(new Vector(0, 1, length()-1).get());
		quickSortAscend(vector, index, 0, length()-1);
		return index;
	}

	public static final String SORT_DESCEND = "descend";
	public static final String SORT_ASCEND = "ascend";

	/**
	* 配列を昇順・降順を指定してソートして書き換えます。<br>
	* 返り値はインデックス配列です。<br>
	* 返り値がindexの場合、vector.get(index[x])が元の値になります。<br>
	* @param direction ソート方向("descend"(降順)もしくは"ascend"(昇順))
	* @return インデックス配列
	*/
	public int[] sort(String direction){
		int[] index = Cast.doubleToInt(new Vector(0, 1, length()-1).get());
		if(direction == SORT_DESCEND)	//降順
		{
			quickSortDescend(vector, index, 0, length()-1);
			return index;
		}
		else if(direction == SORT_ASCEND) //昇順
		{
			quickSortAscend(vector, index, 0, length()-1);
			return index;
		}
		else
		{
			System.out.println("ソート方向を指定してください。");
			return null;
		}
	}

	/**
	* 再帰呼び出しを使い、クイックソートアルゴリズムで配列を降順にソートします。<br>
	* @param buff ソートしたい配列
	* @param idx インデックス配列
	* @param low ソートデータ開始位置
	* @param high ソートデータ終了位置
	*/
	private static void quickSortDescend(double[] buff,int[] idx, int low, int high)
	{
		double p = buff[(low + high) / 2];
		int i = low, j = high;
		while(true){
			while(buff[i] > p) i++;
			while(buff[j] < p) j--;
			if(i >= j) break;
			double temp = buff[i];	int tmp = idx[i];
			buff[i] = buff[j];		idx[i] = idx[j];
			buff[j] = temp;			idx[j] = tmp;
			i++;
			j--;
		}
		if(i - low > 1) quickSortDescend(buff, idx, low, i - 1);
		if(high - j > 1) quickSortDescend(buff, idx,  j + 1, high);
	}

	/**
	* 再帰呼び出しを使い、クイックソートアルゴリズムで配列を昇順にソートします。<br>
	* @param buff ソートしたい配列
	* @param idx インデックス配列
	* @param low ソートデータ開始位置
	* @param high ソートデータ終了位置
	*/
	private static void quickSortAscend(double[] buff,int[] idx, int low, int high)
	{
		double p = buff[(low + high) / 2];
		int i = low, j = high;
		while(true){
			while(buff[i] < p) i++;
			while(buff[j] > p) j--;
			if(i >= j) break;
			double temp = buff[i];	int tmp = idx[i];
			buff[i] = buff[j];		idx[i] = idx[j];
			buff[j] = temp;			idx[j] = tmp;
			i++;
			j--;
		}
		if(i - low > 1) quickSortAscend(buff, idx, low, i - 1);
		if(high - j > 1) quickSortAscend(buff, idx,  j + 1, high);
	}

}
