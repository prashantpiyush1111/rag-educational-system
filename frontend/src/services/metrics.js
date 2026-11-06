import api from "./api";
export async function getMetrics() { return (await api.get("/metrics")).data; }
