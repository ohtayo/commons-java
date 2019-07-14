package jp.ohtayo.commons.image;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import jp.ohtayo.commons.log.Logging;

/**
* Robotクラスを用いて画面のキャプチャを行うクラスです。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
*/
public class Capture {

	/** ロボットクラス	 */
	private Robot robot;
	
	/** 
	 * デフォルトコンストラクタ<br>
	 */
	public Capture()
	{
		initialize();
	}
	
	/**
	 * Robotクラス初期化関数<br>
	 * コンストラクタで呼ばれます。<br>
	 */
	public void initialize(){
		try{
			robot = new Robot();
			robot.setAutoDelay(0);
		}catch(AWTException e)
		{
			Logging.logger.severe(e.toString());
		}
	}

	/**
	 * 画面サイズの大きさ分キャプチャします。<br>
	 * @return キャプチャした画像バッファ
	 */
	public BufferedImage capture()
	{
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle rectangle = environment.getMaximumWindowBounds();
		return robot.createScreenCapture(rectangle);
	}

	/**
	 * 画面サイズのうち指定位置・サイズをキャプチャします。<br>
	 * @param x キャプチャ開始x位置
	 * @param y キャプチャ開始y位置
	 * @param width キャプチャ画像幅
 	 * @param height キャプチャ画像高さ
	 * @return キャプチャした画像バッファ
	 */
	public BufferedImage capture(int x, int y, int width, int height)
	{
		Rectangle rectangle = new Rectangle(x,y,width, height);
		return robot.createScreenCapture(rectangle);
	}
	
	/**
	 * ディスプレイ番号を指定してキャプチャします。<br>
	 * @param display ディスプレイ番号(メインディスプレイ:0, サブディスプレイ:1～)
	 * @return キャプチャした画像バッファ
	 */
	public BufferedImage capture(int display)
	{
		Rectangle rectangle = null;
		try{
			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = environment.getScreenDevices();
			GraphicsConfiguration[] config = devices[display].getConfigurations();
			rectangle = config[0].getBounds();
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
		return robot.createScreenCapture(rectangle);
	}
	
	/**
	 * ディスプレイ番号と範囲を指定してキャプチャします。<br>
	 * @param display ディスプレイ番号(メインディスプレイ:0, サブディスプレイ:1～)
	 * @param x キャプチャ開始x位置
	 * @param y キャプチャ開始y位置
	 * @param width キャプチャ画像幅
 	 * @param height キャプチャ画像高さ
	 * @return キャプチャした画像バッファ
	 */
	public BufferedImage capture(int display, int x, int y, int width, int height)
	{
		Rectangle rectangle = null;
		try{
			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = environment.getScreenDevices();
			GraphicsConfiguration[] config = devices[display].getConfigurations();
			rectangle = config[0].getBounds();
			rectangle.x += x;
			rectangle.y += y;
			rectangle.width = width;
			rectangle.height = height;
		}catch(Exception e){
			Logging.logger.severe(e.toString());
		}
		return robot.createScreenCapture(rectangle);
	}
	
}
