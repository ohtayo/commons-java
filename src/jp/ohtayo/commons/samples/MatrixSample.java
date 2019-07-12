package jp.ohtayo.commons.samples;

import jp.ohtayo.commons.io.Csv;
import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.math.Vector;

/**
 * Sample codes of Matrix class
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
 */
public class MatrixSample {

	public static void main(String[] args) {
		//Matrix(double[][] target)
		//値を指定して行列を生成します。
		double[][] temp = {{-1,0},{3,4}};
		Matrix A = new Matrix(temp);
		//java.lang.String	toString()
		//行列を文字列として返します。
		System.out.println("A = ");
		System.out.println(A.toString());
	
		//Matrix(int row, int column)
		//デフォルトコンストラクタ
		Matrix B = new Matrix(3, 3);
		System.out.println("B = ");
		System.out.println(B.toString());
		
		//Matrix(int dimension)
		//デフォルトコンストラクタ
		B = new Matrix(3);
		System.out.println("B = ");
		System.out.println(B.toString());
		
		//Matrix(int row, int column, double value)
		//全ての要素がvalueの行列を生成します。
		Matrix C = new Matrix(2,3,1);
		System.out.println("C = ");
		System.out.println(C.toString());
		
		//Matrix(int row, int column, java.lang.String string)
		//ランダムな行列か単位行列を返します。
		Matrix R = new Matrix(2,2,Matrix.CONSTRUCT_RANDOM);
		System.out.println("R = ");
		System.out.println(R.toString());
		Matrix I = new Matrix(3, 2, Matrix.CONSTRUCT_IDENTITY);
		System.out.println("I = ");
		System.out.println(I.toString());

		//Matrix(Matrix value)
		//入力と同じ行列を別メモリに生成します。
		Matrix D = new Matrix(A);
		System.out.println("D = ");
		System.out.println(D.toString());
		
		//Matrix	abs()
		//行列の絶対値を返します。
		System.out.println("A.abs() = ");
		System.out.println(A.abs().toString());
		
		//Matrix	add(Matrix target, java.lang.String direction)
		//行列を連結します
		System.out.println("[A,C] = ");
		Matrix AC = A.add(C, Matrix.DIRECTION_ADD_RIGHT);
		System.out.println(AC.toString());
		Matrix BC = B.add(C, Matrix.DIRECTION_ADD_BOTTOM);
		System.out.println("[B;C] = ");
		System.out.println(BC.toString());

		//int	columnLength()
		//行列の列数を返します。
		System.out.println("B.columnLength() = ");
		System.out.println(B.columnLength());
		
		//double	determinant()
		//行列の行列式を返します。
		System.out.println("A.determinant() = ");
		System.out.println(A.determinant());
		
		//double[][]	get()
		//行列の中身を返します。
		double[][] result1 = A.get();
		System.out.println("A(0,0) = ");
		System.out.println(result1[0][0]);
		
		//double	get(int row, int column)
		//行列の中身を返します。
		System.out.println("A(0,0) = ");
		System.out.println(A.get(0,0));
		
		//Vector	getColumn(int column)
		//行列の列を抽出します。
		System.out.println("A.getColumn(0) = ");
		System.out.println(A.getColumn(0).toString());
		//Matrix	getColumns(int[] columns)
		//行列の列を抽出します。
		double[][] temp2 = {{1,2,3},{5,6,7},{9,10,11},{13,14,15}};
		C = new Matrix(temp2);
		System.out.println("C = \n" + C.toString());
		int[] columns = {0, 2};
		System.out.println("C.getColumns([0, 2]) = ");
		System.out.println(C.getColumns(columns).toString());
		
		//Vector	getRow(int row)
		//	行列の行を抽出します。
		System.out.println("A.getRow(0) = ");
		System.out.println(A.getRow(0).toString());
		//Matrix	getRows(int[] rows)
		//行列の行を抽出します。
		int[] rows = {0, 2, 3};
		System.out.println("C.getColumns([0, 2, 3]) = ");
		System.out.println(C.getRows(rows).toString());		
		
		//Matrix	getSubMatrix(int row, int rowLength, int column, int columnLength)
		//	行列の一部を返す。
		System.out.println("C.getSubMatrix(0, 2, 0, 3) = ");
		System.out.println(C.getSubMatrix(0, 2, 0, 3).toString());
		Matrix CC = C.getSubMatrix(0, 2, 0,3);
		Matrix C2 = new Matrix(C);
		C.setSubMatrix(1, 1, 1, 2, CC);
		System.out.println("C.setSubMatrix(1, 1, 1, 2, CC)=\n" + C.toString());
		//Matrix add()
		//行列を指定方向に連結します。
		C=C.add(C2.getSubMatrix(0,1,0,3), Matrix.DIRECTION_ADD_BOTTOM);
		System.out.println("C=C.add(C2.getSubMatrix(0,1,0,3))=\n"+C.toString());
		//Matrix getSubMatrix(int[] rows, int[] columns)
		//行列の一部の行列を返す。
		System.out.println("C.getSubMatrix(rows, columns)=\n"+C.getSubMatrix(rows, columns).toString());
		
		//Matrix	inverse()
		//	行列の逆行列を返します。
		System.out.println("A.inverse() = ");
		System.out.println(A.inverse().toString());
		
		//Matrix	invert()
		//上下、左右に反転した行列を返します。
		System.out.println("A.invert(Matrix.INVERT_UPPER_BOTTOM) = ");
		System.out.println(A.invert(Matrix.INVERT_UPPER_BOTTOM).toString());
		System.out.println("A.invert(Matrix.INVERT_LEFT_RIGHT) = ");
		System.out.println(A.invert(Matrix.INVERT_LEFT_RIGHT).toString());

		//int	length()
		//行列の行数を返します。
		System.out.println("B.length() = ");
		System.out.println(B.length());
		
		//Vector	max(java.lang.String direction)
		//行列の各列もしくは各行ごとの最大値を返します。
		int[] index = new int[A.length()];
		System.out.println("A.max(Matrix.DIRECTION_ROW) = ");
		System.out.println(A.max(Matrix.DIRECTION_ROW, index).toString());
		System.out.println("A.max(Matrix.DIRECTION_ROW,index); index = ");
		System.out.println(new Vector(Cast.intToDouble(index)).toString());
		index = new int[2];
		System.out.println("A.max() = ");
		System.out.println(A.max(index));
		System.out.println("A.max(index); index = ");
		System.out.println(new Vector(Cast.intToDouble(index)).toString());
		
		//Vector	min(java.lang.String direction)
		//行列の各列もしくは各行ごとの最小値を返します。
		index = new int[A.columnLength()];
		System.out.println("A.min(Matrix.DIRECTION_COLUMN) = ");
		System.out.println(A.min(Matrix.DIRECTION_COLUMN, index).toString());
		System.out.println("A.max(Matrix.DIRECTION_COLUMN,index); index = ");
		System.out.println(new Vector(Cast.intToDouble(index)).toString());
		index = new int[2];
		System.out.println("A.min() = ");
		System.out.println(A.min(index));
		System.out.println("A.min(index); index = ");
		System.out.println(new Vector(Cast.intToDouble(index)).toString());
		
		//double max()
		
		//Matrix	minus(double target)
		//行列の各要素から定数を引きます。
		System.out.println("A.minus(3) = ");
		System.out.println(A.minus(3).toString());
		
		//Matrix	minus(Matrix target)
		//行列の各要素を減算します。
		double[][] target3 = {{2, 3},{1, 2}};
		Matrix E = new Matrix(target3);
		System.out.println("E = ");
		System.out.println(E.toString());
		System.out.println("A.minus(E) = ");
		System.out.println(A.minus(E).toString());
		
		//Matrix	multiply(double target)
		//行列を定数倍します。
		System.out.println("A.multiply(3) = ");
		System.out.println(A.multiply(3).toString());
		
		//Matrix	multiply(Matrix target)
		//行列の積を計算します。
		System.out.println("A.multiply(D) = ");
		System.out.println(A.multiply(D).toString());

		//Matrix	multiplyEach(Matrix target)
		//行列の各要素ごとに積を計算します。
		System.out.println("A.multiplyEach(D) = ");
		System.out.println(A.multiplyEach(D).toString());

		//Matrix	normalize()
		//行列を各列ごとに正規化して返します。
		System.out.println("A.normalize() = ");
		System.out.println(A.normalize().toString());

		//Matrix	normalize(double[] maxValue, double[] minValue)
		//行列を各列ごとに正規化して返します。
		double[] maxValue = {10, 10};
		double[] minValue = {0,0};
		System.out.println("A.normalize({10,10}{0,0}) = ");
		System.out.println(A.normalize(maxValue, minValue).toString());

		//Matrix	plus(double target)
		//行列の各要素に定数を加算します。
		System.out.println("A.plus(3) = ");
		System.out.println(A.plus(3).toString());

		//Matrix	plus(Matrix target)
		//行列の各要素の加算結果を返します。
		System.out.println("A.plus(D) = ");
		System.out.println(A.plus(D).toString());

		//Matrix	rotate(int angle)
		//回転した行列を返します。
		System.out.println("A.rotate(90)180,270 = ");
		System.out.println(A.rotate(90).toString());
		System.out.println(A.rotate(180).toString());
		System.out.println(A.rotate(270).toString());

		//void	set(double[][] target)
		//行列の値を設定します。
		double[][] target4 = {{1, 2},{3, -4}};
		A.set(target4);
		System.out.println("A.set{1,2;3,-4} = ");
		System.out.println(A.toString());

		//void	set(int row, int column, double target)
		//行列の値を設定します。
		A.set(0,1,4);
		System.out.println("A.set(0,1)←4 = ");
		System.out.println(A.toString());
		
		//void	setColumn(int column, Vector target)
		//	行列の列に配列を設定します。
		double[] target5 = {5,1};
		A.setColumn(1, new Vector(target5));
		System.out.println("A.setColumn(1){5,1} = ");
		System.out.println(A.toString());

		//void	setRow(int row, Vector target)
		//	行列の行に配列を設定します。
		double[] target6 = {4,7};
		A.setRow(0, new Vector(target6));
		System.out.println("A.setRow({4,7} = ");
		System.out.println(A.toString());
		
		//void	setSubMatrix(int row, int rowLength, int column, int columnLength, Matrix target)
		//	行列の一部を別の行列の値に設定する。
		double[][] target7 = {{1.0,2.0}};
		A.setSubMatrix(1, 1, 0, 2, new Matrix(target7));
		System.out.println("A(1,0)起点に{1,2}を設定 = ");
		System.out.println(A.toString());
		
		//	int[]	size()
		//行列のサイズを返します。
		int[] result2 = A.size();
		System.out.println("Aのサイズ[0] = ");
		System.out.println(result2[0]);
		
		//Matrix	spritMatrix(int row, int column)
		//行列の要素を指定すると、その行・列を排除した行列を返します。
		System.out.println("A.spritMatrix(1,1) = ");
		System.out.println(A.spritMatrix(1, 1).toString());

		//Matrix	sqrt()
		//行列の平方根を返します。
		System.out.println("A.sqrt() = ");
		System.out.println(A.sqrt().toString());

		//Matrix	square()
		//行列の個々の要素の二乗を返します。
		System.out.println("A.square() = ");
		System.out.println(A.square().toString());
		
		//Matrix	squareEach()
		//行列の個々の要素の二乗を返します。
		System.out.println("A.squareEach() = ");
		System.out.println(A.squareEach().toString());
		
		//Matrix	transpose()
		//転置行列を返します。
		System.out.println("A.transpose() = ");
		System.out.println(A.transpose().toString());

		//Aにサンプルデータを入れる。
		A = new Matrix(Csv.read(".\\csv\\data.csv"));
		
		//Matrix	covariance()
		//共分散
		System.out.println("A.covariance(0,2) = ");
		System.out.println(A.covariance(0,2));

		//Matrix	correlation()
		//相関係数
		System.out.println("A.correlation(0,2) = ");
		System.out.println(A.correlation(0,2));

		//Matrix	correlation()
		//相関係数行列
		System.out.println("A.correlation() = ");
		System.out.println(A.correlation().toString());

		//Matrix	determination()
		//決定変数
		System.out.println("A.determination(0,2) = ");
		System.out.println(A.determination(0,2));
		
		//Matrix	singleRegression(0,1)
		//単回帰式
		System.out.println("A.singleRegression(0,2) = ");
		System.out.println(A.singleRegression(0,2));
		
		//Matrix	multipleRegression(0,1)
		//重回帰式
		System.out.println("A.multipleRegression(0,2) = ");
		System.out.println(A.multipleRegression(0,2));
		
		//Matrix	regression(0,1)
		//回帰式で推定した値を求める
		System.out.println("A.regression(0,2) = ");
		System.out.println(A.regression(0,2));
		
		//Matrix	multipleRegression(0,1)
		//重相関係数の二乗の決定係数を求める
		System.out.println("A.determination(0,2) = ");
		System.out.println(A.determination(0,2));

	}
}
