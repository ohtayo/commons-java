package jp.ohtayo.commons.math;

import jp.ohtayo.commons.random.Random;

/**
* 行列クラスです。<br>
* 行列同士の四則演算や、転置行列・逆行列・行列式等の計算を行うメソッドを含みます。<br>
* 内部行列はdouble[][]で表します。<br>
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
*/
public class Matrix{

	protected double[][] matrix;
	/**
	 * 行数と列数を指定して行列を生成します。
	 * @param row
	 * @param column
	 */
	public Matrix(int row, int column)
	{
		this.matrix = new double[row][column];
	}
	/**
	 * 行数と列数を指定して行列を生成します。
	 * @param dimension
	 */
	public Matrix(int dimension)
	{
		matrix = new double[dimension][dimension];
	}
	/**
	 * 値を指定して行列を生成します。
	 * @param target 行列の初期値
	 */
	public Matrix(double[][] target)
	{
		matrix = new double[target.length][target[0].length];
		for(int i=0; i<target.length; i++)
			System.arraycopy(target[i], 0, matrix[i], 0, target[0].length);
	}
	/**
	 * 値を指定して行列を生成します。1行n列もしくはn行1列の行列を作成します．
	 * @param target 行列の初期値
	 */
	public Matrix(double[] target, String direction)
	{
		matrix = new double[1][target.length];
		System.arraycopy(target, 0, matrix[0], 0, target.length);
		if(direction == DIRECTION_COLUMN)
		{
			matrix = this.transpose().get();
		}
	}
	/**
	 * 全ての要素がvalueの行列を生成します。
	 * @param row 行数
	 * @param column 列数
	 * @param value 値
	 */
	public Matrix(int row, int column, double value)
	{
		matrix = new double[row][column];
		for(int r=0; r<row; r++){
			for(int c=0; c<column; c++){
				matrix[r][c] = value;
			}
		}
	}
	public static final String CONSTRUCT_RANDOM = "random";
	public static final String CONSTRUCT_IDENTITY = "identity";
	/**
	 * ランダムな行列もしくは単位行列を生成します。
	 * @param row 行数
	 * @param column 列数
	 * @param string CONSTRUCT_RANDOM or CONSTRUCT_IDENTITY
	 */
	public Matrix(int row, int column, String string)
	{
		if(string == CONSTRUCT_RANDOM)
		{
			Random random = new Random();
			matrix = random.matrix(row, column);
		}
		else if(string == CONSTRUCT_IDENTITY)
		{
			matrix = new double[row][column];
			for(int r=0; r<row; r++){
				for(int c=0; c<column; c++){
					if( r == c )
						matrix[r][c] = 1;
					else
						matrix[r][c] = 0;
				}
			}
		}
	}
	/**
	 * 入力と同じ行列を別メモリに生成します。
	 * @param value コピーしたい行列
	 */
	public Matrix( Matrix value )
	{
		matrix = new double[value.length()][value.columnLength()];
		for(int i=0; i<value.length(); i++)
			System.arraycopy(value.getRow(i).get(), 0, matrix[i], 0, value.columnLength());
	}


	//------------------------------------------------------------------------//
	// 行列の操作関数群                                                       //
	//------------------------------------------------------------------------//
	/**
	* 行列を文字列として返します。
	* @return 変換した文字列
	*/
	public String toString()
	{
		String str = "";

		for(int i=0; i<length(); i++ ){
			str += "  ";
			for(int j=0; j<columnLength(); j++ ){
				str += String.valueOf(matrix[i][j]) + " ";
//				str += String.format("%.5g ", matrix[i][j]);
			}
			str += "\r\n";
		}
		return str;
	}

