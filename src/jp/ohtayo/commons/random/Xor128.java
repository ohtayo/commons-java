package jp.ohtayo.commons.random;

/**
 * 乱数生成クラス<br>
 * 乱数生成にXorshiftを利用します。<br>
 * 関数仕様をjava.util.Randomに合わせています。<br>
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class Xor128 {

	/** シフトレジスタ	*/	private static long x=123456789, y=362436069, z=521288629;// w=88675123;
	/** シード			*/	private static long w;
	
	/**
	 * デフォルトコンストラクタ<br>
	 * 時刻をシードに用いて、乱数生成器を初期化します。<br>
	 */
	public Xor128()
	{	
		w = System.currentTimeMillis();
		for(int i=0; i<100; i++)	nextLong();	//longが埋まるまで少し回す
	}

	/**
	 * コンストラクタ<br>
	 * シードを指定して、乱数生成器を初期化します。<br>
	 * @param seed シード
	 */
	public Xor128(long seed)
	{	
		setSeed(seed);
	}
	
	/**
	 * コンストラクタ<br>
	 * シードにint 配列を指定して、乱数生成器を初期化します。<br>
	 * @param array シード
	 */
	public Xor128(int[] array)
	{
		w=0;
		for(int i=0; i<array.length; i++){
			w += array[i];
		}
		for(int i=0; i<100; i++)	nextLong();
	}

	/**
	* long 型のシードを使って乱数のシードを設定します。
	* @param seed 乱数のシード
	*/
    synchronized public void setSeed(long seed) {
    	w = seed;
			x=123456789;
			y=362436069;
			z=521288629;
			for(int i=0; i<100; i++)	nextLong();
		}
    
    /**
     * long 型の一様乱数を生成します。
     * @return 一様乱数
     */
    synchronized public long nextLong()
	{
		long t;
		t = ( x^(x<<11) );
		x = y;
		y = z;
		z = w;
		w = ( w^(w>>>19) )^( t^(t>>>8) ) ;
		return w; 
	}

    /**
     * 指定したビット数のランダムビット列を出力します。
     * @param bits ビット数
     * @return ランダムビット列
     */
    public int next(int bits)
    {
    	return (int)(nextLong()>>>(64-bits));
    }
    
	/**
	* true / false をランダムに返します。<br>
	* @return true / false
	*/
	public boolean nextBoolean()
	{
		if( nextLong() > 0  )
			return false;
		else
			return true;
	}

	/**
	* ランダムバイト配列を生成し、指定されたバイト配列に配置します。
	* @param bytes ランダムバイト配列を配置する配列
	*/
	public void nextBytes(byte[] bytes)
	{
		byte[] values = new byte[bytes.length];
		for(int i=0; i<bytes.length; i++)
		{
			values[i] = (byte)((nextLong()>>>44)&0x000000ff);
		}
		System.arraycopy(values, 0, bytes, 0, bytes.length);
	}
	
	/**
	* int 型の一様乱数を生成します。<br>
	* @return 一様乱数
	*/
	public int nextInt()
	{
		return (int)(nextLong()>>>32);
	}

	/**
	* int 型の0から指定された値の範囲(0は含むが、その指定された値は含まない)の一様乱数を生成します。<br>
	* @param n 範囲指定値<br>
	* @return 0から指定された値の範囲の一様乱数<br>
	*/
	public int nextInt(int n)
	{
		return (int)Math.floor(nextDouble()*n);
	}

	/**
	* 0～1のdouble 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/	
	public double nextDouble()
	{
		long n = nextLong() >>> 11;
		return n/ (double)(1L << 53);
	}
	
	/**
	* 0～1のfloat 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/
	public float nextFloat()
	{
		return (float)nextDouble();
	}
}
