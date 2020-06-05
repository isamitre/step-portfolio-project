
async function getGreeting() {
  const response = await fetch('/greeting');
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
  let button = document.getElementById("comment-btn");

  if (button.innerText == "Show Comments"){
    button.innerText = "Hide Comments";
    button.style.marginBottom = "10px";
    getComments();
  }
  else if (button.innerText == "Hide Comments") {
    button.innerText = "Show Comments";
    button.style.marginBottom = "20px";
    document.getElementById('comments-history').innerText = "";
  }
}

async function getComments() {
  fetch('/list-comments').then(response => response.json()).then((comments) => {
    const commentsElement = document.getElementById('comments-history');
    comments.forEach((comment) => {
      commentsElement.appendChild(createComment(comment));
    })
  });
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

  const commentContent = document.createElement('div');
  commentContent.className = "comment-content";

  const pElement = document.createElement('p');
  pElement.className = "comment-text"
  pElement.innerText = text.comment;
  commentContent.appendChild(pElement);

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = "Delete";
  deleteButtonElement.className = "delete-btn";
  deleteButtonElement.addEventListener('click', ( )=> {
    let deleteBool = deleteComment(text);
    if (deleteBool) {
        divElement.remove();
    }
  });
  commentContent.appendChild(deleteButtonElement);

  divElement.appendChild(commentContent);

  console.log(divElement);
  return divElement;
}

function deleteComment(comment){
  let yes = confirm("Are you sure you want to delete this comment? This action cannot be undone.")
  if (yes) {
    const params = new URLSearchParams();
    params.append('id', comment.id);
    fetch('/delete-comment', {method: 'POST', body: params});
    return true;
  }
  return false;
}
