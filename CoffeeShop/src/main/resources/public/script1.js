function displayCard(){
    var products = getProducts();

    document.getElementById("main").style.display ="none";
    document.getElementById("key").style.display ="none";
    document.getElementById("bean").style.display ="none";
    document.getElementById("container").style.display ="none";
    document.getElementById ("cardPage").style.display = "";
    document.getElementById("customer-name").value = "";
    document.getElementById("customer-email").value = "";
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
	while(body.rows.length > 0) {
      body.deleteRow(0);
    }

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

function getProducts (order) {
	console.log('ALEX get products from server');
	$.ajax({
		type: 'GET',
		url: '/products/active',
		success: function(data) {
		    createTableOrders(data);
		    if (order) {
		        populateFields(order);
		    }
		},
		error: function(xhr, status, error) {
			alert('Cant get products from server: ' + xhr.status + '. Please try later.');
		}
	});
}

function submitOrder() {
    console.log("ALEX Start Order submit.");
    let orderData = orderFormToJSON();
    console.log("ALEX Order Request: " + JSON.stringify(orderData));
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: '/order',
		data: orderData,
		success: function(order) {
		    createModalSuccess("Order is created! Order number is " + order.orderId +". You can proceed to payment.");
            total = roundTo(order.orderTotal, 2);
            updateDisableTag(true);
            document.getElementById("total_cost").innerHTML = "EUR "+ total;
            document.getElementById ("paymentDiv").style.display = "";
            document.getElementById ("confirmChoice").style.display = "none";
            document.getElementById ("totalCost").style.display = "";
            document.getElementById ("updateOrder").style.display = "none";
            document.getElementById ("deleteOrder").style.display = "none";
            document.getElementById ("orderNumber").style.display = "";
            document.getElementById("orderNumberText").innerHTML = "Order Number " + order.orderId;
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order was not created. Status: ' + JSON.stringify(xhr.responseText));
		}
	});
}

function orderFormToJSON (paid) {
    var order = {
        "customerName": "",
        "customerEmail":"",
        "paid": false,
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
    if (paid) {
        order.paid = paid;
    }

    var customerName = document.getElementById("customer-name").value;
    var customerEmail = document.getElementById("customer-email").value;
    console.log("ALEX customer-email is: " + customerEmail);
    order.customerName = customerName;
    order.customerEmail = customerEmail;
    return JSON.stringify(order);
}

function createModalSuccess(text) {
    var header = document.getElementsByClassName("modal-header");
    header[0].style.backgroundColor = "#34495E";
    var modalHeaderText = document.getElementById("modal-header-text");
    modalHeaderText.innerHTML = "Success!";

    var modalBodyText = document.getElementById("modal-body-text");
    modalBodyText.innerHTML = text;

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
}

function createModalFailure(text) {
    var header = document.getElementsByClassName("modal-header");
//    header[0].style.backgroundColor = "#DC143C"; //red
    var modalHeaderText = document.getElementById("modal-header-text");
    modalHeaderText.innerHTML = "Failure!";

    var modalBodyText = document.getElementById("modal-body-text");
    modalBodyText.innerHTML = text;

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
}

function updateDisableTag(disabled) {
    const leftDiv = document.getElementById("leftDiv");
    let inputs = leftDiv.getElementsByTagName('input');
    for (index = 0; index < inputs.length; ++index) {
        inputs[index].disabled = disabled;
    }
}

function back() {
    const paymentDiv = document.getElementById("paymentDiv");
    if (paymentDiv.style.display=="none") {
        document.getElementById ("paymentDiv").style.display = "none";
        document.getElementById ("totalCost").style.display = "none";
        document.getElementById("total_cost").innerHTML = "EUR ";

        document.getElementById("main").style.display ="";
        document.getElementById("key").style.display ="";
        document.getElementById("bean").style.display ="";
        document.getElementById("container").style.display ="";
        document.getElementById ("cardPage").style.display = "none";
        document.getElementById ("customer-name").value = "";
        document.getElementById ("customer-email").value = "";
        document.getElementById ("updateOrder").style.display = "none";
        document.getElementById ("payOrder").style.display = "none";
        document.getElementById ("deleteOrder").style.display = "none";
        document.getElementById ("orderNumber").style.display = "none";
        document.getElementById("orderNumberText").innerHTML = "Order Number ";
        document.getElementById ("confirmChoice").style.display = "";
    } else {
            updateDisableTag(false);
            document.getElementById("total_cost").innerHTML = "EUR ";
            document.getElementById ("paymentDiv").style.display = "none";
            document.getElementById ("confirmChoice").style.display = "none";
            document.getElementById ("totalCost").style.display = "none";
            document.getElementById ("updateOrder").style.display = "";
            document.getElementById ("payOrder").style.display = "";
            document.getElementById ("deleteOrder").style.display = "";
    }
}

function updateOrder() {
    console.log("ALEX Start Order UPDATE.");
    let longOrderId = document.getElementById ("orderNumberText").innerHTML;
    const orderId = longOrderId.split("Order Number ")[1];
    let orderData = orderFormToJSON();
    console.log("ALEX Order Request: " + JSON.stringify(orderData));
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: '/order/' + orderId,
		data: orderData,
		success: function(order) {
		    createModalSuccess("Order is updated! You can proceed to payment.");
            total = roundTo(order.orderTotal, 2);
            updateDisableTag(true);
            document.getElementById("total_cost").innerHTML = "EUR "+ total;
            document.getElementById ("paymentDiv").style.display = "";
            document.getElementById ("confirmChoice").style.display = "none";
            document.getElementById ("totalCost").style.display = "";
            document.getElementById ("updateOrder").style.display = "none";
            document.getElementById ("payOrder").style.display = "none";
            document.getElementById ("deleteOrder").style.display = "none";
            document.getElementById ("orderNumber").style.display = "";
            document.getElementById("orderNumberText").innerHTML = "Order Number " + order.orderId;
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order was not updated. Status: ' + JSON.stringify(xhr.responseText));
		}
	});
}

