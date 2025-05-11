/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package PomodoroTimer;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
// Files
import java.io.*;

// Queue
import java.util.Queue;

/**
 *
 * @author Cameron B
 */
public class JavaPomodoroApp {

    // ============= Settings
    static JPASettingsWindow jpaSettingsWindow;
    // ============== Timer
    static JPATimerWindow jpaTimerWindow;
    static JPATimer jpaTimer;

    // ============= Interface
    static TimeUpdateListener timeUpdateListener;

    // ActionListeners
    static ButtonListener btnListener = new ButtonListener();
    static MyWindowListener winListener = new MyWindowListener();

    // Queue
    static Queue<Integer> timeQueue = new LinkedList<>();

    // Settings HashMap
    static Map<String, Integer> configMap = new HashMap<>();

    // Databse
    static FocusDB focusDB;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        readSettings();
        focusDB = new FocusDB();
        jpaSettingsWindow = new JPASettingsWindow(
                btnListener,
                winListener,
                new ConfigCallback() {
            @Override
            public Map<String, Integer> getConfigMap() {
                return configMap;
            }
        }, new FocusLogListener() {
            @Override
            public int getLoggedMinutes() {
                return focusDB.getLoggedMinutes();
            }
        });
    }

    private static void generateTimeSequence() {
        timeQueue.clear();
        int work = configMap.get("workTime");
        int shortBreak = configMap.get("shortBreakTime");
        int longBreak = configMap.get("longBreakTime");
        int rounds = configMap.get("rounds");
        for (int i = 1; i <= rounds; i++) {
            timeQueue.add(work);
            if (i < rounds) {
                timeQueue.add(shortBreak);
            } else {
                timeQueue.add(longBreak);
            }
        }
    }

    public static void launchTimer() {
        getSpinnersConfig();
        generateTimeSequence();
        int workTime = timeQueue.peek();

        jpaTimerWindow = new JPATimerWindow(btnListener, winListener);
        timeUpdateListener = jpaTimerWindow;

        jpaTimer = new JPATimer(
                workTime,
                timeUpdateListener,
                new FocusLogUpdater() {
            @Override
            public void logSession(int duration) {
                focusDB.logSession(duration);
            }
        },
                // ========== NEW CALLBACK ==========
                new SessionCompleteListener() {
            @Override
            public void sessionComplete() {
                // remove the one that just finished
                timeQueue.poll();
                if (!timeQueue.isEmpty()) {
                    int next = timeQueue.peek();
                    jpaTimer.updateDuration(next);
                    jpaTimer.startTimer();
                }
            }
        });
    }

    public static void startTimer() {
        if (!jpaTimer.isRunning()) {
            new Thread(jpaTimer).start();
            Thread.currentThread().setPriority(10);
        }
        jpaTimer.startTimer();
    }

    public static void writeSettings() {
        getSpinnersConfig();

        try (PrintWriter writer = new PrintWriter(new File("settings.txt"))) {
            for (Map.Entry<String, Integer> entry : configMap.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error saving settings: " + ex.getMessage());
        }
    }

    public static void readSettings() {
        try (Scanner scanner = new Scanner(new File("settings.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    configMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Settings file not found. Using default values.");
        }
    }

    public static void getSpinnersConfig() {
        configMap.put("workTime", (Integer) JPASettingsWindow.workTimeSpinner.getValue());
        configMap.put("shortBreakTime", (Integer) JPASettingsWindow.shortBreakTimeSpinner.getValue());
        configMap.put("longBreakTime", (Integer) JPASettingsWindow.longBreakTimeSpinner.getValue());
        configMap.put("rounds", (Integer) JPASettingsWindow.roundsSpinner.getValue());
    }

    public static void clearLog() {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Yikes. Are you sure you want to clear all log history?\nThis action cannot be undone.",
                "Confirm Clear History",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            focusDB.clearFocusHistory();
            JPASettingsWindow.historyLabel.setText("Logged Time: 0");
            JOptionPane.showMessageDialog(null, "History cleared successfully.");
        }
    }
}

interface TimeUpdateListener {

    public void updateTime(long timeLeftMs, long timeRan);
}

interface ConfigCallback {

    Map<String, Integer> getConfigMap();
}

interface FocusLogListener {

    int getLoggedMinutes();
}

interface FocusLogUpdater {

    void logSession(int duration);
}

interface SessionCompleteListener {

    void sessionComplete();
}

class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonPressed = (JButton) e.getSource();

        if (buttonPressed == JPASettingsWindow.launchTimerBtn) {
            JavaPomodoroApp.launchTimer();

        } else if (buttonPressed == JPASettingsWindow.saveSettingsBtn) {
            // System.out.println("Button: Save Settings Button Pressed");
            JavaPomodoroApp.writeSettings();

        } else if (buttonPressed == JPATimerWindow.startTimerBtn) { // startTimer
            // System.out.println("Button: Start Timer Button Pressed");
            JavaPomodoroApp.startTimer();

        } else if (buttonPressed == JPATimerWindow.pauseTimerBtn) {
            // System.out.println("Button: Pause Timer Button Pressed");
            JavaPomodoroApp.jpaTimer.pauseTimer();

        } else if (buttonPressed == JPATimerWindow.stopTimerBtn) {
            // System.out.println("Button: Stop Timer Button Pressed");
            JavaPomodoroApp.jpaTimer.stopTimer();

        } else if (buttonPressed == JPATimerWindow.viewSwitchBtn) {
            // System.out.println("Button: View Toggle Button Pressed");
            JavaPomodoroApp.jpaTimerWindow.toggleView();

        } else if (buttonPressed == JPASettingsWindow.loadSettingsBtn) {
            // System.out.println("Button: Load Settings Button Pressed");
            JavaPomodoroApp.readSettings();

        } else if (buttonPressed == JPASettingsWindow.rstHistoryBtn) {
            // System.out.println("Button: Reset History Button Pressed");
            JavaPomodoroApp.clearLog();
        } else if (buttonPressed == JPATimerWindow.skipButton) {
            // System.out.println("Button: Skip Button Pressed");
            JavaPomodoroApp.jpaTimer.skipSession();
        }
    }
}

class MyWindowListener extends JFrame implements WindowListener {

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
        if (JavaPomodoroApp.jpaTimer != null) {
            JavaPomodoroApp.jpaTimer.stopTimer();
            Thread.currentThread().interrupt();
        }
        JPASettingsWindow.historyLabel.setText(
                "Logged Minutes: " + JavaPomodoroApp.focusDB.getLoggedMinutes());
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
