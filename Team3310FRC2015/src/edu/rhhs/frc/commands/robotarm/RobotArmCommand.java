package edu.rhhs.frc.commands.robotarm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public abstract class RobotArmCommand {

    /** The time since this command was initialized */
    private double m_startTime = -1;
    /** The time (in seconds) before this command "times out" (or -1 if no timeout) */
    private double m_timeout = -1;
    /** Whether or not this command has been initialized */
    private boolean m_initialized = false;

    public RobotArmCommand() {
    }
    
    public RobotArmCommand(double timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must not be negative.  Given:" + timeout);
        }
        m_timeout = timeout;
    }

    /**
     * Sets the timeout of this command.
     * @param seconds the timeout (in seconds)
     * @throws IllegalArgumentException if seconds is negative
     * @see Command#isTimedOut() isTimedOut()
     */
    protected synchronized final void setTimeout(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_timeout = seconds;
    }

    /**
     * Returns the time since this command was initialized (in seconds).
     * This function will work even if there is no specified timeout.
     * @return the time since this command was initialized (in seconds).
     */
    public synchronized final double timeSinceInitialized() {
        return m_startTime < 0 ? 0 : Timer.getFPGATimestamp() - m_startTime;
    }

    /**
     * The run method is used internally to actually run the commands.
     * @return whether or not the command should stay within the {@link Scheduler}.
     */
    public synchronized boolean run() {
        if (!m_initialized) {
            startTiming();
            initialize();
            m_initialized = true;
        }
        execute();
        return isFinished();
    }
    
    public synchronized void reset() {
    	m_initialized = false;
    }

    /**
     * Called to indicate that the timer should start.
     * This is called right before {@link Command#initialize() initialize()} is, inside the
     * {@link Command#run() run()} method.
     */
    private void startTiming() {
        m_startTime = Timer.getFPGATimestamp();
    }

    /**
     * Returns whether or not the {@link Command#timeSinceInitialized() timeSinceInitialized()}
     * method returns a number which is greater than or equal to the timeout for the command.
     * If there is no timeout, this will always return false.
     * @return whether the time has expired
     */
    protected synchronized boolean isTimedOut() {
        return m_timeout != -1 && timeSinceInitialized() >= m_timeout;
    }

    protected abstract void initialize();

    protected abstract void execute();

    protected abstract boolean isFinished();

    protected abstract void end();
}
