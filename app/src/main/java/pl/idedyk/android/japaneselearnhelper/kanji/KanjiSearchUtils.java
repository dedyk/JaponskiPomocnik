package pl.idedyk.android.japaneselearnhelper.kanji;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;

public class KanjiSearchUtils {

    public static void generateKanjiSearchGeneralResult(final Activity activity, List<KanjiEntry> kanjiList, List<IScreenItem> screenItemList, boolean addKanjiStrokeCountTitle) {

        final int maxElementsInTableRow = 7;

        // glowny layout
        TableLayout tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, null, true);

        screenItemList.add(tableLayout);

        TableRow tableRow = null;

        String lastStrokeCount = null;

        for (final KanjiEntry currentKanjiEntry : kanjiList) {

            if (tableRow == null) {
                tableRow = new TableRow();

                tableLayout.addTableRow(tableRow);
            }

            String currentKanjiEntryStrokeCount = currentKanjiEntry.getKanjiDic2Entry() != null ?
                    String.valueOf(currentKanjiEntry.getKanjiDic2Entry().getStrokeCount()) : "-";

            // dodajemy pole z liczba kresek
            if (addKanjiStrokeCountTitle == true) {
                if (lastStrokeCount == null || lastStrokeCount.equals(currentKanjiEntryStrokeCount) == false) {

                    lastStrokeCount = currentKanjiEntryStrokeCount;

                    StringValue strokeCountTitle = new StringValue(" " + currentKanjiEntryStrokeCount + " ", 30.0f, 0);

                    strokeCountTitle.setNullMargins(true);
                    strokeCountTitle.setGravity(Gravity.CENTER);
                    strokeCountTitle.setBackgroundColor(JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getTitleItemBackgroundColorAsColor());

                    tableRow.addScreenItem(strokeCountTitle);
                }
            }

            // czy nowy wiersz
            if (tableRow.getScreenItemSize() >= maxElementsInTableRow) {

                tableRow = new TableRow();

                tableLayout.addTableRow(tableRow);
            }

            // dodajemy znak
            StringValue kanjiValue = new StringValue(currentKanjiEntry.getKanji(), 25.0f, 0);

            kanjiValue.setNullMargins(true);
            kanjiValue.setGravity(Gravity.CENTER);

            // dodanie czynnosci
            kanjiValue.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    Intent intent = new Intent(activity.getApplicationContext(), KanjiDetails.class);

                    intent.putExtra("item", currentKanjiEntry);

                    activity.startActivity(intent);
                }
            });

            tableRow.addScreenItem(kanjiValue);

            // czy nowy wiersz
            if (tableRow.getScreenItemSize() >= maxElementsInTableRow) {

                tableRow = new TableRow();

                tableLayout.addTableRow(tableRow);
            }
        }
    }
}
