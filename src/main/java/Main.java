import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Main {
    private static final String HELP_MESSAGE = """
    Available commands:
    1 - Apply Grayscale filter
    2 - Apply Sepia filter
    3 - Apply Reflection filter
    q - Quit the program
    h - Show this help message
    """;

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main <input_image>");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        BufferedImage originalImage = loadImage(args[0]);
        if (originalImage == null) {
            System.err.println("Could not load image file");
			scanner.close();
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\nCurrent image: " + args[0]);
            System.out.println(HELP_MESSAGE);
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1" -> {
                    BufferedImage grayscaleImage = copyImage(originalImage);
                    applyGrayscale(grayscaleImage);
                    saveImage(grayscaleImage, getFilteredFilename(args[0], "grayscale"));
                }
                    
                case "2" -> {
                    BufferedImage sepiaImage = copyImage(originalImage);
                    applySepia(sepiaImage);
                    saveImage(sepiaImage, getFilteredFilename(args[0], "sepia"));
                }
                    
                case "3" -> {
                    BufferedImage reflectionImage = copyImage(originalImage);
                    applyReflection(reflectionImage);
                    saveImage(reflectionImage, getFilteredFilename(args[0], "reflection"));
                }
                    
                case "q" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                    
                case "h" -> System.out.println(HELP_MESSAGE);
                    
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }

    private static BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    private static void saveImage(BufferedImage image, String filename) {
        try {
            String extension = getFileExtension(filename);
            ImageIO.write(image, extension, new File(filename));
            System.out.println("Filtered image saved as: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex + 1) : "jpg";
    }

    private static String getFilteredFilename(String originalPath, String filterType) {
        // Remove any path prefixes like ".\" or "./"
        String cleanFilename = originalPath;
        if (cleanFilename.startsWith(".\\" )) {
            cleanFilename = cleanFilename.substring(2);
        } else if (cleanFilename.startsWith("./")) {
            cleanFilename = cleanFilename.substring(2);
        }
        
        // Extract the filename without extension
        int dotIndex = cleanFilename.lastIndexOf('.');
        String nameWithoutExtension = dotIndex > 0 ? cleanFilename.substring(0, dotIndex) : cleanFilename;
        String extension = dotIndex > 0 ? cleanFilename.substring(dotIndex) : ".jpg";
        
        return filterType + "_" + nameWithoutExtension + extension;
    }

    private static BufferedImage copyImage(BufferedImage original) {
        BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                copy.setRGB(x, y, original.getRGB(x, y));
            }
        }
        return copy;
    }

    private static void applyGrayscale(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                
                int gray = (red + green + blue) / 3;
                int grayRGB = (gray << 16) | (gray << 8) | gray;
                image.setRGB(x, y, grayRGB);
            }
        }
    }

    private static void applySepia(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                
                int newRed = (int)(0.393 * red + 0.769 * green + 0.189 * blue);
                int newGreen = (int)(0.349 * red + 0.686 * green + 0.168 * blue);
                int newBlue = (int)(0.272 * red + 0.534 * green + 0.131 * blue);
                
                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));
                
                int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;
                image.setRGB(x, y, newRGB);
            }
        }
    }


    private static void applyReflection(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 2; x++) {
                int leftRGB = image.getRGB(x, y);
                int rightRGB = image.getRGB(width - 1 - x, y);
                
                image.setRGB(x, y, rightRGB);
                image.setRGB(width - 1 - x, y, leftRGB);
            }
        }
    }
}