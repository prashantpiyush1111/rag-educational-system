package com.education.rag.client;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import com.education.rag.dto.response.QueryResponse;
import com.education.rag.exception.CustomExceptions.PythonServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PythonServiceClient {
 private final RestClient client;private final String apiKey;
 public PythonServiceClient(@Value("${app.python-service.base-url:http://127.0.0.1:8000}") String baseUrl,@Value("${app.python-service.api-key}") String apiKey){
  SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();factory.setConnectTimeout(Duration.ofSeconds(3));factory.setReadTimeout(Duration.ofSeconds(30));
  this.client=RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();this.apiKey=apiKey;
 }
 public QueryResponse query(String question,int topK,Long userId,String sessionId){
  try{
   Map<?,?> data=client.post().uri("/query/").contentType(MediaType.APPLICATION_JSON).header("X-API-Key",apiKey).header("X-User-Id",String.valueOf(userId))
    .body(Map.of("question",question,"top_k",topK,"session_id",sessionId==null?"":sessionId)).retrieve()
    .onStatus(HttpStatusCode::isError,(req,res)->{throw new PythonServiceException("AI service rejected the query",res.getStatusCode().value());}).body(Map.class);
   if(data==null)throw new PythonServiceException("Empty response from AI service",502);
   Object sourceValue=data.get("sources");List<String> sources=sourceValue instanceof List<?> list?list.stream().map(String::valueOf).toList():List.of();
   Object answer=data.get("answer"),q=data.get("question");return new QueryResponse(answer==null?"":String.valueOf(answer),sources,q==null?question:String.valueOf(q));
  }catch(RestClientResponseException e){throw new PythonServiceException("AI service request failed",e.getStatusCode().value());}
   catch(ResourceAccessException e){throw new PythonServiceException("AI service is unavailable",503);}
 }
 public Map<?,?> upload(MultipartFile file,Long userId){
  try{
   ByteArrayResource resource=new ByteArrayResource(file.getBytes()){@Override public String getFilename(){return file.getOriginalFilename()==null?"upload.txt":file.getOriginalFilename();}};
   MultiValueMap<String,Object> form=new LinkedMultiValueMap<>();form.add("file",resource);form.add("user_id",String.valueOf(userId));
   Map<?,?> body=client.post().uri("/documents/upload").header("X-API-Key",apiKey).header("X-User-Id",String.valueOf(userId)).contentType(MediaType.MULTIPART_FORM_DATA).body(form).retrieve()
    .onStatus(HttpStatusCode::isError,(req,res)->{throw new PythonServiceException("AI service document upload failed",res.getStatusCode().value());}).body(Map.class);
   return body==null?Map.of():body;
  }catch(RestClientResponseException e){throw new PythonServiceException("AI service document upload failed",e.getStatusCode().value());}
   catch(ResourceAccessException e){throw new PythonServiceException("AI service is unavailable",503);}
   catch(java.io.IOException e){throw new PythonServiceException("Could not read uploaded file",400);}
 }
 public void deleteDocument(String filename,Long userId){
  try{client.delete().uri("/documents/{filename}",filename).header("X-API-Key",apiKey).header("X-User-Id",String.valueOf(userId)).retrieve()
   .onStatus(HttpStatusCode::isError,(req,res)->{throw new PythonServiceException("AI service document deletion failed",res.getStatusCode().value());}).toBodilessEntity();}
  catch(RestClientResponseException e){throw new PythonServiceException("AI service document deletion failed",e.getStatusCode().value());}
  catch(ResourceAccessException e){throw new PythonServiceException("AI service is unavailable",503);}
 }
}
