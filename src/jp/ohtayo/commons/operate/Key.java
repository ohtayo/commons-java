package jp.ohtayo.commons.operate;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import jp.ohtayo.commons.log.Logging;

/**
 * Robotクラスを用いてキー操作を行うクラスです。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class Key {

	/** Robotクラス */	private Robot robot;
	/** 遅延時間	*/	private final int delayTime = 10;	

	/**
	 * デフォルトコンストラクタ<br>
	 * Robotクラスを初期化します。<br>
	 */
	public Key(){
		try{
			robot = new Robot();
			robot.setAutoDelay(delayTime);
		}catch(AWTException e)
		{
			Logging.logger.severe(e.toString());
		}
	}
	
	/**
	 * 文字列を与えると、その文字のキーを連続で押します。<br>
	 * 半角英数字・記号のみ。一部の記号には対応していません。<br>
	 * @param word キーを打ちたい文字列
	 */
	public void pressString(String word)
	{
		String str;
		byte[] code;
		int eventNumber;
		for(int i=0; i<word.length(); i++){
			str = word.substring(i,i+1);
			code = str.getBytes();
			//小文字の場合
			if(code[0] >= 97 && code[0] <= 122){
				eventNumber = code[0]-32;
				pressKey(eventNumber);
			}
			//大文字の場合
			else if(code[0] >= 65 && code[0] <= 90){
				eventNumber = code[0];
				holdShift();
				pressKey(eventNumber);
				releaseShift();
			}
			//記号の場合
			else{
				eventNumber = code[0];
				pressKey(eventNumber);
			}
		}
	}
	
	/**
	 * 指定されたキーを押します。<br>
	 * @param eventNumber キーイベントナンバー
	 */
	public void pressKey(int eventNumber)
	{
		robot.keyPress(eventNumber);
		robot.keyRelease(eventNumber);
	}
	
	/**
	 * 指定されたキーを押っぱなしにします。<br>
	 * @param eventNumber キーイベントナンバー
	 */
	public void holdKey(int eventNumber)
	{
		robot.keyPress(eventNumber);
	}	
	/**
	 * 指定されたキーを離します。<br>
	 * @param eventNumber キーイベントナンバー
	 */
	public void releaseKey(int eventNumber)
	{
		robot.keyRelease(eventNumber);
	}	
	
	//各キーを押す関数
	/** Enterキーを押します 		*/	public void pressEnter(){		pressKey(KeyEvent.VK_ENTER);	}
	/** F5キーを押します 		*/		public void pressF5(){			pressKey(KeyEvent.VK_F5);	}
	
	/** Shiftキーを押しっぱなします */	public void holdShift()	{		holdKey(KeyEvent.VK_SHIFT);		}
	/** Shiftキーを離します	 		*/	public void releaseShift(){		releaseKey(KeyEvent.VK_SHIFT);	}
	/** Ctrlキーを押しっぱなします	*/	public void holdControl()	{	holdKey(KeyEvent.VK_CONTROL);	}
	/** Ctrlキーを離します 			*/	public void releaseControl(){	releaseKey(KeyEvent.VK_CONTROL);}

	/** Altキーを押します 			*/	public void pressAlt(){			pressKey(KeyEvent.VK_ALT);		}
	/** Altキーを押しっぱなします 	*/	public void holdAlt()	{		holdKey(KeyEvent.VK_ALT);		}
	/** Altキーを離します 			*/	public void releaseAlt(){		releaseKey(KeyEvent.VK_ALT);	}

	/** BackSpaceキーを押します 	*/	public void pressBackSpace(){	pressKey(KeyEvent.VK_BACK_SPACE);}
	/** Deleteキーを押します 		*/	public void pressDelete(){		pressKey(KeyEvent.VK_DELETE);	}
	/** Tabキーを押します 			*/	public void pressTab(){			pressKey(KeyEvent.VK_TAB);		}
}
