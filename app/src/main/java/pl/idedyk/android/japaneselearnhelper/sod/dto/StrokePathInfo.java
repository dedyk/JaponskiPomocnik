package pl.idedyk.android.japaneselearnhelper.sod.dto;

import java.io.Serializable;
import java.util.List;

public class StrokePathInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> strokePaths;

	public List<String> getStrokePaths() {
		return strokePaths;
	}

	public void setStrokePaths(List<String> strokePaths) {
		this.strokePaths = strokePaths;
	}	
}
