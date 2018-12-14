package com.eplat.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 水印工具类
 * 
 * 
 */
public class ImageRemarkUtil {
	
	// 水印透明度
	private static float alpha = 0.5f;
	// 水印横向位置
	private static int positionWidth = 150;
	// 水印纵向位置
	private static int positionHeight = 300;
	// 水印大小 宽度
	private static int iconWidth=0;
	// 水印大小 高度
	private static int iconHeight=0;
	
	
	public static void setIconWidth(int iconWidth) {
		ImageRemarkUtil.iconWidth = iconWidth;
	}

	public static void setIconHeight(int iconHeight) {
		ImageRemarkUtil.iconHeight = iconHeight;
	}

	// 水印文字字体
	private static Font font = new Font("宋体", Font.BOLD, 72);
	// 水印文字颜色
	private static Color color = Color.red;

	public static void setAlpha(float alpha) {
		ImageRemarkUtil.alpha = alpha;
	}

	public static void setPositionWidth(int positionWidth) {
		ImageRemarkUtil.positionWidth = positionWidth;
	}

	public static void setPositionHeight(int positionHeight) {
		ImageRemarkUtil.positionHeight = positionHeight;
	}

	/**
	 * 
	 * @param alpha
	 *            水印透明度
	 * @param positionWidth
	 *            水印横向位置
	 * @param positionHeight
	 *            水印纵向位置
	 * @param font
	 *            水印文字字体
	 * @param color
	 *            水印文字颜色
	 */
	public static void setImageMarkOptions(float alpha, int positionWidth, int positionHeight, Font font, Color color) {
		if (alpha != 0.0f)
			ImageRemarkUtil.alpha = alpha;
		if (positionWidth != 0)
			ImageRemarkUtil.positionWidth = positionWidth;
		if (positionHeight != 0)
			ImageRemarkUtil.positionHeight = positionHeight;
		if (font != null)
			ImageRemarkUtil.font = font;
		if (color != null)
			ImageRemarkUtil.color = color;
	}

	/**
	 * 给图片添加水印图片
	 * 
	 * @param iconPath 水印图片路径
	 * @param srcImgPath 源图片路径
	 * @param targerPath 目标图片路径
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印图片、可设置水印图片旋转角度
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
		OutputStream os = null;
		try {

			Image srcImg = ImageIO.read(new File(srcImgPath));

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 1、得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 2、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			// 3、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}

			// 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);
			// 5、得到Image对象。
			Image img = imgIcon.getImage();

			if (iconHeight>0 && iconWidth>0) {
                //为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
				Image smallImage =img.getScaledInstance(iconWidth,iconHeight,Image.SCALE_FAST);
                // 再由修改后的Image来生成合适的Icon
				ImageIcon smallIcon = new ImageIcon(smallImage);
                img=smallIcon.getImage();
			}
			
            //透明度设置
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			// 6、水印图片的位置
			g.drawImage(img, positionWidth, positionHeight, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			// 7、释放资源
			g.dispose();

			// 8、获取目标文件扩展名
			String ext=targerPath.substring(targerPath.lastIndexOf(".")+1);
			
			// 9、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			System.out.println("图片完成添加水印图片");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给图片添加水印文字
	 * 
	 * @param logoText
	 *            水印文字
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath) {
		markImageByText(logoText, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * 
	 * @param logoText
	 * @param srcImgPath
	 * @param targerPath
	 * @param degree
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {
		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			// 4、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 5、设置水印文字颜色
			g.setColor(color);
			// 6、设置水印文字Font
			g.setFont(font);
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			g.drawString(logoText, positionWidth, positionHeight);
			// 9、释放资源
			g.dispose();
			
			// 8、获取目标文件扩展名
			String ext=targerPath.substring(targerPath.lastIndexOf(".")+1);
			
			// 10、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			System.out.println("图片完成添加水印文字");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		
		String srcImgPath = "d:/1.jpg";
		String logoText = "复 印 无 效";
		String iconPath = "d:/2.jpg";

		String targerTextPath = "d:/qie_text.jpg";
		String targerTextPath2 = "d:/qie_text_rotate.jpg";

		String targerIconPath = "d:/qie_icon.jpg";
		String targerIconPath2 = "d:/qie_icon_rotate.jpg";

		System.out.println("给图片添加水印文字开始...");
		// 给图片添加水印文字
		markImageByText(logoText, srcImgPath, targerTextPath);
		// 给图片添加水印文字,水印文字旋转-45
		markImageByText(logoText, srcImgPath, targerTextPath2, -45);
		System.out.println("给图片添加水印文字结束...");

		System.out.println("给图片添加水印图片开始...");
		setImageMarkOptions(0.3f, 1, 1, null, null);
		// 给图片添加水印图片
		markImageByIcon(iconPath, srcImgPath, targerIconPath);
		// 给图片添加水印图片,水印图片旋转-45
		markImageByIcon(iconPath, srcImgPath, targerIconPath2, -45);
		System.out.println("给图片添加水印图片结束...");

	}
}
