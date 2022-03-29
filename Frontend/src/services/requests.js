// import { apiUrl } from '@/config/global';

const apiUrl = "";

export async function getRequest(route, queryParameters) {
  let url = new URL(apiUrl + route);

  url.search = new URLSearchParams(queryParameters).toString();

  let getResponse = await fetch(url, {
    headers: _getBasicHeaders(),
  }).then((response) => response);

  return _getResponseData(getResponse);
}

export async function postRequest(route, dataObject) {
  const postResponse = await fetch(apiUrl + route, {
    method: "POST",
    headers: _getBasicHeaders(),
    body: JSON.stringify(dataObject),
  }).then((response) => response);

  return _getResponseData(postResponse);
}

export async function putRequest(route, dataObject) {
  const putResponse = await fetch(apiUrl + route, {
    method: "PUT",
    headers: _getBasicHeaders(),
    body: JSON.stringify(dataObject),
  }).then((response) => response);
  return _getResponseData(putResponse);
}

function _getBasicHeaders() {
  let headers = {
    "Content-Type": "application/json",
  };

  return headers;
}

async function _getResponseData(response) {
  const responseDataObject = await response.json().then((data) => {
    return data;
  });
  return { status: response.status, dataObject: responseDataObject };
}
