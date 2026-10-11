import React,{useEffect,useState} from "react";
import { getMetrics } from "../../services/metrics";
import { responseDetail } from "../../services/api";
export default function MetricsDashboard(){
 const [metrics,setMetrics]=useState(null);const [error,setError]=useState("");const [loading,setLoading]=useState(true);
 useEffect(()=>{let mounted=true;getMetrics().then(data=>{if(mounted)setMetrics(data)}).catch(err=>{if(mounted)setError(responseDetail(err,"Unable to load metrics"))}).finally(()=>{if(mounted)setLoading(false)});return()=>{mounted=false}},[]);
 if(loading)return <p role="status">Loading metrics…</p>;if(error)return <p role="alert">{error}</p>;
 const items=[["Documents",metrics?.documents_count],["Total chunks",metrics?.total_chunks],["Queries",metrics?.queries_count],["Average response (ms)",Math.round(metrics?.avg_response_time_ms||0)]];
 return <div className="metrics-grid">{items.map(([label,value])=><article className="metric-card" key={label}><h2>{label}</h2><p>{value??0}</p></article>)}</div>;
}
