package com.gear.poi.search.pgvector.word2vec;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.gear.poi.search.pgvector.word2vec.util.DataInputStreamUtil.readString;

public class Word2Vec {

    HashMap<String, float[]> wordMap = new HashMap<>();

    public void loadGoogleModel2(String path) throws IOException {
        DataInputStream dis = null;
        BufferedInputStream bis = null;
        double len = 0;
        float vector = 0;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            dis = new DataInputStream(bis);
            int words;
            int size;

            // //读取词数
            words = Integer.parseInt(readString(dis));
            // //大小
            size = Integer.parseInt(readString(dis));
            float[] vectors = null;
            for (int i = 0; i < words; i++) {
                String[] lineSplit = dis.readLine().split(" ");
                String word = new String(lineSplit[0].getBytes(StandardCharsets.ISO_8859_1), "utf8");
//				word = readString(dis);
                vectors = new float[size];
                len = 0;
                for (int j = 0; j < size; j++) {
//					vector = readFloat(dis);
//					double readDouble = dis.readDouble();
                    vector = Float.parseFloat(lineSplit[j + 1]);
                    len += vector * vector;
                    vectors[j] = (float) vector;
                }
                len = Math.sqrt(len);

                for (int j = 0; j < size; j++) {
                    vectors[j] /= len;
                }

                wordMap.put(word, vectors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (dis != null) {
                dis.close();
            }
        }
    }

    /**
     * 得到词向量
     *
     * @param word
     * @return
     */
    public float[] getWordVector(String word) {
        return wordMap.get(word);
    }

    /**
     * 加载模型
     *
     * @param path 模型的路径
     * @throws IOException
     */
    public void loadJavaModel(String path) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            int words;
            int size;
            words = dis.readInt();
            size = dis.readInt();

            float vector = 0;

            String key = null;
            float[] value = null;
            for (int i = 0; i < words; i++) {
                double len = 0;
                key = dis.readUTF();
                value = new float[size];
                for (int j = 0; j < size; j++) {
                    vector = dis.readFloat();
                    len += vector * vector;
                    value[j] = vector;
                }

                len = Math.sqrt(len);

                for (int j = 0; j < size; j++) {
                    value[j] /= len;
                }
                wordMap.put(key, value);
            }
        }
    }
}
