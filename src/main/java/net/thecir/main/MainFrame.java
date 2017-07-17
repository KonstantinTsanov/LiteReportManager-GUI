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
package net.thecir.main;

import net.thecir.enums.Languages;
import net.thecir.panels.MainPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;

/**
 * The frame for the application. Main entry point is located here.
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class MainFrame extends JFrame {

    private MainPanel mainPanel;

    private JMenuBar topMenuBar;
    private JMenu fileJMenu, optionsJMenu, languageJMenu;
    private JMenuItem exitJMenuItem;
    private final Locale locale = getLocaleFromPreferences();

    public MainFrame() {
        setMinimumSize(new Dimension(550, 300));
        setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
        setTitle("Lite Report Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel = new MainPanel(this);
        initMenuBar();
        add(mainPanel);
        setLanguage(locale);
        pack();
    }

    private void initMenuBar() {
        topMenuBar = new JMenuBar();
        createInitFileMenu();
        createInitOptionsMenu();
        setJMenuBar(topMenuBar);
    }

    /**
     * creates and initializes the file menu and submenus.
     */
    private void createInitFileMenu() {
        fileJMenu = new JMenu();
        fileJMenu.setMnemonic(KeyEvent.VK_F);
        exitJMenuItem = new JMenuItem();
        exitJMenuItem.setMnemonic(KeyEvent.VK_E);
        exitJMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        fileJMenu.add(exitJMenuItem);
        topMenuBar.add(fileJMenu);
    }

    /**
     * Creates and initializes the options menu and submenu.
     */
    private void createInitOptionsMenu() {
        optionsJMenu = new JMenu();
        optionsJMenu.setMnemonic(KeyEvent.VK_O);
        languageJMenu = new JMenu();
        for (Languages lang : Languages.values()) {
            JMenuItem language = new JMenuItem(lang.getName());
            language.addActionListener((ActionEvent ActionEvent) -> {
                if (!getLocaleFromPreferences().getCountry().equals(lang.getLocale().getCountry())
                        && !getLocaleFromPreferences().getLanguage().equals(lang.getLocale().getLanguage())) {
                    setLanguage(lang.getLocale());
                    setLocaleToPreferences(lang);
                    pack();
                }
            });
            languageJMenu.add(language);
        }
        optionsJMenu.add(languageJMenu);
        topMenuBar.add(optionsJMenu);
    }

    /**
     * Sets the language for the whole application.
     *
     * @param locale Language to be used.
     */
    private void setLanguage(Locale locale) {
        Locale.setDefault(locale);
        mainPanel.setComponentText();
        setComponentText();
    }

    /**
     * Gets the lastly used language from the registry(windows) or whatever it
     * is in linux
     *
     * @return new locale object, composed of the extracted parameters
     */
    private Locale getLocaleFromPreferences() {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        String language = "Language";
        String country = "Country";
        return new Locale(prefs.get(language, "en"), prefs.get(country, "US"));
    }

    /**
     * Sets the lastly used language in the registry(windows) or whatever it is
     * in linux
     *
     * @param lang enum containing the required variables
     */
    private void setLocaleToPreferences(Languages lang) {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        String language = "Language";
        String country = "Country";
        prefs.put(language, lang.getShortLanguage());
        prefs.put(country, lang.getShortCountry());
    }

    /**
     * Sets the text for the frame's components.
     *
     * @param locale
     */
    private void setComponentText() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
        fileJMenu.setText(r.getString("MainFrame.optionsMenu.fileJMenu"));
        exitJMenuItem.setText(r.getString("MainFrame.optionsMenu.exitJMenuItem"));
        optionsJMenu.setText(r.getString("MainFrame.optionsMenu.optionsJMenu"));
        languageJMenu.setText(r.getString("MainFrame.optionsMenu.languageJMenu"));
    }

    /**
     * Entry point for the application.
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
