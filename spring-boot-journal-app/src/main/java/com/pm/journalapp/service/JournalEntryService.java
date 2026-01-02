package com.pm.journalapp.service;

import com.pm.journalapp.entity.JournalEntry;
import com.pm.journalapp.entity.User;
import com.pm.journalapp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;
    public  JournalEntryService(JournalEntryRepository journalEntryRepository,  UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional //spring will create a transactional context which will treat all db queries as one. IF one fails, rollback everything
    public void saveEntryAndUpdateUser(JournalEntry journalEntry, User user){
        //add new journal, update journalId in users as well
        try{
//            User user = userService.findByUserName(userName);
            //updated journal entry of the user
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

            //update the journal entry id in users collection
            user.getJournalEntries().add(savedEntry);
            userService.updateJournalEntryInUser(user);
        }
        catch (Exception e){
            log.error("Exception occurred in saveEntry. " + e.getMessage());
        }
    }

    //updating entry, no need to update journalId in users
    public void updateJournalEntry(JournalEntry journalEntry){
        try{
            journalEntryRepository.save(journalEntry);
        }
        catch (Exception e){
            log.error("Exception occurred while updating journal entry. " + e.getMessage());
        }
    }

    public List<JournalEntry> getAllJournalEntries(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getJournalEntryById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public void deleteJournalEntryById(ObjectId id, User user){
        try{
            log.info("remove journal entry from user {}", user.getUserName());
            boolean removed= user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.updateJournalEntryInUser(user); //when you run save on same id it gets updated

                log.info("deleting journal entry in journal_entries collection");
                journalEntryRepository.deleteById(id);
            }
            log.info("No entry found with this id {}", id);

        }
        catch (Exception e){
            log.error("Exception occurred in deleteJournalEntryById. Message= " + e.getMessage());
        }
    }

    public JournalEntry updateJournalEntryById(ObjectId id, @RequestBody JournalEntry journalEntry){
        return journalEntryRepository.save(journalEntry);
    }
}
