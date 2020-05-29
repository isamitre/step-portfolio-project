console.log("check 0");
// Get the modal
var modal = document.getElementById('myModal');
console.log("check 1");
// Get the image and insert it inside the modal - use its "alt" text as a caption
var img = $('.gallery');
var modalImg = $("#img01");
$('.gallery').click(function(){
    modal.style.display = "block";
    var newSrc = this.src;
    modalImg.attr('src', newSrc);
});
console.log("check2");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
console.log("check 3");
// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}
console.log("check 4");

