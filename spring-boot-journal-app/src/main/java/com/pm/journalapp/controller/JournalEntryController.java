package com.pm.journalapp.controller;

import com.pm.journalapp.entity.JournalEntry;
import com.pm.journalapp.entity.User;
import com.pm.journalapp.service.JournalEntryService;
import com.pm.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private static final Logger log = LoggerFactory.getLogger(JournalEntryController.class);
    private final JournalEntryService journalEntryService;
    private final UserService userService;
    public JournalEntryController(JournalEntryService journalEntryService,  UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        try{
            log.info("Entered getAll journal controller.");
            //fetching details of logged-in user, if valid
            User user = userService.authenticatedUser();
            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<JournalEntry> myEntries =  user.getJournalEntries();
            if(myEntries !=null && !myEntries.isEmpty() ){
                return new ResponseEntity<>(myEntries, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.error("Exception occurred while fetching journal entries. " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<JournalEntry> saveEntry(@RequestBody JournalEntry myEntry) {
        try{
            //fetching details of logged-in user, if valid
            User user = userService.authenticatedUser();
            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //create journal entry for that user
            journalEntryService.saveEntryAndUpdateUser(myEntry, user);
            log.info("journal entry created for user {}", user.getUserName());
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            log.info("Exception occurred in saveEntry (controller). Message= {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        try{
            User user = userService.authenticatedUser();
            if(user == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x-> x.getId().equals(myId)).toList();

            if(!journalEntries.isEmpty()){
                JournalEntry journalEntry = journalEntryService.getJournalEntryById(myId).orElse(null);
                if(journalEntry!= null)
                    return new ResponseEntity<>(journalEntry, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        //authenticate user
        User user = userService.authenticatedUser();
        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //check if user is trying to update it own journal entry
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x-> x.getId().equals(myId)).toList();

        if(!journalEntries.isEmpty()){
            JournalEntry oldEntry = journalEntryService.getJournalEntryById(myId).orElse(null);
            if (oldEntry != null) {
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.updateJournalEntry(oldEntry);
                return new  ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        try{
            //authenticate user
            User user = userService.authenticatedUser();
            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //check if user is trying to update it own journal entry
            List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x-> x.getId().equals(myId)).toList();

            if(!journalEntries.isEmpty()){
                JournalEntry oldEntry = journalEntryService.getJournalEntryById(myId).orElse(null);
                if (oldEntry != null) {
                    journalEntryService.deleteJournalEntryById(myId, user);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }

            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
