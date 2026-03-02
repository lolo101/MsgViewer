package at.redeye.FrameWork.base.imagestorage;

import javax.swing.*;
import java.awt.*;

public class ImageUtils {

    public static ImageIcon loadScaledImageIcon(byte[] bytes, String descr, int width, int height) {
        ImageIcon image = loadImageIcon(bytes, descr);
        image.setImage(scaleImage(image.getImage(), width, height));
        return image;
    }

    private static ImageIcon loadImageIcon(byte[] bytes, String descr) {
        return new ImageIcon(bytes, descr);
    }

    private static Image scaleImage(Image image, int width, int height) {
        int image_width = image.getWidth(null);
        int image_height = image.getHeight(null);

        if (image_height <= height &&
                image_width <= width) {
            // do not scale
            return image;
        }

        double ratio = (double) image_width / (double) image_height;

        int scale_height = (int) ((double) width / ratio);

        if (scale_height <= 0)
            scale_height = height;

        return image.getScaledInstance(width, scale_height, Image.SCALE_SMOOTH);
    }
}
