package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.Date;

public class WordTestSM2DayStat {
	
	private int id;
	
	private Date dateStat;
	
	private int newWords;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateStat() {
		return dateStat;
	}

	public void setDateStat(Date dateStat) {
		this.dateStat = dateStat;
	}

	public int getNewWords() {
		return newWords;
	}

	public void setNewWords(int newWords) {
		this.newWords = newWords;
	}
}
