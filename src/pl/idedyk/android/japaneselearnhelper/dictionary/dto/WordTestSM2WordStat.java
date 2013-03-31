package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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
	
	public void processRecallResult(int qualityOfRecall) {

		if (qualityOfRecall < 3) {

			repetitions = 0;
			interval = 0;

		} else {
			easinessFactor = calculate_easiness_factor(easinessFactor, qualityOfRecall);

			repetitions += 1;

			if (repetitions == 1) {
				interval = 1;

			} else if (repetitions == 2) {
				interval = 6;

			} else {
				interval *= easinessFactor;
			}
		}

		Calendar calendar = Calendar.getInstance();
		
		Random random = new Random();

		if (interval != 0) {
			calendar.add(Calendar.DAY_OF_MONTH, interval);	
			
			calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24));
			calendar.set(Calendar.MINUTE, random.nextInt(60));
			calendar.set(Calendar.SECOND, random.nextInt(60));
			
		} else {
			
			int randomSecond = random.nextInt(5 * 60);

			calendar.add(Calendar.SECOND, randomSecond);
		}		

		nextRepetitions = calendar.getTime();
		lastStudied = new Date();
	}
	
	private float calculate_easiness_factor(float easinessFactor, int quality) {
		return Math.max(1.3f, easinessFactor + (0.1f -(5.0f - quality) * (0.08f + (5.0f - quality) * 0.02f)));
	}

	@Override
	public String toString() {
		return "WordTestSM2WordStat [id=" + id + ", power=" + power + ", easinessFactor=" + easinessFactor
				+ ", repetitions=" + repetitions + ", interval=" + interval + ", nextRepetitions=" + nextRepetitions
				+ ", lastStudied=" + lastStudied + ", wasNew=" + wasNew + "]";
	}
}
