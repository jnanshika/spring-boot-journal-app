package com.pm.journalapp.cache;

import com.pm.journalapp.entity.ConfigJournalAppEntity;
import com.pm.journalapp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        weather_api;
    }

    public Map<String, String> appCache;

    private final ConfigJournalAppRepository configJournalAppRepository;
    public AppCache(ConfigJournalAppRepository configJournalAppRepository){
        this.configJournalAppRepository = configJournalAppRepository;
    }

    @PostConstruct
    public void init(){
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> allConfig = configJournalAppRepository.findAll();
        for( ConfigJournalAppEntity configJournalAppEntity : allConfig ){
            appCache.put( configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }
}
