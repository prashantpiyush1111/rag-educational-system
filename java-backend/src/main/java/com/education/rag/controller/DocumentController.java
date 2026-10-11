package com.education.rag.controller;

import java.util.List;
import java.util.Map;
import com.education.rag.client.PythonServiceClient;
import com.education.rag.entity.Document;
import com.education.rag.entity.User;
import com.education.rag.exception.CustomExceptions.ResourceNotFoundException;
import com.education.rag.repository.DocumentRepository;
import com.education.rag.repository.UserRepository;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final PythonServiceClient python;
    private final DocumentRepository documents;
    private final UserRepository users;
    public DocumentController(PythonServiceClient python,DocumentRepository documents,UserRepository users){this.python=python;this.documents=documents;this.users=users;}

    @PostMapping(value="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file, Authentication auth) {
        if(file.isEmpty()) throw new IllegalArgumentException("Uploaded file is empty");
        User user=users.findByEmail(auth.getName()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Map<?,?> result=python.upload(file,user.getId());
        String filename=file.getOriginalFilename()==null?"upload":file.getOriginalFilename();
        Document doc=new Document(); doc.setUser(user); doc.setFilename(filename);
        Object count=result.get("chunks_created"); doc.setChunksCreated(count instanceof Number n?n.intValue():0);
        documents.save(doc);
        return Map.of("id",doc.getId(),"filename",doc.getFilename(),"chunks_created",doc.getChunksCreated(),"status","success");
    }

    @GetMapping @Transactional(readOnly=true)
    public List<Map<String,Object>> list(Authentication auth) {
        return documents.findAllByUserEmailOrderByUploadedAtDesc(auth.getName()).stream().map(d->Map.<String,Object>of(
            "id",d.getId(),"filename",d.getFilename(),"chunks_created",d.getChunksCreated(),"uploaded_at",d.getUploadedAt().toString())).toList();
    }

    @DeleteMapping("/{id}") @Transactional
    public void delete(@PathVariable @Positive Long id, Authentication auth) {
        Document document=documents.findByIdAndUserEmail(id,auth.getName()).orElseThrow(()->new ResourceNotFoundException("Document not found"));
        python.deleteDocument(document.getFilename(),document.getUser().getId());
        documents.delete(document);
    }
}