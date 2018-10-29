package zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipMultipleFiles {
        public static void zipAction(File[] srcFiles,String zipDestination) {
            try {
                // create byte buffer

                FileOutputStream fos = new FileOutputStream(zipDestination);
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (int i=0; i < srcFiles.length; i++) {
                    File newFile = srcFiles[i];
                    if (!newFile.isDirectory()) {
                        addOneFile(zos, newFile);
                    }
                    else {
                        List<File> fileList = new ArrayList<File>();
                        getAllFiles(newFile, fileList);
                        for(File oneFile:fileList) {
                            addOneFile(zos, oneFile);
                        }
                    }
                    zos.closeEntry();
                }
                zos.close();
            }
            catch (IOException ioe) {
                System.out.println("Error creating zip file: " + ioe);
            }
        }

    private static void addOneFile(ZipOutputStream zos, File newFile) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(newFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addFile(zos, newFile, fis);
        fis.close();
    }

    private static void addFile(ZipOutputStream zos, File newFile, FileInputStream fis) throws IOException {
        byte[] buffer = new byte[1024];
        String nameFile = newFile.getPath();
        if (nameFile.indexOf("./") ==0)
            nameFile = nameFile.substring(2);
        if (nameFile.indexOf(".\\") ==0)
            nameFile = nameFile.substring(2);
        zos.putNextEntry(new ZipEntry(nameFile));
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }
    }

    public static void getAllFiles(File dir, List<File> fileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    getAllFiles(file, fileList);
                } else {
                    fileList.add(file);
                    System.out.println("     file:" + file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

