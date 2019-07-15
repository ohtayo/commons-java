package jp.ohtayo.commons.log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.LogManager;

/**
 * ログを出力するクラスです。java標準のAPIを使用します。<br>
 * ログの出力プロパティはjavalog.propertiesを編集することで変更できます。<br>
 * 外部クラスからは以下のように使用します。<br>
 * Logging.logger.info("msg");<br>
 * <br>
 * デフォルトでは以下の書式でログを出力し、またログファイル(Logging_n_n.log)に書き出します。<br>
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
public class Logging {
	
	/** ログプロパティ保存ファイル名 */
	protected static final String LOGGING_PROPERTIES  = "javalog.properties";
	/** デフォルトログプロパティ */
	protected static final String LOGGING_PROPERTIES_DATA  = 
			"handlers=java.util.logging.ConsoleHandler,java.util.logging.FileHandler\n" +
			".level=FINEST\n" +
			"java.util.logging.ConsoleHandler.level=CONFIG\n" +
			"java.util.logging.ConsoleHandler.formatter=jp.ohtayo.commons.log.LogFormatter\n" +
			"java.util.logging.FileHandler.level=CONFIG\n" +
			"java.util.logging.FileHandler.pattern=Logging_%u_%g.log\n" +
			"java.util.logging.FileHandler.formatter=jp.ohtayo.commons.log.LogFormatter\n" +
			"java.util.logging.FileHandler.count=10\n";
	/** ロガー */
	public static final Logger logger;

	//static initializer によるログ設定の初期化
	/** デフォルトコンストラクタ<br>
	 *  static initializerによりログ設定を初期化します。<br>
	 *  最初に本クラスが呼び出された時に使用されます。<br>
	 */
	static {
		logger = Logger.getLogger("ohtayo-commons-java");
		
		// クラスパスの中から ログ設定プロパティファイルを取得
		logger.fine("Setting logger from : " + LOGGING_PROPERTIES);
		InputStream inStream = Logging.class.getClassLoader().getResourceAsStream(LOGGING_PROPERTIES);

		//設定ファイルが見当たらなければ
		if (inStream == null) {
		    logger.info("File not found on class path: " + LOGGING_PROPERTIES);
		    logger.info("Use default logger");
		    try{
		    	inStream = new ByteArrayInputStream( LOGGING_PROPERTIES_DATA.getBytes("UTF-8") );
		    	LogManager.getLogManager().readConfiguration(inStream);
		    } catch (Exception e){
			    logger.severe("Error in setting LogManager : "+e.toString());		    	
		    } finally {
		    	try{
			    	if(inStream != null) inStream.close();		    		
		    	} catch(Exception e){
				    logger.severe("Error in stream close : "+e.toString());		    			    		
		    	}
		    }
		} else {
			//設定ファイルが見つかれば
			try {
		        LogManager.getLogManager().readConfiguration(inStream);
		        logger.config("Setting logger using LogManager");
		    } catch (IOException e) {
		        logger.warning("Error in setting LogManager : "+ e.toString());
		    } finally {
		        try {
		            if (inStream != null) inStream.close(); 
		        } catch (IOException e) {
		            logger.warning("Error in stream close : "+ e.toString());
	            }
	        }
	    }
	}
	

}
