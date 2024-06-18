package net.catfoolyou.pakutils;

import java.awt.*;
import java.awt.FileDialog;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.jcraft.jzlib.InflaterInputStream;
import net.lax1dude.eaglercraft.v1_8.*;
import net.lax1dude.eaglercraft.bintools.EPKDecompiler;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;


/*
 * More shit code I've written, its a wonder it even works.
 * 
 * Copyright (c) Catfoolyou 2022-2024. All Rights Reserved.
 */


public class FileCopyUtils {

    public static boolean copyEPK() {
    Frame frame = new Frame();
    FileDialog fileDialog = new FileDialog(frame, "Select EPK File", FileDialog.LOAD);
    fileDialog.setFile("*.epk");
    fileDialog.setVisible(true);

    String selectedFilePath = fileDialog.getFile();
    String selectedDirectoryPath = fileDialog.getDirectory();

    if (selectedFilePath != null && selectedDirectoryPath != null) {
            File selectedFile = new File(selectedDirectoryPath, selectedFilePath);
            String destinationDirectory = "." + File.separator + "saves" + File.separator + selectedFile.getName();
            File destFile = new File(destinationDirectory + selectedFile.getName());

            try {
                copyFile(selectedFile, destFile);
                EPKDecompiler.unpack(destFile, new File(destinationDirectory));

                if (!destFile.delete()) {
                    System.err.println("Warning: Failed to delete the EPK file.");
                }

                return true;
            } catch (IOException e) {
                System.err.println("Error copying or extracting EPK file: " + e.getMessage());
            }
        }
        return false;
}

    public static boolean copyZip() {
        Frame frame = new Frame();
        FileDialog fileDialog = new FileDialog(frame, "Select ZIP File", FileDialog.LOAD);
        fileDialog.setFile("*.zip");
        fileDialog.setVisible(true);

        String selectedFilePath = fileDialog.getFile();
        String selectedDirectoryPath = fileDialog.getDirectory();

        if (selectedFilePath != null && selectedDirectoryPath != null) {
            File selectedFile = new File(selectedDirectoryPath, selectedFilePath);
            String destinationDirectory = "." + File.separator + "saves" + File.separator;
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