package jp.ohtayo.commons.operate;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;

import jp.ohtayo.commons.log.Logging;

/**
 * Robotクラスを用いてマウスの位置とクリック操作を行うクラスです。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class Mouse {
	
	/** Robotクラス */	private Robot robot;
	/** 遅延時間	*/	private final int delayTime = 10;
	/** 左ボタン	*/	public final String BUTTON_LEFT = "left";
	/** 右ボタン	*/	public final String BUTTON_RIGHT = "right";
	/** 中ボタン	*/	public final String BUTTON_MIDDLE = "middle";
	
	/**
	 * デフォルトコンストラクタ<br>
	 * Robotクラスを初期化します。<br>
	 */
	public Mouse(){
		try{
			robot = new Robot();
			robot.setAutoDelay(delayTime);
		}catch(AWTException e)
		{
			Logging.logger.severe(e.toString());
		}
	}
	
	/**
	 * 1秒後のマウス位置を返します。<br>
	 * @return マウス位置を示す文字列
	 */
	public String getMousePosition()
	{
		robot.delay(1000);
		PointerInfo pointerInfo;
		Point point = new Point();
		try{
			//現在のマウス位置取得
			pointerInfo = MouseInfo.getPointerInfo();
			point = pointerInfo.getLocation();
		}
		catch(Exception e){
			point = null;
			Logging.logger.severe(e.toString());
		}
		if(point != null)
			return "x = " + point.x + ", y = " + point.y + ".\n";
		else
			return null;
	}

	/**
	 * 指定位置で左クリックします。<br>
	 * @param x x位置
	 * @param y y位置
	 */
	public void leftClick(int x, int y)
	{
		click(x, y, BUTTON_LEFT);
	}
	/**
	 * 指定位置で中クリックします。<br>
	 * @param x x位置
	 * @param y y位置
	 */
	public void middleClick(int x, int y)
	{
		click(x, y,BUTTON_MIDDLE);
	}
	/**
	 * 指定位置で右クリックします。<br>
	 * @param x x位置
	 * @param y y位置
	 */
	public void rightClick(int x, int y)
	{
		click(x, y, BUTTON_RIGHT);
	}
		
	/**
	 * 位置を指定してマウスクリックします。<br>
	 * @param x x位置
	 * @param y y位置
	 * @param button クリックするボタンの指定("left"か"right")
	 */
	public void click(int x, int y, String button)
	{
		PointerInfo pointerInfo;
		Point point = new Point();
		try{
			//現在のマウス位置取得
			pointerInfo = MouseInfo.getPointerInfo();
			point = pointerInfo.getLocation();
			
			//マウスを移動してクリック
			robot.mouseMove(x, y);
			click(button);
			
			//マウスを元の位置に戻す
			robot.mouseMove(point.x, point.y);
		}
		catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}
	
	/**
	 * 現在の位置でマウスクリックします。<br>
	 * @param button クリックするボタンの指定("left"か"right")
	 */
	public void click(String button)
	{
		if(button == BUTTON_LEFT)
		{
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		else if(button ==  BUTTON_RIGHT)
		{
			robot.mousePress(InputEvent.BUTTON2_MASK);
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
		}
		else if(button == BUTTON_MIDDLE)
		{
			robot.mousePress(InputEvent.BUTTON3_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
	}

	/**
	 * マウスを指定位置に移動します。
	 * @param x x位置
	 * @param y y位置
	 */
	public void move(int x, int y)
	{
		try{
			//マウスを移動
			robot.mouseMove(x, y);
		}
		catch(Exception e){
			Logging.logger.severe(e.toString());
		}
	}
}
