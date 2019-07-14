package jp.ohtayo.commons.image;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import jp.ohtayo.commons.util.Cast;
import jp.ohtayo.commons.log.Logging;
import jp.ohtayo.commons.math.Matrix;
import jp.ohtayo.commons.math.Numeric;
import jp.ohtayo.commons.math.Vector;

import javax.imageio.ImageIO;

/**
 * 画像処理用のメソッドを提供するクラスです。<br>
 * 画像切抜きや拡縮、画像比較を行うメソッド等を含みます。<br>
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
*/
public class ImageProcessing {

	/**
	 * 内部の画像バッファ
	 */
	private BufferedImage image;

	/**
	 * コンストラクタ
	 * @param image BufferedImage型の画像データ
	 */
	public ImageProcessing(BufferedImage image){ this.image = image; }

	/**
	 * コンストラクタ
	 * @param fileName 読み込む画像のファイル名
	 */
	public ImageProcessing(String fileName)
	{
		read(fileName);
	}

	/**
	 * 画像バッファを返す
	 * @return 画像バッファ
	 */
	public BufferedImage get()
	{
		return image;
	}

	/**
	 * 画像をファイルから読み込みます．
	 * @param fileName 読み込みファイル名
	 * @return 読み込めたらtrue
	 */
	public boolean read(String fileName)
	{
		try{
			image = ImageIO.read(new File(fileName));
		}catch (Exception e){
			Logging.logger.severe(e.getMessage());
			image = null;
			return false;
		}
		return true;
	}

