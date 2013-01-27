package pl.idedyk.android.japaneselearnhelper.utils;

import java.util.List;

public class EntryOrderList<T> {
	
	private List<T> list;
	
	private int groupSize;
	
	private int currentPos;
	
	public EntryOrderList(List<T> list, int groupSize) {
		
		this.list = list;
		
		this.groupSize = groupSize;
		
		this.currentPos = 0;
	}
	
	public T getNext() {
		
		if (currentPos < list.size()) {
			return list.get(currentPos);
		}
		
		return null;
	}
	
	public int getCurrentPos() {
		return currentPos;
	}
	
	public int size() {
		return list.size();
	}
	
	public T getEntry(int idx) {
		return list.get(idx);
	}

	public void currentPositionOk() {		
		currentPos++;
	}
	
	public void currentPositionBad() {
		
		if (currentPos >= list.size()) {
			return;
		}
		
		T currentPosT = list.get(currentPos);
		
		int nextPosition = currentPos + groupSize;
		
		if (nextPosition > list.size()) {
			nextPosition = list.size();
		}
		
		list.add(nextPosition, currentPosT);
		
		currentPos++;
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		for (int idx = 0; idx < list.size(); ++idx) {
			
			if (idx == currentPos) {
				sb.append("* ");
			}
			
			if (idx == currentPos + groupSize) {
				sb.append("| ");
			}
			
			sb.append(list.get(idx));
			
			if (idx != list.size() - 1) {
				sb.append(" ");
			}
		}
		
		return sb.toString();		
	}
}
