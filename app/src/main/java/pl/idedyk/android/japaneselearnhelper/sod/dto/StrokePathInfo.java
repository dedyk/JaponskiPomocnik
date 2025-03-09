package pl.idedyk.android.japaneselearnhelper.sod.dto;

import java.io.Serializable;
import java.util.List;

import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;

public class StrokePathInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<List<String>> strokePaths;

	public List<List<String>> getStrokePaths() {
		return strokePaths;
	}

	public void setStrokePaths(List<List<String>> strokePaths) {
		this.strokePaths = strokePaths;
	}	
}
