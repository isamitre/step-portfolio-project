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
closeSPan.onclick = function() { 
   modal.style.display = "none";
}
