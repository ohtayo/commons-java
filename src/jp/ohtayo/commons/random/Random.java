package jp.ohtayo.commons.random;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import jp.ohtayo.commons.log.Logging;

/**
 * 乱数を生成するクラスです。<br>
 * 乱数生成手法を選択します。<br>
 * SFMT : SIMD-oriented Fast Mersenne Twister<br>
 * MT : Mersenne twister<br>
 * XOR : Xor shift<br>
 * LCG : Linear congruential generators(java.util.Random クラスの生成手法)<br>
 * 指定例：Random r = new Random(Random.SFMT, seed);<br>
 * <br>
 * 乱数生成に加え、ランダム配列・行列生成や配列シャッフルを行うメソッドを提供します。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class Random{

	/** 乱数生成手法クラス						*/	private Class<?> cClass;
	/** 乱数生成手法クラスのインスタンス			*/	private Object object;
	/** 乱数生成手法クラスのnextDouble メソッド	*/	private Method nextDouble;
	/** 乱数生成手法クラスのnextFloat メソッド		*/	private Method nextFloat;
	/** 乱数生成手法クラスのnextInt メソッド		*/	private Method nextInt;
	/** 乱数生成手法クラスのnextBoolean メソッド	*/	private Method nextBoolean;
	/** 乱数生成手法クラスのnextIntArg メソッド	*/	private Method nextIntArg;
	/** 乱数生成手法クラスのnextBytes メソッド		*/	private Method nextBytes;
	/** 乱数生成手法クラスのsetSeedメソッド		*/	private Method setSeed;

	/** SFMT 乱数生成クラス	*/	public static final String SFMT	= "jp.ohtayo.commons.random.SFMT";
	/** XOR 乱数生成クラス	*/	public static final String XOR	= "jp.ohtayo.commons.random.Xor128";
	/** LCG 乱数生成クラス	*/	public static final String LCG	= "java.util.Random";

	/**
	* デフォルトコンストラクタ<br>
	* 乱数生成手法にXORを設定し、シードを初期化します。<br>
	*/
	public Random()
	{
		try{
			cClass = Class.forName(SFMT);
			object = cClass.newInstance();
			getMethods();
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* コンストラクタ<br>
	* 乱数生成手法にXORを設定し、シードを指定値に設定します。<br>
	* @param seed シード値の設定値
	*/
	public Random(long seed)
	{
		try{
			cClass = Class.forName(SFMT);
			Constructor<?> constructor = cClass.getConstructor(long.class);
			object = constructor.newInstance(seed);
			getMethods();
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* コンストラクタ<br>
	* 乱数生成手法を指定された手法に設定し、シードを初期化します。<br>
	* @param method 乱数生成手法のクラス名。以下の値のどれかを設定します。<br>
	* Random.SFMT<br>
	* Random.MT<br>
	* Random.XOR<br>
	* Random.LCG<br>
	*/
	public Random(String method)
	{
		try{
			cClass = Class.forName(method);
			object = cClass.newInstance();
			getMethods();
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* コンストラクタ<br>
	* 乱数生成手法を指定された手法に設定し、シードを指定値に設定します。<br>
	* @param method 乱数生成手法のクラス名。以下の値のどれかを設定します。<br>
	* Random.SFMT<br>
	* Random.MT<br>
	* Random.XOR<br>
	* Random.LCG<br>
	* @param seed シード値の設定値
	*/
	public Random(String method, long seed)
	{
		try{
			cClass = Class.forName(method);
			Constructor<?> constructor = cClass.getConstructor(long.class);
			object = constructor.newInstance(seed);
			getMethods();
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* コンストラクタで呼び出されるメソッド取得関数<br>
	* 乱数生成手法クラスの各メソッドを取得して格納しておきます。<br>
	*/
	private void getMethods()
	{
		try{
			nextDouble	= object.getClass().getMethod("nextDouble");
			nextFloat	= object.getClass().getMethod("nextFloat");
			nextInt		= object.getClass().getMethod("nextInt");
			nextBoolean = object.getClass().getMethod("nextBoolean");
			nextIntArg	= object.getClass().getMethod("nextInt", int.class);
			nextBytes	= object.getClass().getMethod("nextBytes", byte[].class);
			setSeed		= object.getClass().getMethod("setSeed", long.class);
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* 0～1のdouble 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/
	public double nextDouble()
	{
		Object ret = null;
		try
		{
			ret = nextDouble.invoke(object);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		return (Double)ret;
	}
	/**
	* 0～1のfloat 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/
	public float nextFloat()
	{
		Object ret = null;
		try
		{
			ret = nextFloat.invoke(object);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		return (Float)ret;
	}
	/**
	* int 型の一様乱数を生成します。<br>
	* @return 一様乱数
	*/
	public int nextInt()
	{
		Object ret = null;
		try
		{
			ret = nextInt.invoke(object);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		return (Integer)ret;
	}
	/**
	* true / false をランダムに返します。<br>
	* @return true / false
	*/
	public boolean nextBoolean()
	{
		Object ret = null;
		try
		{
			ret = nextBoolean.invoke(object);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		return (Boolean)ret;
	}

	/**
	* int 型の0から指定された値の範囲(0は含むが、その指定された値は含まない)の一様乱数を生成します。<br>
	* @param n 範囲指定値<br>
	* @return 0から指定された値の範囲の一様乱数<br>
	*/
	public int nextInt(int n)
	{
		Object ret = null;
		try
		{
			ret = nextIntArg.invoke(object, n);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
		return (Integer)ret;
	}

	/**
	* ランダムバイト配列を生成し、指定されたバイト配列に配置します。
	* @param bytes ランダムバイト配列を配置する配列
	*/
	public void nextBytes(byte[] bytes)
	{
		try
		{
			nextBytes.invoke(object, bytes);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
	}

	/**
	* long 型のシードを使って乱数のシードを設定します。
	* @param seed 乱数のシード
	*/
	public void setSeed(long seed)
	{
		try
		{
			setSeed.invoke(object, seed);
		}
		catch(Exception e)
		{
			Logging.logger.severe(e.toString());
		}
	}

	//------------------アプリケーション用関数-------------------//
	/**
	* 0～1の一様乱数を生成します。<br>
	* nextDouble()のラッパーです。<br>
	* @return 0～1までの一様乱数
	*/
	public double rand()
	{ return nextDouble(); }

	/**
	* 0～1の一様乱数の配列を生成します。<br>
	* array()のラッパーです。<br>
	* @param length 配列の長さ
	* @return 0～1までの一様乱数の配列
	*/
	public double[] rand(int length)
	{ return array(length); }

	/**
	* 0～1の一様乱数の行列を生成します。<br>
	* matrix()のラッパーです。<br>
	* @param row 行列の行数
	* @param column 行列の列数
	* @return 0～1までの一様乱数の行列
	*/
	public double[][] rand(int row, int column)
	{	return matrix(row, column);	}

	/**
	* 0～1の一様乱数の配列を生成します。
	* @param length 配列の長さ
	* @return 0～1までの一様乱数の配列
	*/
	public double[] array(int length)
	{
		double[] result = new double[length];
		for (int i=0; i<length; i++)
		{
			result[i] = nextDouble();
		}
		return result;
	}

	/**
	* 0～1の一様乱数の行列を生成します。
	* @param row 行列の行数
	* @param column 行列の列数
	* @return 0～1までの一様乱数の行列
	*/
	public double[][] matrix(int row, int column)
	{
		double[][] result = new double[row][column];

		for (int j=0; j<column; j++)
		{
			for (int i=0; i<row; i++)
			{
					result[i][j] = nextDouble();
			}
		}
		return result;
	}

	/**
	 * 平均0分散1の正規分布に従った正規乱数を発生させる。
	 * @return 正規乱数
	 */
	public double randomNormalDistribution()
	{
		double sum=0;
		for(int i=0; i<12; i++)	sum+=nextDouble();
		return sum-6.0;
	}

	/**
	* 0～1の一様乱数の配列を生成します。
	* @param length 配列の長さ
	* @return 0～1までの一様乱数の配列
	*/
	public double[] arrayNormal(int length)
	{
		double[] result = new double[length];
		for (int i=0; i<length; i++)
		{
			result[i] = randomNormalDistribution();
		}
		return result;
	}

	/**
	* 0～1の一様乱数の行列を生成します。
	* @param row 行列の行数
	* @param column 行列の列数
	* @return 0～1までの一様乱数の行列
	*/
	public double[][] matrixNormal(int row, int column)
	{
		double[][] result = new double[row][column];

		for (int j=0; j<column; j++)
		{
			for (int i=0; i<row; i++)
			{
					result[i][j] = randomNormalDistribution();
			}
		}
		return result;
	}

	/**
	 * 平均0分散1の正規分布に従った正規乱数を発生させる。
	 * randomNormalDistribution()のラッパー
	 * @return 正規乱数
	 */
	public double randn()
	{	return randomNormalDistribution();	}
	/**
	* 0～1の一様乱数の配列を生成します。<br>
	* array()のラッパーです。<br>
	* @param length 配列の長さ
	* @return 0～1までの一様乱数の配列
	*/
	public double[] randn(int length)
	{ return arrayNormal(length); }
	/**
	* 0～1の一様乱数の行列を生成します。<br>
	* matrix()のラッパーです。<br>
	* @param row 行列の行数
	* @param column 行列の列数
	* @return 0～1までの一様乱数の行列
	*/
	public double[][] randn(int row, int column)
	{	return matrixNormal(row, column);	}


	/**
	* double[]型配列を、Fisher-Yates法を用いてランダムに並び替えます。
	* @param array 並び替えたい配列
	* @return 並び替えられた配列
	*/
	public double[] shuffle(double[] array){
		for (int i=array.length-1; i>0; i--)
		{
			int j = (int) Math.floor(nextDouble() * (i+1));
			double tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		return array;
	}

	/**
	* ランダムな文字列(英数字)を生成します。(記号は含みません。)
	* @param length 文字列の長さ
	* @return ランダム文字列
	*/
	public String string(int length){
		//ランダムバイト列生成
		byte[] bytes = new byte[length];
		byte temp = 0;
		String str=null;
		for(int i=0; i<length; i++)
		{
			temp = (byte)nextInt(62);
			temp += 0x30;
			if(temp> 0x39)	temp += 7;
			if(temp> 0x5a)	temp += 6;
			bytes[i] = temp;
		}
		try{
			str = new String(bytes, "UTF-8");
		}catch(Exception e){}
		return str;
	}

	/**
	* ランダムな文字列(英数字)を生成します。記号を含みます。<br>
	* 含む記号は[Oracle Identity Manager][Microsoft Active Directory]どちらでも使用可能なものです。<br>
	* https://docs.oracle.com/cd/E16441_01/doc.910/B54369-03/app_special_char.htm
	* @param length 文字列の長さ
	* @param symbol 記号を含むか。trueなら記号を含む。falseなら含まない。
	* @return ランダム文字列
	*/
	public String string(int length, boolean symbol){
		if(symbol){
			//ランダムバイト列生成
			byte[] bytes = new byte[length];
			byte temp = 0;
			String str=null;
			for(int i=0; i<length; i++)
			{
				temp = (byte)nextInt(83);	//ASCIIコードのうち使用可能な文字数83
				temp += 0x21;	//制御コードは無視
				if(temp== 0x22)	temp += 1;
				if(temp== 0x26)	temp += 1;
				if(temp== 0x2A)	temp += 1;
				if(temp== 0x2E)	temp += 1;
				if(temp== 0x3B)	temp += 5;
				if(temp== 0x5C)	temp += 1;
				if(temp== 0x7C)	temp += 1;
				bytes[i] = temp;
			}
			try{
				str = new String(bytes, "UTF-8");
			}catch(Exception e){}
			return str;
		}else{
			return string(length);
		}
	}
}
