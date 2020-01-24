package custom;

import callbacks.DropFilesCallback;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileDropHandler extends TransferHandler {

    private DropFilesCallback callback;

    public FileDropHandler(DropFilesCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.isFlavorJavaFileListType()) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!this.canImport(support))
            return false;

        List<File> files;
        try {
            files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            // should never happen (or JDK is buggy)
            return false;
        }

        for (File file : files) {
            callback.receivedFile(file);
        }
        return true;
    }
}