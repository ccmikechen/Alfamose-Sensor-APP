package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.kuasmis.wmc.nike.DataGetterInterface;

public class NikeDataPlayer implements Runnable {
	
	private static final float DEFAULT_SPEED = 32f;
	
	private NikeDataLoader dataLoader;
	
	private int currentTime = 0;
	private float speed = DEFAULT_SPEED;
	
	private boolean isStarted = false;
	private boolean isRunning = false;
	
	private NikeDataGetter leftDataGetter = null;
	private NikeDataGetter rightDataGetter = null;
	
	private List<PlayTimeListener> playTimeListenerList = null;
	private List<EndPlayingListener> endPlayingListenerList = null;
	private Thread thread = null;
	
	public NikeDataPlayer(NikeDataLoader dataLoader) {
		this.dataLoader = dataLoader;
		this.playTimeListenerList = new ArrayList<PlayTimeListener>();
		this.endPlayingListenerList = new ArrayList<EndPlayingListener>();
	}
	
	public void run() {
		while (isRunning) {
			if (currentTime < getLength()) {
				this.leftDataGetter.dataCallBack(dataLoader.getLeftSignals()[currentTime]);
				this.rightDataGetter.dataCallBack(dataLoader.getRightSignals()[currentTime]);
				notifyAllPlayTimeListener();
				if (isStarted) {
					this.currentTime++;
				}
				try {
					TimeUnit.MILLISECONDS.sleep((int) (1000 / speed));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				end();
			}
		}
	}
	
	public void start() {
		if (this.thread == null) {
			this.isRunning = true;
			this.thread = new Thread(this);
			this.thread.start();
		}
		this.isStarted = true;
		if (getCurrentTime() >= (getLength() - 1)) {
			this.currentTime = 0;
			this.speed = DEFAULT_SPEED;
		}
	}
	
	public void stop() {
		this.isStarted = false;
	}
	
	public void end() {
		this.isStarted = false;
		this.speed = DEFAULT_SPEED;
		this.currentTime = getLength() - 1;
		this.leftDataGetter.dataCallBack(new float[] {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f});
		this.rightDataGetter.dataCallBack(new float[] {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f});
		notifyAllEndPlayingListener();
	}
	
	public void close() {
		this.isRunning = false;
		this.thread = null;
	}
	
	public int getLength() {
		return this.dataLoader.size();
	}
	
	public int getCurrentTime() {
		return currentTime;
	}
	
	public void setCurrentTime(int time) {
		time = Math.max(time, 0);
		time = Math.min(time, getLength());
		this.currentTime = time;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void speedUp() {
		this.speed *= 2;
	}
	
	public void speedDown() {
		this.speed /= 2;
	}
	
	public void setDataGetter(NikeDataGetter leftDataGetter, 
								NikeDataGetter rightDataGetter) {
		this.leftDataGetter = leftDataGetter;
		this.rightDataGetter = rightDataGetter;
	}
	
	public void addPlayTimeListener(PlayTimeListener playTimeListener) {
		this.playTimeListenerList.add(playTimeListener);
	}

	private void notifyAllPlayTimeListener() {
		new Thread() {
			public void run() {
				for (PlayTimeListener listener : playTimeListenerList)
					listener.tick(currentTime);
			}
		}.start();
	}
	
	public void addEndPlayingListener(EndPlayingListener endPlayingListener) {
		this.endPlayingListenerList.add(endPlayingListener);
	}
	
	private void notifyAllEndPlayingListener() {
		new Thread() {
			public void run() {
				for (EndPlayingListener listener : endPlayingListenerList) {
					listener.onPlayingEnd();
				}
			}
		}.start();
	}
}
