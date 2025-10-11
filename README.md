# Image Filter Application

A Java application for applying various image filters including grayscale, sepia, and reflection effects.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Building the Application

### Clean and Compile
```bash
mvn clean compile
```

### Create JAR Package
```bash
mvn package
```

## Running the Application

### Option 1: Using Maven Exec Plugin
```bash
mvn exec:java "-Dexec.args=<path-to-image>"
```

**Examples:**
```bash
# Run with an image in current directory
mvn exec:java "-Dexec.args=image.jpg"

# Run with a specific path
mvn exec:java "-Dexec.args=./photos/image.jpg"
```

### Option 2: Using the JAR File
```bash
java -jar target/image-filter-1.0.0.jar <path-to-image>
```

**Examples:**
```bash
# Run with an image in current directory
java -jar target/image-filter-1.0.0.jar image.jpg

# Run with a specific path
java -jar target/image-filter-1.0.0.jar ./photos/image.jpg
```

## Available Filters

1. **Grayscale** - Converts image to grayscale
2. **Sepia** - Applies warm sepia tone effect
3. **Reflection** - Horizontally mirrors the image

## Output Files

Filtered images are saved with the filter name as prefix:
- `grayscale_<original-name>.jpg`
- `sepia_<original-name>.jpg`
- `reflection_<original-name>.jpg`

## Maven Commands Cheatsheet

| Command | Description |
|---------|-------------|
| `mvn clean` | Clean compiled files |
| `mvn compile` | Compile source code |
| `mvn package` | Create JAR file |
| `mvn clean compile` | Clean and compile |
| `mvn exec:java "-Dexec.args=image.jpg"` | Run with Maven |

## Project Structure

```
src/
  main/
    java/
      Main.java          # Main application class
target/
  classes/               # Compiled classes
  image-filter-1.0.0.jar # Executable JAR
pom.xml                  # Maven configuration
README.md               # This file
```

## Interactive Usage

When you run the application, you'll see a menu:

```
Current image: your-image.jpg
Available commands:
1 - Apply Grayscale filter
2 - Apply Sepia filter  
3 - Apply Reflection filter
q - Quit the program
h - Show this help message

Enter your choice:
```

Simply enter the number corresponding to the filter you want to apply, or 'q' to quit.