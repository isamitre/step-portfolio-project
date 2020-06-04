
async function getGreeting() {
  const response = await fetch('/greeting');
  const greeting = await response.text();
  document.getElementById('greeting-container').innerHTML = greeting;
}

/* Old method to get the hard coded comments */
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

/* Hides the "Hide Comments" button */
async function hideComments() {
  hideDisplay();
  const commentsElement = document.getElementById('comments-history');
  commentsElement.innerText = "";
}

/* Loads comments */
async function getComments() {
  display();

  const response = await fetch('/list-comments');
  const comments = await response.json();

  if (Object.keys(comments)) {
    const commentsElement = document.getElementById('comments-history');
    commentsElement.innerText = "";
    for (let key of Object.keys(comments)) {
        let value = comments[key];
        commentsElement.appendChild(createComment(value));
    }
  }
}

/** Creates an div element containing the comment. */
function createComment(text) {
  const divElement = document.createElement('div');
  divElement.className = "single-comment";

  const commentHeader = document.createElement('div');
  commentHeader.className = "comment-header";

  
  const h4Element = document.createElement('h4');
  h4Element.innerText = text.name;
  h4Element.className = "comment-author";
  commentHeader.appendChild(h4Element);

  const date = document.createElement('p');
  date.className = "comment-date";
  date.innerText = text.date;
  commentHeader.appendChild(date);
  
  divElement.appendChild(commentHeader);

  const pElement = document.createElement('p');
  pElement.className = "comment-text"
  pElement.innerText = text.comment;
  
  divElement.appendChild(pElement);

  return divElement;
}

function hideDisplay() {
  const elements = document.getElementsByClassName("hide");
  for (let i = 0; i < elements.length; i++) {
      elements.item(i).style.display = "none";
  }
}
function display() {
  const elements = document.getElementsByClassName("hide");
  for (let i = 0; i < elements.length; i++) {
      elements.item(i).style.display = "initial";
  }
}
