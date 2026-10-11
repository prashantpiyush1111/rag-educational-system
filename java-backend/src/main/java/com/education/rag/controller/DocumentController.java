package com.education.rag.controller;

import java.util.List;
import java.util.Map;
import com.education.rag.service.DocumentService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
 private final DocumentService service;
 public DocumentController(DocumentService service){this.service=service;}
 @PostMapping(value="/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
 public Map<String,Object> upload(@RequestParam("file") MultipartFile file,Authentication auth){return service.upload(auth.getName(),file);}
 @GetMapping public List<Map<String,Object>> list(Authentication auth){return service.list(auth.getName());}
 @DeleteMapping("/{id}") public void delete(@PathVariable @Positive Long id,Authentication auth){service.delete(auth.getName(),id);}
}