	/**
	 * 画像をファイルへ書き込みます。<br>
	 * @param fileName 画像ファイルのファイル名
	 * return 書き込めたらtrue
	 */
	public boolean write(String fileName)
	{
		try{
			File f = new File(fileName);
			if(f.exists()){
				if(f.canWrite()){
					ImageIO.write(image, "png", f);
				}
			}else{
				ImageIO.write(image, "png", f);
			}
		}catch(Exception e){
			Logging.logger.severe(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 画像を平滑化する<br>
	 * ガウス分布を用い、範囲をシグマで指定する<br>
	 * @param size 平滑サイズ
	 * @param sigma 範囲 大きいほうがよく平滑化される
	 * @return 平滑化された画像
	 */
	public BufferedImage smooth( int size, double sigma )
	{
		//オペレータを作成する
		Matrix operator = gaussianOperator( size, sigma );

		//オペレータを使って畳み込みする
		return convolution(operator, 0);
	}

	/**
	 * 画像を鮮鋭化する<br>
	 * ガウス分布を用い、範囲をシグマで指定する。<br>
	 * @param size 鮮鋭化サイズ
	 * @param sigma 範囲 大きいほうがよく鮮鋭化される
	 * @return 鮮鋭化された画像
	 */
	public BufferedImage sharpen( int size, double sigma )
	{
		//オペレータを作成する
		Matrix operator = gaussianOperator( size, sigma );

		//オペレータをアンシャープマスクにする
		operator = operator.multiply(-1);
		operator.set(size/2, size/2, (2-operator.get(size/2, size/2)*(-1)) );

		//オペレータを使って畳み込みする
		return convolution(operator, 0);
	}

	/**
	 * 正規分布をつかってオペレータを作る
	 * @param size オペレータのサイズ(奇数、7か15を推奨)
	 * @param sigma 正規分布の標準偏差
	 * @return オペレータの行列
	 */
	public static Matrix gaussianOperator( int size, double sigma )
	{
		//サイズが奇数でなければエラー
		if( size%2 == 0 )	return null;

		//オペレータをつくる
		Matrix operator = new Matrix(size, size);

		double distance;
		int center = size/2;
		//各マスに正規分布の値を入れる
		for(int r=0; r<size; r++){
			for(int c=0; c<size; c++){
				distance = Math.hypot(r-center, c-center);//中央からの距離を計算
				operator.set(r, c, Numeric.normalDistribution(distance, 0, sigma));	//正規分布の値をセット
			}
		}

		//全部の値の合計が1になるように調整
		operator = operator.multiply(1/operator.sum());

		return operator;
	}

	/**
	 * BufferedImageの画像を畳み込みする
	 * @param operator 畳み込みオペレータ
	 * @param type エッジ処理 0：処理しない、1:周辺は強制的にゼロ(黒)
	 * @return 畳み込まれた画像
	 */
	public BufferedImage convolution( Matrix operator,int type)
	{
		//オペレータをfloat[]に変換する準備をする
		Vector temp = new Vector(0);
		int size = operator.length();
		for(int i=0; i<size; i++) temp = temp.add(operator.getRow(i));

		//畳み込む
		Kernel kernel=new Kernel(size,size,Cast.doubleToFloat(temp.get()));
		ConvolveOp convolveOp;
		if(type==1) {
			convolveOp=new ConvolveOp(kernel,ConvolveOp.EDGE_ZERO_FILL,null);
		}	else {
			convolveOp=new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
		}
		BufferedImage output=convolveOp.filter(image,null);
		return output;
	}


	/**
	 * BufferedImageのうち指定サイズを切り抜きます。<br>
	 * @param rectangle	切抜きサイズ
	 * @return			切り抜いた画像ファイルバッファ
	 */
	public BufferedImage cut( Rectangle rectangle )
	{
		return image.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	/**
	 * BufferedImageのうち指定サイズを切り抜きます。<br>
	 * @param x 切り抜きエリア左上のx座標
	 * @param y 切り抜きエリア左上のy座標
	 * @param width 切り抜き幅
	 * @param height 切り抜き高さ
	 * @return	切り抜いた画像ファイルバッファ
	 */
	public BufferedImage cut(int x, int y, int width, int height)
	{
		return image.getSubimage(x, y, width, height);
	}

	/**
	 * BufferedImageの画像サイズを拡縮して返します。<br>
	 * @param per	拡縮%
	 * @return		拡縮した画像ファイルバッファ
	 */
	public BufferedImage resize(int per) {

		int nw = (int)Math.round( (double)(image.getWidth() * per) / 100.0);
		int nh = (int)Math.round( (double)(image.getHeight()* per) / 100.0);

		BufferedImage dstImage = null;
		if( image.getColorModel() instanceof IndexColorModel ) {
			dstImage = new BufferedImage(nw, nh, image.getType(), (IndexColorModel)image.getColorModel());
		} else {
			if( image.getType() == 0 ) {
				dstImage = new BufferedImage(nw, nh, BufferedImage.TYPE_4BYTE_ABGR_PRE);
			} else {
				dstImage = new BufferedImage(nw, nh, image.getType());
			}
		}

		double sx = (double) nw / image.getWidth();
		double sy = (double) nh / image.getHeight();
		AffineTransform trans = AffineTransform.getScaleInstance(sx, sy);

		if( dstImage.getColorModel().hasAlpha() && dstImage.getColorModel() instanceof IndexColorModel) {
			int transparentPixel = ((IndexColorModel) dstImage.getColorModel()).getTransparentPixel();
			for(int i=0;i<dstImage.getWidth();++i) {
				for(int j=0;j<dstImage.getHeight();++j) {
					dstImage.setRGB(i, j, transparentPixel);
				}
			}
		}

		Graphics2D g2 = (Graphics2D)dstImage.createGraphics();
		g2.drawImage(image, trans, null);
		g2.dispose();

		return dstImage;
	}

	/**
	 * 画像を比較し、似ていればtrueを、そうでなければfalseを返します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 似ていればtrue
	 */
	public boolean determine(BufferedImage target, double threshold)
	{
		double difference = compare(target);
		Logging.logger.fine(String.valueOf(difference));

		if(difference > threshold)	return false;
		else return true;
	}

	//キャプチャ画像とフォルダ内の全画像との比較
	/**
	 * フォルダを指定し、フォルダ内の全画像と指定画像を比較し、最も似ている画像のファイル名を返します。<br>
	 * @param folderName 比較する画像のあるフォルダ
	 * @param extension 比較画像の拡張子
	 * @return 最も似ていた画像ファイル名
	 */
	public String determine(String folderName, String extension)
	{
		double difference = 0;
		double minimum = Double.MAX_VALUE;
		String fileName = "";
		ImageProcessing target;

		//画像ファイル検索
		File[] files = new File(folderName).listFiles();
		List<File> list = new ArrayList<>();
		for (int i=0; i<files.length; i++){
			String targetExtension = files[i].getAbsolutePath().substring(1, files[i].getAbsolutePath().lastIndexOf("."));
			if(extension.equalsIgnoreCase(targetExtension)) {
				list.add(files[i]);
			}
		}
		//該当したファイル全てに対し比較を実行
		for(int i=0; i< list.size(); i++){
			target = new ImageProcessing(list.get(i).getPath());

			if(!isSameSize(target))	//画像サイズが違ったら比較せず最大値
				difference = Double.MAX_VALUE;
			else
				difference = compare(target);
			Logging.logger.fine(difference + " : " +list.get(i).getName());
			if(minimum > difference){
				minimum = difference;
				fileName = list.get(i).getPath();
			}
		}
		Logging.logger.fine("Minimum = " + minimum + " : " + fileName);
		return fileName;
	}

	/**
	 * 画像が同じサイズならtrue,そうでなければfalseを返します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 同じサイズか否か
	 */
	public boolean isSameSize( ImageProcessing target )
	{
		return isSameSize(target.get());
	}
	/**
	 * 画像が同じサイズならtrue,そうでなければfalseを返します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 同じサイズか否か
	 */
	public boolean isSameSize( BufferedImage target )
	{
		if( image.getWidth() != target.getWidth() || image.getHeight() != target.getHeight() )
		{
			Logging.logger.severe("illegal image size\n");
			return false;
		}
		return true;
	}

	/**
	 * 画像をピクセルごとに比較して、比較値を返します。<br>
	 * 動作が遅いのでcompare()の使用を推奨します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 比較値
	 * @deprecated
	 */
	public double compareUsingIntArray(BufferedImage target)
	{
		int count=0;
		double diff=0;

		//画像サイズが違ったらNaNを返す
		if(!isSameSize(target))	return Double.NaN;

		//画像をint配列に変換42222,42317
		int[] buf1 = convertIntARGB();
		int[] buf2 = new ImageProcessing(target).convertIntARGB();

		//比較する
		for(int i=0; i<buf1.length; i+=4){
			//透過しているピクセル以外を比較する。
			if(buf1[i]==0) continue;
			if(buf2[i]==0) continue;

			//絶対値を加算
			diff += (double)(
					Numeric.square((buf1[i+1]) - (buf2[i+1]))
				 +	Numeric.square((buf1[i+2]) - (buf2[i+2]))
				 +	Numeric.square((buf1[i+3]) - (buf2[i+3]))
				 )/65536/3;
			count++;
		}

		return diff/(double)count;
	}

	/**
	 * 画像をピクセルごとに比較して、比較値を返します。<br>
	 * 動作が遅いのでcompare()の使用を推奨します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 比較値
	 * @deprecated
	 */
	public double compareUsingByteArray(BufferedImage target)
	{
		int count=0;
		double diff=0;

		//画像サイズが違ったらNaNを返す
		if(!isSameSize(target))	return Double.NaN;

		//画像をbyte配列に変換38255,34165,ImageUtility.convert(
		byte[] buf1 = convertByteARGB();
		byte[] buf2 = new ImageProcessing(target).convertByteARGB();

		//比較する
		for(int i=0; i<buf1.length; i+=4){
			//透過しているピクセル以外を比較する。
			if(buf1[i]==0) continue;
			if(buf2[i]==0) continue;

			//絶対値を加算
			diff += (double)(
					Numeric.square(Cast.byteToUnsignedInt(buf1[i+1]) - Cast.byteToUnsignedInt(buf2[i+1]))
				 +	Numeric.square(Cast.byteToUnsignedInt(buf1[i+2]) - Cast.byteToUnsignedInt(buf2[i+2]))
				 +	Numeric.square(Cast.byteToUnsignedInt(buf1[i+3]) - Cast.byteToUnsignedInt(buf2[i+3]))
				 )/65536/3;
			count++;
		}

		return diff/(double)count;
	}


	/**
	 * 画像をピクセルごとに比較して、比較値を返します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 比較値
	 */
	public double compare( BufferedImage target )
	{
		int count=0;
		double diff=0;

		//画像サイズが違ったらNaNを返す
		if(!isSameSize(target))	return Double.NaN;

		//画像をbyte配列に変換45773,57137
		DataBufferByte buf1 = convertARGB();
		DataBufferByte buf2 = new ImageProcessing(target).convertARGB();

		//比較する
		for(int i=0; i<buf1.getSize(); i+=4){
			//透過しているピクセル以外を比較する。
			if(buf1.getElem(i)==0) continue;
			if(buf2.getElem(i)==0) continue;

			//絶対値を加算
			diff += (double)(
					Numeric.square(buf1.getElem(i+1) - buf2.getElem(i+1))
				 +	Numeric.square(buf1.getElem(i+2) - buf2.getElem(i+2))
				 +	Numeric.square(buf1.getElem(i+3) - buf2.getElem(i+3))
				 )/65536/3;
			count++;
		}

		return diff/(double)count;
	}

	/**
	 * 画像をピクセルごとに比較して、比較値を返します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 比較値
	 */
	public double compare( ImageProcessing target )
	{
		return compare(target.get());
	}

	/**
	 * 画像をピクセルごとに比較して、比較値を返します。<br>
	 * 動作が遅いのでcompare()の使用を推奨します。<br>
	 * @param target 2つ目の画像ファイルバッファ
	 * @return 比較値
	 * @deprecated
	 */
	public double compareUsingGetRGB( BufferedImage target)
	{
		int height = image.getHeight();
		int width = target.getWidth();
		int count=0;
		double diff=0;
		int color1 = 0;
		int color2 = 0;

		//画像サイズが違ったらNaNを返す
		if(!isSameSize(target))	return Double.NaN;

		//ピクセルデータをint配列で取得
		int[] pixel1 = image.getRGB(0, 0, width, height, null, 0, width);
		int[] pixel2 = target.getRGB(0, 0, width, height, null, 0, width);

		//比較する
		for(int h=0; h<height; h++){
			for(int w=0; w<width; w++){
				//色の読込
				color1 = pixel1[h*width+w];
				color2 = pixel2[h*width+w];
				if(ImageUtility.alpha(color1)==0) continue;
				if(ImageUtility.alpha(color2)==0) continue;

				//絶対値を加算
				diff += (double)(
						Numeric.square(ImageUtility.red(color1)  - ImageUtility.red(color2))
					 +	Numeric.square(ImageUtility.green(color1)- ImageUtility.green(color2))
					 +	Numeric.square(ImageUtility.blue(color1) - ImageUtility.blue(color2))
					 )/65536/3;
				count++;
			}
		}

		return diff/(double)count;
	}

	/**
	 * 画像をネガポジ反転します。<br>
	 */
	public void negaposi()
	{
		int height = image.getHeight();
		int width = image.getWidth();
		int[] ints = new int[width * height];
		int red, blue, green, alpha, count=0;

		//ピクセルデータをByte配列で取得
		DataBufferByte buf = convertARGB();
		DataBuffer iBuf = new DataBufferInt(ints, width*height);

		//反転する
		for(int i=0; i<buf.getSize(); i+=4, count++){
			alpha = buf.getElem(i);
			red   = 255-buf.getElem(i+1);
			green = 255-buf.getElem(i+2);
			blue  = 255-buf.getElem(i+3);
			iBuf.setElem(count, ImageUtility.argb(alpha, red, green, blue));
		}

		int[] bandMasks = new int[] { 0x00ff0000, 0x0000ff00, 0x000000ff };
		WritableRaster writableRaster = Raster.createPackedRaster(iBuf, width, height, width, bandMasks, null);
		image.setData(writableRaster);
		//もしくは
		//DirectColorModel colorModel = new DirectColorModel(32, bandMasks[0], bandMasks[1], bandMasks[2]);
		//return image = new BufferedImage(colorModel, writableRaster, true, null);
	}

	/**
	 * 画像をネガポジ反転します。<br>
	 * 動作が遅いのでnegaposi()の使用を推奨します。<br>
	 * @deprecated
	 */
	public void negaposiUsingArray()
	{
		int height = image.getHeight();
		int width = image.getWidth();
		int red, blue, green, alpha, count=0;

		//ピクセルデータをByte配列で取得
		//DataBufferByte buf = ImageUtility.convertARGB(image);
		//byte[] bufa = buf.getData();
		byte[] bufa = convertByteARGB();
		int[] iBuf = new int[width*height];

		//反転する
		for(int i=0; i<bufa.length; i+=4, count++){
			alpha = bufa[i];
			red   = 255-bufa[i+1];
			green = 255-bufa[i+2];
			blue  = 255-bufa[i+3];
			iBuf[count] = ImageUtility.argb(alpha, red, green, blue);
		}

		int[] bandMasks = new int[] { 0x00ff0000, 0x0000ff00, 0x000000ff };
		WritableRaster writableRaster = Raster.createPackedRaster(new DataBufferInt(iBuf, width*height), width, height, width, bandMasks, null);
		image.setData(writableRaster);
		//もしくは
		//DirectColorModel colorModel = new DirectColorModel(32, bandMasks[0], bandMasks[1], bandMasks[2]);
		//return image = new BufferedImage(colorModel, writableRaster, true, null);
	}

	/**
	 * 画像をネガポジ反転します。<br>
	 * 動作が遅いのでnegaposi()の使用を推奨します。<br>
	 * @deprecated
	 */
	public void negaposiUsingBufferedImagePixelArray()
	{
		int height = image.getHeight();
		int width = image.getWidth();
		int color = 0;
		int red, blue, green, alpha;
		//ピクセルデータをint配列で取得
		int[] pixel = image.getRGB(0, 0, width, height, null, 0, width);

		//反転する
		for(int h=0; h<height; h++){
			for(int w=0; w<width; w++){
				//色の読込
				color = pixel[h*width+w];
				//反転
				alpha = ImageUtility.alpha(color);
				red   = 255-ImageUtility.red(color);
				blue  = 255-ImageUtility.blue(color);
				green = 255-ImageUtility.green(color);
				pixel[h*width+w] = ImageUtility.argb(alpha, red, green, blue);
			}
		}
		image.setRGB(0, 0, width, height, pixel, 0, width);
	}

	/**
	 * 画像をネガポジ反転します。<br>
	 * 動作が遅いのでnegaposi()の使用を推奨します。<br>
	 * @deprecated
	 */
	public void negaposiUsingBufferedImageEach()
	{
		int height = image.getHeight();
		int width = image.getWidth();
		int color = 0;
		int red, blue, green, alpha;

		//反転する
		for(int h=0; h<height; h++){
			for(int w=0; w<width; w++){
				//色の読込
				color = image.getRGB(w, h);
				//反転
				alpha = ImageUtility.alpha(color);
				red   = 255-ImageUtility.red(color);
				blue  = 255-ImageUtility.blue(color);
				green = 255-ImageUtility.green(color);
				//色をセット
				image.setRGB(w, h, ImageUtility.argb(alpha, red, green, blue));
			}
		}
	}

	/**
	 * BufferedImageのimageTypeに関わらず4ByteABGR形式のDataBufferByteに変換します。<br>
	 * ※BufferedImageにはint[]型、byte[]型やRGB, ARGB, BGR, ABGR等が混在しています。<br>
	 * ※白黒画像等のimageTypeの場合はnullを返します。<br>
	 * @return		変換後の4byteABGR形式データバッファ
	 */
	public DataBufferByte convertABGR()
	{
		int alpha=1, count=0, color=0;
		DataBufferByte rBuf=null;
		DataBuffer iBuf = image.getRaster().getDataBuffer();

		switch(image.getType()){
			case BufferedImage.TYPE_INT_RGB:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, 0x80);
					rBuf.setElem( i*4+1, color&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color>>16&0xff );
				}
				break;

			case BufferedImage.TYPE_INT_ARGB:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, color>>24&0xff );
					rBuf.setElem( i*4+1, color&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color>>16&0xff );
				}
				break;

			case  BufferedImage.TYPE_INT_ARGB_PRE:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i); alpha = color>>24&0xff;
					rBuf.setElem( i*4+0, alpha );
					rBuf.setElem( i*4+1, (color&0xff)/alpha );
					rBuf.setElem( i*4+2, (color>>8&0xff)/alpha );
					rBuf.setElem( i*4+3, (color>>16&0xff)/alpha );
				}
				break;

