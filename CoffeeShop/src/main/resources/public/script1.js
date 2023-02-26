/*function onClick(element) {
  document.getElementById("img01").src = element.src;
  document.getElementById("modal01").style.display = "block";
  var captionText = document.getElementById("caption");
  captionText.innerHTML = element.alt;
}*/

function displayCard(){
    var products = getProducts();

    document.getElementById("main").style.display ="none";
    document.getElementById("key").style.display ="none";
    document.getElementById("bean").style.display ="none";
    document.getElementById("container").style.display ="none";
    document.getElementById ("cardPage").style.display = "";
    //document.getElementById ("image").style.display = "";
    document.getElementById ("user").style.display = "";
}

function total() {
    var tableBody = document.getElementById("order-table-body");
    var bodyLength = tableBody.rows.length;
    console.log("ALEX Body length is: " + bodyLength);
    var total = 0.0;

    //loops through rows
    for (i = 0; i < bodyLength; i++){

       //gets cells of current row
        var oCells = tableBody.rows.item(i).cells;

        var price = oCells[1].innerHTML;
        console.log("ALEX Price for " + i + " is: " + price);
        var quantity = oCells[2].children[0].value;
        console.log("ALEX Quantity for " + i + " is: " + quantity);
        total = total + (price * quantity);
    }
    total = roundTo(total, 2);
    document.getElementById("total_cost").innerHTML = "EUR "+ total;

}

function roundTo(n, digits) {
    var negative = false;
    if (digits === undefined) {
        digits = 0;
    }
    if (n < 0) {
        negative = true;
        n = n * -1;
    }
    var multiplicator = Math.pow(10, digits);
    n = parseFloat((n * multiplicator).toFixed(11));
    n = (Math.round(n) / multiplicator).toFixed(digits);
    if (negative) {
        n = (n * -1).toFixed(digits);
    }
    return n;
}

  function createTableOrders (products) {
	console.log('ALEX  AFTER get products. First product is: ' + products[0].product_name);

	var body = document.getElementById("order-table-body");

	for (var i = 0; i < products.length; i++) {
		var tr = body.insertRow();
		var tdCoffee = tr.insertCell();
		tdCoffee.appendChild(document.createTextNode(products[i].product_name));
		var tdPrice = tr.insertCell();
		tdPrice.appendChild(document.createTextNode(products[i].price));
		var newinputbox = document.createElement("input");
		newinputbox.setAttribute("type", "number");
		newinputbox.setAttribute("id", products[i].product_name);
		newinputbox.setAttribute("value", "0");
		newinputbox.setAttribute("placeholder", "Enter Quantity");
		var tdQuantity = tr.insertCell();
		tdQuantity.appendChild(newinputbox);
	}
  }

  function getProducts () {
	console.log('ALEX get products from server');
	$.ajax({
		type: 'GET',
		url: '/products/active',
		success: function(data) {
		    return createTableOrders(data);
		},
		error: function(xhr, status, error) {
			alert('Cant get products from server: ' + xhr.status + '. Please try later.');
		}
	});
  }

  function submitOrder() {
    console.log("ALEX Start Order submit.");
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: '/order',
		data: orderFormToJSON(),
		success: function() {
		    var modalBodyText = document.getElementById("modal-body-text");
		    modalBodyText.innerHTML = "Order is on the way! Please pick it up in 10 minutes.";

            // Get the modal
            var modal = document.getElementById("myModal");


            // Get the <span> element that closes the modal
            var span = document.getElementsByClassName("close")[0];


            // When the user clicks on <span> (x), close the modal
            span.onclick = function() {
              modal.style.display = "none";
            }

            // When the user clicks anywhere outside of the modal, close it
            window.onclick = function(event) {
              if (event.target == modal) {
                modal.style.display = "none";
              }
            }

		    modal.style.display = "block";
		},
		error: function(xhr, status, error) {
			alert('Something went wrong. Order was not created. Status: ' + xhr.status);
		}
	});
  }

  function orderFormToJSON () {
    var order = {
        "customerName": "",
        "customerEmail":"",
        "productsWithQuantities":[]
    };
    var tableBody = document.getElementById("order-table-body");
    var bodyLength = tableBody.rows.length;
    console.log("ALEX Body length is: " + bodyLength);
    var total = 0.0;

    //loops through rows
    for (i = 0; i < bodyLength; i++){

       //gets cells of current row
        var oCells = tableBody.rows.item(i).cells;

        var productName = oCells[0].innerHTML;
        console.log("ALEX Product name for " + i + " is: " + productName);
        var quantity = oCells[2].children[0].value;
        console.log("ALEX Quantity for " + i + " is: " + quantity);
        var productQuantity = {
            "productName": productName,
            "quantity": quantity
        }
        order.productsWithQuantities.push(productQuantity);
    }

    var customerName = document.getElementById("customer-name").value;
    var customerEmail = document.getElementById("customer-email").value;
    console.log("ALEX customer-email is: " + customerEmail);
    order.customerName = customerName;
    order.customerEmail = customerEmail;
    return JSON.stringify(order);
  }