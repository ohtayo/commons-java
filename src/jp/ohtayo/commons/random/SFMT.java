package jp.ohtayo.commons.random;

/**
 * 乱数生成を生成するクラスです。<br>
 * 乱数生成にSIMD-oriented Fast Mersenne Twisterを利用しています。<br>
 * 関数仕様をjava.util.Randomに合わせています。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class SFMT {

	/** 乱数生成器 */private SFMT19937 sfmt = null;
	/** NextBit での残りのビット	*/	int    coin_bits;
	/** NextBit での値保持			*/	int    coin_save;
	/** NextByte で使用したバイト数 */	int    byte_pos;
	/** NextByte での値保持			*/	int    byte_save;
	/** NextIntEx で前回の範囲		*/	int    range;
	/** NextIntEx で前回の基準値	*/	int    base;
	/** NextIntEx で前回のシフト数	*/	int    shift;

	/**
	 * デフォルトコンストラクタ<br>
	 * 時刻をシードに用いて、乱数生成器を初期化します。<br>
	 */
	public SFMT() {
		this(System.currentTimeMillis());
		coin_bits=0;byte_pos=0;range=0;
	}

	/**
	 * コンストラクタ<br>
	 * シードを指定して、乱数生成器を初期化します。<br>
	 * @param s シード
	 */
	public SFMT(long s){
		sfmt = new SFMT19937(s);
		coin_bits=0;byte_pos=0;range=0;
	}

	/**
	 * コンストラクタ<br>
	 * シードを指定して、乱数生成器を初期化します。<br>
	 * @param array シード
	 */
	public SFMT(int[] array){
		sfmt = new SFMT19937();
		sfmt.initByArray(array);
		coin_bits=0;byte_pos=0;range=0;
	}

	/**
	* long 型のシードを使って乱数のシードを設定します。
	* @param seed 乱数のシード
	*/
    synchronized public void setSeed(long seed) {
    	sfmt.setSeed(seed);
			coin_bits=0;byte_pos=0;range=0;
    }
    
    /**
     * 指定したビット数のランダムビット列を出力します。
     * @param bits ビット数
     * @return ランダムビット列
     */
    public int next(int bits)
    {
    	return nextInt()>>>(32-bits);
    }

	/** ０か１を返す乱数 */
	synchronized public int NextBit() {
		if (--coin_bits==-1)
		{ coin_bits=31; return(coin_save=sfmt.next())&1; }
		else return(coin_save>>>=1)&1;
	}

  /**
	* true / false をランダムに返します。<br>
	* @return true / false
	*/
	public boolean nextBoolean()
	{
		if( NextBit() == 0 )
			return false;
		else
			return true;
	}

	/** ０から２５５を返す乱数 */
	synchronized public int NextByte() {
		if (--byte_pos==-1)
		{ byte_pos=3; return(int)(byte_save=sfmt.next())&255; }
		else return(int)(byte_save>>>=8)&255;
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
			values[i] = (byte)NextByte();
		}
		System.arraycopy(values, 0, bytes, 0, bytes.length);
	}

	/** ０以上１未満の乱数(53bit精度) */
	public double NextUnif() {
		double z=sfmt.next()>>>11,y=sfmt.next();
		if (y<0) y+=4294967296.0;
		return(y*2097152.0+z)*(1.0/9007199254740992.0);
	}

	/**
	* 0～1のdouble 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/
	public double nextDouble()
	{
		return NextUnif();
	}
	

	/**
	* 0～1のfloat 型の一様乱数を生成します。<br>
	* @return 0～1までの一様乱数
	*/
	public float nextFloat()
	{
		return (float)nextDouble();
	}
	
	/**
	* int 型の一様乱数を生成します。<br>
	* @return 一様乱数
	*/
	public int nextInt()
	{
		return sfmt.next();
	}

	/** 丸め誤差のない０以上 range_ 未満の整数乱数 */
	synchronized public int NextIntEx(int range_) {
		int y_,base_,remain_; int shift_;

		if (range_<=0) return 0;
		if (range_!=range) {
			base=(range=range_);
			for (shift=0;base<=(1<<30)&&base!=1<<31;shift++) base<<=1;
		}
		for (;;) {
			y_=sfmt.next()>>>1;
			if (y_<base||base==1<<31) return(int)(y_>>>shift);
			base_=base; shift_=shift; y_-=base_;
			remain_=(1<<31)-base_;
			for (;remain_>=(int)range_;remain_-=base_) {
				for (;base_>remain_;base_>>>=1) shift_--;
				if (y_<base_) return(int)(y_>>>shift_);
				else y_-=base_;
			}
		}
	}

	/**
	* int 型の0から指定された値の範囲(0は含むが、その指定された値は含まない)の一様乱数を生成します。<br>
	* @param n 範囲指定値<br>
	* @return 0から指定された値の範囲の一様乱数<br>
	*/
	public int nextInt(int n)
	{
		return NextIntEx(n);
	}
}
