/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PomodoroTimer;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Cameron B
 * 
 *         JPASettingsWindow is the GUI for the settings and also where the user
 *         customaizing the timer and then launches the timer.
 */
public class JPASettingsWindow extends JFrame {

    // ========= Buttons
    static JButton launchTimerBtn;
    static JButton saveSettingsBtn;
    static JButton loadSettingsBtn;
    static JButton rstHistoryBtn;
    static JButton skipBtn;

    // ========= Spinners
    static JSpinner workTimeSpinner;
    static JSpinner shortBreakTimeSpinner;
    static JSpinner longBreakTimeSpinner;
    static JSpinner roundsSpinner;

    // =========== Labels
    static JLabel historyLabel;

    // Settings Values
    static int loggedTime = 0;

    // ========== Panels
    JPanel launchPanel = new JPanel();
    JPanel settingsPanel = new JPanel();
    JPanel settingsBtnPanel = new JPanel();
    JPanel historyPanel = new JPanel();
    JPanel spinnerPanel = new JPanel();

    // Interface
    ConfigCallback myConfigCallback;
    FocusLogListener logListener;

    JPASettingsWindow(ButtonListener btnListener, MyWindowListener winListener, ConfigCallback configCallback,
            FocusLogListener focusLogListener) {

        this.myConfigCallback = configCallback;
        this.logListener = focusLogListener;
        init(btnListener, winListener);
    }

    private boolean init(ButtonListener btnListener, MyWindowListener winListener) {

        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        initSettingsBtnPanel(btnListener);
        initHistoryPanel(btnListener);
        initLaunchPanel(btnListener);

        initSpinnerPanel(btnListener);

        initSettingsPanel(btnListener);

        add(launchPanel, java.awt.BorderLayout.LINE_END);
        add(settingsPanel, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);
        setVisible(true);

        return true;
    }

    private void initSettingsBtnPanel(ButtonListener btnListener) {
        settingsBtnPanel.setLayout(new BoxLayout(settingsBtnPanel, BoxLayout.X_AXIS));

        saveSettingsBtn = new JButton("Save Settings");
        loadSettingsBtn = new JButton("Load Settings");

        saveSettingsBtn.addActionListener(btnListener);
        loadSettingsBtn.addActionListener(btnListener);

        settingsBtnPanel.add(saveSettingsBtn);
        settingsBtnPanel.add(Box.createHorizontalGlue());
        settingsBtnPanel.add(loadSettingsBtn);
        settingsBtnPanel.add(Box.createRigidArea(new Dimension(5, 10)));
    }

    private void initSpinnerPanel(ButtonListener btnListener) {
        // =========== Constants
        int labelSpinnerWidth = 120;
        int labelSpinnerHeight = 25;
        int rowSpinnerWidth = 80;
        int rowSpinnerHeight = 30;
        Dimension spinnerSize = new Dimension(rowSpinnerWidth, rowSpinnerHeight);
        Dimension labelSpinnerSize = new Dimension(labelSpinnerWidth, labelSpinnerHeight);

        int configWorkTime = this.myConfigCallback.getConfigMap().get("workTime");
        int configShortBreakTime = this.myConfigCallback.getConfigMap().get("shortBreakTime");
        int configLongBreakTime = this.myConfigCallback.getConfigMap().get("longBreakTime");
        int configRounds = this.myConfigCallback.getConfigMap().get("rounds");

        // ========== Spinner Objects
        workTimeSpinner = new JSpinner(new SpinnerNumberModel(configWorkTime, 1, 60, 1));
        shortBreakTimeSpinner = new JSpinner(new SpinnerNumberModel(configShortBreakTime, 1, 60, 1));
        longBreakTimeSpinner = new JSpinner(new SpinnerNumberModel(configLongBreakTime, 1, 60, 1));
        roundsSpinner = new JSpinner(new SpinnerNumberModel(configRounds, 1, 60, 1));

        // Spinner Size
        workTimeSpinner.setPreferredSize(spinnerSize);
        shortBreakTimeSpinner.setPreferredSize(spinnerSize);
        longBreakTimeSpinner.setPreferredSize(spinnerSize);
        roundsSpinner.setPreferredSize(spinnerSize);

        // =============== Layout
        spinnerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel workLabel = new JLabel("Work Time:");
        workLabel.setPreferredSize(labelSpinnerSize);
        spinnerPanel.add(workLabel, gbc);
        gbc.gridx = 1;
        spinnerPanel.add(workTimeSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel shortBreakLabel = new JLabel("Short Break Time:");
        shortBreakLabel.setPreferredSize(labelSpinnerSize);
        spinnerPanel.add(shortBreakLabel, gbc);
        gbc.gridx = 1;
        spinnerPanel.add(shortBreakTimeSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel longBreakLabel = new JLabel("Long Break Time:");
        longBreakLabel.setPreferredSize(labelSpinnerSize);
        spinnerPanel.add(longBreakLabel, gbc);
        gbc.gridx = 1;
        spinnerPanel.add(longBreakTimeSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel roundsLabel = new JLabel("Rounds:");
        roundsLabel.setPreferredSize(labelSpinnerSize);
        spinnerPanel.add(roundsLabel, gbc);
        gbc.gridx = 1;
        spinnerPanel.add(roundsSpinner, gbc);
    }

    private void initLaunchPanel(ButtonListener btnListener) {
        launchTimerBtn = new JButton("Launch Timer");

        launchPanel.setLayout(new BoxLayout(launchPanel, BoxLayout.Y_AXIS));

        launchTimerBtn.addActionListener(btnListener);

        launchPanel.add(Box.createVerticalGlue());
        launchPanel.add(launchTimerBtn);
        launchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        launchPanel.add(Box.createVerticalGlue());
        launchPanel.add(Box.createRigidArea(new Dimension(25, 30)));

    }

    private void initHistoryPanel(ButtonListener btnListener) {
        rstHistoryBtn = new JButton("Reset History");
        historyLabel = new JLabel("Logged Minutes: " + logListener.getLoggedMinutes());

        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.X_AXIS));
        rstHistoryBtn.addActionListener(btnListener);
        historyPanel.add(rstHistoryBtn);
        historyPanel.add(Box.createHorizontalGlue());
        historyPanel.add(historyLabel);
        historyPanel.add(Box.createHorizontalGlue());
    }

    private void initSettingsPanel(ButtonListener btnListener) {
        settingsPanel.add(historyPanel);
        settingsPanel.add(spinnerPanel);
        settingsPanel.add(settingsBtnPanel);
    }
}
