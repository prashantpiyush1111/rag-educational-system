package com.education.rag.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.education.rag.client.PythonServiceClient;
import com.education.rag.entity.Document;
import com.education.rag.entity.User;
import com.education.rag.exception.CustomExceptions.ResourceNotFoundException;
import com.education.rag.repository.DocumentRepository;
import com.education.rag.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {
 private final PythonServiceClient python; private final DocumentRepository documents; private final UserRepository users;
 public DocumentService(PythonServiceClient python,DocumentRepository documents,UserRepository users){this.python=python;this.documents=documents;this.users=users;}
 @Transactional public Map<String,Object> upload(String email,MultipartFile file){
  if(file.isEmpty())throw new IllegalArgumentException("Uploaded file is empty");
  User user=users.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
  Map<?,?> result=python.upload(file,user.getId());
  Document doc=new Document();doc.setUser(user);doc.setFilename(file.getOriginalFilename()==null?"upload":file.getOriginalFilename());
  Object count=result.get("chunks_created");doc.setChunksCreated(count instanceof Number n?n.intValue():0);documents.save(doc);
  return Map.of("id",doc.getId(),"filename",doc.getFilename(),"chunks_created",doc.getChunksCreated(),"status","success");
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> list(String email){
  return documents.findAllByUserEmailOrderByUploadedAtDesc(email).stream().map(d->Map.<String,Object>of("id",d.getId(),"filename",d.getFilename(),"chunks_created",d.getChunksCreated(),"uploaded_at",d.getUploadedAt().toString())).toList();
 }
 @Transactional public void delete(String email,Long id){
  Document d=documents.findByIdAndUserEmail(id,email).orElseThrow(()->new ResourceNotFoundException("Document not found"));
  python.deleteDocument(d.getFilename(),d.getUser().getId());documents.delete(d);
 }
}
