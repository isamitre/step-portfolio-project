
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

/* Loads comments */
async function getComments() {
  console.log("getComments()");
  const response = await fetch('/list-comments');
  console.log(response);
  console.log(response.json.length);
  const comments = await response.json();
  console.log("comments:");
  console.log(comments);

  const commentsElement = document.getElementById('comments-history');
  commentsElement.innerText = "";

  if (Object.keys(comments)) {
    comments.forEach((comment) => {
        commentsElement.appendChild(createCommentElement(comment))
    })
  }
}

/** Creates an div element containing the comment. */
function createCommentElement(comment) {
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
