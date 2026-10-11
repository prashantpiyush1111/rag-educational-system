package com.education.rag.dto.response;
import java.util.List;
public record QueryResponse(String answer,List<String> sources,String question,Long queryLogId){
 public QueryResponse(String answer,List<String> sources,String question){this(answer,sources,question,null);}
}
