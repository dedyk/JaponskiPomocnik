package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ListView;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

public class UserGroupContentsListItem {

    private UserGroupItemEntity userGroupItemEntity;

    private JMdict.Entry dictionaryEntry2;
    private DictionaryEntry dictionaryEntry;
    private KanjiCharacterInfo kanjiEntry;

    //

    private String title;

    public UserGroupContentsListItem(UserGroupItemEntity userGroupItemEntity, DictionaryEntry dictionaryEntry, JMdict.Entry dictionaryEntry2) {
        this.userGroupItemEntity = userGroupItemEntity;
        this.dictionaryEntry = dictionaryEntry;
        this.dictionaryEntry2 = dictionaryEntry2;
    }

    public UserGroupContentsListItem(UserGroupItemEntity userGroupItemEntity, KanjiCharacterInfo kanjiEntry) {
        this.userGroupItemEntity = userGroupItemEntity;
        this.kanjiEntry = kanjiEntry;
    }

    public UserGroupContentsListItem(String title) {
        this.title = title;
    }

    public ItemType getItemType() {

        if (title != null) {
            return ItemType.TITLE;

        } else {
            switch (userGroupItemEntity.getType()) {

                case DICTIONARY_ENTRY:
                    return ItemType.DICTIONARY_ENTRY;

                case KANJI_ENTRY:
                    return ItemType.KANJI_ENTRY;

                default:
                    throw new RuntimeException("" + userGroupItemEntity.getType());
            }
        }
    }

    public Spanned getText() {

        if (title != null) {
            return new SpannableString(title);

        } else {
            switch (userGroupItemEntity.getType()) {

                case DICTIONARY_ENTRY:

                    String dictionaryEntryToString = WordKanjiDictionaryUtils.getWordFullTextWithMark(dictionaryEntry);

                    return new SpannableString(dictionaryEntryToString);

                case KANJI_ENTRY:

                    String kanjiEntryToString = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(kanjiEntry);

                    return Html.fromHtml(kanjiEntryToString.replaceAll("\n", "<br/>"));

                default:
                    throw new RuntimeException("" + userGroupItemEntity.getType());
            }
        }
    }

    public UserGroupItemEntity getUserGroupItemEntity() {
        return userGroupItemEntity;
    }

    public DictionaryEntry getDictionaryEntry() {
        return dictionaryEntry;
    }

    public JMdict.Entry getDictionaryEntry2() {
        return dictionaryEntry2;
    }

    public KanjiCharacterInfo getKanjiEntry() {
        return kanjiEntry;
    }

    public static enum ItemType {

        DICTIONARY_ENTRY(R.layout.user_group_contents_simplerow, 0),

        KANJI_ENTRY(R.layout.user_group_contents_simplerow, 0),

        TITLE(R.layout.user_group_contents_title_simplerow, 1);

        private int layoutResourceId;

        private int viewTypeId;

        // ten sam layoutResourceId musi sie rownac tego samemu viewTypeId !!!
        ItemType(int layoutResourceId, int viewTypeId) {
            this.layoutResourceId = layoutResourceId;
            this.viewTypeId = viewTypeId;
        }

        public int getViewTypeId() {
            return viewTypeId;
        }

        public static int getLayoutResourceId(int itemViewTypeId) {

            ItemType[] values = values();

            for (ItemType itemType : values) {

                if (itemType.getViewTypeId() == itemViewTypeId) {
                    return itemType.layoutResourceId;
                }
            }

            throw new RuntimeException("Unknown itemViewTypeId: " + itemViewTypeId);
        }
    }
}
