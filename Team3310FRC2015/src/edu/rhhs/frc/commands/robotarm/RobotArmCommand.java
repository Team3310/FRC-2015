package edu.rhhs.frc.commands.robotarm;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public abstract class RobotArmCommand 
{
	public enum RobotArmCommandType { BEGIN_CYCLE, BEGIN_STACK, MIDDLE_STACK, NULL };
	protected RobotArmCommandType m_commandType;
    /** The time since this command was initialized */
    private double m_startTime = -1;
    /** The time (in seconds) before this command "times out" (or -1 if no timeout) */
    private double m_timeout = -1;
    /** Whether or not this command has been initialized */
    private boolean m_initialized = false;
    protected ArrayList<RobotArmCommand> m_parallelCommandList = new ArrayList<RobotArmCommand>();
    protected int m_parallelCommandStartIndex;
    protected int m_parallelCommandEndIndex;

    public RobotArmCommand() {
    	m_commandType = RobotArmCommandType.NULL;
    }
    
    public RobotArmCommand(RobotArmCommandType givenType) {
    	m_commandType = givenType;
    }
    
    public RobotArmCommand(double timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must not be negative.  Given:" + timeout);
        }
        m_timeout = timeout;
    }
    
    protected void setCommandType(RobotArmCommandType type) {
    	m_commandType = type;
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
    
    public RobotArmCommandType getCommandType() {
    	return m_commandType;
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
    
    public boolean isInitialized() {
    	return m_initialized;
    }

    protected abstract void initialize();

    protected abstract void execute();

    protected abstract boolean isFinished();

    protected abstract void end();
    
    protected void setParallelCommandIndexStart(int index) {
    	m_parallelCommandStartIndex = index;
    	m_parallelCommandEndIndex = -1;
    }
    
    protected int getParallelCommandIndexStart() {
    	return m_parallelCommandStartIndex;
    }
    
    protected void setParallelCommandIndexEnd(int index) {
    	m_parallelCommandStartIndex = -1;
    	m_parallelCommandEndIndex = index;
    }
    
    protected int getParallelCommandIndexEnd() {
    	return m_parallelCommandEndIndex;
    }
    
    protected void addParallelStartCommand(RobotArmCommand command, int startIndex) {
    	m_parallelCommandList.add(command);
    	command.setParallelCommandIndexStart(startIndex);
    }
    
    protected void addParallelEndCommand(RobotArmCommand command, int endIndex) {
    	m_parallelCommandList.add(command);
    	command.setParallelCommandIndexEnd(endIndex);
    }
}