/*
 * The MIT License
 *
 * Copyright 2017 Konstantin Tsanov <k.tsanov@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.thecir.filechoosers;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.thecir.callbacks.FileCallback;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class CreateNewFileChooser extends JFileChooser implements FileCallback {

    private final JFrame parent;
    private final JTextField newFileField;
    //To set the output file
    private final JFileChooser outputFileChooser;
    private ResourceBundle errorBundle;

    public CreateNewFileChooser(JFrame parent, JTextField newFileField, JFileChooser outputFileChooser, String... extensions) {
        this.parent = parent;
        this.newFileField = newFileField;
        this.outputFileChooser = outputFileChooser;
        setFileSelectionMode(FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Workbook (.xlsx)", extensions);
        setFileFilter(filter);
        setAcceptAllFileFilterUsed(false);
    }

    @Override
    public File getFile() {
        int result = showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        } else {
            return null;
        }
    }

    @Override
    public void setAsOutputAndDisplay() {
        newFileField.setText(getSelectedFile() == null ? null : getSelectedFile().toString());
        outputFileChooser.setSelectedFile(getSelectedFile());
    }

    @Override
    public void approveSelection() {
        File f = manageExtension(getSelectedFile());
        setSelectedFile(f);
        if (getDialogType() == SAVE_DIALOG) {
            if (f.exists()) {
                errorBundle = ResourceBundle.getBundle("LanguageBundles/ErrorMessages");
                int result = JOptionPane.showConfirmDialog(this, errorBundle.getString("FileAlreadyExistsOverwrite"), errorBundle.getString("ExistingFileLabel"), JOptionPane.YES_NO_CANCEL_OPTION);
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                }
            } else {
                super.approveSelection();
            }
        }
        super.approveSelection();
    }

    private File manageExtension(File file) {
        if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xlsx")) {
            if ("".equals(FilenameUtils.getExtension(file.getAbsolutePath()))) {
                return new File(file.toString() + ".xlsx");
            } else {
                return new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".xlsx");
            }
        }
        return file;
    }
}
