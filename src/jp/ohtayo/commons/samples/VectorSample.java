package jp.ohtayo.commons.samples;

import jp.ohtayo.commons.math.Vector;

/**
 * Sample codes of Vector class
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class VectorSample {
	public static void main(String[] args){

		//Vector(double[] target)
		//配列の中身を指定して作成します。
		double[] target1 = {-1, 0, 1, 2, 4};
		Vector vector1 = new Vector(target1);
		System.out.println("vector1 =");
		System.out.println(vector1.toString());
		
		//Vector(int length)
		//ある長さの配列を作成します。
		Vector vector2 = new Vector(5);
		System.out.println("vector2 =");
		System.out.println(vector2.toString());
		
		//Vector(int length, double value)
		//要素が全てvalueの配列を作成します。
		Vector vector3 = new Vector(3, 3);
		System.out.println("vector3 =");
		System.out.println(vector3.toString());
		
		//Vector(int start, int difference, int end)
		//整数の等差数列を作成します。
		Vector vector4 = new Vector(1, 1, 10);
		System.out.println("vector4 =");
		System.out.println(vector4.toString());
		
		//Vector(int length, java.lang.String string)
		//ランダムな配列を作成します。
		Vector vector5 = new Vector(6, "random");
		System.out.println("vector5 =");
		System.out.println(vector5.toString());
		
		//Vector(Vector value)
		//入力のベクトルと全く同じベクトルをメモリの別の場所にコピーします。
		Vector vector6 = new Vector( vector1 );
		System.out.println("vector6 =");
		System.out.println(vector6.toString());
		
		//abs()
		//配列の絶対値を返します。
		System.out.println("vector1.abs() =");
		System.out.println(vector1.abs());
		
		//Vector	add(Vector target)
		//配列を接続します。
		Vector vector7 = vector2.add( vector3 );
		System.out.println("vector7 =");
		System.out.println(vector7.toString());
		
		//Vector	delete(int offset, int length)
		//配列の要素を削除します。
		Vector vector8 = vector7.delete(3, 4);
		System.out.println("vector8 =");
		System.out.println(vector8.toString());
		
		//Vector	division(double target)
		//配列の1/定数を計算します。
		System.out.println("vector7/10 =");
		System.out.println(vector7.division(10).toString());

		//Vector	division(Vector target)
		//配列の各要素ごとに割り算します。
		System.out.println("vector6/vector1 =");
		System.out.println(vector6.division(vector1).toString());
		
		//double[]	get()
		//ベクトルの中身を返します。
		double[] result = vector1.get();
		System.out.println("vector1.get()[0] =");
		System.out.println(result[0]);
		//double	get(int index)
		//ベクトルの要素を返します。
		System.out.println("vector1.get(0) =");
		System.out.println(vector1.get(0));
		//Vector	get(int[] index)
		//配列のうちindexで指定した場所の値をピックアップして返します。
		int[] index1 = {1, 2, 4}; 
		System.out.println("vector1[1,2,4] =");
		System.out.println(vector1.get(index1).toString());
		
		//Vector	get(int offset, int length)
		//配列の一部を抽出して返します。
		System.out.println("vector1[5:8] =");
		System.out.println(vector1.get(2, 3).toString());
		
		//Vector	invert()
		//反転した配列を返します。
		System.out.println("vector1.invert() =");
		System.out.println(vector1.invert().toString());
		
		//int	length()
		//ベクトルの長さを返します。
		System.out.println("vector1.length() =");
		System.out.println(vector1.length());
		
		//double	max()
		//double[]型配列の最大値を返します。
		System.out.println("\nvector1.max() =");
		System.out.println(vector1.max());

		//double	max(int[] index)
		//int[]型配列の最大値と最大値の位置を返します。
		int[] position = new int[1];
		vector1.max(position);
		System.out.println("\nvector1の最大値位置 =");
		System.out.println(position[0]);
		
		//double	mean()
		//配列の算術平均値(mean)を返します。
		System.out.println("\nvector1.mean() =");
		System.out.println(vector1.mean());
		
		//double	min()
		//配列の最小値を返します。
		System.out.println("\nvector1.min() =");
		System.out.println(vector1.min());
		//double	min(int[] index)
		//配列の最小値を返します。
		vector1.min(position);
		System.out.println("\nvector1.最小値位置 =");
		System.out.println(position[0]);
		
		//Vector	minus(double target)
		//配列の各要素から定数を減算します。
		System.out.println("\nvector1.minus(3) =");
		System.out.println(vector1.minus(3).toString());

		//Vector	minus(Vector target)
		//配列の各要素ごとの差を計算します。
		System.out.println("vector1.minus(vector2) =");
		System.out.println(vector1.minus(vector2).toString());

		//Vector	muliply(double target)
		//配列を定数倍します。
		System.out.println("vector1.multiply(3.0) =");
		System.out.println(vector1.multiply(3.0).toString());

		//	Vector	multiply(Vector target)
		//2つの配列の各要素ごとの積を計算します。
		System.out.println("vector1.multiply(vector6)) =");
		System.out.println(vector1.multiply(vector6).toString());

		//Vector	normalize()
		//配列を正規化して返します。
		System.out.println("vector1.normalize() =");
		System.out.println(vector1.normalize().toString());

		//Vector	normalize(double maxValue, double minValue)
		//配列を正規化して返します。
		System.out.println("vector1.normalize(5,0) =");
		System.out.println(vector1.normalize(5, 0).toString());

		//Vector	plus(double target)
		//配列の各要素に定数を加算します。
		System.out.println("vector1.plus(3) =");
		System.out.println(vector1.plus(3).toString());

		//Vector	plus(Vector target)
		//配列の各要素を加算します。
		System.out.println("vector1.plus(vector6) =");
		System.out.println(vector1.plus(vector6).toString());

		//double	rootMeanSquare()
		//ベクトルのRMS(Root Mean Square、二乗平均平方根)を求めます。
		System.out.println("vector1.rootMeanSquare() =");
		System.out.println(vector1.rootMeanSquare());

		//void	set(double[] target)
		//ベクトルの値を設定します。
		double[] target2 = {1, 3, -3, 5};
		vector1.set(target2);
		System.out.println("\nnew Vector1({1,3,-3,5})=");
		System.out.println(vector1.toString());

		//void	set(int index, double target)
		//ベクトルの要素の値を設定します。
		vector1.set(3, 4);
		System.out.println("vector1.set(3,4) =");
		System.out.println(vector1.toString());
		
		//void	set(int index, Vector target)
		//ベクトルのある位置に別のベクトルの値を設定します。
		vector1.set(1, vector3);
		System.out.println("vector1.set(1, vector3) =");
		System.out.println(vector1.toString());
		
		//Vector	shuffle()
		//配列をランダムに並び替えます。
		System.out.println("vector1.shuffle() =");
		System.out.println(vector1.shuffle().toString());
		//int[]	sort()
		//配列を昇順にソートして書き換えます。
		int[] index2 = vector1.sort();
		System.out.println("vector1.sort() =");
		System.out.println(vector1.toString());
		System.out.println("vector1.sort()のindex =");
		System.out.println(index2[0]);
		
		//int[]	sort(java.lang.String direction)
		//配列を降順を指定してソートして書き換えます。
		int[] index3 = vector1.sort(Vector.SORT_DESCEND);
		System.out.println("vector1.sort(sort_descend) =");
		System.out.println(vector1.toString());
		System.out.println("vector1.sort(sort_descend)のindex =");
		System.out.println(index3[0]);
		
		//Vector	sqrt()
		//配列の平方根を返します。
		System.out.println("vector1.sqrt() =");
		System.out.println(vector1.sqrt().toString());
		
		//Vector	square()
		//配列の個々の要素の二乗を返します。
		System.out.println("vector1.square() =");
		System.out.println(vector1.square().toString());
		
		//double	sum()
		//double[]型配列の和を返します。
		System.out.println("vector1.sum() =");
		System.out.println(vector1.sum());
		
		
		//vector1の中身を再表示
		double[] temp = {0, 1, 3, 5, 6};
		vector1 = new Vector(temp);
		System.out.println("vector1 =");
		System.out.println(vector1.toString());
		
		//double	median()
		System.out.println("vector1.median() =");
		System.out.println(vector1.median());
		
		//double	range()
		System.out.println("vector1.range() =");
		System.out.println(vector1.range());
		
		//double	midRange()
		System.out.println("vector1.midRange() =");
		System.out.println(vector1.midRange());
		
		//double	variance()
		System.out.println("vector1.variance() =");
		System.out.println(vector1.variance());
		
		//double	standardDeviation()
		System.out.println("vector1.standardDeviation() =");
		System.out.println(vector1.standardDeviation());
		
		//double	unbiasedVariance()
		System.out.println("vector1.unbiasedVariance() =");
		System.out.println(vector1.unbiasedVariance());
		
		//double	standardize()
		System.out.println("vector1.standardize() =");
		System.out.println(vector1.standardize());
		
		//double	deviate()
		System.out.println("vector1.deviationValue() =");
		System.out.println(vector1.deviationValue());
		
		
	}
	

}
