/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PomodoroTimer;

/**
 *
 * @author Cameron B
 * 
 *         JPATimer is what handles the timer logic. It is a thread that runs in
 *         the background.
 */
public class JPATimer implements Runnable {

    TimeUpdateListener timeUpdater;
    FocusLogUpdater logUpdater;

    long durationMs;
    long timeLeftMs;

    long targetTime;
    long currentTime;

    boolean showSeconds = true;

    boolean isCountingDown = false;
    boolean isPaused = false;
    boolean isStopped = false;

    public volatile boolean threadRunning = false;

    private final SessionCompleteListener sessionListener;

    JPATimer(int time, TimeUpdateListener timeUpdateListener, FocusLogUpdater focusLogUpdater,
            SessionCompleteListener sessionListener) {
        timeUpdater = timeUpdateListener;
        logUpdater = focusLogUpdater;
        this.sessionListener = sessionListener;
        this.updateDuration(time);
    }

    public synchronized void startTimer() {
        if (!isCountingDown) {
            if (isPaused) {
                isPaused = false;
                isStopped = false;
                isCountingDown = true;
                targetTime = System.currentTimeMillis() + timeLeftMs;
            } else if (isStopped) {
                isStopped = false;
                isPaused = false;
                isCountingDown = true;
                timeLeftMs = durationMs;
                targetTime = System.currentTimeMillis() + timeLeftMs;
            } else {
                isCountingDown = true;
                targetTime = System.currentTimeMillis() + durationMs;
            }
        }
    }

    public synchronized void stopTimer() {
        isCountingDown = false;
        isStopped = true;
        isPaused = false;
        timeLeftMs = durationMs;

        logSessionMinutes();
        timeUpdater.updateTime(durationMs, 0);
    }

    public synchronized void pauseTimer() {
        if (isCountingDown) {
            isCountingDown = false;
            isPaused = true;

            logSessionMinutes();
            timeLeftMs = targetTime - currentTime;

            timeUpdater.updateTime(timeLeftMs, durationMs - timeLeftMs);
        }
    }

    public synchronized void skipSession() {
        if (isCountingDown) {
            isCountingDown = false;
            isStopped = true;
            isPaused = false;
            timeLeftMs = durationMs;

            logSessionMinutes();
            timeUpdater.updateTime(durationMs, 0);
        }
    }

    @Override
    public void run() {
        threadRunning = true;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    if (isCountingDown) {
                        currentTime = System.currentTimeMillis();
                        long diff = targetTime - currentTime;

                        if (diff <= 0) {
                            isCountingDown = false;
                            logSessionMinutes();
                            timeUpdater.updateTime(0, durationMs);
                            sessionListener.sessionComplete();
                        } else {
                            timeUpdater.updateTime(diff, durationMs - diff);
                        }
                    }
                    Thread.sleep(250);
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Exception caught");
            Thread.currentThread().interrupt();
        } finally {
            threadRunning = false;
            System.out.println("JPATimerLabel: Thread isStopped.");
        }
    }

    public synchronized void updateDuration(long time) {
        durationMs = time * 60000;
        timeLeftMs = durationMs;
        targetTime = System.currentTimeMillis() + durationMs;
        isCountingDown = false;
        isPaused = false;
        isStopped = true;
        timeUpdater.updateTime(durationMs, 0);
    }

    public void finish() {
    }

    private void logSessionMinutes() {
        if (logUpdater != null) {
            long now = System.currentTimeMillis();

            long elapsedMs = durationMs - (targetTime - now);
            elapsedMs = Math.max(0, Math.min(durationMs, elapsedMs));

            int focusedMinutes = (int) (elapsedMs / 60000);
            logUpdater.logSession(focusedMinutes);
        }
    }

    public boolean isRunning() {
        return threadRunning;
    }

    public synchronized boolean isPaused() {
        return isPaused;
    }

    public synchronized boolean isStopped() {
        return isStopped;
    }
}
