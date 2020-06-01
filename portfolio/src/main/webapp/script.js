let modal = document.getElementById("myModal");
let i;
let img = document.getElementsByClassName("gallery");
let modalImg = document.getElementById("img01");
for(i=0;i< img.length;i++) {    
  img[i].onclick = function() {
    modal.style.display = "block";
    modalImg.src = this.src;
  }
}

let closeSpan = document.getElementsByClassName("close")[0];
closeSpan.onclick = function() { 
  modal.style.display = "none";
}


async function getGreeting() {
  const response = await fetch('/greeting');
  const greeting = await response.text();
  document.getElementById('greeting-container').innerText = greeting;
}

async function getJSON() {
  const response = await fetch('/data');
  const comments = await response.json();
}