package com.pm.journalapp.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique=true)  //indexing is not done automatically, set auto-index = true in application properties
    @NotNull
    private String userName;
    @NotNull
    private String password;

    private String email;
    private boolean sentimentAnalysis;

    private List<String> roles;

    @DBRef //link journalEntry with users
    private List<JournalEntry> journalEntries = new ArrayList<>();

}
