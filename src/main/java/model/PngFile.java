package model;

import java.io.File;

public class PngFile {

    public static int COL_FILE_NAME = 0;
    public static int COL_FILE_DIR= 1;
    public static int COL_RESULT = 2;
    public static int COL_SIZE = 3;
    public static int COL_COMPRESSED_SIZE = 4;
    public static int COL_SAVED_SIZE = 5;
    public static int COL_SAVED_SIZE_PERCENT = 6;

    private String fileName;
    private String filePath;
    private String result;
    private float size;
    private float compressedSize;
    private float savedSize;
    private float savedSizePercent;
    private String fileDir;

    public PngFile() {

    }

    public PngFile(File file){
        if (file != null) {
            fileName = file.getName();
            filePath = file.getAbsolutePath();
            result = "";
            size = file.length() / 1024f; // file size in KB
            compressedSize = 0;
            savedSize = 0;
            savedSizePercent = 0;
            fileDir = file.getParent();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(float compressedSize) {
        this.compressedSize = compressedSize;
    }

    public float getSavedSize() {
        return savedSize;
    }

    public void setSavedSize(float savedSize) {
        this.savedSize = savedSize;
    }

    public float getSavedSizePercent() {
        return savedSizePercent;
    }

    public void setSavedSizePercent(float savedSizePercent) {
        this.savedSizePercent = savedSizePercent;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public Object[] parseInfoToTable() {
        return new Object[]{fileName, fileDir, result, size, compressedSize, savedSize, savedSizePercent};
    }

    public String getFileNameWithoutExt() {
        if (fileName.length() < 5) return "";
        return fileName.substring(0, fileName.length() - 4);
    }
}
