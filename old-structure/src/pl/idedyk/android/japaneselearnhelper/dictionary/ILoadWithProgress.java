package pl.idedyk.android.japaneselearnhelper.dictionary;

public interface ILoadWithProgress {
	
	public void setMaxValue(int maxValue);
	
	public void setCurrentPos(int currentPos);
	
	public void setDescription(String desc);
	
	public void setError(String errorMessage);
}
