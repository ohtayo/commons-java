package jp.ohtayo.commons.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * ログのフォーマットを指定するクラスです。java標準のAPIを使用します。<br>
 * ロガークラスから使用されます。<br>
 * <br>
 * 以下の書式を規定します。<br>
 * YYYY/MM/DD hh:mm:dd.sss LEVEL CLASS_NAME METHOD_NAME - msg<br>
 * CLASS_NAME, METHOD_NAME：ログ出力したクラス名・メソッド名<br>
 * LEVEL：ログレベル<br>
 * ログレベルには以下があります。<br>
 * FINEST FINER FINE CONFIG INFO WARN SEVERE<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 * @see java.util.logging.Logger
 * @see java.util.logging.LogManager
 */
public class LogFormatter extends Formatter {
	
	/** 時刻フォーマット */
	private final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	/** ログ出力フォーマット
	 * @param argLogRecord ログ記録関数からの呼び出し
	 * @return ログメッセージ文字列
	 */
	public String format(final LogRecord argLogRecord)
	{
		final StringBuffer buf = new StringBuffer();
		
		buf.append(sdFormat.format(
		    new Date(argLogRecord.getMillis())));
		buf.append(" ");
		
		if (argLogRecord.getLevel() == Level.FINEST) {
		    buf.append("FINEST");
		} else if (argLogRecord.getLevel() == Level.FINER) {
		    buf.append("FINER ");
		} else if (argLogRecord.getLevel() == Level.FINE) {
		    buf.append("FINE  ");
		} else if (argLogRecord.getLevel() == Level.CONFIG) {
		    buf.append("CONFIG");
		} else if (argLogRecord.getLevel() == Level.INFO) {
		    buf.append("INFO  ");
		} else if (argLogRecord.getLevel() == Level.WARNING) {
		    buf.append("WARN  ");
		} else if (argLogRecord.getLevel() == Level.SEVERE) {
		    buf.append("SEVERE");
		} else {
		    buf.append(Integer.toString(argLogRecord.getLevel().intValue()));
		    buf.append(" ");
		}
		buf.append(" ");
		
		//呼び出されたクラス名、関数名を入れる
		buf.append(" ");
		buf.append(argLogRecord.getSourceClassName());
		buf.append(" ");
		buf.append(argLogRecord.getSourceMethodName());
		buf.append(" - ");
		buf.append(argLogRecord.getMessage());
		buf.append("\n");
		
		return buf.toString();
    }
}

