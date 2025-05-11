/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PomodoroTimer;

import java.awt.*;
import static java.lang.Math.floor;
import javax.swing.*;

/**
 *
 * @author Cameron B
 *
 *         JPATimerWindow is the GUI for the timer. It displays the time left as
 *         updated from JPATimer.
 */
public class JPATimerWindow extends JFrame implements TimeUpdateListener {

    static JLabel numbersLabel;
    static JButton viewSwitchBtn;
    static JButton startTimerBtn;
    static JButton pauseTimerBtn;
    static JButton stopTimerBtn;
    static JButton skipButton;

    // ================ Panels
    static JPanel timePanel;
    static JPanel timeDisplayPanel;
    static JPanel timerBtnPanel;
    static JPanel numbersPanel;
    static JPanel visualPanel;

    // =========== Visual Timer
    private static VisualTimerPanel graphicsPanel;
    static boolean isVisual = false;

    JPATimerWindow(ButtonListener btnListener, MyWindowListener winListener) {
        init(btnListener, winListener);
    }

    private boolean init(ButtonListener btnListener, MyWindowListener winListener) {
        // ================= Buttons
        viewSwitchBtn = new JButton("Switch Display");
        startTimerBtn = new JButton("Start");
        pauseTimerBtn = new JButton("Pause");
        stopTimerBtn = new JButton("Stop");
        skipButton = new JButton("Skip");

        // ================ Panels
        timePanel = new JPanel();
        timeDisplayPanel = new JPanel();
        timerBtnPanel = new JPanel();

        // Card Panels
        visualPanel = new JPanel();
        numbersPanel = new JPanel();

        // ================= Layout
        setLayout(new BorderLayout()); // Frame
        timerBtnPanel.setLayout(new BoxLayout(timerBtnPanel, BoxLayout.Y_AXIS));
        timePanel.setLayout(new BorderLayout());
        numbersPanel.setLayout(new BorderLayout());
        timeDisplayPanel.setLayout(new CardLayout());

        // =============== Graphics
        graphicsPanel = new VisualTimerPanel();
        visualPanel.setLayout(new BorderLayout());
        visualPanel.add(graphicsPanel, BorderLayout.CENTER);

        // =============== Label
        numbersLabel = new JLabel("--:--", SwingConstants.CENTER);
        numbersPanel.add(numbersLabel, BorderLayout.CENTER);
        numbersLabel.setFont(new Font("Segoe UI", Font.BOLD, 96));

        // ================= Add buttons
        timerBtnPanel.add(Box.createVerticalGlue());
        startTimerBtn.addActionListener(btnListener);
        timerBtnPanel.add(startTimerBtn);
        timerBtnPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        timerBtnPanel.add(Box.createVerticalGlue());
        pauseTimerBtn.addActionListener(btnListener);
        timerBtnPanel.add(pauseTimerBtn);
        timerBtnPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        timerBtnPanel.add(Box.createVerticalGlue());
        stopTimerBtn.addActionListener(btnListener);
        timerBtnPanel.add(stopTimerBtn);

        timerBtnPanel.add(Box.createVerticalGlue());
        skipButton.addActionListener(btnListener);
        timerBtnPanel.add(skipButton);
        timerBtnPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        timerBtnPanel.add(Box.createVerticalGlue());

        // Toggle Button
        timePanel.add(viewSwitchBtn, BorderLayout.PAGE_START);
        viewSwitchBtn.addActionListener(btnListener);

        // Add panels
        add(timerBtnPanel, BorderLayout.LINE_END);

        // Add cards
        timeDisplayPanel.add(numbersPanel, "numbers");
        timeDisplayPanel.add(visualPanel, "visual");

        // Time Display Panel
        timePanel.add(timeDisplayPanel, BorderLayout.CENTER);

        // time Panel
        add(timePanel, BorderLayout.CENTER);

        addWindowListener(winListener);
        setSize(400, 200);
        setAlwaysOnTop(true);
        setResizable(false);
        setVisible(true);

        return true;
    }

    public void toggleView() {
        isVisual = !isVisual;
        CardLayout cl = (CardLayout) (timeDisplayPanel.getLayout());
        cl.next(timeDisplayPanel);
    }

    @Override
    public void updateTime(long timeLeftMs, long timeRanMs) {
        long minLeft = (long) floor(timeLeftMs / 60000);
        long secLeft = (timeLeftMs % 60000) / 1000;
        String formattedTime = String.format("%d:%02d", minLeft, secLeft);

        graphicsPanel.updateTime(timeLeftMs, timeRanMs);

        numbersLabel.setText(formattedTime);

    }
}

/*
 * VisualTimerPanel is the JPanel that displays the visual timer. It draws a
 * circle and subtracts the time left from the circle as it counts down.`
 */
class VisualTimerPanel extends JPanel {

    private long timeLeftMs = 0;
    private long timeRanMs = 0;

    public void updateTime(long timeLeft, long timeRan) {
        this.timeLeftMs = timeLeft;
        this.timeRanMs = timeRan;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics gfx) {
        super.paintComponent(gfx);

        int padding = 20;
        int size = Math.min(getWidth(), getHeight()) - 2 * padding;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        Graphics2D g2d = (Graphics2D) gfx;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(x, y, size, size);

        g2d.setColor(Color.RED);

        if (timeLeftMs == 0 && timeRanMs == 0) {
            g2d.fillOval(x, y, size, size);
        } else {
            double progress = (double) timeLeftMs / (timeLeftMs + timeRanMs);
            double angle = 360.0 * progress;
            g2d.fillArc(x, y, size, size, 90, -(int) angle);
        }
    }
}
