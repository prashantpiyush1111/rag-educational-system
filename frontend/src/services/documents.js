import api from "./api";
export const documentsApi = {
  async list() { return (await api.get("/documents")).data; },
  async remove(id) { return api.delete(`/documents/${id}`); },
  async upload(file,onProgress) {
    const form = new FormData(); form.append("file",file);
    return (await api.post("/documents/upload",form,{headers:{"Content-Type":"multipart/form-data"},onUploadProgress:event=>{if(event.total)onProgress?.(Math.round(event.loaded*100/event.total));}})).data;
  }
};
