package jp.ohtayo.commons.samples;
import java.text.ParseException;
import java.util.Calendar;

import jp.ohtayo.commons.io.Csv;
import jp.ohtayo.commons.io.Text;
import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.io.TimeSeries;
import jp.ohtayo.commons.math.Vector;

/**
 * FileとCSVデータ操作のサンプルプログラム<br>
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class FileSample {
	public static void main(String[] args) {

		String fileName = ".\\csv\\sample.csv";
		String roughName = ".\\csv\\sample_rough.csv";
		String writeName = ".\\csv\\sample_write.csv";
		String lackedName = ".\\csv\\sample_lacked.csv";

		//string読み込み
		Text test = new Text();
		test.read(fileName);
		//System.out.println(test);
		//string書込のテスト
		test.write(writeName);
		test.append(writeName);

		//CSVをStringで読み込む
		String[][] string = Csv.readString(fileName, 1, 1);
		double[][] sample = Cast.stringToDouble(string);
		Csv.write(writeName, string, "serial, ch1, ch2");

		//CSVをdoubleで読み書き
		sample = Csv.read(fileName, 2, 1);
		Csv.write(writeName, sample, "てすと");

		//データ列数とデータ点数を指定した時系列データ生成テスト
		TimeSeries timeSeries1 = new TimeSeries(10, 11);	//10行11列の行列生成
		System.out.println("timeTest1 = \n" + timeSeries1.toString());

		//行列を利用した時系列データ生成テスト
		TimeSeries timeSeries2 = new TimeSeries(new Matrix(sample));
		System.out.println("timeTest2 = \n" +timeSeries2.toString());

		//時系列データのコピーテスト
		TimeSeries timeSeries3 = new TimeSeries(timeSeries2);
		System.out.println("timeTest3 = \n" +timeSeries3.toString());

		//時系列データ読み込み
		String format = "yyyy/MM/dd HH:mm:ss";
		String header = "time, serial, ch1, ch2";
		TimeSeries timeSeriesSample = new TimeSeries(fileName, 1, format);
		System.out.println("time = \n" +timeSeriesSample.toString());

		//歯抜けの時系列データの読み込み
		TimeSeries timeSeriesLacked = new TimeSeries(1, 1);
		timeSeriesLacked.read(lackedName, 1, format);
		System.out.println("time2 = \n" +timeSeriesLacked.toString());

		//時系列データを移動平均して書き込む
		timeSeriesSample = timeSeriesSample.movingAverage(2, 3);	//2列目のデータに3点の移動平均フィルタをかける
		System.out.println("time = \n" +timeSeriesSample.toString());
		timeSeriesSample.setFormat( "yyyy-MM-dd-HH-mm-ss");
		timeSeriesSample.write(writeName, header);

		//時系列データを補間するテスト
		TimeSeries timeSeriesRough = new TimeSeries(roughName, 1, format);
		//時刻の等差数列を生成し、それを含む行列を作って時系列データを生成。
		Vector time3 = TimeSeries.sequence("2014/12/02 14:00:00", 1, "2014/12/03 14:52:29", "yyyy/MM/dd HH:mm:ss");
		Matrix ttime = new Matrix(time3.length(), 4);
		ttime.setColumn(0, time3);
		//線形補完
		TimeSeries timeSeriesFineRough = new TimeSeries(ttime);
		timeSeriesFineRough = timeSeriesFineRough.interpolate(timeSeriesRough, TimeSeries.LINEAR_INTERPOLATE);
		timeSeriesFineRough.write(writeName+"linear.csv", header);
		//前回値ホールド
		timeSeriesFineRough = new TimeSeries(ttime);
		timeSeriesFineRough = timeSeriesFineRough.interpolate(timeSeriesRough, TimeSeries.PREVIOUS_HOLD);
		timeSeriesFineRough.write(writeName+"previous.csv", header);
		//直近値ホールド
		timeSeriesFineRough = new TimeSeries(ttime);
		timeSeriesFineRough = timeSeriesFineRough.interpolate(timeSeriesRough, TimeSeries.NEAREST_HOLD);
		timeSeriesFineRough.write(writeName+"nearest.csv", header);

		//時系列データの指定日時を抽出するテスト
		Calendar from = null, to = null;
		try {
			from = TimeSeries.stringToCalendar("2014/12/02 14:00:00",format);
			to = TimeSeries.stringToCalendar("2014/12/02 14:00:40",format);
		} catch (ParseException e) {
			System.out.println("simpleDataFormat型に変換できませんでした。\n");
			e.printStackTrace();
		}
		TimeSeries picup = timeSeriesSample.slice(from, to);
		picup.setFormat(format);
		picup.write(writeName+"picup.csv", "picup");

		//時系列データの間引きテスト
		TimeSeries skip = picup.thin(3, TimeSeries.THIN_AVERAGE);
		skip.setFormat(format);
		skip.write(writeName+"skip.csv", "skip");

		//時系列データの相関分析テスト
		fileName = ".\\csv\\sample_correl.csv";		//ファイル名
		TimeSeries timeSeriesCorrel = new TimeSeries(fileName, 1, format);	//時系列データ読み込み
		System.out.println("dataCorrel = \n" +timeSeriesCorrel.toString());	//時系列データ表示
		System.out.println("相関係数行列 = ");
		System.out.println(timeSeriesCorrel.correlation().toString());
		System.out.println("重回帰分析係数 = ");
		System.out.println(timeSeriesCorrel.multipleRegression(1, 2, 3));

		Vector regression = timeSeriesCorrel.regression(1, 2, 3);	//重回帰式で推定した値を計算
		System.out.println("重回帰式で推定した値 = ");
		System.out.println(regression.toString());					//上記値を表示
		Matrix target = new Matrix(regression.length(),1);
		target.setColumn(0, regression);
		timeSeriesCorrel = new TimeSeries( timeSeriesCorrel.add(target, TimeSeries.DIRECTION_ADD_RIGHT) );	//上記値を行列の右側に追加
		timeSeriesCorrel.write(fileName+"regression.csv", "time, data0, data1, data2, regression");	//書き込み
	}
}
