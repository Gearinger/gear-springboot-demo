package com.gear.poi.search.pgvector.config;

import com.gear.poi.search.pgvector.word2vec.Word2Vec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * word2矢量配置
 *
 * @author guoyingdong
 * @date 2024/04/28
 */
@Slf4j
@Component
public class Word2VecConfig {

    @Bean
    public Word2Vec word2Vec() throws IOException {
        log.info("--------------------加载word2vec模型--------------------");
        Word2Vec word2Vec = new Word2Vec();
        word2Vec.loadGoogleModel2("./poi-search-pgvector/data/sgns.baidubaike.bigram-char");
        log.info("--------------------加载word2vec模型完成--------------------");
        return word2Vec;
    }

}
