/*function onClick(element) {
  document.getElementById("img01").src = element.src;
  document.getElementById("modal01").style.display = "block";
  var captionText = document.getElementById("caption");
  captionText.innerHTML = element.alt;
}*/

function displayCard(){
	
	document.getElementById("main").style.display ="none";
	document.getElementById("key").style.display ="none";
	document.getElementById("bean").style.display ="none";
	document.getElementById("container").style.display ="none";
	document.getElementById ("cardPage").style.display = "";
	document.getElementById ("image").style.display = "";
	document.getElementById ("user").style.display = "";
}

function total() {
    var Latte;
    var Cappuccino;
    var Machiato;
    var Americano
    var Dopio
    var FlatWhite
    var sum;
    Machiato = parseInt(document.getElementById("qty3").value);
    Americano = parseInt(document.getElementById("qty2").value);
    Dopio = parseInt(document.getElementById("qty1").value);
    FlatWhite = parseInt(document.getElementById("qty4").value);
    Cappuccino = parseInt(document.getElementById("qty5").value);
    Latte = parseInt(document.getElementById("qty6").value);
    sum = (Latte * 4) + (Cappuccino * 3.75) + (Dopio * 3) +(Machiato *4.50)+ (Americano * 2) + (FlatWhite *4);
    document.getElementById("total_cost").innerHTML = "EUR "+sum + ".00";

  }
  function submit() {
    alert("Your Order has been Submitted, Successfully!");
  }