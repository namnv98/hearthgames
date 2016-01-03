package com.hearthgames.utils;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageManipulation {

    public static void diffFiles(File newFile) throws IOException {

        if (newFile.isDirectory()) {
            File[] files = newFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    diffFiles(file);
                }
            }
        } else {
            File oldFile = new File(newFile.getAbsolutePath().replace("_NEW", "_OLD"));
            if (!oldFile.exists()) {
                FileUtils.copyFile(newFile, new File(newFile.getAbsolutePath().replace("_NEW", "DIFF")));
            }
        }
    }

    public static void performDiff() throws IOException {
        File oldDir = new File("C:\\HearthstoneData\\_NEW");
        diffFiles(oldDir);
    }

    public static void main(String[] args) throws Exception {


        heropowers();


    }

    private static void weapons() {
        File inputDir = new File("c:\\images\\weaponimages");
        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file1 : files) {
                File file2 = new File("C:\\images\\assets\\weapon-mask.png");
                File outputFile = new File("C:\\images\\weaponoutput2\\" + file1.getName());

                try {
                    combineWeaponImages(file1, file2, outputFile);
                } catch (Exception e) {
                    System.out.println("failed to convert + " + file1.getAbsolutePath());
                }
            }
        }
    }


    private static void heropowers() {
        File inputDir = new File("c:\\images\\heropowers");
        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file1 : files) {

                File file2 = new File("C:\\images\\assets\\hpmask3.png");
                File outputFile = new File("C:\\images\\hpoutput3\\" + file1.getName());

                try {
                    combineHeroPowerImages(file1, file2, outputFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("failed to convert + " + file1.getAbsolutePath());
                }

            }
        }
    }


    private static void newMinions() {
        File inputDir = new File("c:\\images\\temp");
        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file1 : files) {

                File file2 = new File("C:\\images\\oval.png");
                File outputFile = new File("C:\\images\\output2\\" + file1.getName());

                try {
                    combineImages(file1, file2, outputFile);
                } catch (Exception e) {
                    System.out.println("failed to convert + " + file1.getAbsolutePath());
                }

            }
        }
    }

    private static void existingMinions() {
        File inputDir = new File("c:\\images\\minons");
        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file1 : files) {

                File file2 = new File("C:\\images\\assets\\oval2.png");
                File outputFile = new File("C:\\images\\output2\\" + file1.getName());

                try {
                    combineExistingMinionImages(file1, file2, outputFile);
                } catch (Exception e) {
                    System.out.println("failed to convert + " + file1.getAbsolutePath());
                }

            }
        }
    }

    private static void bubble() throws Exception {
        BufferedImage image = ImageIO.read(new File("c:\\images\\bubble\\DivineShield_Bubble1.png"));
        BufferedImage mask = ImageIO.read(new File("c:\\images\\bubble\\DivineShield_Mask2.png"));

        Image transpImg = TransformGrayToTransparency(mask);
        BufferedImage result = ApplyTransparency(image, transpImg);

        ImageIO.write(result, "PNG", new File("c:\\images\\bubble\\output.png"));
    }


    private static Image TransformGrayToTransparency(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage ApplyTransparency(BufferedImage image, Image mask) {
        BufferedImage dest = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(mask, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public static void combineWeaponImages(File file1, File file2, File outputFile) throws IOException {

        BufferedImage originalImage = ImageIO.read(file1);

        BufferedImage combined = new BufferedImage(307, 395, BufferedImage.TYPE_INT_ARGB);

        BufferedImage resized = toBufferedImage(originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH));

        BufferedImage weaponmask = ImageIO.read(file2);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(resized, 49, 29, null);
//            g.drawImage(resizeImagePng, 0, 0, null);
        g.drawImage(weaponmask, 0, 0, null);

//        ImageIO.write(combined, "png", new File(file1.getName()+".combined.png"));

        Image imageWithTransparency = makeColorTransparent(combined);
        BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);

        BufferedImage croppedImage = transparentImage.getSubimage(29, 25, 235, 205);
//
        BufferedImage finalImage = toBufferedImage(croppedImage.getScaledInstance(176, 155, Image.SCALE_SMOOTH));

        ImageIO.write(finalImage, "PNG", outputFile);

    }

    public static void combineHeroPowerImages(File file1, File file2, File outputFile) throws IOException {

        BufferedImage originalImage = ImageIO.read(file1);

        BufferedImage combined = new BufferedImage(329, 454, BufferedImage.TYPE_INT_ARGB);

        BufferedImage resized = toBufferedImage(originalImage.getScaledInstance(329, 454, Image.SCALE_SMOOTH));

//        BufferedImage combined = new BufferedImage(243, 336, BufferedImage.TYPE_INT_ARGB);
//
//        BufferedImage resized = toBufferedImage(originalImage.getScaledInstance(243, 336, Image.SCALE_SMOOTH));


        BufferedImage heropowermask = ImageIO.read(file2);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(resized, 0, 0, null);
//            g.drawImage(resizeImagePng, 0, 0, null);
        g.drawImage(heropowermask, 0, 0, null);

//        ImageIO.write(combined, "png", new File(file1.getName()+".combined.png"));

        Image imageWithTransparency = makeColorTransparent(combined);
        BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);


        ImageIO.write(transparentImage, "PNG", new File("C:\\images\\hpoutput3\\1.png"));

//        BufferedImage croppedImage = transparentImage.getSubimage(54, 26, 190, 174);
        BufferedImage croppedImage = transparentImage.getSubimage(86, 57, 157, 167);
//        BufferedImage croppedImage = transparentImage.getSubimage(44, 5, 157, 167);
//        ImageIO.write(croppedImage, "PNG", new File("C:\\images\\hpoutput3\\2.png"));

//        BufferedImage finalImage = toBufferedImage(croppedImage.getScaledInstance(157, 169, Image.SCALE_SMOOTH));

        ImageIO.write(croppedImage, "PNG", outputFile);

    }


    public static void combineExistingMinionImages(File file1, File file2, File outputFile) throws IOException {

        BufferedImage originalImage = ImageIO.read(file1);

        BufferedImage combined = new BufferedImage(110, 130, BufferedImage.TYPE_INT_ARGB);

        BufferedImage oval = ImageIO.read(file2);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(originalImage, 9, 5, null);
//            g.drawImage(resizeImagePng, 0, 0, null);
        g.drawImage(oval, 0, 0, null);

//        ImageIO.write(combined, "png", new File(file1.getName()+".combined.png"));

        Image imageWithTransparency = makeColorTransparent(combined);
        BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);


        BufferedImage croppedImage = transparentImage.getSubimage(6, 4, 94, 123);

        ImageIO.write(croppedImage, "PNG", outputFile);
    }


    public static void combineImages(File file1, File file2, File outputFile) throws IOException {

        BufferedImage originalImage = ImageIO.read(file1);
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width != 512 && height != 512) {
            System.out.println();
//            BufferedImage resizeImagePng = resizeImage(originalImage, type);
//        ImageIO.write(resizeImagePng, "png", new File(file1.getName()+".resized.png"));

            BufferedImage combined = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);

            BufferedImage oval = ImageIO.read(file2);

            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(originalImage, 0, 0, null);
//            g.drawImage(resizeImagePng, 0, 0, null);
            g.drawImage(oval, 0, 0, null);

//        ImageIO.write(combined, "png", new File(file1.getName()+".combined.png"));

            Image imageWithTransparency = makeColorTransparent(combined);
            BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);

            BufferedImage croppedImage = transparentImage.getSubimage(81, 46, 240, 310);

            BufferedImage finalImage = toBufferedImage(croppedImage.getScaledInstance(93, 120, Image.SCALE_SMOOTH));

            ImageIO.write(finalImage, "PNG", outputFile);

        }
    }


    private static BufferedImage imageToBufferedImage(final Image image) {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    private static Image makeColorTransparent(final BufferedImage im) {
        final ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(400, 400, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 47, 42, 307, 307, null);
        g.dispose();

        return resizedImage;
    }


    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
