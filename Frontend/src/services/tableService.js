import { getRequest, postRequest, putRequest } from "./requests";

export async function getTables(queryParameters) {
  return getRequest("tables", queryParameters);
}

export async function getTable(id, queryParameter) {
  return getRequest(`tables/${id}`, queryParameter);
}

export async function updateTable(id, data) {
  return putRequest(`tables/${id}`, data);
}

export async function createTable(data) {
  return postRequest("tables", data);
}

export async function deleteTable(id, data) {
  return postRequest(`tables/${id}`, data);
}
