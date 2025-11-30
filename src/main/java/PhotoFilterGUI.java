import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PhotoFilterGUI {

    private static JLabel selectedFileLabel;
    private static JLabel selectedOutputDirLabel;
    private static File selectedFile;
    private static File outputDir;
    private static JComboBox<String> filterComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Photo Filter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new GridLayout(4, 1));

        JPanel fileSelectionPanel = new JPanel();
        JButton selectFileButton = new JButton("Select Image");
        selectedFileLabel = new JLabel("No file selected");
        fileSelectionPanel.add(selectFileButton);
        fileSelectionPanel.add(selectedFileLabel);

        JPanel outputDirSelectionPanel = new JPanel();
        JButton selectOutputDirButton = new JButton("Select Output Directory");
        outputDir = new File(System.getProperty("user.dir"));
        selectedOutputDirLabel = new JLabel(outputDir.getAbsolutePath());
        outputDirSelectionPanel.add(selectOutputDirButton);
        outputDirSelectionPanel.add(selectedOutputDirLabel);

        JPanel filterPanel = new JPanel();
        filterComboBox = new JComboBox<>(new String[]{"Grayscale", "Sepia", "Reflection"});
        filterPanel.add(new JLabel("Select Filter:"));
        filterPanel.add(filterComboBox);

        JPanel processPanel = new JPanel();
        JButton processButton = new JButton("Process Image");
        processPanel.add(processButton);

        frame.add(fileSelectionPanel);
        frame.add(outputDirSelectionPanel);
        frame.add(filterPanel);
        frame.add(processPanel);

        selectFileButton.addActionListener(e -> chooseFile());
        selectOutputDirButton.addActionListener(e -> chooseOutputDir());
        processButton.addActionListener(e -> processImage());

        frame.setVisible(true);
    }

    private static void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText(selectedFile.getName());
        }
    }

    private static void chooseOutputDir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            outputDir = fileChooser.getSelectedFile();
            selectedOutputDirLabel.setText(outputDir.getAbsolutePath());
        }
    }

    private static void processImage() {
        if (selectedFile == null || outputDir == null) {
            JOptionPane.showMessageDialog(null, "Please select an image and an output directory.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedImage originalImage = loadImage(selectedFile.getAbsolutePath());
        if (originalImage == null) {
            JOptionPane.showMessageDialog(null, "Could not load image file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedFilter = (String) filterComboBox.getSelectedItem();
        BufferedImage filteredImage = copyImage(originalImage);

        switch (selectedFilter) {
            case "Grayscale":
                applyGrayscale(filteredImage);
                break;
            case "Sepia":
                applySepia(filteredImage);
                break;
            case "Reflection":
                applyReflection(filteredImage);
                break;
        }

        String outputFileName = getFilteredFilename(selectedFile.getName(), selectedFilter.toLowerCase());
        File outputFile = new File(outputDir, outputFileName);
        saveImage(filteredImage, outputFile.getAbsolutePath());
        JOptionPane.showMessageDialog(null, "Image processed successfully and saved as " + outputFileName, "Success", JOptionPane.INFORMATION_MESSAGE);
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
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex + 1) : "jpg";
    }

    private static String getFilteredFilename(String originalPath, String filterType) {
        String cleanFilename = new File(originalPath).getName();
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
