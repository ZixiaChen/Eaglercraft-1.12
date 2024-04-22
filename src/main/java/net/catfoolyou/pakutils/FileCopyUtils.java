package net.catfoolyou.pakutils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileCopyUtils {

    public static boolean copyEPK() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("EPK files", "epk");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String destinationDirectory = "." + File.separator + "saves" + File.separator; // Relative path to "./saves/"

            try {
                copyFile(selectedFile, new File(destinationDirectory + selectedFile.getName()));
                return true; 
            } catch (IOException e) {
                System.err.println("Error copying file: " + e.getMessage());
            }
        }
        return false;
    }

    public static boolean copyZip() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ZIP files", "zip");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String destinationDirectory = "." + File.separator + "saves" + File.separator; // Relative path to "./saves/"
            File destFile = new File(destinationDirectory + selectedFile.getName());

            try {
                copyFile(selectedFile, destFile);
                extractZip(destFile, destinationDirectory);

                if (!destFile.delete()) {
                    System.err.println("Warning: Failed to delete the zip file.");
                }

                return true;
            } catch (IOException e) {
                System.err.println("Error copying or extracting zip file: " + e.getMessage());
            }
        }
        return false;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    public static void extractZip(File zipFile, String destinationDirectory) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destinationDirectory + File.separator + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    // Create parent directories if they don't exist
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        }
    }
}
