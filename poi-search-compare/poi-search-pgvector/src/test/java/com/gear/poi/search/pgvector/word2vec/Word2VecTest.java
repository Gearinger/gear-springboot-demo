package com.gear.poi.search.pgvector.word2vec;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class Word2VecTest {

    @Test
    void loadGoogleModel2() {
        Word2Vec word2Vec = new Word2Vec();
        try {
            word2Vec.loadGoogleModel2("data/sgns.baidubaike.bigram-char");

            float[] wordVector1 = word2Vec.getWordVector("中国");
            System.out.println(Arrays.toString(wordVector1));
            System.out.println(wordVector1.length);

            float[] wordVector2 = word2Vec.getWordVector("苹果");
            System.out.println(Arrays.toString(wordVector2));
            System.out.println(wordVector2.length);

            float[] wordVector3 = word2Vec.getWordVector("飞机");
            System.out.println(Arrays.toString(wordVector3));
            System.out.println(wordVector3.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getWordVector() {
    }
}