package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class FindWordResult {
	
	public List<ResultItem> result;
	
	public boolean moreElemetsExists = false;
	
	public static class ResultItem {
		
		private DictionaryEntry dictionaryEntry;
		
		private GrammaFormConjugateResult grammaFormConjugateResult;
		
		private ExampleResult exampleResult;
		private ExampleGroupType exampleGroupType;
		
		private DictionaryEntry relatedDictionaryEntryById;
		
		public ResultItem(DictionaryEntry dictionaryEntry) {
			this.dictionaryEntry = dictionaryEntry;
		}
		
		public ResultItem(GrammaFormConjugateResult grammaFormConjugateResult, DictionaryEntry relatedDictionaryEntryById) {
			this.grammaFormConjugateResult = grammaFormConjugateResult;
			
			this.relatedDictionaryEntryById = relatedDictionaryEntryById;
		}

		public ResultItem(ExampleResult exampleResult, ExampleGroupType exampleGroupType, DictionaryEntry relatedDictionaryEntryById) {
			this.exampleResult = exampleResult;
			this.exampleGroupType = exampleGroupType;
			
			this.relatedDictionaryEntryById = relatedDictionaryEntryById;
		}

		public DictionaryEntry getDictionaryEntry() {
			if (dictionaryEntry != null) {
				return dictionaryEntry;
			} else if (grammaFormConjugateResult != null || exampleResult != null) {
				return relatedDictionaryEntryById;
			}
			
			throw new RuntimeException("getDictionaryEntry");
		}

		public boolean isKanjiExists() {
			
			if (dictionaryEntry != null) {
				return dictionaryEntry.isKanjiExists();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.isKanjiExists();
			} else if (exampleResult != null) {
				return exampleResult.isKanjiExists();
			}
			
			throw new RuntimeException("isKanjiExists");
		}

		public String getKanji() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getKanji();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.getKanji();
			} else if (exampleResult != null) {
				return exampleResult.getKanji();
			}
			
			throw new RuntimeException("getKanji");
		}

		public String getPrefixKana() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getPrefixKana();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.getPrefixKana();
			} else if (exampleResult != null) {
				return null;
			}
			
			throw new RuntimeException("getPrefixKana");
		}

		public List<String> getKanaList() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getKanaList();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.getKanaList();
			} else if (exampleResult != null) {
				return exampleResult.getKanaList();
			}
			
			throw new RuntimeException("getKanaList");
		}

		public String getPrefixRomaji() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getPrefixRomaji();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.getPrefixRomaji();
			} else if (exampleResult != null) {
				return null;
			}
			
			throw new RuntimeException("getPrefixRomaji");
		}

		public List<String> getRomajiList() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getRomajiList();
			} else if (grammaFormConjugateResult != null) {
				return grammaFormConjugateResult.getRomajiList();
			} else if (exampleResult != null) {
				return exampleResult.getRomajiList();
			}
			
			throw new RuntimeException("getRomajiList");
		}

		public List<String> getTranslates() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getTranslates();
			} else if (grammaFormConjugateResult != null) {
				return relatedDictionaryEntryById.getTranslates();
			} else if (exampleResult != null) {
				return relatedDictionaryEntryById.getTranslates();
			}
			
			throw new RuntimeException("getTranslates");
		}

		public String getInfo() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getInfo();
			} else if (grammaFormConjugateResult != null) {
				
				String relatedDictionaryEntryByIdInfo = relatedDictionaryEntryById.getInfo();
				
				String result = "";
				
				if (relatedDictionaryEntryByIdInfo != null && relatedDictionaryEntryByIdInfo.equals("") == false) {
					result = relatedDictionaryEntryByIdInfo + ", ";
				}
				
				result = result + grammaFormConjugateResult.getResultType().getName();
				
				return result;
			} else if (exampleResult != null) {
				String relatedDictionaryEntryByIdInfo = relatedDictionaryEntryById.getInfo();
				
				String result = "";
				
				if (relatedDictionaryEntryByIdInfo != null && relatedDictionaryEntryByIdInfo.equals("") == false) {
					result = relatedDictionaryEntryByIdInfo + ", ";
				}
				
				result = result + exampleGroupType.getName() + (exampleGroupType.getInfo() != null ? ", " + exampleGroupType.getInfo() : "");
				
				return result;
				
			}
			
			throw new RuntimeException("getInfo");
		}
	}
}
