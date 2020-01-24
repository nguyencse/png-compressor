package ui;

import callbacks.CompressCallback;
import callbacks.ConfirmCallback;
import callbacks.DropFilesCallback;
import custom.FileDropHandler;
import model.PngFile;
import model.Settings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatter;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static commons.Constants.*;
import static model.PngFile.*;

public class ChooseImagePathDialog extends JDialog implements ActionListener, ChangeListener, DropFilesCallback, MouseListener {
    private JPanel contentPane;
    private JButton btnOK;
    private JTextField txtDstDir;
    private JPanel panelMain;
    private JButton btnBrowse;
    private JTable tbFiles;
    private JSlider sldColorMin;
    private JSpinner spColorMin;
    private JSlider sldPerformance;
    private JCheckBox chkDithered;
    private JCheckBox chkIE6Support;
    private JButton btnAdd;
    private JButton btnClear;
    private JPanel panelActionFileList;
    private JRadioButton radOutputSameDir;
    private JRadioButton radOutputOtherDir;
    private JPanel panelDropFiles;
    private JTextField txtOutputFileName;
    private JSlider sldColorMax;
    private JSpinner spColorMax;
    private JRadioButton radDefault;
    private JRadioButton radCustom;
    private JPanel panelSettings;
    private JPanel panelColors;
    private JPanel panelPerformance;
    private JLabel lblMin;
    private JLabel lblMax;
    private JLabel lblHighQuality;
    private JLabel lblHighSpeed;
    private JButton btnCompress;
    private JPanel panelActions;
    private JLabel txtExtension;
    private JProgressBar prbProcessing;

    private ConfirmCallback confirmCallback;
    private CompressCallback compressCallback;
    private DefaultTableModel tableModel;


    String[] columnNames = {"File Name", "Path", "Result", "Size (KB)", "Compressed Size (KB)", "Saved Size (KB)", "Saved Size (%)"};

    public ChooseImagePathDialog(CompressCallback compressCallback) {
        this.compressCallback = compressCallback;

        setTitle("PNG Compressor");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOK);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbFiles.setModel(tableModel);

        sldPerformance.setMinimum(MIN_SPEED);
        sldPerformance.setMaximum(MAX_SPEED);
        sldPerformance.setExtent(TICK_SPACING_SPEED);
        sldPerformance.setMajorTickSpacing(TICK_SPACING_SPEED);
        sldPerformance.setPaintTicks(true);
        sldPerformance.setValue(DEFAULT_SPEED);

        sldColorMin.setMinimum(MIN_COLOR);
        sldColorMin.setMaximum(MAX_COLOR);
        sldColorMin.setExtent(TICK_SPACING_COLOR);
        sldColorMin.setMajorTickSpacing(TICK_SPACING_COLOR * 10);
        sldColorMin.setPaintTicks(true);
        sldColorMin.setValue(DEFAULT_MIN_COLOR);

        sldColorMax.setMinimum(MIN_COLOR);
        sldColorMax.setMaximum(MAX_COLOR);
        sldColorMax.setExtent(TICK_SPACING_COLOR);
        sldColorMax.setMajorTickSpacing(TICK_SPACING_COLOR * 10);
        sldColorMax.setPaintTicks(true);
        sldColorMax.setValue(DEFAULT_MAX_COLOR);

        spColorMin.setValue(DEFAULT_MIN_COLOR);
        spColorMax.setValue(DEFAULT_MAX_COLOR);
        ((DefaultFormatter) (((JSpinner.DefaultEditor) spColorMin.getEditor()).getTextField().getFormatter())).setCommitsOnValidEdit(true);
        ((DefaultFormatter) (((JSpinner.DefaultEditor) spColorMax.getEditor()).getTextField().getFormatter())).setCommitsOnValidEdit(true);

        btnCompress.setText("Compress");

        sldColorMin.addChangeListener(this);
        sldColorMax.addChangeListener(this);
        sldPerformance.addChangeListener(this);
        spColorMin.addChangeListener(this);
        spColorMax.addChangeListener(this);

        panelDropFiles.addMouseListener(this);
        panelDropFiles.setTransferHandler(new FileDropHandler(this));
        tbFiles.setTransferHandler(new FileDropHandler(this));
        btnAdd.addActionListener(this);
        btnClear.addActionListener(this);
        btnBrowse.addActionListener(this);
        radDefault.addActionListener(this);
        radCustom.addActionListener(this);
        radOutputSameDir.addActionListener(this);
        radOutputOtherDir.addActionListener(this);
        btnCompress.addActionListener(this);
        btnOK.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public void receivedFile(File file) {
        if (tableModel != null) {
            tableModel.addRow(parseFileInfo(file));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object view = e.getSource();

        if (view.equals(btnAdd)) {
            browseFilesInput();
        } else if (view.equals(btnClear)) {
            tableModel.setRowCount(0);
        } else if (view.equals(btnBrowse)) {
            browseFolder();
        } else if (view.equals(radOutputSameDir) || view.equals(radOutputOtherDir)) {
            handleChangeRadOutput(view);
        } else if (view.equals(radDefault) || view.equals(radCustom)) {
            handleChangeRadSettings(view);
        } else if (view.equals(btnCompress)) {
            compress();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object view = e.getSource();
        if (view.equals(sldColorMin)) {
            spColorMin.setValue(sldColorMin.getValue());
        } else if (view.equals(sldColorMax)) {
            spColorMax.setValue(sldColorMax.getValue());
        } else if (view.equals(spColorMin)) {
            sldColorMin.setValue((Integer) spColorMin.getValue());
        } else if (view.equals(spColorMax)) {
            sldColorMax.setValue((Integer) spColorMax.getValue());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // browse png files to put into files list input
        browseFilesInput();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void onCancel() {
        dispose();
    }

    private Object[] parseFileInfo(File file) {
        if (file.getName().endsWith(".png")) {
            return new PngFile(file).parseInfoToTable();
        }
        return new PngFile().parseInfoToTable();
    }

    private void browseFilesInput() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        FileFilter pngFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                return file.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "PNG Image";
            }
        };
        chooser.setFileFilter(pngFilter);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            tableModel.addRow(new Object[]{selectedFile.getName(), selectedFile});
        }
    }