	/**
	 * 行列の行数を返します。
	 * @return 行数
	 */
	public int length()
	{
		return matrix.length;
	}
	/**
	 * 行列の列数を返します。
	 * @return 列数
	 */
	public int columnLength()
	{
		return matrix[0].length;
	}
	/**
	 * 行列のサイズを返します。
	 * @return 行列のサイズ
	 */
	public int[] size()
	{
		int[] result = new int[2];
		result[0] = length();
		result[1] = columnLength();
		return result;
	}
	/**
	 * 行列の中身を返します。
	 * @param row 行
	 * @param column 列
	 * @return matrixの中身
	 */
	public double get(int row, int column)
	{
		return matrix[row][column];
	}
	/**
	 * 行列の値を設定します。
	 * @param row 設定する行
	 * @param column 設定する列
	 * @param target 設定する値
	 */
	public void set(int row, int column, double target)
	{
		matrix[row][column] = target;
	}
	/**
	 * 行列の中身を返します。
	 * @return matrixの中身
	 */
	public double[][] get()
	{
		return matrix;
	}
	/**
	 * 行列の値を設定します。
	 * @param target
	 */
	public void set(double[][] target)
	{
		matrix = new double[target.length][target[0].length];
		for(int i=0; i<target.length; i++)
			System.arraycopy(target[i], 0, matrix[i], 0, target[0].length);
	}
	/**
	* 行列の行を抽出します。
	* @param row 抽出する行番号
	* @return 取り出した行
	*/
	public Vector getRow( int row )
	{
		return new Vector(matrix[row]);
	}
	/**
	 * 行列の指定列を抽出します。
	 * @param rows 抽出する列番号の配列
	 * @return 取り出した行列
	 */
	public Matrix getRows( int[] rows )
	{
		Matrix result = new Matrix(rows.length, this.columnLength());
		for (int r=0; r<rows.length; r++){
			result.setRow(r, this.getRow(rows[r]));
		}
		return result;
	}
	/**
	* 行列の行に配列を設定します。
	* @param row 設定する行の番号
	* @param target 設定する行の値
	*/
	public void setRow( int row, Vector target)
	{
		System.arraycopy(target.get(), 0, matrix[row], 0, target.length());
	}
	/**
	* 行列の列を抽出します。
	* @param column 抽出する列番号
	* @return 取り出した列
	*/
	public Vector getColumn( int column )
	{
		Vector result = new Vector(length());
		for (int i=0; i<result.length(); i++)
		{
			result.set(i, matrix[i][column]);
		}
		return result;
	}
	/**
	 * 行列の指定列を抽出します。
	 * @param columns 抽出する列番号の配列
	 * @return 取り出した行列
	 */
	public Matrix getColumns( int[] columns )
	{
		Matrix result = new Matrix(this.length(), columns.length);
		for (int c=0; c<columns.length; c++){
			result.setColumn(c, this.getColumn(columns[c]));
		}
		return result;
	}
	/**
	* 行列の列に配列を設定します。
	* @param column 設定する列の番号
	* @param target 設定する列の値
	*/
	public void setColumn( int column, Vector target )
	{
		if (length() != target.length())
			return;

		for (int i=0; i<length(); i++)
		{
			matrix[i][column] = target.get(i);
		}
	}

	/**
	 * 行列の一部を返す。
	 * @param row 欲しい行列の最初の行番号
	 * @param rowLength 行の長さ
	 * @param column 欲しい行列の最初の列番号
	 * @param columnLength 列の長さ
	 * @return 抽出した行列
	 */
	public Matrix getSubMatrix(int row, int rowLength, int column, int columnLength)
	{
		double[][] result = new double[rowLength][columnLength];
		for(int i=row; i<row+rowLength; i++)
		{
			System.arraycopy(this.getRow(i).get(), column, result[i-row], 0, columnLength);
		}
		return new Matrix(result);
	}

	/**
	 * 行列の一部を抜き出して返す。
	 * 例：行列の指定列だけ抽出したい場合
	 * 		int[] columns = {1, 2, 5, 7};	//抽出したい列
	 * 		int[] index =  {1, 2, 5, 7};
	 * 		sampleMatrix.getSubMatrix(index, columns);	//抽出
	 * @param rows 欲しい行列の行番号配列
	 * @param columns 欲しい行列の列番号配列
	 * @return 抽出した行列
	 */
	public Matrix getSubMatrix(int[] rows, int[] columns)
	{
		return this.getColumns(columns).getRows(rows);
	}

