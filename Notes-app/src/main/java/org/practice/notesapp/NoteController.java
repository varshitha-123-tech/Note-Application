package org.practice.notesapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
public class NoteController {

@Autowired
private NoteRepository noteRepository;
private final RestClient userClient = RestClient.create("http://localhost:8081");


@PostMapping("/Note")
public Note createNote(@RequestBody Note note) {
     if (note.getOwner() == null || note.getOwner().isBlank()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner is required");
     }

     Boolean exists;
     try {
          exists = userClient.get()
                  .uri("/users/{username}/exists", note.getOwner())
                  .retrieve()
                  .body(Boolean.class);
     } catch (RestClientException e) {
          throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "User service is unavailable");
     }

     if (!Boolean.TRUE.equals(exists)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
     }

     note.setUpdateAt(LocalDateTime.now());
     return noteRepository.save(note);
}
@GetMapping("/Notes")
     public List<Note> getAllNotes() {
     return noteRepository.findAll();
}
@GetMapping("/notes/{id}")
     public Note getNote(@PathVariable  long id) {
     return noteRepository.findById(id).orElseThrow();
}
@GetMapping("/Notes/owner/{owner}")
public List<Note> getNotesByOwner(@PathVariable String owner) {
     return noteRepository.findByOwner(owner);
}
@DeleteMapping("/notes/{id}")
     public void deleteNote(@PathVariable long id) {
     noteRepository.deleteById(id);
}
@PutMapping("/notes/{id}")
     public Note updateNote(@PathVariable long id, @RequestBody Note note) {
     Note existingNote = noteRepository.findById(id).orElseThrow();
     existingNote.setTitle(note.getTitle());
     existingNote.setContent(note.getContent());
     existingNote.setCategory(note.getCategory());
     existingNote.setUpdateAt(LocalDateTime.now());
     return noteRepository.save(existingNote);
}
}