    private void browseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            txtDstDir.setText(selectedFile.getAbsolutePath());
        }
    }

    private void handleChangeRadOutput(Object view) {
        if (view.equals(radOutputSameDir)) {
            radOutputOtherDir.setSelected(false);
            txtDstDir.setEnabled(false);
            btnBrowse.setEnabled(false);
        } else if (view.equals(radOutputOtherDir)) {
            radOutputSameDir.setSelected(false);
            txtDstDir.setEnabled(true);
            btnBrowse.setEnabled(true);
        }
    }

    private void handleChangeRadSettings(Object view) {
        if (view.equals(radDefault)) {
            radCustom.setSelected(false);
        } else if (view.equals(radCustom)) {
            radDefault.setSelected(false);
        }
        panelSettings.setEnabled(radCustom.isSelected());
        panelColors.setEnabled(radCustom.isSelected());
        panelPerformance.setEnabled(radCustom.isSelected());
        lblMin.setEnabled(radCustom.isSelected());
        lblMax.setEnabled(radCustom.isSelected());
        lblHighQuality.setEnabled(radCustom.isSelected());
        lblHighSpeed.setEnabled(radCustom.isSelected());
        sldColorMin.setEnabled(radCustom.isSelected());
        sldColorMax.setEnabled(radCustom.isSelected());
        sldPerformance.setEnabled(radCustom.isSelected());
        chkDithered.setEnabled(radCustom.isSelected());
        chkIE6Support.setEnabled(radCustom.isSelected());
        spColorMin.setEnabled(radCustom.isSelected());
        spColorMax.setEnabled(radCustom.isSelected());
    }

    private void compress() {
        btnCompress.setEnabled(false);
        prbProcessing.setValue(0);
        prbProcessing.setString(0 + "%");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, COL_RESULT);
            tableModel.setValueAt(0f, i, COL_COMPRESSED_SIZE);
            tableModel.setValueAt(0f, i, COL_SAVED_SIZE);
            tableModel.setValueAt(0f, i, COL_SAVED_SIZE_PERCENT);
        }

        List<PngFile> inputFilePaths = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            inputFilePaths.add(getPngFileFromTable(i));
        }

        Settings settings = new Settings();
        settings.setInputPngFiles(inputFilePaths);
        settings.setCustom(radCustom.isSelected());
        settings.setMinColor(sldColorMin.getValue());
        settings.setMaxColor(sldColorMax.getValue());
        settings.setSpeed(sldPerformance.getValue());
        settings.setDithered(chkDithered.isSelected());
        settings.setIE6Support(chkIE6Support.isSelected());
        settings.setOtherOutputDir(radOutputOtherDir.isSelected());
        settings.setOtherOutputDir(txtDstDir.getText().trim());
        settings.setFileNameOutput(txtOutputFileName.getText().trim());

        if (compressCallback != null) {
            compressCallback.onCompress(settings);
        }
    }

    private PngFile getPngFileFromTable(int row) {
        PngFile file = new PngFile();
        file.setFileName(tableModel.getValueAt(row, COL_FILE_NAME).toString());
        file.setFileDir(tableModel.getValueAt(row, COL_FILE_DIR).toString());
        file.setResult(tableModel.getValueAt(row, COL_RESULT).toString());
        file.setSize((Float) tableModel.getValueAt(row, COL_SIZE));
        file.setCompressedSize((Float) tableModel.getValueAt(row, COL_COMPRESSED_SIZE));
        file.setSavedSize((Float) tableModel.getValueAt(row, COL_SAVED_SIZE));
        file.setSavedSizePercent((Float) tableModel.getValueAt(row, COL_SAVED_SIZE_PERCENT));
        file.setFilePath(file.getFileDir() + "/" + file.getFileName());
        return file;
    }

    public void updateFileInfoAtRow(PngFile file, int row) {
        if (row < 0 || row > tableModel.getRowCount()) return;
        Object[] data = file.parseInfoToTable();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            tableModel.setValueAt(data[i], row, i);
        }
    }

    public void updateProgress(float percent) {
        prbProcessing.setValue((int)(percent * 100));
        prbProcessing.setString((int)(percent * 100) + "%");
    }

    public void compressDone() {
        btnCompress.setEnabled(true);
    }
}