	/**
	 * 行列の一部を別の行列の値に設定する。
	 * @param row 挿入先の最初の行番号
	 * @param rowLength 行長さ
	 * @param column 挿入先の最初の列番号
	 * @param columnLength 列長さ
	 * @param target 挿入する値を持つ行列
	 */
	public void setSubMatrix(int row, int rowLength, int column, int columnLength, Matrix target)
	{
		for(int i=row; i<row+rowLength; i++)
		{
			System.arraycopy(target.getRow(i-row).get(), 0, matrix[i], column, columnLength);
		}
	}
	/**
	* 行番号を指定すると、その番号の行を排除した行列を返します。
	* @param number 排除したい行
	* @return 排除した行列
	*/
	public Matrix drop(int number)
	{
		double[][] result = new double[length()-1][columnLength()];
		int row=0;
		for(int i=0; i<length(); i++ ){
			if(i!=number){
				System.arraycopy(matrix[i], 0, result[row], 0, columnLength());
				row++;
			}
		}
		return new Matrix(result);
	}

	/**
	 * 行番号か列番号と方向を指定すると、その番号の行or列を削除した行列を返します。
	 * @param number 行番号or列番号
	 * @param direction 行or列
	 * @return 削除した行列
	 */
	public Matrix drop(int number, String direction) {
		if(direction == DIRECTION_ROW) {
			return drop(number);
		}
		else if(direction == DIRECTION_COLUMN) {
			return transpose().drop(number).T();
		}
		else {
			return null;
		}
	}
	/**
	* 行列の要素を指定すると、その行・列を排除した行列を返します。
	* @param row 排除したい行
	* @param column 排除したい列
	* @return 排除した行列
	*/
	public Matrix spritMatrix(int row, int column)
	{
		double[][] result = new double[length()-1][columnLength()-1];
		int m, n;
		m=0;
		for(int i=0; i<length(); i++ ){
			if(i!=row){
				n=0;
				for(int j=0; j<columnLength(); j++ ){
					if(j!=column){
						result[m][n] = matrix[i][j];
						n++;
					}
				}
				m++;
			}
		}
		return new Matrix(result);
	}


	public static final String DIRECTION_ADD_BOTTOM = "bottom";
	public static final String DIRECTION_ADD_RIGHT = "right";

	/**
	* 行列を連結します<br>
	* @param target 連結する行列
	* @param direction 連結方向
	* @return 変換した文字列
	*/
	public Matrix add( Matrix target, String direction)
	{
		double[][] result = null;
		if(direction == DIRECTION_ADD_BOTTOM)
		{
			result = new double[length()+target.length()][columnLength()];
			for(int i=0; i<length(); i++ ){
				result[i] = matrix[i];
			}
			for(int i=0; i<target.length(); i++ ){
				result[i+length()] = target.getRow(i).get();
			}
		}
		else if(direction == DIRECTION_ADD_RIGHT)
		{
			result = new double[length()][columnLength()+target.columnLength()];
			for(int i=0; i<length(); i++ ){
				result[i] = new Vector(matrix[i]).add(target.getRow(i)).get();
			}
		}
		return new Matrix(result);
	}

	/**
	* 90, 180, 270°に回転した行列を返します。
	* @param angle 回転角度
	*/
	public Matrix rotate(int angle)
	{
		Matrix result = null;
		if(angle == 180)
		{
			result = invert(INVERT_UPPER_BOTTOM).invert(INVERT_LEFT_RIGHT);
		}
		else if(angle == 90)
		{
			result = new Matrix(columnLength(), length());
			for (int i=0; i<length(); i++)
			{
				result.setColumn(length()-i-1, getRow(i));
			}
		}
		else if(angle == 270)
		{
			result = rotate(90).rotate(180);
		}

		return result;
	}

	public static final String INVERT_UPPER_BOTTOM = "upper_bottom";
	public static final String INVERT_LEFT_RIGHT = "left_right";
	/**
	* 上下もしくは左右に反転した行列を返します。
	* @param string 反転方向 INVERT_UPPER_BOTTOM or INVERT_LEFT_RIGHT
	*/
	public Matrix invert(String string)
	{
		Matrix result = new Matrix(length(),columnLength());
		if(string == INVERT_UPPER_BOTTOM)
		{
			for(int c=0; c<columnLength(); c++){
				result.setColumn(c, getColumn(c).invert());
			}
		}
		else if(string == INVERT_LEFT_RIGHT)
		{
			for(int r=0; r<length(); r++){
				result.setRow(r, getRow(r).invert());
			}
		}
		return result;
	}

