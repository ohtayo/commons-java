package jp.ohtayo.commons.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.math.Vector;
import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.log.Logging;

/**
 * 時系列データのクラスです。<br>
 * データの間引き・補間・フィルタ等の処理メソッドを提供します。
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class TimeSeries extends Matrix {

	private String format = "yyyy/MM/dd HH:mm:ss";	//時系列の時刻フォーマット
	
	/**
	 * データ数、データ長さを指定した時系列データを生成します。
	 * @param distance サンプル数
	 * @param dataLength 時系列データの数
	 */
	public TimeSeries(int distance, int dataLength)
	{
		super(distance, dataLength);
	}
	/**
	 * データを行列で指定した時系列データを生成します。
	 * @param data 時系列データ
	 */
	public TimeSeries(Matrix data)
	{
		super(data);
	}
	
	/**
	 * csvファイルから時系列データを読み取って生成します。
	 * @param fileName 読み込みCSVファイル名
	 * @param header ヘッダ行数
	 * @param format 時刻列のフォーマット
	 */
	public TimeSeries(String fileName, int header, String format)
	{
		super(1,1);
		read(fileName, header, format);
	}
	
	/**
	 * 同じ時系列データのコピーを生成ｒします。
	 * @param time 時系列データ
	 */
	public TimeSeries(TimeSeries time)
	{
		super(1,1);
		matrix = time.get();
		
	}
	
	/**
	 * double[][]型のデータで初期化して生成します。
	 * @param data 時系列データ
	 */
	public TimeSeries(double[][] data)
	{
		super(data);
	}
	
	/**
	 * 時系列データのフォーマット取得
	 * @return フォーマット
	 */
	public String getFormat()
	{
		return this.format;
	}
	
	/**
	 * 時系列データのフォーマット指定
	 * @param value フォーマット
	 */
	public void setFormat(String value)
	{
		this.format = value;
	}
	
	
	/**
	 * csvから時系列データを読み取り<br>
	 * 時刻は一番左列にあるデータ(UnixTime)を読み取る。<br>
	 * 時刻フォーマットの指定の仕方はSimpleDateFormat型に準ずる。<br>
	 * 例：timeSeries.read(fileName, header, "yyyy/MM/dd hh:mm:ss");<br>
	 * @param fileName csvファイル名
	 * @param header ヘッダ行数
	 * @param format 文字列のフォーマット。
	 */
	/*
	public void readOld(String fileName, int header, String format)
	{
		String[][] string = Csv.readString(fileName, header, 0);
		matrix = new Matrix(string.length, string[0].length).get();
		
		for(int i=0; i<string.length; i++)
		{
			try{
				this.set(i, 0, dateToUnixTime( stringToDate(string[i][0], format) ));
			}
			catch(ParseException e){
				System.out.println(e.getMessage());
			}
			
			for (int j=1; j<string[i].length; j++){
				this.set(i,j,Double.parseDouble(string[i][j]));
			}
		}
	}
	*/
	
	/**
	 * CSVから時系列データを読み取り。<br>
	 * 歯抜けのファイルにも対応。歯抜けはNaNが入る。<br>
	 * @param fileName csvファイル名
	 * @param header ヘッダ行数
	 * @param format 文字列のフォーマット。
	 */
	public void read(String fileName, int header, String format)
	{
		this.format = format;
		String[][] string = Csv.readString(fileName, header, 0);
		matrix = new Matrix(string.length, string[0].length).get();
		
		for(int i=0; i<string.length; i++)
		{
			try{
				this.set(i, 0, dateToUnixTime( stringToDate(string[i][0], format) ));
			}
			catch(ParseException e){
				System.out.println(e.getMessage());
			}
			
			for (int j=1; j<string[i].length; j++){
				try{
					this.set(i,j,Double.parseDouble(string[i][j]));
				}catch( Exception e ){
					this.set(i,j, Double.NaN);
				}
			}
		}
	}	
	
	/**
	 * 時系列データの行列をCSVに書き込む。
	 * 時刻はformatで指定した形式に変換する。
	 * @param fileName csvファイル名
	 * @param header ヘッダ
	 * @return 成功：0、失敗：-1
	 */
	public int write(String fileName, String header)
	{
		//Stringに変換
		String[][] string = Cast.doubleToString(this.get());
		
		//時刻データをフォーマットに変換
		for(int i=0; i<string.length; i++)
		{
			string[i][0] = dateToString(unixTimeToDate((long)this.get(i,0)), this.format);
		}
		
		//書込
		return Csv.write(fileName, string, header);
	}
	
	/**
	* 行列を文字列として返します。
	* @return 変換した文字列
	*/
	@Override
	public String toString()
	{
		String str = "";
		
		for(int i=0; i<length(); i++ ){
			str += "  ";
			
			str += dateToString(unixTimeToDate((long)this.get(i,0)), this.format) + " ";
			
			for(int j=1; j<columnLength(); j++ ){
				str += String.valueOf(this.get(i, j)) + " ";
			}
			str += "\r\n";
		}
		return str;
	}
	
	/**
	 * 移動平均フィルタをかける
	 * @param column フィルタを掛けるデータ列番号
	 * @param point 移動平均点数
	 * @return フィルタ後のデータ
	 */
	public TimeSeries movingAverage(int column, int point)
	{
		double sum=0;
		
		//pointが奇数で無ければエラー
		if( point%2 == 0 ){
			Logging.logger.severe("point is not odd");
			return null;
		}
		
		int halfPoint = Math.round(point/2);
		
		double[] result = new double[this.length()];
		Matrix resultMatrix = new Matrix(this.get());
		
		//前端数処理
		for(int i=0; i<halfPoint; i++)
		{
			sum = 0;
			for(int j=i-i; j<i+halfPoint+1; j++)
				sum += this.get(j,column);
			result[i] = sum/point;
		}
		
		//移動平均処理
		for(int i=halfPoint; i<this.length()-halfPoint; i++)
		{
			sum = 0;
			for(int j=i-halfPoint; j<i+halfPoint+1; j++)
				sum += this.get(j,column);
			result[i] = sum/point;
		}
		//後ろ端数処理
		for(int i=this.length()-halfPoint; i<this.length(); i++)
		{
			sum = 0;
			for(int j=i-halfPoint; j<i+(this.length()-1-i)+1; j++)
				sum += this.get(j,column);
			result[i] = sum/point;
		}
		
		resultMatrix.setColumn(column, new Vector(result));
		
		return new TimeSeries(resultMatrix);
	}
	
	/**
	 * 指定日時の間のデータを抽出する
	 * @param from 抽出開始時刻
	 * @param to 抽出終了時刻
	 * @return 抽出したデータ
	 */
	public TimeSeries slice(Calendar from, Calendar to)
	{
		long fromTime = dateToUnixTime(from.getTime());
		long toTime = dateToUnixTime(to.getTime());
		int fromIndex = 0;
		int toIndex = this.length();
		boolean fromFlag = true;
		boolean toFlag = true;
		
		//もし開始時刻が終了時刻より後ならエラー
		if(fromTime>= toTime){
			Logging.logger.severe("toTime is before fromTime");
			return null;
		}
		//区間がかぶってなければエラー
		if(fromTime < this.get(0, 0) && toTime < this.get(0, 0)){Logging.logger.severe("fromTime and toTime are before data time");	return null;}
		if(fromTime > this.get(this.length()-1,0) && toTime > this.get(this.length()-1, 0) ){Logging.logger.severe("fromTime and toTime are after data time");	return null;}
		
		//抽出開始時刻と終了時刻のインデックスを採取
		for(int i=1; i<this.length()-1; i++)
		{
			if( this.get(i,0) > fromTime && fromFlag )
			{
				fromIndex = i-1;
				fromFlag = false;
			}
			if( this.get(i,0) > toTime && toFlag )
			{
				toIndex = i;
				toFlag = false;
			}
		}
		
		//もし開始時刻が終了時刻より後ならエラー
		if(fromIndex>= toIndex){Logging.logger.severe("toIndex is before fromIndex");	return null;}
		
		//インデックス間のデータを抽出
		Matrix ret = new Matrix( toIndex-fromIndex, this.columnLength() );
		for(int i=fromIndex; i<toIndex; i++)
			ret.setRow(i-fromIndex, this.getRow(i));
		
		return new TimeSeries(ret);
	}
	

	
	/** 単純間引き */	public static final String THIN_SIMPLE = "simple thinning";
	/** 平均間引き */	public static final String THIN_AVERAGE = "average thinning";
	/**
	 * 時系列データを間引きします。<br>
	 * methodで間引き方法を指定します。シンプルに間引くか、周辺の平均値をとって残すかを選べます。
	 * @param point 間引き点数(欲しいサンプルーサンプルの距離。1ならそのまま、2なら1点飛ばす)
	 * @param method 間引き方法
	 * @return 間引かれたデータ
	 */
	public TimeSeries thin(int point, String method)
	{
		//データ長さが間引く点数より少なければエラー
		if(this.length() <= point*2){
			Logging.logger.severe("data length is too short");
			return null;
		}
		//double[][] result = new double[Math.round(data.length/point)+1][data[0].length];
		Matrix result = new Matrix( (int)Math.ceil(this.length()/point), this.columnLength());
		int count=0;
		
		if(method == THIN_SIMPLE)
		{
			for(int i=0; i<this.length(); i+= point)
			{
				//result[count] = data[i];
				result.setRow(count, this.getRow(i));;
				count++;
			}
		}
		
		else if(method == THIN_AVERAGE)
		{
			int halfPoint = Math.round(point/2);
			//最初の1点
			for(int j=0; j<this.columnLength(); j++)
			{
				//result[count][j] = Numeric.mean(Vector.slice(Matrix.getColumn(data, j), 0, point));
				result.set(count, j, this.getColumn(j).get(0, point).mean());
			}
			count++;
			
			//中間点
			for(int i=point; i<this.length()-point; i+= point)
			{
				for(int j=0; j<this.columnLength(); j++)
				{
					//result[count][j] = Numeric.mean(Vector.slice(Matrix.getColumn(data, j), i-halfPoint+1, point) );
					result.set(count, j, this.getColumn(j).get(i-halfPoint+1, point).mean());
				}
				count++;
			}
			
			//最後の1点
			for(int j=0; j<this.columnLength(); j++)
			{
				int temp = count * point;
				//result[count][j] = Numeric.mean(Vector.slice(Matrix.getColumn(data, j), temp-halfPoint, point));
				result.set(count, j, this.getColumn(j).get(temp-halfPoint, point).mean());
			}
		}
		
		return new TimeSeries(result);
	}
	
	/** 前回値ホールド	*/	public static final String PREVIOUS_HOLD = "previous hold";
	/** 直近値ホールド	*/	public static final String NEAREST_HOLD = "nearest hold";
	/** 線形補間 */			public static final String LINEAR_INTERPOLATE = "linear";
	/**
	 * 時系列データの抜けを補間する。
	 * 補間方法は前回値ホールド、直近値ホールド、線形補間に対応。
	 * @param rough 粗いデータ。補間対象。
	 * @param method 補間方法
	 * @return 補間したroughデータ
	 */
	public TimeSeries interpolate(TimeSeries rough, String method)
	{
		//fineの開始時刻・終了時刻と、roughの開始・終了時刻が異なればエラー
		if( (this.get(0, 0) != rough.get(0, 0)) || (this.get(this.length()-1, 0) != rough.get(rough.length()-1, 0)) ){
			Logging.logger.severe("startTime and/or endTime are different");
			return null;
		}
		
		//直近値ホールドの場合
		if( method == NEAREST_HOLD ){
			return new TimeSeries(nearestHoldInterpolation( new Matrix(this.get()), new Matrix(rough.get()) ));
		}
		//前回値ホールド
		else if( method == PREVIOUS_HOLD )
		{
			return new TimeSeries(previousHoldInterpolation( new Matrix(this.get()), new Matrix(rough.get()) ));
		}
		//線形ホールド
		else if(method == LINEAR_INTERPOLATE )
		{
			return new TimeSeries(linearInterpolation( new Matrix(this.get()), new Matrix(rough.get()) ));
		}
		else {
			Logging.logger.severe("method is abnormal");
			return null;
		}
	}
	
	//線形補間の中身。
	private Matrix linearInterpolation(Matrix fine, Matrix rough)
	{
		Matrix fineRough = new Matrix(fine.length(), rough.columnLength());		//補間したデータ用意
		int[] minIndex = new int[1];	//直近時刻のインデックス
		minIndex[0] = 0;
		Vector roughDataIndex = new Vector(0);
		Vector fineTime = fine.getColumn(0);	//粗いデータの時刻リスト

		//時刻はセットしておく
		fineRough.setColumn(0, fine.getColumn(0));
		//サンプルの存在するところだけデータを埋める
		for(int i=0; i<rough.length(); i++){
			//roughの一番近いデータ位置のインデックスを取得
			//Numeric.min(Numeric.abs(Vector.minus(fineTime, rough[i][0])), minIndex);
			fineTime.minus(rough.get(i, 0)).abs().min(minIndex);
			//そのインデックスにデータコピー
			for(int j=1; j<fineRough.columnLength(); j++)
				fineRough.set(minIndex[0], j, rough.get(i, j));
			//データがあったインデックスを保存
			//roughDataIndex = Vector.add(roughDataIndex, Cast.intToDouble(minIndex));
			roughDataIndex = roughDataIndex.add(new Vector(Cast.intToDouble(minIndex)));
		}
		
		//サンプルの存在しないところを、存在するデータから線形補間する
		for(int i=0; i<roughDataIndex.length(); i++)
		{
			//最初のインデックス処理
			if(i == 0){
				//最初にindex=0であれば飛ばす
				if(roughDataIndex.get(i) == 0.0){ continue; }
				//そうでなければ、インデックスまでをインデックスの位置の値で埋める。
				else{
					for(int f=0; f<=roughDataIndex.get(i); f++){
						for(int j=1; j<fineRough.columnLength(); j++)
							fineRough.set(f, j, rough.get(i, j));
					}
				}
			}
			//最後のインデックス処理
			else if(i == roughDataIndex.length()-1)
			{
				//インデックスまでの補間処理
				linearInterpolateAlgorithm(fineRough,(int)roughDataIndex.get(i-1), (int)roughDataIndex.get(i), rough );
				//インデックス以降の補間処理
				if(roughDataIndex.get(i) != fineRough.length()-1)
				{
					for(int f=(int)roughDataIndex.get(i)+1; f<fineRough.length(); f++){
						for(int j=1; j<fineRough.columnLength(); j++)
							fineRough.set(f, j, rough.get(i, j));
					}	
				}
			}
			//中間インデックス処理
			else{
				linearInterpolateAlgorithm(fineRough,(int)roughDataIndex.get(i-1), (int)roughDataIndex.get(i), rough );
			}
		}
		
		return fineRough;
	}
	
	//サンプルの間を補完するアルゴリズム
	private static void linearInterpolateAlgorithm(Matrix fineRough, int startIndex, int endIndex, Matrix rough)
	{
		//前回とインデックスが同じ値であれば何もしない
		if(startIndex == endIndex)
			return;
		
		//前回インデックスと今回インデックスまでで、その間のデータを算出して補間する
		for(int f=startIndex+1; f<endIndex; f++)
		{
			int distance = endIndex-startIndex;
			for(int j=1; j<fineRough.columnLength(); j++)
				fineRough.set(f, j, ((fineRough.get(endIndex,j)-fineRough.get(startIndex,j)) * (f-startIndex)/distance ) + fineRough.get(startIndex,j) );
		}
	}
	
	//近似ホールド補間
	private Matrix nearestHoldInterpolation(Matrix fine, Matrix rough)
	{
		Matrix fineRough = new Matrix(fine.length(), rough.columnLength());		//補間したデータ用意
		int[] minIndex = new int[1];	//直近時刻のインデックス
		minIndex[0] = 0;
		Vector roughTime = rough.getColumn(0);	//粗いデータの時刻リスト
		
		for(int i=0; i<fine.length(); i++)
		{
			//fineの時刻に最も近い時間のインデックスを求める。
			//Numeric.min(Numeric.abs(Vector.minus(roughTime, fine[i][0])), minIndex);
			roughTime.minus(fine.get(i, 0)).abs().min(minIndex);
			//時刻はfineの時刻
			fineRough.set(i, 0, fine.get(i, 0));
			//残りのデータはインデックスのデータをコピー
			for(int j=1; j<fineRough.columnLength(); j++)
				fineRough.set(i, j, rough.get(minIndex[0], j));
		}
		return fineRough;
	}

	//前回値ホールド補間
	private Matrix previousHoldInterpolation(Matrix fine, Matrix rough)
	{	
		Matrix fineRough = new Matrix(fine.length(), rough.columnLength());		//補間したデータ用意
		int[] minIndex = new int[1];	//直近時刻のインデックス
		minIndex[0] = 0;
		
		for(int i=0; i<fine.length(); i++){
			
			//fineの手前のroughの時間のインデックスを求める。
			//roughが手前に無ければ、roughの最初の時間を使う。
			for(int idx=rough.length()-1; idx>=0; idx--){
				if(rough.get(idx, 0)<=fine.get(i, 0)){
					minIndex[0] = idx;
					break;
				}
			}
			
			//時刻はfineの時刻
			fineRough.set(i, 0, fine.get(i, 0));
			//残りのデータはインデックスのデータをコピー
			for(int j=1; j<fineRough.columnLength(); j++)
				fineRough.set(i, j, rough.get(minIndex[0], j));
		}
		return fineRough;
	}
	
	//------------------------------------------------------------------------//
	// 便利もの                                                               //
	//------------------------------------------------------------------------//
	/**
	* 時刻の等差数列を返します。<br>
	* differenceは秒数<br>
	* 例：sequence("2014/12/12 10:00:00", 1, "2014/12/12 10:00:00", "yyyy/MM/dd HH:mm:ss");<br>
	* @param start 開始時刻
	* @param difference 差
	* @param end 終了時刻
	* @param format 時刻指定形式のフォーマット
	* @return 等差数列
	*/
	public static Vector sequence(String start, long difference, String end, String format)
	{
		double startTime=Double.NaN, endTime=Double.NaN;
		try {
			startTime = (double)dateToUnixTime(stringToDate(start, format));
			endTime	 = (double)dateToUnixTime(stringToDate(end, format));
			double test  = dateToUnixTime(stringToDate("1970/01/01 09:00:00",format));
			System.out.println(test);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		double[] result = new double[(int)Math.floor( Math.abs(endTime-startTime)/difference )+1];
		int count = 0;
		for (double i=startTime; i<endTime+1; i=i+difference)
		{
			result[count] = i;
			count += 1;
		}
		return new Vector(result);
	}
	
	//------------------------------------------------------------------------//
	// 時刻データ変換                                                         //
	//------------------------------------------------------------------------//
	/**
	 * Excelのシリアル値をUnix時間に変換
	 * @param serial シリアル値
	 * @return Unix時間
	 */
	public static long serialToUnixTime(double serial)
	{
		return (long)( (serial - 25569) * 86400 );
	}
	
	/**
	 * Unix時間をExcelのシリアル値に変換
	 * @param unixTime Unix時間
	 * @return シリアル値
	 */
	public static double unixTimeToSerial(long unixTime)
	{
		return (double)unixTime/86400 + 25569;
	}
	
	/**
	 * Date型をExcelのシリアル値に変換
	 * @param date Date型変数
	 * @return シリアル値
	 */
	public static double dateToSerial(Date date)
	{
		return unixTimeToSerial(date.getTime()/1000);
	}
	/**
	 * Excelのシリアル値をDate型に変換
	 * @param serial シリアル値
	 * @return Date型変数
	 */
	public static Date serialToDate(double serial)
	{
		return new Date( serialToUnixTime(serial)*1000 );
	}
	
	/**
	 * Unix時間をDate型に変換
	 * @param unixTime Unix時間
	 * @return Date型変数
	 */
	public static Date unixTimeToDate(long unixTime)
	{
		return new Date( unixTime*1000);
	}
	/**
	 * Date型変数をUnix時間に変換
	 * @param date Date型変数
	 * @return Unix時間
	 */
	public static long dateToUnixTime(Date date)
	{
		return date.getTime()/1000;
	}
	
	/**
	 * Date型日時を指定したフォーマットの文字列に変換
	 * @param date 日時
	 * @param format 文字列フォーマット。SimpleDateFormat型に準ずる
	 * @return 変換した文字列
	 */
	public static String dateToString(Date date, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 時刻文字列を指定したフォーマットに則ってDate型に変換する<br>
	 * @param str 変換したい文字列
	 * @param format 文字列のフォーマット。SimpleDateFormat型に準ずる
	 * @return 変換した日時
	 * @throws ParseException 変換できなかった場合の例外
	 */
	public static Date stringToDate(String str, String format) throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    Date date = sdf.parse(str);
	    return date;
	}
	
	/**
	 * Calendar型日時を、指定したフォーマットの時刻文字列に変換
	 * @param calendar 日時
	 * @param format 文字列フォーマット。SimpleDateFormat型に準ずる
	 * @return 変換した文字列
	 */
	public static String calendarToString(Calendar calendar, String format)
	{
		return dateToString(calendar.getTime(), format);
	}
	/**
	 * 時刻文字列を指定したフォーマットに則ってCalendar型に変換する。
	 * @param string 変換したい文字列
	 * @param format 文字列のフォーマット。SimpleDateFormat型に準ずる
	 * @return 変換した日時
	 * @throws ParseException 変換できなかった場合の例外
	 */
	public static Calendar stringToCalendar(String string, String format) throws ParseException {
		Date date = stringToDate(string, format);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setLenient(false);
	    calendar.setTime(date);
		return calendar;
	}
	
	
}
