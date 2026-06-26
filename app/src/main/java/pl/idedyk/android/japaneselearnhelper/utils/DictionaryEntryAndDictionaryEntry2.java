package pl.idedyk.android.japaneselearnhelper.utils;

import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;

public class DictionaryEntryAndDictionaryEntry2 {
    private DictionaryEntry dictionaryEntry;
    private JMdict.Entry dictionaryEntry2;

    public DictionaryEntryAndDictionaryEntry2(DictionaryEntry dictionaryEntry, JMdict.Entry dictionaryEntry2) {
        this.dictionaryEntry = dictionaryEntry;
        this.dictionaryEntry2 = dictionaryEntry2;
    }

    public DictionaryEntry getDictionaryEntry() {
        return dictionaryEntry;
    }

    public void setDictionaryEntry(DictionaryEntry dictionaryEntry) {
        this.dictionaryEntry = dictionaryEntry;
    }

    public JMdict.Entry getDictionaryEntry2() {
        return dictionaryEntry2;
    }

    public void setDictionaryEntry2(JMdict.Entry dictionaryEntry2) {
        this.dictionaryEntry2 = dictionaryEntry2;
    }
}
