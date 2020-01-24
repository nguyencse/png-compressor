package model;

import java.util.ArrayList;
import java.util.List;

import static commons.Constants.*;

public class Settings {
    private boolean isCustom = false;
    private int minColor = DEFAULT_MIN_COLOR;
    private int maxColor = DEFAULT_MAX_COLOR;
    private int speed = DEFAULT_SPEED;
    private boolean isDithered = true;
    private boolean isIE6Support = false;
    private List<PngFile> inputPngFiles = new ArrayList<>();
    private boolean isOtherOutputDir = false;
    private String otherOutputDir = null;
    private String fileNameOutput = null;

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public int getMinColor() {
        return minColor;
    }

    public void setMinColor(int minColor) {
        this.minColor = minColor;
    }

    public int getMaxColor() {
        return maxColor;
    }

    public void setMaxColor(int maxColor) {
        this.maxColor = maxColor;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isDithered() {
        return isDithered;
    }

    public void setDithered(boolean dithered) {
        isDithered = dithered;
    }

    public boolean isIE6Support() {
        return isIE6Support;
    }

    public void setIE6Support(boolean IE6Support) {
        isIE6Support = IE6Support;
    }

    public List<PngFile> getInputPngFiles() {
        return inputPngFiles;
    }

    public void setInputPngFiles(List<PngFile> inputPngFiles) {
        this.inputPngFiles = inputPngFiles;
    }

    public boolean isOtherOutputDir() {
        return isOtherOutputDir;
    }

    public void setOtherOutputDir(boolean otherOutputDir) {
        isOtherOutputDir = otherOutputDir;
    }

    public String getOtherOutputDir() {
        return otherOutputDir;
    }

    public void setOtherOutputDir(String otherOutputDir) {
        this.otherOutputDir = otherOutputDir;
    }

    public String getFileNameOutput() {
        return fileNameOutput;
    }

    public void setFileNameOutput(String fileNameOutput) {
        this.fileNameOutput = fileNameOutput;
    }
}