function deleteOrder() {
    console.log("ALEX Start Order DELETE.");
    let longOrderId = document.getElementById ("orderNumberText").innerHTML;
    const orderId = longOrderId.split("Order Number ")[1];
	$.ajax({
		type: 'DELETE',
		contentType: 'application/json',
		url: '/order/' + orderId,
		success: function() {
		    createModalSuccess("Order is successfully deleted!");
            updateDisableTag(false);
            document.getElementById ("paymentDiv").style.display = "none";
            document.getElementById ("totalCost").style.display = "none";
            document.getElementById("total_cost").innerHTML = "EUR ";

            document.getElementById("main").style.display ="";
            document.getElementById("key").style.display ="";
            document.getElementById("bean").style.display ="";
            document.getElementById("container").style.display ="";
            document.getElementById("cardPage").style.display = "none";
            document.getElementById("customer-name").value = "";
            document.getElementById("customer-email").value = "";
            document.getElementById("updateOrder").style.display = "none";
            document.getElementById("payOrder").style.display = "none";
            document.getElementById("deleteOrder").style.display = "none";
            document.getElementById("orderNumber").style.display = "none";
            document.getElementById("orderNumberText").innerHTML = "Order Number ";
            document.getElementById("confirmChoice").style.display = "";
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order was not deleted. Status: ' + JSON.stringify(xhr.responseText));
		}
	});
}

function goToPayOrder() {
    let longOrderId = document.getElementById ("orderNumberText").innerHTML;
    const orderId = longOrderId.split("Order Number ")[1];
    let orderData = orderFormToJSON();
    console.log("ALEX Order Request: " + JSON.stringify(orderData));
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: '/order/' + orderId,
		data: orderData,
		success: function(order) {
		    if (order.paid === true) {
		        createModalSuccess("Order is already payed! Order can not be updated.");
		        return;
		    }
            total = roundTo(order.orderTotal, 2);
            updateDisableTag(true);
            document.getElementById("total_cost").innerHTML = "EUR "+ total;
            document.getElementById ("paymentDiv").style.display = "";
            document.getElementById ("confirmChoice").style.display = "none";
            document.getElementById ("totalCost").style.display = "";
            document.getElementById ("updateOrder").style.display = "none";
            document.getElementById ("payOrder").style.display = "none";
            document.getElementById ("deleteOrder").style.display = "none";
            document.getElementById ("orderNumber").style.display = "";
            document.getElementById("orderNumberText").innerHTML = "Order Number " + order.orderId;
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order could not be refreshed. Status: ' + JSON.stringify(xhr.responseText));
		}
	});
}

