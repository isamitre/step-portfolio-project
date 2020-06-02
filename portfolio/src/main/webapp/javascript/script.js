
async function getGreeting() {
  const response = await fetch('/greeting2');
  const greeting = await response.text();
  document.getElementById('greeting-container').innerHTML = greeting;
}

/*Old method to get the hard coded comments */
async function getFakeComments() {
  const response = await fetch('/talent');
  const comments = await response.json();
  const commentsElement = document.getElementById('comments-container');
  commentsElement.innerText = "";
  for (let key of Object.keys(comments)) {
    let value = comments[key];
    commentsElement.appendChild(createListElement(value));
  }
}

/* Checks if comments are hidden or not*/
async function checkComments() {
  let btn = document.getElementById("comment-btn");

  if(btn.innerText == "Show Comments"){
    btn.innerText = "Hide Comments";
    getComments();
  }
  else if (btn.innerText == "Hide Comments") {
    btn.innerText = "Show Comments";
    const commentsElement = document.getElementById('comments-history');
    commentsElement.innerText = "";
    let divElement = document.getElementById("comments-div")
    divElement.style.backgroundColor = "white";
    divElement.style.borderStyle = "none";
  }
}

async function getComments() {
  let divElement = document.getElementById("comments-div")
  divElement.style.backgroundColor = "#cba6b9";
  divElement.style.borderStyle = "solid";

  const response = await fetch('/comment');
  const comments = await response.json();

  const commentsElement = document.getElementById('comments-history');
  commentsElement.innerText = "";
  for (let key of Object.keys(comments)) {
    let value = comments[key];
    console.log("key: ", key);
    console.log("value: ", value);
    commentsElement.appendChild(createComment(value));
  }
}

/** Creates an div element containing the comment. */
function createComment(text) {
  const divElement = document.createElement('div');

  const h4Element = document.createElement('h4');
  h4Element.innerText = text.name;
  divElement.appendChild(h4Element);

//   const hr = document.createElement('hr');
//   divElement.appendChild(hr);

  const pElement = document.createElement('p');
  pElement.innerText = text.comment;
  divElement.className = "single-comment";
  divElement.appendChild(pElement);

  return divElement;
}