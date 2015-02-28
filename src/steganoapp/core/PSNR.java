/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
Author: DarkLilac email:contact@darklilac.com
*/
package steganoapp.core;

import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public final class PSNR {
    private File imageFile1;
    private File imageFile2;
    
    public PSNR() {
            //nothing to see here people, move along
    }

    public void setImage(File image1, File image2) {
        this.imageFile1 = image1;
        this.imageFile2 = image2;
    }
    
    public double getPSNR() throws IOException {
        BufferedImage image1 = ImageIO.read(imageFile1);
        BufferedImage image2 = ImageIO.read(imageFile2);
        if (image1.getWidth() != image2.getWidth()) {
                System.err.println("Those two file do not have the same width");
                return 0;
        }
        if (image1.getHeight() != image2.getHeight()) {
                System.err.println("Those two file do not have the same height");
                return 0;
        }
        final int size = image1.getHeight() * image1.getWidth();
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        int maxRed = -1;
        int maxGreen = -1;
        int maxBlue = -1;
        int worstRedX = -1;
        int worstRedY = -1;
        int worstGreenX = -1;
        int worstGreenY = -1;
        int worstBlueX = -1;
        int worstBlueY = -1;
        double maxDistance = -1;
        int maxX = -1;
        int maxY = -1;
        double totalDistance = 0;

        for (int i = 0; i < image1.getWidth(); i++) {
                for (int j = 0; j < image1.getHeight(); j++) {
                        final Color color1 = new Color(image1.getRGB(i, j));
                        final Color color2 = new Color(image2.getRGB(i, j));
                        final double distance = getColorDistance(color1, color2);
                        totalDistance += distance;
                        if (distance > maxDistance) {
                                maxDistance = distance;
                                maxX = i;
                                maxY = j;
                        }
                        final int redDiff = color1.getRed() - color2.getRed();
                        if (redDiff > maxRed) {
                                maxRed = redDiff;
                                worstRedX = i;
                                worstRedY = j;
                        }
                        final int greenDiff = color1.getGreen() - color2.getGreen();
                        if (greenDiff > maxGreen) {
                                maxGreen = greenDiff;
                                worstGreenX = i;
                                worstGreenY = j;
                        }
                        final int blueDiff = color1.getBlue() - color2.getBlue();
                        if (blueDiff > maxBlue) {
                                maxBlue = blueDiff;
                                worstBlueX = i;
                                worstBlueY = j;
                        }
                        totalRed += redDiff * redDiff;
                        totalGreen += greenDiff * greenDiff;
                        totalBlue += blueDiff * blueDiff;
                }
        }
        long total = (totalRed + totalGreen + totalBlue);
        long divisor = (image1.getWidth() * image1.getHeight() * 3);
        
        System.out.println("Width : " + image1.getWidth());
        System.out.println("Height : " + image1.getHeight());
        System.out.print("maxDistance: " + maxDistance);
        System.out.println(" at: " + maxX + " " + maxY);
        System.out.println("averageDistance: " + (totalDistance / size));
        System.out.println("total red: " + totalRed);
        System.out.println("total green: " + totalGreen);
        System.out.println("total blue: " + totalBlue);
        System.out.println("Worst red at " + worstRedX + " " + worstRedY + " is " + maxRed);
        System.out.println("Worst green at " + worstGreenX + " " + worstGreenY + " is " + maxGreen);
        System.out.println("Worst blue at " + worstBlueX + " " + worstBlueY + " is " + maxBlue);
        System.out.println("total all: " + total);
        System.out.println("divisor: " + divisor);
        
        float meanSquaredError = (float)total/ (float)divisor;            
        System.out.println("mean squarederror is " + meanSquaredError);
        double peakSignalToNoiseRatio = 10 * StrictMath.log10((255 * 255) / meanSquaredError);
        System.out.println("peak signal to noise ratio is " + peakSignalToNoiseRatio);
        
        return peakSignalToNoiseRatio;
    }
    
    private double getColorDistance(Color source, Color target) {
            if (source.equals(target)) {
                    return 0.0d;
            }
            final double red = source.getRed() - target.getRed();
            final double green = source.getGreen() - target.getGreen();
            final double blue = source.getBlue() - target.getBlue();
            return Math.sqrt(red * red + blue * blue + green * green);
    }
}