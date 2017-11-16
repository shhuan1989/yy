package com.yijia.yy.schedule;

import com.yijia.yy.repository.DictionaryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 *
 * To keep db connection alive
 */
@Component
public class DbKeeper {

    @Inject
    private DictionaryRepository dictionaryRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void keepAlive() {
        dictionaryRepository.count();
    }
}
