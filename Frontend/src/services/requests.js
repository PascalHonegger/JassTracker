let apiUrl = "http://localhost:8080/api/";
if (process.env.NODE_ENV === "production") {
  apiUrl = "/api/";
}

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
  return {
    "Content-Type": "application/json",
  };
}

async function _getResponseData(response) {
  const responseDataObject = await response.json().then((data) => {
    return data;
  });
  return { status: response.status, dataObject: responseDataObject };
}
