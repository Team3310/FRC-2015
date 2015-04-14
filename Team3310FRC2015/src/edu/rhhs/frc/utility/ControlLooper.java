package edu.rhhs.frc.utility;

import java.util.Timer;
import java.util.TimerTask;

public class ControlLooper 
{
	private Timer controlLoopTimer;
	private boolean isControlLoopEnabled = false;
	
	private ControlLoopable loopable;
	private long periodMs;

	public ControlLooper(ControlLoopable loopable, long periodMs) {
		this.loopable = loopable;
		this.periodMs = periodMs;
	}
	
	private class ControlLoopTask extends TimerTask {
		private ControlLooper controlLooper;

		public ControlLoopTask(ControlLooper controlLooper) {
			if (controlLooper == null) {
				throw new NullPointerException("Given control looper was null");
			}
			this.controlLooper = controlLooper;
		}

		@Override
		public void run() {
			controlLooper.controlLoopUpdate();
		}
		
	}
	
	public void start() {
		if (controlLoopTimer == null) {
			controlLoopTimer = new Timer();
			controlLoopTimer.schedule(new ControlLoopTask(this), 0L, periodMs);
		}
	}
	
	public void stop() {
		if (controlLoopTimer != null) {
			controlLoopTimer.cancel();
			controlLoopTimer = null;
		}
	}
	
	public synchronized void enable() {
		isControlLoopEnabled = true;
	}

	public synchronized void disable() {
		isControlLoopEnabled = false;
	}

	public synchronized boolean isEnabled() {
		return isControlLoopEnabled;
	}

	
	private void controlLoopUpdate() {
		boolean enabled;
        synchronized (this) {
            enabled = isControlLoopEnabled; // take snapshot of this value
        }
		
        if (enabled) {
        	loopable.controlLoopUpdate();
        }

	}
}