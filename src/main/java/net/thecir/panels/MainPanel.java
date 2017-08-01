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

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.FILES_ONLY;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import net.thecir.core.LiteReportManager;
import net.thecir.enums.Stores;
import net.thecir.filechoosers.CreateNewFileChooser;
import net.thecir.filemanagers.NewFileManager;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class MainPanel extends JPanel {

    private JLabel inputFileLabel;
    private JLabel outputFileLabel;

    private JTextField inputFilePath;
    private volatile JTextField outputFilePath;

    private JButton selectSrcFileButton;
    private JButton selectDestFileButton;
    private JButton createNewFileButton;
    private JButton clearInputFileButton;
    private JButton clearOutputFileButton;

    private JRadioButton technopolisJButton;
    private JRadioButton technomarketJButton;
    private ButtonGroup retailersButtonGroup;

    private JButton generateReport;

    //If the data from the input file must be subtracted from the output file rather than added to it
    private JCheckBox subtractCheckBox;

    private JLabel statusLabel;
    private JTextArea statusTextArea;

    private final JFrame parent;

    private final JFileChooser inputFileChooser;
    private final JFileChooser outputFileChooser;
    private volatile CreateNewFileChooser createNewFileChooser;

    private ResourceBundle errorBundle;

    public MainPanel(JFrame parent) {
        MigLayout layout = new MigLayout("", "[shrink 0][grow][shrink 0][shrink 0]", "[shrink 0][shrink 0][shrink 0][shrink 0][shrink 0]");
        setLayout(layout);
        this.parent = parent;
        errorBundle = ResourceBundle.getBundle("LanguageBundles/ErrorMessages");
        inputFileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                super.approveSelection();
                inputFilePath.setText(getSelectedFile().toString());
            }
        };
        outputFileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                super.approveSelection();
                outputFilePath.setText(getSelectedFile().toString());
            }
        };
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Workbook (*.xls|*.xlsx)", "xlsx", "xls");
        inputFileChooser.setFileSelectionMode(FILES_ONLY);
        inputFileChooser.setAcceptAllFileFilterUsed(false);
        inputFileChooser.setFileFilter(filter);
        outputFileChooser.setFileSelectionMode(FILES_ONLY);
        outputFileChooser.setAcceptAllFileFilterUsed(false);
        outputFileChooser.setFileFilter(filter);
        initComponents();
        createNewFileChooser = new CreateNewFileChooser(parent, outputFilePath, outputFileChooser, "xlsx");
        addComponents();
        setComponentText();
        attachListeners();
    }

    /**
     * Initializes all panel components.
     */
    private void initComponents() {
        inputFileLabel = new JLabel();
        outputFileLabel = new JLabel();

        inputFilePath = new JTextField();
        inputFilePath.setEditable(false);

        outputFilePath = new JTextField();
        outputFilePath.setEditable(false);

        selectSrcFileButton = new JButton("...");
        selectDestFileButton = new JButton("...");
        createNewFileButton = new JButton("...");
        clearInputFileButton = new JButton();
        clearOutputFileButton = new JButton();

        technopolisJButton = new JRadioButton();
        technomarketJButton = new JRadioButton();
        retailersButtonGroup = new ButtonGroup();
        retailersButtonGroup.add(technomarketJButton);
        retailersButtonGroup.add(technopolisJButton);

        generateReport = new JButton();
        subtractCheckBox = new JCheckBox();

        statusLabel = new JLabel();
        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        statusTextArea.setOpaque(true);
    }

    private void addComponents() {
        add(inputFileLabel);
        add(inputFilePath, "growx");
        add(selectSrcFileButton, "growx");
        add(clearInputFileButton, "wrap");
        add(outputFileLabel, "span 1 2");
        add(outputFilePath, "growx, span 1 2, center");
        add(selectDestFileButton, "growx");
        add(clearOutputFileButton, "span 1 2,wrap");
        add(createNewFileButton, "growx, wrap");
        add(technopolisJButton, "growx");
        add(technomarketJButton, "growx, wrap");
        add(generateReport, "span, center, split 2");
        add(subtractCheckBox, "wrap");
        add(statusLabel);
        add(statusTextArea, "span, growx");
    }

    public void setComponentText() {
        createNewFileChooser.setLocale(Locale.getDefault());
        ResourceBundle r = ResourceBundle.getBundle("LanguageBundles/ComponentText");
        inputFileLabel.setText(r.getString("MainPanel.srcFileLabel"));
        clearInputFileButton.setText(r.getString("MainPanel.clearButton"));
        outputFileLabel.setText(r.getString("MainPanel.destFileLabel"));
        clearOutputFileButton.setText(r.getString("MainPanel.clearButton"));
        createNewFileButton.setText(r.getString("MainPanel.createNewFileButton"));
        technopolisJButton.setText(r.getString("MainPanel.technopolisJButton"));
        technomarketJButton.setText(r.getString("MainPanel.technomarketJButton"));
        generateReport.setText(r.getString("MainPanel.generateReport"));
        subtractCheckBox.setText(r.getString("MainPanel.subtractCheckBox"));
        statusLabel.setText(r.getString("MainPanel.statusLabel"));
    }

    private void attachListeners() {
        selectSrcFileButton.addActionListener((ae) -> {
            inputFileChooser.showOpenDialog(parent);
        });
        selectDestFileButton.addActionListener((ae) -> {
            outputFileChooser.showOpenDialog(parent);
        });
        createNewFileButton.addActionListener((ae) -> {
            NewFileManager.getInstance().setFileCallback(createNewFileChooser);
            LiteReportManager.getInstance().initOutputComponents(parent);
            LiteReportManager.getInstance().createNewFile();
        });
        clearInputFileButton.addActionListener((ae) -> {
            inputFileChooser.setSelectedFile(null);
            inputFilePath.setText(null);
        });
        clearOutputFileButton.addActionListener((ae) -> {
            outputFileChooser.setSelectedFile(null);
            outputFilePath.setText(null);
        });
        generateReport.addActionListener((ae) -> {
            if (inputFileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(parent, errorBundle.getString("NoInputFileSelected"));
            } else if (outputFileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(parent, errorBundle.getString("NoOutputFileSelected"));
            } else if (retailersButtonGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(parent, errorBundle.getString("SelectRetailerMessage"));
            } else {
                LiteReportManager.getInstance().initOutputComponents(parent);
                LiteReportManager.getInstance().generateReport(inputFileChooser.getSelectedFile(),
                        outputFileChooser.getSelectedFile(), subtractCheckBox.isSelected(), technomarketJButton.isSelected() ? Stores.Technomarket : Stores.Technopolis);
            }
        });
    }
}
