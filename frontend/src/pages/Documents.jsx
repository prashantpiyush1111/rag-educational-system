import React, { useState } from "react";
import DocumentUpload from "../components/Documents/DocumentUpload";
import DocumentList from "../components/Documents/DocumentList";
export default function Documents(){const [refreshKey,setRefreshKey]=useState(0);return <section className="page"><h1>Documents</h1><DocumentUpload onUploaded={()=>setRefreshKey(v=>v+1)}/><DocumentList refreshKey={refreshKey}/></section>;}
