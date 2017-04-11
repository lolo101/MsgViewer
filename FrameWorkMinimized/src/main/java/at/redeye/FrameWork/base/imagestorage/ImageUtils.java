/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package at.redeye.FrameWork.base.imagestorage;

import at.redeye.FrameWork.base.AutoLogger;
// import at.redeye.FrameWork.base.imagestorage.bindtypes.DBImage;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/* Utils.java is used by FileChooserDemo2.java. */
/* Utils.java is used by FileChooserDemo2.java. */
public class ImageUtils {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    public final static String bmp = "bmp";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static Icon loadScaledImageAsIcon(String path, int w, int h) {
        ImageIcon icon = createImageIcon(path);
        Image image = scaleImage(icon.getImage(),w,h);
        icon.setImage(image);
        return icon;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static ImageIcon loadScaledImageIcon(String path)
    {
        return loadScaledImageIcon(path, 50,50 );
    }

    public static ImageIcon loadScaledImageIcon(String path, int width, int height )
    {
        return loadScaledImageIcon(path, width, height, -1 );
    }
    
    public static ImageIcon loadScaledImageIcon(String path, int width, int height, int max_height) {

        try {
            final java.net.URL imgURL;

            imgURL = new java.net.URL("file:" + path);

            if (imgURL != null) {

                ImageIcon icon = null;

                if( path.endsWith("bmp") )
                {
                    try
                    {
                        BufferedImage bimg = ImageIO.read(imgURL);

                        if( bimg == null )
                            return null;

                        icon = new ImageIcon(bimg);
                            
                    } catch( IOException ex) {
                        return null;
                    }
                }
                else
                {
                    icon =  new ImageIcon(imgURL);
                }

                Image image = icon.getImage();
                image = scaleImage(image,width,height,max_height);
                icon.setImage(image);
                return icon;
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public static ImageIcon loadImageIcon(byte bytes[], String descr) 
    {
        ImageIcon image = new ImageIcon(bytes,descr);
        return image;        
    }

    public static ImageIcon loadScaledImageIcon(byte bytes[], String descr, int width, int height) 
    {
        ImageIcon image = loadImageIcon(bytes,descr);
        image.setImage(scaleImage(image.getImage(),width,height));
        return image;
    }
    
    public static ImageIcon loadScaledImageIcon(byte bytes[], String descr) 
    {
        return loadScaledImageIcon(bytes, descr, 50, 50);
    }

    public static Image scaleImage(Image image, int width, int height )
    {
        return scaleImage(image, width , height, -1);
    }

    /**
     * Returns the size of the scaled image, by not changing the ratio of the origin with an height.
     * If the size of the image is smaller than the with and height, nothing will be scaled
     * This function does not scale the image itself. It only calcs the dimension
     * @param image
     * @param width
     * @param height
     * @param max_height
     * @return
     */
    public static Dimension getScaledSize(Image image, int width, int height, int max_height )
    {
        int image_width = image.getWidth(null);
        int image_height = image.getHeight(null);

        int scale_width = width;
        int scale_height = height;

        if( image_width < width )
            scale_width = image_width;

        if( image_height < height )
            scale_height = image_height;

        if( image_height < height &&
            image_width < width )
        {
            // do not scale
            return new Dimension(image_width, image_height);
        }

        double ratio = (double)image_width / (double)image_height;

        scale_width = width;

        scale_height = (int)((double)width / ratio);

        if( scale_width <= 0 )
            scale_width = width;

        if( scale_height <= 0 )
            scale_height = height;

        if( max_height != -1 && scale_height > max_height )
            scale_height = max_height;

        return new Dimension( scale_width, scale_height );
    }

    public static Image scaleImage(Image image, int width, int height, int max_height )
    {
        int image_width = image.getWidth(null);
        int image_height = image.getHeight(null);
        
        int scale_width = width;
        int scale_height = height;
        
        if( image_width < width )
            scale_width = image_width;
        
        if( image_height < height )
            scale_height = image_height;
        
        if( image_height < height &&
            image_width < width )
        {
            // do not scale            
            return image;
        }
        
        double ratio = (double)image_width / (double)image_height;
        
        scale_width = width;
        
        scale_height = (int)((double)width / ratio);

        if( scale_width <= 0 )
            scale_width = width;

        if( scale_height <= 0 )
            scale_height = height;

        if( max_height != -1 && scale_height > max_height )
            scale_height = max_height;
        
        image = image.getScaledInstance(scale_width, scale_height, Image.SCALE_SMOOTH);
            
        return image;
    }

    final static double DEGREE_90 = 90.0 * Math.PI / 180.0;

    /**
     * Creates a rotated version of the input image.
     *
     * @param c            The component to get properties useful for painting, e.g. the foreground
     *                     or background color.
     * @param icon         the image to be rotated.
     * @param rotatedAngle the rotated angle, in degree, clockwise. It could be any double but we
     *                     will mod it with 360 before using it.
     *
     * @return the image after rotating.
     */
    public static ImageIcon createRotatedImage(Component c, Icon icon, double rotatedAngle) {

        // convert minus values into right
        while( rotatedAngle < 0 )
            rotatedAngle += 360;

        // convert rotatedAngle to a value from 0 to 360
        double originalAngle = rotatedAngle % 360;
        if (rotatedAngle != 0 && originalAngle == 0) {
            originalAngle = 360.0;
        }


        // convert originalAngle to a value from 0 to 90
        double angle = originalAngle % 90;
        if (originalAngle != 0.0 && angle == 0.0) {
            angle = 90.0;
        }

        double radian = Math.toRadians(angle);

        int iw = icon.getIconWidth();
        int ih = icon.getIconHeight();
        int w;
        int h;

        if ((originalAngle >= 0 && originalAngle <= 90) || (originalAngle > 180 && originalAngle <= 270)) {
            w = (int) (iw * Math.sin(DEGREE_90 - radian) + ih * Math.sin(radian));
            h = (int) (iw * Math.sin(radian) + ih * Math.sin(DEGREE_90 - radian));
        }
        else {
            w = (int) (ih * Math.sin(DEGREE_90 - radian) + iw * Math.sin(radian));
            h = (int) (ih * Math.sin(radian) + iw * Math.sin(DEGREE_90 - radian));
        }
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();

        // calculate the center of the icon.
        int cx = iw / 2;
        int cy = ih / 2;

        // move the graphics center point to the center of the icon.
        g2d.translate(w/2, h/2);

        // rotate the graphcis about the center point of the icon
        g2d.rotate(Math.toRadians(originalAngle));

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        icon.paintIcon(c, g2d, -cx, -cy);

        g2d.dispose();
        return new ImageIcon(image);
    }

    /**
     * calculates width and height of an image stream
     * @param bytes
     * @param descr
     * @return Dimensions
     */
    public static Dimension calcDimensions( byte bytes[], String descr )
    {
        // TODO implement better code for that, without rendering the image
        ImageIcon image = loadImageIcon(bytes, descr );
        Dimension dim = new Dimension(image.getIconWidth(),image.getIconHeight());

        return dim;
    }
/*
    public static Dimension calcDimensions( DBImage img )
    {
        // I hope in the DB are correct values
        if( img.width.getValue() > 0 && img.height.getValue() > 0 )
            return new Dimension( img.width.getValue(), img.height.getValue() );

        return calcDimensions(img.image.value, img.file_name.toString() );
    }
*/
}
