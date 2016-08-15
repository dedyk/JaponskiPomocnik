#!/bin/bash

rm -rf db-lucene

rm -f app/src/main/assets/kana.csv
rm -f app/src/main/assets/radical.csv
rm -f app/src/main/assets/transitive_intransitive_pairs.csv
rm -f app/src/main/assets/word-power.csv
rm -f app/src/main/assets/kanji_recognizer.model.db
rm -f app/src/main/assets/dictionary.zip
rm -rf release/

./gradlew clean