	//------------------------------------------------------------------------//
	// 行列の演算関数群                                                       //
	//------------------------------------------------------------------------//
	/**
	* 行列の各要素の加算結果を返します。
	* @param target 行列
	* @return 加算された行列
	*/
	public Matrix plus( Matrix target )
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]+target.get(i,j);
			}
		}
		return new Matrix(result);
	}
	/**
	* 行列の各要素に定数を加算します。
	* @param target 定数
	* @return 加算された行列
	*/
	public Matrix plus(double target)
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]+target;
			}
		}
		return new Matrix(result);
	}

	/**
	* 行列の各要素を減算します。
	* @param target 引く行列
	* @return 減算された行列
	*/
	public Matrix minus(Matrix target)
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]-target.get(i, j);
			}
		}
		return new Matrix(result);
	}
	/**
	* 行列の各要素から定数を引きます。
	* @param target 定数
	* @return 減算された行列
	*/
	public Matrix minus(double target)
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]-target;
			}
		}
		return new Matrix(result);
	}

	/**
	* 行列の積を計算します。
	* @param target 行列
	* @return 積の行列
	*/
	public Matrix multiply(Matrix target)
	{
		//自分の列数とかける行列の行数が同じでなければ掛け算できない。
		if(columnLength() != target.length())	return null;

		double temp;
		double[][] result = new double[length()][target.columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<target.columnLength(); j++ ){
				temp = 0;
				for (int k=0; k<columnLength(); k++ ){
					temp += matrix[i][k] * target.get(k, j);
				}
				result[i][j] = temp;
			}
		}
		return new Matrix(result);
	}

	/**
	* 行列を定数倍します。
	* @param target 定数
	* @return 定数倍の行列
	*/
	public Matrix multiply(double target)
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]*target;
			}
		}
		return new Matrix(result);
	}

	/**
	* 行列の各要素ごとに積を計算します。
	* @param target かけられる行列
	* @return 計算した積の行列
	*/
	public Matrix multiplyEach(Matrix target)
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ ){
			for(int j=0; j<columnLength(); j++ ){
				result[i][j] = matrix[i][j]*target.get(i,j);
			}
		}
		return new Matrix(result);
	}

	/**
	* 転置行列を返します。
	* @return 転置した行列
	*/
	public Matrix transpose()
	{
		double[][] result = new double[columnLength()][length()];

		for(int c=0; c<columnLength(); c++ ){
			for(int r=0; r<length(); r++ ){
				result[c][r] = matrix[r][c];
			}
		}
		return new Matrix(result);
	}
	/**
	 * 転置行列を返します<br>
	 * transpose()のラッパーです<br>
	 * @return 転置した行列
	 */
	public Matrix T()
	{
		return transpose();
	}

	/**
	* 行列の行列式を返します。
	* @return 行列式
	*/
	public double determinant()
	{
		//正方行列でない
		if(length() != columnLength()){ return Double.NaN; }
		//行列が1*1
		else if(length() == 1){	return matrix[0][0]; }
		//行列が2*2
		else if(length() == 2){	return (matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0]); }
		//行列が3*3以上
		else{
			int i;
			double det=0;
			Matrix cofactorMatrix = new Matrix(length()-1,columnLength()-1);

			for( i=0; i<length(); i++ )
			{
				cofactorMatrix = spritMatrix(i,0);
				if( i%2 == 0)	det += matrix[i][0]*cofactorMatrix.determinant();
				else			det -= matrix[i][0]*cofactorMatrix.determinant();
			}
			return det;
		}
	}
	public double det() {
		return determinant();
	}

	/**
	* 行列の逆行列を返します。
	* @return 逆行列
	*/
	public Matrix inverse()
	{
		double[][] result = new double[length()][columnLength()];

		for(int i=0; i<length(); i++ )
		{
			for(int j=0; j<columnLength(); j++ )
			{
				if( (i+j)%2 == 0)	result[i][j] = spritMatrix(i, j).determinant();
				else				result[i][j] = (-1)*spritMatrix(i, j).determinant();
			}
		}
		return new Matrix(result).transpose().multiply(  1/determinant() );
	}


	//------------------------------------------------------------------------//
	// 行列の数値計算                                                         //
	//------------------------------------------------------------------------//
	public static final String DIRECTION_ROW = "row";
	public static final String DIRECTION_COLUMN = "column";

	/**
	* 行列の各列もしくは各行ごとの最大値を返します。<br>
	* @param direction 最大値を求める方向("row"もしくは"column")
	* @return 最大値配列
	*/
	public Vector max(String direction)
	{
		Vector max=null;
		if(direction == "row")
		{
			max = new Vector(length());
			for (int i=0; i<max.length(); i++)
			{
				max.set(i, getRow(i).max());
			}
		}
		else if(direction == "column")
		{
			max = new Vector(columnLength());
			for (int i=0; i<max.length(); i++)
			{
				max.set(i, getColumn(i).max());
			}
		}
		return max;
	}
	/**
	* 行列の各列もしくは各行ごとの最大値を返します。<br>
	* @param direction 最大値を求める方向("row"もしくは"column")
	* @return 最大値配列
	*/
	public Vector max(String direction, int[] index)
	{
		Vector max=null;
		double temp;
		int[] idx= new int[1];
		if(direction == "row")
		{
			max = new Vector(length());
			for (int i=0; i<max.length(); i++)
			{
				temp = getRow(i).max(idx);
				max.set(i, temp);
				index[i] = idx[0];
			}
		}
		else if(direction == "column")
		{
			max = new Vector(columnLength());
			for (int i=0; i<max.length(); i++)
			{
				temp = getColumn(i).max(idx);
				max.set(i, temp);
				index[i] = idx[0];
			}
		}
		return max;
	}
	/**
	* 行列の全要素の最大値を返します。<br>
	* @return 最大値
	*/
	public double max()
	{
		return this.max(DIRECTION_ROW).max();
	}
	/**
	* 行列の全要素の最大値とその位置を返します。<br>
	* @param index 最大値の位置配列(長さ2のint)
	* @return 最大値
	*/
	public double max(int[] index)
	{
		int[] idx = new int[length()];
		Vector temp;
		double result;

		temp = this.max(DIRECTION_ROW, idx);
		result = temp.max(index);
		index[1] = idx[index[0]];
		return result;
	}
	/**
	* 行列の各列もしくは各行ごとの最小値を返します。<br>
	* @param direction 最小値を求める方向("row"もしくは"column")
	* @return 最小値配列
	*/
	public Vector min(String direction)
	{
		Vector min=null;
		if(direction == "row")
		{
			min = new Vector(length());
			for (int i=0; i<min.length(); i++)
			{
				min.set(i, getRow(i).min());
			}
		}
		else if(direction == "column")
		{
			min = new Vector(columnLength());
			for (int i=0; i<min.length(); i++)
			{
				min.set(i, getColumn(i).min());
			}
		}
		return min;
	}
	/**
	* 行列の各列もしくは各行ごとの最小値とその位置を返します。<br>
	* @param direction 最小値を求める方向("row"もしくは"column")
	* @param index 最小値の位置配列。長さを含めて正しく初期化されていること。new int[length()]もしくはnew int[columnLength()]
	* @return 最小値配列
	*/
	public Vector min(String direction, int[] index)
	{
		Vector min=null;
		double temp;
		int[] idx= new int[1];
		if(direction == "row")
		{
			min = new Vector(length());
			for (int i=0; i<min.length(); i++)
			{
				temp = getRow(i).min(idx);
				min.set(i, temp);
				index[i] = idx[0];
			}
		}
		else if(direction == "column")
		{
			min = new Vector(columnLength());
			for (int i=0; i<min.length(); i++)
			{
				temp = getColumn(i).min(idx);
				min.set(i, temp);
				index[i] = idx[0];
			}
		}
		return min;
	}
	/**
	* 行列の全要素の最小値を返します。<br>
	* @return 最小値
	*/
	public double min()
	{
		return this.min(DIRECTION_ROW).min();
	}
	/**
	* 行列の全要素の最小値とその位置を返します。<br>
	* @param index 最小値の位置配列(長さ2のint)
	* @return 最小値
	*/
	public double min(int[] index)
	{
		int[] idx = new int[length()];
		Vector temp;
		double result;

		temp = this.min(DIRECTION_ROW, idx);
		result = temp.min(index);
		index[1] = idx[index[0]];
		return result;
	}
	/**
	* 行列の各列もしくは各行ごとの平均値を返します。<br>
	* @param direction 平均値を求める方向("row"もしくは"column")
	* @return 平均値配列
	*/
	public Vector mean(String direction)
	{
		Vector mean=null;
		if(direction == "row")
		{
			mean = new Vector(length());
			for (int i=0; i<mean.length(); i++)
			{
				mean.set(i, getRow(i).mean());
			}
		}
		else if(direction == "column")
		{
			mean = new Vector(columnLength());
			for (int i=0; i<mean.length(); i++)
			{
				mean.set(i, getColumn(i).mean());
			}
		}
		return mean;
	}
	/**
	* 行列の全要素の平均を返します。<br>
	* @return 平均値
	*/
	public double mean()
	{
		return this.mean(DIRECTION_ROW).mean();
	}

	/**
	* 行列の要素を四捨五入して返します。<br>
	* @return 四捨五入した行列
	*/
	public Matrix round()
	{
		Matrix result = new Matrix(length(), columnLength());
		for(int i=0; i<result.length(); i++)
		{
			result.setRow(i, getRow(i).round());
		}
		return result;
	}

	/**
	* 行列の絶対値を返します。<br>
	* @return 絶対値
	*/
	public Matrix abs()
	{
		Matrix result = new Matrix(length(), columnLength());
		for(int i=0; i<result.length(); i++)
		{
			result.setRow(i, getRow(i).abs());
		}
		return result;
	}

	/**
	* 行列の平方根を返します。<br>
	* @return 平方根
	*/
	public Matrix sqrt()
	{
		Matrix result = new Matrix(length(), columnLength());
		for(int i=0; i<result.length(); i++)
		{
			result.setRow(i, getRow(i).sqrt());
		}
		return result;
	}

	/**
	* 行列の二乗を返します。<br>
	* @return 二乗値
	*/
	public Matrix square()
	{
		return multiply(this);
	}
	/**
	* 行列の個々の要素の二乗を返します。<br>
	* @return 二乗値
	*/
	public Matrix squareEach()
	{
		return multiplyEach(this);
	}

	/**
	* 行列の各列もしくは各行ごとの平均値を返します。<br>
	* @param direction 平均値を求める方向("row"もしくは"column")
	* @return 平均値配列
	*/
	public Vector sum(String direction)
	{
		Vector sum=null;
		if(direction == "row")
		{
			sum = new Vector(length());
			for (int i=0; i<sum.length(); i++)
			{
				sum.set(i, getRow(i).sum());
			}
		}
		else if(direction == "column")
		{
			sum = new Vector(columnLength());
			for (int i=0; i<sum.length(); i++)
			{
				sum.set(i, getColumn(i).sum());
			}
		}
		return sum;
	}
	/**
	 * 行列の各要素の和を返します。<br>
	 * @return 和
	 */
	public double sum()
	{
		return this.sum(DIRECTION_ROW).sum();
	}

	/**
	* 行列を各列ごとに正規化して返します。<br>
	* 行列と最大値配列・最小値配列を与えると、各列ごとに最大値を1, 最小値を0として変換した行列を返します。<br>
	* 最大値配列・最小値配列の長さは行列の列数と等しい必要があります。<br>
	* @param maxValue 正規化の最大値配列
	* @param minValue 正規化の最小値配列
	* @return 正規化された行列
	*/
	public Matrix normalize(double[] maxValue, double[] minValue)
	{
		Matrix result = new Matrix(length(), columnLength());
		for (int i=0; i<columnLength(); i++)
		{
			 result.setColumn(i, getColumn(i).normalize(maxValue[i], minValue[i]));
		}
		return result;
	}
	/**
	* 行列を各列ごとに正規化して返します。<br>
	* 最大値・最小値は各列のデータから計算します。<br>
	* @return 正規化された行列
	*/
	public Matrix normalize()
	{
		Matrix result = new Matrix(length(), columnLength());
		for (int i=0; i<columnLength(); i++)
		{
			result.setColumn(i, getColumn(i).normalize());
		}
		return result;
	}

	/**
	 * x列目とy列目のデータの共分散Cxyを求めます。<br>
	 * 共分散Cov(X,Y) = E[(X-E[X])(Y-E[Y])]
	 * @param x 列番号
	 * @param y 列番号
	 * @return 共分散
	 */
	public double covariance(int y, int x)
	{
		Vector yData = getColumn(y);
		Vector xData = getColumn(x);
		return yData.minus(yData.mean()).multiply(xData.minus(xData.mean())).mean();
	}

	//------------------------------------------------------------------------//
	// 行列の統計解析処理                                                     //
	//------------------------------------------------------------------------//
	/**
	 * x列目とy列目のデータの相関係数r_xyを求めます
	 * @param x 列番号
	 * @param y 列番号
	 * @return 相関係数
	 */
	public double correlation(int y, int x)
	{
		Vector yData = getColumn(y);
		Vector xData = getColumn(x);
		return covariance(y,x) / (yData.standardDeviation()*xData.standardDeviation());
	}

	/**
	 * データ列同士の相関行列を求めます。
	 * @return 相関行列
	 */
	public Matrix correlation()
	{
		Matrix result = new Matrix(columnLength(), columnLength());

		for(int r=0; r<columnLength(); r++){
			for(int t=0; t<columnLength(); t++){
				result.set(r, t, correlation(r, t));
			}
		}
		return result;
	}

	/**
	 * 単回帰式の決定係数を求めます。
	 * @param x 列番号
	 * @param y 列番号
	 * @return 決定係数
	 */
	public double determination(int y, int x)
	{
		return Numeric.square( correlation(y, x) );
	}

	/**
	 * 単回帰式y=a+bxの係数a, bを求める。
	 * @param x 被説明変数の列番号
	 * @param y 説明変数の列番号
	 * @return 係数([0]がb, [1]がa)
	 */
	public Vector singleRegression(int y, int x)
	{
		Vector xData = getColumn(x);	//被説明変数
		Vector yData = getColumn(y);	//説明変数

		double[] result = new double[2];
		result[1] = xData.minus(xData.mean()).multiply(yData.minus(yData.mean())).sum()
				/ xData.minus(xData.mean()).square().sum();	//係数b
		result[0] = yData.mean()-result[1] * xData.mean();	//係数a

		return new Vector(result);
	}

	/**
	 * 重回帰式y=b0+ b1*x1+ b2*x2+ ...の係数bを求める。
	 * 参考：https://www.sist.ac.jp/~suganuma/kougi/other_lecture/SE/multi/multi.htm
	 * @param y 被説明変数の列番号
	 * @param x 説明変数の列番号配列
	 * @return 係数配列
	 */
	public Vector multipleRegression(int y, int... x)
	{
		//XYbの入れ物を作る
		Matrix X = new Matrix(length(), x.length+1);
		Matrix Y = new Matrix(length(), 1);
		Matrix b = new Matrix(x.length+1, 1);

		//XとYを用意
		X.setColumn(0, new Vector(length(), 1));
		for(int i=1; i<=x.length; i++){
			X.setColumn(i, getColumn(x[i-1]));
		}
		Y.setColumn(0, getColumn(y));

		//b=[X^T×X]^-1×X^T×Yを計算
		//Matrix xt = X.transpose();
		//Matrix xtx = xt.multiply(X);
		//Matrix xtx1= xtx.inverse();
		//Matrix xtx1xt=xtx1.multiply(xt);
		//b = xtx1xt.multiply(Y);
		b = X.transpose().multiply(X).inverse().multiply(X.transpose()).multiply(Y);

		return b.getColumn(0);	//bは1行n列なので列だけ取り出す。
	}

	/**
	 * 回帰値(重回帰式で推定した値y)を求める
	 * @param y 被説明変数の列番号
	 * @param x 説明変数の列番号配列
	 * @return 回帰値
	 */
	public Vector regression(int y, int... x)
	{
		Vector b = multipleRegression(y, x);
		Vector result = new Vector(length());
		double temp = 0;

		for(int i=0; i<length(); i++){
			temp = b.get(0);
			for(int t=1; t<b.length(); t++)
			{
				temp += b.get(t)*get(i,x[t-1]);
			}
			result.set(i, temp);
		}

		return result;
	}

	/**
	 * 重回帰の決定係数η^2を求める。<br>
	 * 重回帰式で完全に説明できれば1、説明できなければ0になる。<br>
	 * ηは重相関係数<br>
	 * @param y 被説明変数の列番号
	 * @param x 説明変数の列番号配列
	 * @return 決定係数
	 */
	public double determination(int y, int... x)
	{
		Vector reg = regression(y, x);
		Vector yData = getColumn(y);

		return 1-( reg.minus(yData).square().sum() ) / (yData.minus(yData.mean()).square().sum());
	}
}

//file end.-------------------------------------------------------------------//
