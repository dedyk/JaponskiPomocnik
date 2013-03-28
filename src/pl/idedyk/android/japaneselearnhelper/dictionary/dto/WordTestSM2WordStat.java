package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.Date;

public class WordTestSM2WordStat {
	
	private int id;
	
	private int power;
	
	private float easinessFactor;
	
	private int repetitions;
	
	private int interval;
	
	private Date nextRepetitions;
	
	private Date lastStudied;
	
	private boolean wasNew;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public float getEasinessFactor() {
		return easinessFactor;
	}

	public void setEasinessFactor(float easinessFactor) {
		this.easinessFactor = easinessFactor;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Date getNextRepetitions() {
		return nextRepetitions;
	}

	public void setNextRepetitions(Date nextRepetitions) {
		this.nextRepetitions = nextRepetitions;
	}

	public Date getLastStudied() {
		return lastStudied;
	}

	public void setLastStudied(Date lastStudied) {
		this.lastStudied = lastStudied;
	}

	public boolean isWasNew() {
		return wasNew;
	}

	public void setWasNew(boolean wasNew) {
		this.wasNew = wasNew;
	}

	@Override
	public String toString() {
		return "WordTestSM2WordStat [id=" + id + ", power=" + power + ", easinessFactor=" + easinessFactor
				+ ", repetitions=" + repetitions + ", interval=" + interval + ", nextRepetitions=" + nextRepetitions
				+ ", lastStudied=" + lastStudied + ", wasNew=" + wasNew + "]";
	}
}