function findYourOrder() {
    var header = document.getElementsByClassName("modal-header");
    header[0].style.backgroundColor = "#6E4D33";

    var modalHeaderText = document.getElementById("find-modal-header-text");
    modalHeaderText.innerHTML = "Find your order by id";

    // Get the modal
    var modal = document.getElementById("findModal");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("closee")[0];

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
}

function searchYourOrder() {
    const span = document.getElementsByClassName("closee")[0];
    span.click();
    let orderId = document.getElementById("searchOrder").value;
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: '/order/' + orderId,
		success: function(order) {
            total = roundTo(order.orderTotal, 2);
            updateDisableTag(false);

            var products = getProducts(order);

            document.getElementById("searchOrder").value = "";

            document.getElementById("main").style.display ="none";
            document.getElementById("key").style.display ="none";
            document.getElementById("bean").style.display ="none";
            document.getElementById("container").style.display ="none";
            document.getElementById ("cardPage").style.display = "";

//            document.getElementById("total_cost").innerHTML = "EUR "+ total;
            document.getElementById ("paymentDiv").style.display = "none";
            document.getElementById ("confirmChoice").style.display = "none";
            document.getElementById ("totalCost").style.display = "none";
            document.getElementById ("updateOrder").style.display = "";
            document.getElementById ("payOrder").style.display = "";
            document.getElementById ("deleteOrder").style.display = "";
            document.getElementById ("orderNumber").style.display = "";
            document.getElementById("orderNumberText").innerHTML = "Order Number " + order.orderId;
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order could not be find. Status: ' + JSON.stringify(xhr.responseText));
		}
	});
}

function populateFields(order) {
    document.getElementById ("customer-name").value = order.customerName;
    document.getElementById ("customer-email").value = order.customerEmail;
    order.productQuantityRequests.forEach(function(entry) {
        document.getElementById (entry.productName).value = entry.quantity;
    });
}

function payOrder() {
    console.log("ALEX Start Order PAY.");
    let longOrderId = document.getElementById ("orderNumberText").innerHTML;
    const orderId = longOrderId.split("Order Number ")[1];
    let orderData = orderFormToJSON(true);
    console.log("ALEX Order Request: " + JSON.stringify(orderData));
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: '/order/' + orderId,
		data: orderData,
		success: function(order) {
		    createModalSuccess("Order " + orderId + " is successfully paid! You can pick up your order in 10 minutes.");
            updateDisableTag(false);
            document.getElementById("total_cost").innerHTML = "EUR ";
            document.getElementById ("paymentDiv").style.display = "none";
            document.getElementById ("confirmChoice").style.display = "";
            document.getElementById ("totalCost").style.display = "none";
            document.getElementById ("updateOrder").style.display = "none";
            document.getElementById ("payOrder").style.display = "none";
            document.getElementById ("deleteOrder").style.display = "none";
            document.getElementById ("orderNumber").style.display = "none";
            document.getElementById("orderNumberText").innerHTML = "Order Number ";
            document.getElementById("customer-name").value = "";
            document.getElementById("customer-email").value = "";

            document.getElementById("main").style.display ="";
            document.getElementById("key").style.display ="";
            document.getElementById("bean").style.display ="";
            document.getElementById("container").style.display ="";
            document.getElementById ("cardPage").style.display = "none";
		},
		error: function(xhr, status, error) {
		    createModalFailure('Something went wrong. Order was not paid. Status: ' + JSON.stringify(xhr.responseText));
		}
	});

}