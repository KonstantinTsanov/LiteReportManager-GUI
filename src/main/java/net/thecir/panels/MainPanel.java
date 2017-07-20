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
package net.thecir.panels;

import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import net.thecir.core.LiteReportManager;
import net.thecir.filechoosers.CreateNewFileChooser;
import net.thecir.filechoosers.SelectExistingFileChooser;
import net.thecir.filemanagers.NewFileManager;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class MainPanel extends JPanel {

    private JLabel srcFileLabel;
    private JLabel destFileLabel;

    private JTextField srcFilePath;
    private JTextField destFilePath;

    private JButton selectSrcFileButton;
    private JButton selectDestFileButton;
    private JButton createNewFileButton;
    private JButton clearSrcFileButton;
    private JButton clearDestFileButton;

    private JRadioButton technopolisJButton;
    private JRadioButton technomarketJButton;
    private ButtonGroup retailersButtonGroup;

    private JButton generateReport;
    private JButton cancelButton;

    //If the data from the input file must be subtracted from the output file rather than added to it
    private JCheckBox subtractCheckBox;

    private JLabel statusLabel;
    private JTextArea statusTextArea;

    private final JFrame parent;

    private final SelectExistingFileChooser inputFileChooser;
    private final SelectExistingFileChooser outputFileChooser;
    private final CreateNewFileChooser createOutputFileChooser;

    public MainPanel(JFrame parent) {
        this.parent = parent;
        inputFileChooser = new SelectExistingFileChooser(parent, "xlsx", "xls");
        outputFileChooser = new SelectExistingFileChooser(parent, "xlsx");
        createOutputFileChooser = new CreateNewFileChooser(parent, "xlsx");
        MigLayout layout = new MigLayout("", "[shrink 0][grow][shrink 0][shrink 0]", "[shrink 0][shrink 0][shrink 0][shrink 0][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        setComponentText();
        attachListeners();
    }

    /**
     * Initializes all panel components.
     */
    private void initComponents() {
        srcFileLabel = new JLabel();
        destFileLabel = new JLabel();

        srcFilePath = new JTextField();
        srcFilePath.setEditable(false);

        destFilePath = new JTextField();
        destFilePath.setEditable(false);

        selectSrcFileButton = new JButton("...");
        selectDestFileButton = new JButton("...");
        createNewFileButton = new JButton("...");
        clearSrcFileButton = new JButton();
        clearDestFileButton = new JButton();

        technopolisJButton = new JRadioButton();
        technomarketJButton = new JRadioButton();
        retailersButtonGroup = new ButtonGroup();
        retailersButtonGroup.add(technomarketJButton);
        retailersButtonGroup.add(technopolisJButton);

        generateReport = new JButton();
        cancelButton = new JButton();
        subtractCheckBox = new JCheckBox();

        statusLabel = new JLabel();
        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        statusTextArea.setOpaque(true);
    }

    private void addComponents() {
        add(srcFileLabel);
        add(srcFilePath, "growx");
        add(selectSrcFileButton, "growx");
        add(clearSrcFileButton, "wrap");
        add(destFileLabel, "span 1 2");
        add(destFilePath, "growx, span 1 2, center");
        add(selectDestFileButton, "growx");
        add(clearDestFileButton, "span 1 2,wrap");
        add(createNewFileButton, "growx, wrap");
        add(technopolisJButton, "growx");
        add(technomarketJButton, "growx, wrap");
        add(generateReport, "span, center, split 3");
        add(cancelButton);
        add(subtractCheckBox, "wrap");
        add(statusLabel);
        add(statusTextArea, "span, growx");
    }

    public void setComponentText() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
        srcFileLabel.setText(r.getString("MainPanel.srcFileLabel"));
        clearSrcFileButton.setText(r.getString("MainPanel.clearButton"));
        destFileLabel.setText(r.getString("MainPanel.destFileLabel"));
        clearDestFileButton.setText(r.getString("MainPanel.clearButton"));
        createNewFileButton.setText(r.getString("MainPanel.createNewFileButton"));
        technopolisJButton.setText(r.getString("MainPanel.technopolisJButton"));
        technomarketJButton.setText(r.getString("MainPanel.technomarketJButton"));
        generateReport.setText(r.getString("MainPanel.generateReport"));
        cancelButton.setText(r.getString("MainPanel.cancelButton"));
        subtractCheckBox.setText(r.getString("MainPanel.subtractCheckBox"));
        statusLabel.setText(r.getString("MainPanel.statusLabel"));
    }

    private void attachListeners() {
        createNewFileButton.addActionListener((ae) -> {
            NewFileManager.getInstance().setFileCallback(createOutputFileChooser);
            LiteReportManager.getInstance().setOutputArea(destFilePath);
            LiteReportManager.getInstance().createNewFile();
        });
    }
}
