package jp.ohtayo.commons.image;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * BufferedImageクラスの画像をフォーム表示と拡縮するクラスです。
 *
 * @author ohtayo (ohta.yoshihiro@outlook.jp)
 */
public class Picture extends JFrame {

	/** デフォルトシリアルバージョンUID	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage base;
	private BufferedImage buffer;
	
	public Picture(String title, BufferedImage inputImage, int xPosition, int yPosition)
	{
		super(title);
		setBounds(xPosition, yPosition, inputImage.getWidth(), inputImage.getHeight());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		base = inputImage;
		buffer = base;
		
		//イベントリスナー
		addComponentListener(new ComponentAdapter(){
			//ウインドウ移動時イベント
			@Override
			public void componentMoved(ComponentEvent e) {
				repaint();
			}
			//ウインドウサイズ変更時イベント
			@Override
			public void componentResized(ComponentEvent e) {
				int per = (int)( ((double)getWidth()*100) / (double)base.getWidth());
				buffer = new ImageProcessing(base).resize(per);
				repaint();
			}
		});
		
		//画面解像度取得して、サイズ変更する。
		java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
		java.awt.DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();
		if( width< inputImage.getWidth() )
			setSize(width-100, inputImage.getHeight()-100);
		else if( height < inputImage.getHeight() )
			setSize(inputImage.getWidth()-100, height-100);
	}
	
	public Picture(BufferedImage inputImage){
		this("", inputImage, 100, 100);
	}
	
	public void setImage(BufferedImage inputImage){
		base = inputImage;
	}
	
	public BufferedImage getImage(){
		return base;
	}
	
	public void paint(Graphics g){
		g.drawImage(buffer, 0, 0, this);
	}
	
}
