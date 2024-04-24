package net.catfoolyou.pakutils;

import java.awt.*;
import java.awt.FileDialog;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.jcraft.jzlib.InflaterInputStream;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKDecompiler;
import net.lax1dude.eaglercraft.v1_8.*;

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
        String destinationDirectory = "." + File.separator + "saves" + File.separator;
	File destFile = new File(destinationDirectory + selectedFile.getName());

        try {
            copyFile(selectedFile, destFile);
	    byte[] fileContent = Files.readAllBytes(destFile.toPath());
            EPKDecompiler dc = new EPKDecompiler(fileContent);
	    EPKDecompiler.FileEntry f = null;

		//Begin W_EXP

		int lastProgUpdate = 0;
		int prog = 0;
		String hasReadType = null;
		boolean has152Format = false;
		int cnt = 0;
		while((f = dc.readFile()) != null) {
			byte[] b = f.data;
			if(hasReadType == null) {
				if (f.type.equals("HEAD") && f.name.equals("file-type")
						&& ((hasReadType = EPKDecompiler.readASCII(f.data)).equals("epk/world188")
								|| (has152Format = hasReadType.equals("epk/world152")))) {
					if(has152Format) {
						System.err.println("World type detected as 1.5.2, it will be converted to 1.8.8 format");
					}
					continue;
				}else {
					throw new IOException("file does not contain a singleplayer 1.5.2 or 1.8.8 world!");
				}
			}
			if(f.type.equals("FILE")) {
				if(f.name.equals("level.dat") || f.name.equals("level.dat_old")) {
					NBTTagCompound worldDatNBT = CompressedStreamTools.readCompressed(new EaglerInputStream(b));
					worldDatNBT.getCompoundTag("Data").setString("LevelName", destFile.getName());
					worldDatNBT.getCompoundTag("Data").setLong("LastPlayed", System.currentTimeMillis());
					EaglerOutputStream tmp = new EaglerOutputStream();
					CompressedStreamTools.writeCompressed(worldDatNBT, tmp);
					b = tmp.toByteArray();
				}
				//VFile2 ff = new VFile2(worldDir, f.name);
				//ff.setAllBytes(b);
				prog += b.length;
				++cnt;
				if(prog - lastProgUpdate > 25000) {
					lastProgUpdate = prog;
					//System.out.println("Extracted {} files, {} bytes from EPK...", cnt, prog);
					//EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.importing.1", prog);
				}
			}
		}


		//End W_EXP

	    if (!destFile.delete()) {
                    System.err.println("Warning: Failed to delete the EPK file.");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error copying or unpacking EPK file: " + e.getMessage());
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