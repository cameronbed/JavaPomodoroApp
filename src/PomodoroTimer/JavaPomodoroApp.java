/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package PomodoroTimer;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Cameron B
 */
public class JavaPomodoroApp {

    /**
     * @param args the command line arguments
     */
    // ============= Main
    static JFrame mainFrame;

    // ============= Settings
    static JPASettings jpaSettings;
    static JPanel settingsPanel;
    // Buttons
    static JButton launchTimerBtn;
    static JButton saveSettingsBtn;
    // Time Spinner
    static JSpinner timeSpinner;
    static SpinnerNumberModel timeNumberModel;
    // Short Break Spinner
    static JSpinner shortBreakSpinner;
    static SpinnerNumberModel shortBreakNumberModel;
    // Long Break Spinner
    static JSpinner longBreakSpinner;
    static SpinnerNumberModel longBreakNumberModel;

    // ============== Timer
    static JFrame timerMainFrame;
    static JPanel timerMainPanel;
    static JPATime jpaTime;
    static JButton startTimerBtn;
    static JButton pauseTimerBtn;
    static JButton stopTimerBtn;
    static JLabel timerTimeLabel;

    static int setTimerTime = 25;
    static int setShortBreakTime = 5;
    static int setLongBreakTime = 15;
    static int setRounds = 2;
    static ButtonListener btnListener = new ButtonListener();

    public static void main(String[] args) {
        // defaults

        // =========== Panels
        // Settings
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(10, 10));

        // ========= Buttons
        // Luanch Timer
        launchTimerBtn = new JButton("Launch Timer");
        launchTimerBtn.addActionListener(btnListener);
        settingsPanel.add(launchTimerBtn);
        // Save Settings
        saveSettingsBtn = new JButton("Save Settings");
        saveSettingsBtn.addActionListener(btnListener);
        settingsPanel.add(saveSettingsBtn);

        // ======== Spinners
        // Timer Time
        timeNumberModel = new SpinnerNumberModel(setTimerTime, 1, 60, 1);
        timeSpinner = new JSpinner(timeNumberModel);
        settingsPanel.add(timeSpinner);
        // Short Break Time
        shortBreakNumberModel = new SpinnerNumberModel(setShortBreakTime, 1, 60, 1);
        shortBreakSpinner = new JSpinner(shortBreakNumberModel);
        settingsPanel.add(shortBreakSpinner);
        // Long Break Time
        longBreakNumberModel = new SpinnerNumberModel(setLongBreakTime, 1, 60, 1);
        longBreakSpinner = new JSpinner(longBreakNumberModel);
        settingsPanel.add(longBreakSpinner);

        // ========== Class Objects
        // Settings Object
        jpaSettings = new JPASettings();

        // ========== Frames
        // Main
        mainFrame = new JFrame("JavaPomodoroApp");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 400);
        mainFrame.add(settingsPanel);
        mainFrame.setVisible(true);

        System.out.println("This is the end of main");
    }

    public static void LaunchTimer(int time, int shortBreak, int longBreak, int rounds) {
        System.out.println("Timer Launched");

        // Timer Object
        jpaTime = new JPATime();

        // Timer Frame
        timerMainFrame = new JFrame("JPA Timer");
        timerMainFrame.setSize(200, 200);

        // Timer Panel
        timerMainPanel = new JPanel();
        timerMainPanel.setLayout(new GridLayout(10, 10));
        timerMainFrame.add(timerMainPanel);

        // Window Listener
        MyWindowListener WinListener = new MyWindowListener();
        timerMainFrame.addWindowListener(WinListener);

        // Timer Time
        timerTimeLabel = new JLabel("<HTML><font SIZE=24>" + time);
        timerMainPanel.add(timerTimeLabel);

        // Buttons
        startTimerBtn = new JButton("Start");
        pauseTimerBtn = new JButton("Pause");
        stopTimerBtn = new JButton("Stop");

        startTimerBtn.addActionListener(btnListener);
        pauseTimerBtn.addActionListener(btnListener);
        stopTimerBtn.addActionListener(btnListener);

        timerMainPanel.add(startTimerBtn);
        timerMainPanel.add(pauseTimerBtn);
        timerMainPanel.add(stopTimerBtn);

        timerMainFrame.setVisible(true);
    }
}

class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonPressed = (JButton) e.getSource();

        if (buttonPressed == JavaPomodoroApp.launchTimerBtn) {
            JavaPomodoroApp.LaunchTimer(JavaPomodoroApp.setTimerTime, JavaPomodoroApp.setShortBreakTime, JavaPomodoroApp.setLongBreakTime, JavaPomodoroApp.setRounds);
        } else if (buttonPressed == JavaPomodoroApp.saveSettingsBtn) {
            System.out.println("Save Settings Button Pressed");
        } else if (buttonPressed == JavaPomodoroApp.startTimerBtn) {
            System.out.println("Start Timer Button Pressed");
        } else if (buttonPressed == JavaPomodoroApp.pauseTimerBtn) {
            System.out.println("Pause Timer Button Pressed");
        } else if (buttonPressed == JavaPomodoroApp.stopTimerBtn) {
            System.out.println("Stop Timer Button Pressed");
        }
    }
}

class MyWindowListener extends JFrame implements WindowListener {

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("Timer Window Closed");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Timer Window Closing");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        System.out.println("Timer Window Activated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("Timer Window Deactiavated");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("Timer Window Deiconified");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("Timer Window Iconified");
    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("Timer Window Opened");
    }
}

class JPATime {

    long unixTime = Calendar.getInstance().getTime().getTime();
    int ticks = 0;

    public static void startTime(int duration) {
        System.out.println("Time Started");
    }

    public static void stopTime() {
        System.out.println("Time stoped");
    }

    public static void pauseTime() {
        System.out.println("Time paused");
    }
}

class JPASettings {
}
