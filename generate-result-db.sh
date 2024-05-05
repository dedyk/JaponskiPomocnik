#!/bin/bash

#mkdir bin-db-generator

japanese_dictionary_api_version=1.0-SNAPSHOT
japanese_dictionary_lucene_common_version=1.0-SNAPSHOT
lucene_version=4.7.2
gson_version=2.8.9

CLASSPATH=app/libs/javacsv-2.1.jar
CLASSPATH=$CLASSPATH:app/libs/JapaneseDictionaryAPI-${japanese_dictionary_api_version}.jar
CLASSPATH=$CLASSPATH:app/libs/JapaneseDictionaryLuceneCommon-${japanese_dictionary_lucene_common_version}.jar
CLASSPATH=$CLASSPATH:app/libs/lucene-analyzers-common-${lucene_version}.jar
CLASSPATH=$CLASSPATH:libs-db-generator/lucene-core-${lucene_version}.jar
CLASSPATH=$CLASSPATH:libs-db-generator/lucene-suggest-${lucene_version}.jar
CLASSPATH=$CLASSPATH:libs-db-generator/gson-${gson_version}.jar

#java -cp $CLASSPATH pl.idedyk.japanese.dictionary.lucene.LuceneDBGenerator \
#android \
#db/word.csv \
#db/sentences.csv \
#db/sentences_groups.csv \
#db/kanji.csv \
#db/radical.csv \
#db/names.csv \
#db/word2.xml \
#db-lucene

cp db/kana.csv app/src/main/assets/
cp db/radical.csv app/src/main/assets/
#cp db/transitive_intransitive_pairs.csv app/src/main/assets/
#cp db/word-power.csv app/src/main/assets/
#cp db/kanji_recognizer.model.db app/src/main/assets/

#cd db-lucene
#zip -9 ../app/src/main/assets/dictionary.zip *
cd ..
