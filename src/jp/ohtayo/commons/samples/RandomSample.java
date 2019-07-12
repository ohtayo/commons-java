package jp.ohtayo.commons.samples;
import jp.ohtayo.commons.random.Random;
import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.math.Vector;

/**
 * Sample codes of Random class
 * @author ohtayo <ohta.yoshihiro@outlook.jp></>
 */
public class RandomSample {
	public static void main(String[] args) {

		//ランダムグラスのインスタンス化
		//Random rand = new Random(Random.LCG);
//		Random rand = new Random(Random.XOR);
//		Random rand = new Random(Random.SFMT);
		Random rand = new Random(Random.MT);

		//ランダム値生成の例
		System.out.println(rand.nextBoolean());
		System.out.println(rand.nextFloat());
		System.out.println(rand.nextInt());
		System.out.println(rand.nextInt(3));
		System.out.println(new Vector(rand.rand(10)).toString());

		double[] b = rand.shuffle(Cast.intToDouble(new int[] {1, 2, 3}));
		System.out.println(new Vector(b).toString());
		byte[] a = new byte[10];
		rand.nextBytes(a);
		System.out.println(new Vector( Cast.intToDouble(Cast.byteToInt(a)) ).toString());

		//乱数へのシード設定の例
		rand.setSeed(1);
		System.out.println(new Matrix(rand.rand(10,5)).toString());
		rand.setSeed(1);
		System.out.println(rand.rand());

		// example code for generate random string.
		rand = new Random();
		System.out.println(rand.string(20,true));

	}

}
