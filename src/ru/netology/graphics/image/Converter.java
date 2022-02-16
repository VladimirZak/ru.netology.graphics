package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private int maxWidth = 0;
    private int maxHeight = 0;
    private double maxRatio = 0;
    private TextColorSchema schema;


    public Converter() {
        this.schema = new Symbol();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = 0;
        int newHeight = 0;
        int width = img.getWidth();
        int height = img.getHeight();
        double ratio = 0;
        boolean flag = true;

        if (maxRatio != 0) {
            if (width > height) {
                ratio = (double) width / height;
            } else if (height > width) {
                ratio = (double) height / width;
            }
            if (ratio > maxRatio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }

        if (maxWidth == 0 && maxHeight == 0) {
            newWidth = width;
            newHeight = height;
        }

        if (maxWidth != 0) {
            if (width >= maxWidth && width >= height) {
                newWidth = maxWidth;
                newHeight = resizeHeight(height, width, maxWidth);
                flag = false;
            } else if (height > width && maxHeight == 0) {
                newWidth = maxWidth;
                newHeight = resizeHeight(height, width, maxWidth);
                ;
            }
        }

        if (maxHeight != 0 && flag) {
            if (height >= maxHeight && height >= width) {
                newHeight = maxHeight - 50;
                newWidth = resizeWidth(height, width, maxHeight);
            } else if (width > height && maxWidth == 0) {
                newHeight = maxHeight - 50;
                newWidth = resizeWidth(height, width, maxHeight);
                ;
            }
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        ImageIO.write(img, "png", new File("out.png"));

        char[][] textArray = new char[newWidth][newHeight];

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                textArray[w][h] = c;
            }
        }

        StringBuilder str = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                str.append(textArray[w][h]).append(textArray[w][h]);
            }
            str.append('\n');
        }

        return str.toString();
    }

    public int resizeHeight(int height, int width, int maxWidth) {
        double dHeight = (double) height / ((double) width / (double) maxWidth);
        return (int) (dHeight - 50);
    }

    public int resizeWidth(int height, int width, int maxHeight) {
        double dWidth = (double) width / ((double) height / (double) maxHeight);
        return (int) dWidth;
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
