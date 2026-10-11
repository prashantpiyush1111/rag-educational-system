import api from "./api";
export async function sendFeedback(queryLogId,rating,comment="") {
  return (await api.post("/feedback",{queryLogId,rating,comment})).data;
}
