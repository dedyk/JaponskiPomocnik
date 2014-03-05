package pl.idedyk.android.japaneselearnhelper.dictionary.lucene3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import pl.idedyk.japanese.dictionary.api.dictionary.IDatabaseConnector;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dictionary.lucene.LuceneStatic;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

public class Lucene3Database implements IDatabaseConnector {

	private String dbDir;

	private Directory index;
	//private SimpleAnalyzer analyzer;
	private IndexReader reader;
	private IndexSearcher searcher;

	public Lucene3Database(String dbDir) {
		this.dbDir = dbDir;		
	}

	public void open() throws IOException {

		index = FSDirectory.open(new File(dbDir));
		//analyzer = new SimpleAnalyzer(Version.LUCENE_47);
		reader = IndexReader.open(index);
		searcher = new IndexSearcher(reader);
	}

	public void close() throws IOException {		
		reader.close();

		index.close();
	}

	@Override
	public FindWordResult findDictionaryEntries(FindWordRequest findWordRequest) throws DictionaryException {

		FindWordResult findWordResult = new FindWordResult();
		findWordResult.result = new ArrayList<FindWordResult.ResultItem>();

		final int maxResult = 50;

		try {
			if (findWordRequest.wordPlaceSearch != WordPlaceSearch.ANY_PLACE) {

				BooleanQuery query = new BooleanQuery();

				// object type
				PhraseQuery phraseQuery = new PhraseQuery();
				phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

				query.add(phraseQuery, Occur.MUST);

				BooleanQuery wordBooleanQuery = new BooleanQuery();

				if (findWordRequest.searchKanji == true) {
					wordBooleanQuery.add(createQuery(findWordRequest.word, LuceneStatic.dictionaryEntry_kanji, findWordRequest.wordPlaceSearch), Occur.SHOULD);				
				}

				if (findWordRequest.searchKana == true) {
					wordBooleanQuery.add(createQuery(findWordRequest.word, LuceneStatic.dictionaryEntry_kanaList, findWordRequest.wordPlaceSearch), Occur.SHOULD);				
				}

				if (findWordRequest.searchRomaji == true) {
					wordBooleanQuery.add(createQuery(findWordRequest.word, LuceneStatic.dictionaryEntry_romajiList, findWordRequest.wordPlaceSearch), Occur.SHOULD);				
				}

				if (findWordRequest.searchTranslate == true) {
					wordBooleanQuery.add(createQuery(findWordRequest.word, LuceneStatic.dictionaryEntry_translatesList, findWordRequest.wordPlaceSearch), Occur.SHOULD);

					String wordWithoutPolishChars = Utils.removePolishChars(findWordRequest.word);

					wordBooleanQuery.add(createQuery(wordWithoutPolishChars, LuceneStatic.dictionaryEntry_translatesListWithoutPolishChars, 
							findWordRequest.wordPlaceSearch), Occur.SHOULD);
				}

				if (findWordRequest.searchInfo == true) {
					wordBooleanQuery.add(createQuery(findWordRequest.word, LuceneStatic.dictionaryEntry_info, findWordRequest.wordPlaceSearch), Occur.SHOULD);

					String wordWithoutPolishChars = Utils.removePolishChars(findWordRequest.word);

					wordBooleanQuery.add(createQuery(wordWithoutPolishChars, LuceneStatic.dictionaryEntry_infoWithoutPolishChars, 
							findWordRequest.wordPlaceSearch), Occur.SHOULD);
				}

				createDictionaryEntryListFilter(wordBooleanQuery, findWordRequest.dictionaryEntryList);			

				query.add(wordBooleanQuery, Occur.MUST);

				ScoreDoc[] scoreDocs = searcher.search(query, null, maxResult).scoreDocs;

				for (ScoreDoc scoreDoc : scoreDocs) {

					Document foundDocument = searcher.doc(scoreDoc.doc);

					String idString = foundDocument.get(LuceneStatic.dictionaryEntry_id);

					List<String> dictionaryEntryTypeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList));
					List<String> attributeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_attributeList));
					List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_groupsList));

					String prefixKanaString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixKana);

					String kanjiString = foundDocument.get(LuceneStatic.dictionaryEntry_kanji);
					List<String> kanaList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_kanaList));

					String prefixRomajiString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixRomaji);

					List<String> romajiList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_romajiList));				
					List<String> translateList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_translatesList));

					String infoString = foundDocument.get(LuceneStatic.dictionaryEntry_info);

					DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeList, attributeList,
							groupsList, prefixKanaString, kanjiString, kanaList, prefixRomajiString, romajiList,
							translateList, infoString);

					findWordResult.result.add(new FindWordResult.ResultItem(entry));
				}

			} else {

				for (int docId = 0; docId < reader.maxDoc(); docId++) {

					Document document = reader.document(docId);

					String objectType = document.get(LuceneStatic.objectType);

					if (objectType.equals(LuceneStatic.dictionaryEntry_objectType) == false) {
						continue;
					}

					boolean addDictionaryEntry = false;

					String idString = document.get(LuceneStatic.dictionaryEntry_id);

					List<String> dictionaryEntryTypeList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList));
					List<String> attributeList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_attributeList));
					List<String> groupsList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_groupsList));

					String prefixKanaString = document.get(LuceneStatic.dictionaryEntry_prefixKana);

					String kanjiString = document.get(LuceneStatic.dictionaryEntry_kanji);
					List<String> kanaList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_kanaList));

					String prefixRomajiString = document.get(LuceneStatic.dictionaryEntry_prefixRomaji);

					List<String> romajiList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_romajiList));				
					List<String> translateList = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_translatesList));

					String infoString = document.get(LuceneStatic.dictionaryEntry_info);

					if (findWordRequest.wordPlaceSearch == WordPlaceSearch.ANY_PLACE) {

						if (findWordRequest.searchKanji == true) {

							if (kanjiString.indexOf(findWordRequest.word) != -1) {
								addDictionaryEntry = true;
							}
						}

						if (addDictionaryEntry == false && findWordRequest.searchKana == true) {

							for (String currentKana : kanaList) {							
								if (currentKana.indexOf(findWordRequest.word) != -1) {
									addDictionaryEntry = true;

									break;
								}							
							}						
						}

						if (addDictionaryEntry == false && findWordRequest.searchRomaji == true) {

							for (String currentRomaji : romajiList) {
								if (currentRomaji.indexOf(findWordRequest.word) != -1) {
									addDictionaryEntry = true;

									break;
								}							
							}
						}

						if (addDictionaryEntry == false && findWordRequest.searchTranslate == true) {

							String findWordWordLowerCase = findWordRequest.word.toLowerCase();

							for (String currentTranslate : translateList) {

								if (currentTranslate.toLowerCase().indexOf(findWordWordLowerCase) != -1) {
									addDictionaryEntry = true;

									break;
								}							
							}						

							List<String> translateListWithoutPolishChars = Arrays.asList(document.getValues(LuceneStatic.dictionaryEntry_translatesListWithoutPolishChars));

							if (translateListWithoutPolishChars != null && translateListWithoutPolishChars.size() > 0) {

								String findWordWordLowerCaseWithoutPolishChars = Utils.removePolishChars(findWordWordLowerCase);

								for (String currentTranslateListWithoutPolishChars : translateListWithoutPolishChars) {

									if (currentTranslateListWithoutPolishChars.toLowerCase().indexOf(findWordWordLowerCaseWithoutPolishChars) != -1) {
										addDictionaryEntry = true;

										break;
									}
								}
							}						
						}

						if (addDictionaryEntry == false && findWordRequest.searchInfo == true) {

							String findWordWordLowerCase = findWordRequest.word.toLowerCase();

							if (infoString != null) {

								if (infoString.toLowerCase().indexOf(findWordWordLowerCase) != -1) {
									addDictionaryEntry = true;
								}

								if (addDictionaryEntry == false) {								
									String infoStringWithoutPolishChars = document.get(LuceneStatic.dictionaryEntry_infoWithoutPolishChars);

									if (infoStringWithoutPolishChars != null) {
										String findWordWordLowerCaseWithoutPolishChars = Utils.removePolishChars(findWordWordLowerCase);

										if (infoStringWithoutPolishChars.toLowerCase().indexOf(findWordWordLowerCaseWithoutPolishChars) != -1) {
											addDictionaryEntry = true;
										}									
									}
								}
							}
						}

						if (addDictionaryEntry == true) {
							DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeList, attributeList,
									groupsList, prefixKanaString, kanjiString, kanaList, prefixRomajiString, romajiList,
									translateList, infoString);

							findWordResult.result.add(new FindWordResult.ResultItem(entry));

							if (findWordResult.result.size() > maxResult) {
								break;
							}
						}
					}
				}
			}		

			if (findWordResult.result.size() > maxResult) {

				findWordResult.moreElemetsExists = true;

				findWordResult.result.remove(findWordResult.result.size() - 1);
			}

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania słówek: " + e);
		}

		return findWordResult;
	}
	
	@Override
	public DictionaryEntry getDictionaryEntryById(String id) throws DictionaryException {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		query.add(NumericRangeQuery.newIntRange(LuceneStatic.dictionaryEntry_id, Integer.parseInt(id), Integer.parseInt(id), true, true), Occur.MUST);
		
		try {
			ScoreDoc[] scoreDocs = searcher.search(query, null, 1).scoreDocs;
			
			if (scoreDocs.length == 0) {
				return null;
			}
			
			Document foundDocument = searcher.doc(scoreDocs[0].doc);

			String idString = foundDocument.get(LuceneStatic.dictionaryEntry_id);

			List<String> dictionaryEntryTypeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList));
			List<String> attributeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_attributeList));
			List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_groupsList));

			String prefixKanaString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixKana);

			String kanjiString = foundDocument.get(LuceneStatic.dictionaryEntry_kanji);
			List<String> kanaList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_kanaList));

			String prefixRomajiString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixRomaji);

			List<String> romajiList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_romajiList));				
			List<String> translateList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_translatesList));

			String infoString = foundDocument.get(LuceneStatic.dictionaryEntry_info);

			return Utils.parseDictionaryEntry(idString, dictionaryEntryTypeList, attributeList,
					groupsList, prefixKanaString, kanjiString, kanaList, prefixRomajiString, romajiList,
					translateList, infoString);
			
		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas pobierania słowa: " + e);
		}		
	}
	
	@Override
	public DictionaryEntry getNthDictionaryEntry(int nth) throws DictionaryException {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

		query.add(phraseQuery, Occur.MUST);
		
		try {
			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;
			
			if (nth < 0 || nth >= scoreDocs.length) {
				return null;
			}
			
			Document foundDocument = searcher.doc(scoreDocs[nth].doc);

			String idString = foundDocument.get(LuceneStatic.dictionaryEntry_id);

			List<String> dictionaryEntryTypeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList));
			List<String> attributeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_attributeList));
			List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_groupsList));

			String prefixKanaString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixKana);

			String kanjiString = foundDocument.get(LuceneStatic.dictionaryEntry_kanji);
			List<String> kanaList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_kanaList));

			String prefixRomajiString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixRomaji);

			List<String> romajiList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_romajiList));				
			List<String> translateList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_translatesList));

			String infoString = foundDocument.get(LuceneStatic.dictionaryEntry_info);

			return Utils.parseDictionaryEntry(idString, dictionaryEntryTypeList, attributeList,
					groupsList, prefixKanaString, kanjiString, kanaList, prefixRomajiString, romajiList,
					translateList, infoString);
			
		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas pobierania n-tego słowa: " + e);
		}				
	}

	private Query createQuery(String word, String fieldName, WordPlaceSearch wordPlaceSearch) {

		Query query = null;

		if (wordPlaceSearch == WordPlaceSearch.START_WITH) {
			query = new PrefixQuery(new Term(fieldName, word));

		} else if (wordPlaceSearch == WordPlaceSearch.EXACT) {
			query = new TermQuery(new Term(fieldName, word));

		} else {
			throw new RuntimeException();
		}
		
		return query;
	}

	private Query createQuery(String word, String fieldName, FindKanjiRequest.WordPlaceSearch wordPlaceSearch) {

		Query query = null;

		if (wordPlaceSearch == FindKanjiRequest.WordPlaceSearch.START_WITH) {
			query = new PrefixQuery(new Term(fieldName, word));

		} else if (wordPlaceSearch == FindKanjiRequest.WordPlaceSearch.EXACT) {
			query = new TermQuery(new Term(fieldName, word));

		} else {
			throw new RuntimeException();
		}

		return query;
	}

	private void createDictionaryEntryListFilter(BooleanQuery wordBooleanQuery, List<DictionaryEntryType> dictionaryEntryList) {

		if (dictionaryEntryList != null && dictionaryEntryList.size() > 0) {

			DictionaryEntryType[] allDictionaryEntryTypes = DictionaryEntryType.values();

			List<DictionaryEntryType> mustNotDictionaryEntryList = new ArrayList<DictionaryEntryType>();

			for (DictionaryEntryType currentDictionaryEntry : allDictionaryEntryTypes) {
				if (dictionaryEntryList.contains(currentDictionaryEntry) == false) {
					mustNotDictionaryEntryList.add(currentDictionaryEntry);
				}
			}				

			List<String> mustNotDictionaryEntryStringList = DictionaryEntryType.convertToValues(mustNotDictionaryEntryList);

			for (String currentMustNotDictionaryEntryStringList : mustNotDictionaryEntryStringList) {
				wordBooleanQuery.add(createQuery(currentMustNotDictionaryEntryStringList, LuceneStatic.dictionaryEntry_dictionaryEntryTypeList, WordPlaceSearch.EXACT), Occur.MUST_NOT);
			}
		}	
	}
	
	@Override
	public int getDictionaryEntriesSize() {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		try {
			return searcher.search(query, null, Integer.MAX_VALUE).scoreDocs.length;			
			
		} catch (IOException e) {
			throw new RuntimeException("Błąd podczas pobierania liczby słówek: " + e);
		}		
	}
		
	@Override
	public List<GroupEnum> getDictionaryEntryGroupTypes() {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

		query.add(phraseQuery, Occur.MUST);
		
		Set<String> uniqueGroupStringTypes = new HashSet<String>();

		try {			
			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;
						
			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				uniqueGroupStringTypes.addAll(Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_groupsList)));
			}

			List<GroupEnum> result = GroupEnum.convertToListGroupEnum(new ArrayList<String>(uniqueGroupStringTypes));
			
			GroupEnum.sortGroups(result);
			
			return result;
			
		} catch (IOException e) {
			throw new RuntimeException("Błąd podczas pobierania typów grup słówek: " + e);
		}		
	}

	@Override
	public List<DictionaryEntry> getGroupDictionaryEntries(GroupEnum groupEnum) throws DictionaryException {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType));

		query.add(phraseQuery, Occur.MUST);
		
		query.add(createQuery(groupEnum.getValue(), LuceneStatic.dictionaryEntry_groupsList, WordPlaceSearch.EXACT), Occur.MUST);
		
		try {
			List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
			
			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;

			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				String idString = foundDocument.get(LuceneStatic.dictionaryEntry_id);

				List<String> dictionaryEntryTypeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList));
				List<String> attributeList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_attributeList));
				List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_groupsList));

				String prefixKanaString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixKana);

				String kanjiString = foundDocument.get(LuceneStatic.dictionaryEntry_kanji);
				List<String> kanaList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_kanaList));

				String prefixRomajiString = foundDocument.get(LuceneStatic.dictionaryEntry_prefixRomaji);

				List<String> romajiList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_romajiList));				
				List<String> translateList = Arrays.asList(foundDocument.getValues(LuceneStatic.dictionaryEntry_translatesList));

				String infoString = foundDocument.get(LuceneStatic.dictionaryEntry_info);

				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeList, attributeList,
						groupsList, prefixKanaString, kanjiString, kanaList, prefixRomajiString, romajiList,
						translateList, infoString);

				result.add(entry);
			}
			
			return result;
			
		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas pobierania słówek dla grupy: " + e);
		}		
	}

	@Override
	public FindKanjiResult findKanji(FindKanjiRequest findKanjiRequest) throws DictionaryException {

		FindKanjiResult findKanjiResult = new FindKanjiResult();
		findKanjiResult.result = new ArrayList<KanjiEntry>();

		final int maxResult = 50;

		try {
			if (findKanjiRequest.wordPlaceSearch != FindKanjiRequest.WordPlaceSearch.ANY_PLACE) {

				BooleanQuery query = new BooleanQuery();

				// object type
				PhraseQuery phraseQuery = new PhraseQuery();
				phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

				query.add(phraseQuery, Occur.MUST);

				BooleanQuery kanjiBooleanQuery = new BooleanQuery();

				// kanji
				kanjiBooleanQuery.add(createQuery(findKanjiRequest.word, LuceneStatic.kanjiEntry_kanji, findKanjiRequest.wordPlaceSearch), Occur.SHOULD);				

				// translate
				kanjiBooleanQuery.add(createQuery(findKanjiRequest.word, LuceneStatic.kanjiEntry_polishTranslatesList, findKanjiRequest.wordPlaceSearch), Occur.SHOULD);

				String wordWithoutPolishChars = Utils.removePolishChars(findKanjiRequest.word);

				kanjiBooleanQuery.add(createQuery(wordWithoutPolishChars, LuceneStatic.kanjiEntry_polishTranslatesListWithoutPolishChars, 
						findKanjiRequest.wordPlaceSearch), Occur.SHOULD);

				// info
				kanjiBooleanQuery.add(createQuery(findKanjiRequest.word, LuceneStatic.kanjiEntry_info, findKanjiRequest.wordPlaceSearch), Occur.SHOULD);

				kanjiBooleanQuery.add(createQuery(wordWithoutPolishChars, LuceneStatic.kanjiEntry_infoWithoutPolishChars, 
						findKanjiRequest.wordPlaceSearch), Occur.SHOULD);

				query.add(kanjiBooleanQuery, Occur.MUST);

				ScoreDoc[] scoreDocs = searcher.search(query, null, maxResult).scoreDocs;

				for (ScoreDoc scoreDoc : scoreDocs) {

					Document foundDocument = searcher.doc(scoreDoc.doc);

					String idString = foundDocument.get(LuceneStatic.kanjiEntry_id);

					String kanjiString = foundDocument.get(LuceneStatic.kanjiEntry_kanji);

					String strokeCountString = foundDocument.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);

					List<String> strokePathsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));

					String generated = foundDocument.get(LuceneStatic.kanjiEntry_generated);

					List<String> radicalsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

					List<String> onReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
					List<String> kunReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

					List<String> polishTranslateList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

					List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_groupsList));

					String infoString = foundDocument.get(LuceneStatic.kanjiEntry_info);

					KanjiEntry kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
							onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
							groupsList);

					findKanjiResult.result.add(kanjiEntry);
				}				

			} else {

				for (int docId = 0; docId < reader.maxDoc(); docId++) {

					Document document = reader.document(docId);

					String objectType = document.get(LuceneStatic.objectType);

					if (objectType.equals(LuceneStatic.kanjiEntry_objectType) == false) {
						continue;
					}

					String idString = document.get(LuceneStatic.kanjiEntry_id);

					String kanjiString = document.get(LuceneStatic.kanjiEntry_kanji);

					String strokeCountString = document.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);

					List<String> strokePathsList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));

					String generated = document.get(LuceneStatic.kanjiEntry_generated);

					List<String> radicalsList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

					List<String> onReadingList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
					List<String> kunReadingList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

					List<String> polishTranslateList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

					List<String> groupsList = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_groupsList));

					String infoString = document.get(LuceneStatic.kanjiEntry_info);

					boolean addDictionaryEntry = false;

					// kanji
					if (kanjiString.indexOf(findKanjiRequest.word) != -1) {
						addDictionaryEntry = true;
					}

					// translate
					if (addDictionaryEntry == false) {

						String findKanjiWordLowerCase = findKanjiRequest.word.toLowerCase();

						for (String currentPolishTranslate : polishTranslateList) {

							if (currentPolishTranslate.toLowerCase().indexOf(findKanjiWordLowerCase) != -1) {
								addDictionaryEntry = true;

								break;
							}							
						}						

						List<String> polishTranslateListWithoutPolishChars = Arrays.asList(document.getValues(LuceneStatic.kanjiEntry_polishTranslatesListWithoutPolishChars));

						if (polishTranslateListWithoutPolishChars != null && polishTranslateListWithoutPolishChars.size() > 0) {

							String findKanjiWordLowerCaseWithoutPolishChars = Utils.removePolishChars(findKanjiWordLowerCase);

							for (String currentPolishTranslateListWithoutPolishChars : polishTranslateListWithoutPolishChars) {

								if (currentPolishTranslateListWithoutPolishChars.toLowerCase().indexOf(findKanjiWordLowerCaseWithoutPolishChars) != -1) {
									addDictionaryEntry = true;

									break;
								}
							}
						}						
					}					

					// info
					if (addDictionaryEntry == false) {

						String findKanjiWordLowerCase = findKanjiRequest.word.toLowerCase();

						if (infoString != null) {

							if (infoString.toLowerCase().indexOf(findKanjiWordLowerCase) != -1) {
								addDictionaryEntry = true;
							}

							if (addDictionaryEntry == false) {								
								String infoStringWithoutPolishChars = document.get(LuceneStatic.kanjiEntry_infoWithoutPolishChars);

								if (infoStringWithoutPolishChars != null) {
									String findKanjiWordLowerCaseWithoutPolishChars = Utils.removePolishChars(findKanjiWordLowerCase);

									if (infoStringWithoutPolishChars.toLowerCase().indexOf(findKanjiWordLowerCaseWithoutPolishChars) != -1) {
										addDictionaryEntry = true;
									}									
								}
							}
						}
					}

					if (addDictionaryEntry == true) {
						KanjiEntry kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
								onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
								groupsList);

						findKanjiResult.result.add(kanjiEntry);

						if (findKanjiResult.result.size() > maxResult) {
							break;
						}
					}					
				}				
			}

			if (findKanjiResult.result.size() > maxResult) {

				findKanjiResult.moreElemetsExists = true;

				findKanjiResult.result.remove(findKanjiResult.result.size() - 1);
			}

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania słówek: " + e);
		}

		return findKanjiResult;
	}

	@Override
	public Set<String> findAllAvailableRadicals(String[] radicals) throws DictionaryException {

		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		if (radicals.length > 0) {
			BooleanQuery kanjiBooleanQuery = new BooleanQuery();
			

			for (String currentRadical : radicals) {
				kanjiBooleanQuery.add(createQuery(currentRadical, LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList, FindKanjiRequest.WordPlaceSearch.EXACT), Occur.MUST);
			}

			query.add(kanjiBooleanQuery, Occur.MUST);			
		}		

		Set<String> result = new HashSet<String>();

		try {
			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;

			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				result.addAll(Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList)));
			}

			return result;

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania wszystkich dostępnych znaków podstawowych: " + e);
		}
	}

	@Override
	public void findDictionaryEntriesInGrammaFormAndExamples(FindWordRequest findWordRequest, FindWordResult findWordResult)
			throws DictionaryException {

		if (findWordRequest.searchGrammaFormAndExamples == false) {
			return;
		}

		if (findWordResult.moreElemetsExists == true) {
			return;
		}

		throw new UnsupportedOperationException();
	}

	@Override
	public List<KanjiEntry> findKanjiFromRadicals(String[] radicals) throws DictionaryException {

		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		BooleanQuery kanjiBooleanQuery = new BooleanQuery();

		for (String currentRadical : radicals) {
			kanjiBooleanQuery.add(createQuery(currentRadical, LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList, FindKanjiRequest.WordPlaceSearch.EXACT), Occur.MUST);
		}

		query.add(kanjiBooleanQuery, Occur.MUST);

		List<KanjiEntry> result = new ArrayList<KanjiEntry>();

		try {
			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;

			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				String idString = foundDocument.get(LuceneStatic.kanjiEntry_id);

				String kanjiString = foundDocument.get(LuceneStatic.kanjiEntry_kanji);

				String strokeCountString = foundDocument.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);

				List<String> strokePathsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));

				String generated = foundDocument.get(LuceneStatic.kanjiEntry_generated);

				List<String> radicalsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

				List<String> onReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
				List<String> kunReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

				List<String> polishTranslateList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

				List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_groupsList));

				String infoString = foundDocument.get(LuceneStatic.kanjiEntry_info);

				KanjiEntry kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
						onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
						groupsList);

				result.add(kanjiEntry);

			}

			return result;

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania wszystkich znaków kanji po znaków podstawowych: " + e);
		}		
	}

	@Override
	public FindKanjiResult findKanjisFromStrokeCount(int from, int to) throws DictionaryException {

		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		query.add(NumericRangeQuery.newIntRange(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount, from, to, true, true), Occur.MUST);

		final int maxResult = 201;

		try {

			ScoreDoc[] scoreDocs = searcher.search(query, null, maxResult).scoreDocs;

			List<KanjiEntry> result = new ArrayList<KanjiEntry>();

			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				String idString = foundDocument.get(LuceneStatic.kanjiEntry_id);

				String kanjiString = foundDocument.get(LuceneStatic.kanjiEntry_kanji);

				String strokeCountString = foundDocument.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);

				List<String> strokePathsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));

				String generated = foundDocument.get(LuceneStatic.kanjiEntry_generated);

				List<String> radicalsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

				List<String> onReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
				List<String> kunReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

				List<String> polishTranslateList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

				List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_groupsList));

				String infoString = foundDocument.get(LuceneStatic.kanjiEntry_info);

				KanjiEntry kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
						onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
						groupsList);

				result.add(kanjiEntry);
			}				

			FindKanjiResult findKanjiResult = new FindKanjiResult();

			if (result.size() >= maxResult) {
				result.remove(result.size() - 1);

				findKanjiResult.setMoreElemetsExists(true);
			}

			findKanjiResult.setResult(result);

			return findKanjiResult;

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania wszystkich znaków kanji po ilościach kresek: " + e);
		}
	}
	
	@Override
	public KanjiEntry getKanjiEntry(String kanji) throws DictionaryException {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		query.add(createQuery(kanji, LuceneStatic.kanjiEntry_kanji, FindKanjiRequest.WordPlaceSearch.EXACT), Occur.MUST);

		try {
			ScoreDoc[] scoreDocs = searcher.search(query, null, 1).scoreDocs;
			
			if (scoreDocs.length == 0) {
				return null;
			}
			
			Document foundDocument = searcher.doc(scoreDocs[0].doc);

			String idString = foundDocument.get(LuceneStatic.kanjiEntry_id);

			String kanjiString = foundDocument.get(LuceneStatic.kanjiEntry_kanji);

			String strokeCountString = foundDocument.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);

			List<String> strokePathsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));

			String generated = foundDocument.get(LuceneStatic.kanjiEntry_generated);

			List<String> radicalsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

			List<String> onReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
			List<String> kunReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

			List<String> polishTranslateList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

			List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_groupsList));

			String infoString = foundDocument.get(LuceneStatic.kanjiEntry_info);

			return Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
					onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
					groupsList);
			
		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas pobierania znaku kanji: " + e);
		}		
	}

	@Override
	public List<KanjiEntry> getAllKanjis(boolean withDetails, boolean addGenerated) throws DictionaryException {
		
		BooleanQuery query = new BooleanQuery();

		// object type
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType));

		query.add(phraseQuery, Occur.MUST);

		if (addGenerated == false) {
			query.add(createQuery("false", LuceneStatic.kanjiEntry_generated, FindKanjiRequest.WordPlaceSearch.EXACT), Occur.MUST);
		}

		try {

			ScoreDoc[] scoreDocs = searcher.search(query, null, Integer.MAX_VALUE).scoreDocs;

			List<KanjiEntry> result = new ArrayList<KanjiEntry>();

			for (ScoreDoc scoreDoc : scoreDocs) {

				Document foundDocument = searcher.doc(scoreDoc.doc);

				String idString = foundDocument.get(LuceneStatic.kanjiEntry_id);

				String kanjiString = foundDocument.get(LuceneStatic.kanjiEntry_kanji);				

				String generated = foundDocument.get(LuceneStatic.kanjiEntry_generated);

				String strokeCountString = null;
				
				List<String> radicalsList = null;
				List<String> onReadingList = null;
				List<String> kunReadingList = null;
				List<String> strokePathsList = null;
				
				if (withDetails == true) {
					strokeCountString = foundDocument.get(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount);
					
					radicalsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList));

					onReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList));
					kunReadingList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList));

					strokePathsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths));
				}				

				List<String> polishTranslateList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_polishTranslatesList));

				List<String> groupsList = Arrays.asList(foundDocument.getValues(LuceneStatic.kanjiEntry_groupsList));

				String infoString = foundDocument.get(LuceneStatic.kanjiEntry_info);

				KanjiEntry kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, radicalsList,
						onReadingList, kunReadingList, strokePathsList, polishTranslateList, infoString, generated,
						groupsList);

				result.add(kanjiEntry);
			}				

			return result;

		} catch (IOException e) {
			throw new DictionaryException("Błąd podczas wyszukiwania wszystkich znaków kanji po ilościach kresek: " + e);
		}		
	}
}