			case BufferedImage.TYPE_INT_BGR:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, 0x80);
					rBuf.setElem( i*4+1, color>>16&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color&0xff );
				}
				break;

			case BufferedImage.TYPE_3BYTE_BGR:
				rBuf = new DataBufferByte(image.getHeight()*image.getWidth()*4);
				for(int i=0; i<rBuf.getSize(); i++)
				{
					switch(i%4){
						case 0:
							rBuf.setElem(i, 0x80);
							break;
						case 1:
							rBuf.setElem(i, iBuf.getElem(count));
							count++;
							break;
						case 2:
							rBuf.setElem(i, iBuf.getElem(count));
							count++;
							break;
						case 3:
							rBuf.setElem(i, iBuf.getElem(count));
							count++;
							break;
						default:
							return null;
					}
				}
				break;
			case BufferedImage.TYPE_4BYTE_ABGR:
				rBuf = (DataBufferByte)iBuf;
				break;

			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				rBuf = new DataBufferByte(iBuf.getSize());
				for(int i=0; i<rBuf.getSize(); i++)
				{
					if(i%4 == 0){
						alpha = rBuf.getElem(i);
					}else{
						rBuf.setElem( i, rBuf.getElem(i)/alpha );
					}
				}
				break;
			default:
				return null;
		}

		return rBuf;
	}

	/**
	 * BufferedImageのimageTypeに関わらず4ByteARGB形式のDataBufferByteに変換します。<br>
	 * ※BufferedImageにはint[]型、byte[]型やRGB, ARGB, BGR, ABGR等が混在しています。<br>
	 * ※白黒画像等のimageTypeの場合はnullを返します。<br>
	 * @return		変換後の4byteARGB形式データバッファ
	 */
	public DataBufferByte convertARGB()
	{
		int alpha=1, count=0, color=0;
		DataBufferByte rBuf=null;
		DataBuffer iBuf = image.getRaster().getDataBuffer();

		switch(image.getType()){
			case BufferedImage.TYPE_INT_RGB:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, 0x80);
					rBuf.setElem( i*4+1, color>>16&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color&0xff );
				}
				break;

			case BufferedImage.TYPE_INT_ARGB:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, color>>24&0xff );
					rBuf.setElem( i*4+1, color>>16&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color&0xff );
				}
				break;

			case  BufferedImage.TYPE_INT_ARGB_PRE:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i); alpha = color>>24&0xff;
					rBuf.setElem( i*4+0, alpha );
					rBuf.setElem( i*4+1, (color>>16&0xff)/alpha );
					rBuf.setElem( i*4+2, (color>>8&0xff)/alpha );
					rBuf.setElem( i*4+3, (color&0xff)/alpha );
				}
				break;

			case BufferedImage.TYPE_INT_BGR:
				rBuf = new DataBufferByte(iBuf.getSize()*4);
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i);
					rBuf.setElem( i*4+0, 0x80);
					rBuf.setElem( i*4+1, color&0xff );
					rBuf.setElem( i*4+2, color>>8&0xff );
					rBuf.setElem( i*4+3, color>>16&0xff );
				}
				break;

			case BufferedImage.TYPE_3BYTE_BGR:
				rBuf = new DataBufferByte(image.getHeight()*image.getWidth()*4);
				for(int i=0; i<rBuf.getSize(); i++)
				{
					switch(i%4){
						case 0:
							rBuf.setElem(i, 0x80);
							break;
						case 1:
							rBuf.setElem(i, iBuf.getElem(count+2));
							count++;
							break;
						case 2:
							rBuf.setElem(i, iBuf.getElem(count));
							count++;
							break;
						case 3:
							rBuf.setElem(i, iBuf.getElem(count-2));
							count++;
							break;
						default:
							return null;
					}
				}
				break;
			case BufferedImage.TYPE_4BYTE_ABGR:
				rBuf = new DataBufferByte(iBuf.getSize());
				for(int i=0; i<rBuf.getSize(); i++)
				{
					switch(i%4){
						case 0:
							rBuf.setElem(i, iBuf.getElem(i));
							break;
						case 1:
							rBuf.setElem(i, iBuf.getElem(i+2));
							break;
						case 2:
							rBuf.setElem(i, iBuf.getElem(i));
							break;
						case 3:
							rBuf.setElem(i, iBuf.getElem(i-2));
							break;
						default:
							return null;
					}
				}
				break;

			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				rBuf = new DataBufferByte(iBuf.getSize());
				for(int i=0; i<rBuf.getSize(); i++)
				{
					switch(i%4){
						case 0:
							rBuf.setElem(i, iBuf.getElem(i));
							break;
						case 1:
							rBuf.setElem(i, iBuf.getElem(i+2)/alpha);
							break;
						case 2:
							rBuf.setElem(i, iBuf.getElem(i));
							break;
						case 3:
							rBuf.setElem(i, iBuf.getElem(i-2)/alpha);
							break;
						default:
							return null;
					}
				}
			default:
				return null;
		}

		return rBuf;
	}


	/**
	 * BufferedImageのimageTypeに関わらず4ByteARGB形式のbyte[]に変換します。<br>
	 * ※白黒画像等のimageTypeの場合はnullを返します。<br>
	 * @return		変換後の4byteARGB形式データバッファ
	 */
	public byte[] convertByteARGB()
	{
		int alpha=1, count=0, color=0;
		byte[] rBuf=null;
		DataBuffer iBuf = image.getRaster().getDataBuffer();

		switch(image.getType()){
			case BufferedImage.TYPE_INT_RGB:
				rBuf = new byte[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (byte)(0x80);
					rBuf[ i*4+1] = (byte)(color>>16&0xff );
					rBuf[ i*4+2] = (byte)(color>>8&0xff );
					rBuf[ i*4+3] = (byte)(color&0xff );
				}
				break;

			case BufferedImage.TYPE_INT_ARGB:
				rBuf = new byte[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (byte)(color>>24&0xff );
					rBuf[ i*4+1] = (byte)(color>>16&0xff );
					rBuf[ i*4+2] = (byte)(color>>8&0xff );
					rBuf[ i*4+3] = (byte)(color&0xff );
				}
				break;

			case  BufferedImage.TYPE_INT_ARGB_PRE:
				rBuf = new byte[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i); alpha = color>>24&0xff;
					rBuf[ i*4+0] = (byte)(alpha );
					rBuf[ i*4+1] = (byte)((color>>16&0xff)/alpha );
					rBuf[ i*4+2] = (byte)((color>>8&0xff)/alpha );
					rBuf[ i*4+3] = (byte)((color&0xff)/alpha );
				}
				break;

			case BufferedImage.TYPE_INT_BGR:
				rBuf = new byte[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (byte)(0x80);
					rBuf[ i*4+1] = (byte)(color&0xff );
					rBuf[ i*4+2] = (byte)(color>>8&0xff );
					rBuf[ i*4+3] = (byte)(color>>16&0xff );
				}
				break;

			case BufferedImage.TYPE_3BYTE_BGR:
				rBuf = new byte[image.getHeight()*image.getWidth()*4];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (byte)(0x80);
							break;
						case 1:
							rBuf[i] = (byte)(iBuf.getElem(count+2));
							count++;
							break;
						case 2:
							rBuf[i] = (byte)(iBuf.getElem(count));
							count++;
							break;
						case 3:
							rBuf[i] = (byte)(iBuf.getElem(count-2));
							count++;
							break;
						default:
							return null;
					}
				}
				break;
			case BufferedImage.TYPE_4BYTE_ABGR:
				rBuf = new byte[iBuf.getSize()];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (byte)(iBuf.getElem(i));
							break;
						case 1:
							rBuf[i] = (byte)(iBuf.getElem(i+2));
							break;
						case 2:
							rBuf[i] = (byte)(iBuf.getElem(i));
							break;
						case 3:
							rBuf[i] = (byte)(iBuf.getElem(i-2));
							break;
						default:
							return null;
					}
				}
				break;

			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				rBuf = new byte[iBuf.getSize()];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (byte)(iBuf.getElem(i));
							break;
						case 1:
							rBuf[i] = (byte)(iBuf.getElem(i+2)/alpha);
							break;
						case 2:
							rBuf[i] = (byte)(iBuf.getElem(i));
							break;
						case 3:
							rBuf[i] = (byte)(iBuf.getElem(i-2)/alpha);
							break;
						default:
							return null;
					}
				}
			default:
				return null;
		}

		return rBuf;
	}

	/**
	 * BufferedImageのimageTypeに関わらず4ByteARGB形式のDataBufferByteに変換します。<br>
	 * ※BufferedImageにはint[]型、byte[]型やRGB, ARGB, BGR, ABGR等が混在しています。<br>
	 * ※白黒画像等のimageTypeの場合はnullを返します。<br>
	 * @return		変換後の4byteARGB形式データバッファ
	 */
	public int[] convertIntARGB()
	{
		int alpha=1, count=0, color=0;
		int[] rBuf=null;
		DataBuffer iBuf = image.getRaster().getDataBuffer();

		switch(image.getType()){
			case BufferedImage.TYPE_INT_RGB:
				rBuf = new int[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (0x80);
					rBuf[ i*4+1] = (color>>16&0xff );
					rBuf[ i*4+2] = (color>>8&0xff );
					rBuf[ i*4+3] = (color&0xff );
				}
				break;

			case BufferedImage.TYPE_INT_ARGB:
				rBuf = new int[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++){
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (color>>24&0xff );
					rBuf[ i*4+1] = (color>>16&0xff );
					rBuf[ i*4+2] = (color>>8&0xff );
					rBuf[ i*4+3] = (color&0xff );
				}
				break;

			case  BufferedImage.TYPE_INT_ARGB_PRE:
				rBuf = new int[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i); alpha = color>>24&0xff;
					rBuf[ i*4+0] = (alpha );
					rBuf[ i*4+1] = ((color>>16&0xff)/alpha );
					rBuf[ i*4+2] = ((color>>8&0xff)/alpha );
					rBuf[ i*4+3] = ((color&0xff)/alpha );
				}
				break;

			case BufferedImage.TYPE_INT_BGR:
				rBuf = new int[iBuf.getSize()*4];
				for(int i=0; i<iBuf.getSize(); i++)
				{
					color = iBuf.getElem(i);
					rBuf[ i*4+0] = (0x80);
					rBuf[ i*4+1] = (color&0xff );
					rBuf[ i*4+2] = (color>>8&0xff );
					rBuf[ i*4+3] = (color>>16&0xff );
				}
				break;

			case BufferedImage.TYPE_3BYTE_BGR:
				rBuf = new int[image.getHeight()*image.getWidth()*4];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (0x80);
							break;
						case 1:
							rBuf[i] = (iBuf.getElem(count+2));
							count++;
							break;
						case 2:
							rBuf[i] = (iBuf.getElem(count));
							count++;
							break;
						case 3:
							rBuf[i] = (iBuf.getElem(count-2));
							count++;
							break;
						default:
							return null;
					}
				}
				break;
			case BufferedImage.TYPE_4BYTE_ABGR:
				rBuf = new int[iBuf.getSize()];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (iBuf.getElem(i));
							break;
						case 1:
							rBuf[i] = (iBuf.getElem(i+2));
							break;
						case 2:
							rBuf[i] = (iBuf.getElem(i));
							break;
						case 3:
							rBuf[i] = (iBuf.getElem(i-2));
							break;
						default:
							return null;
					}
				}
				break;

			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				rBuf = new int[iBuf.getSize()];
				for(int i=0; i<rBuf.length; i++)
				{
					switch(i%4){
						case 0:
							rBuf[i] = (iBuf.getElem(i));
							break;
						case 1:
							rBuf[i] = (iBuf.getElem(i+2)/alpha);
							break;
						case 2:
							rBuf[i] = (iBuf.getElem(i));
							break;
						case 3:
							rBuf[i] = (iBuf.getElem(i-2)/alpha);
							break;
						default:
							return null;
					}
				}
			default:
				return null;
		}

		return rBuf;
	}
}
