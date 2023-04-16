function hideAll() {
	document.getElementById ("loginSection").style.display = "none";
    document.getElementById ("allUsersDiv").style.display = "none";
    document.getElementById ("updateUserInput").style.display = "none";
    document.getElementById ("deleteUserDiv").style.display = "none";
    document.getElementById ("createUserInput").style.display = "none";
    document.getElementById ("newUserDiv").style.display = "none";
    document.getElementById ("dashboardDiv").style.display = "none";
    document.getElementById ("salesDiv").style.display = "none";
}

function login() {
    const username = document.getElementById ("name").value;
    const password = document.getElementById ("password").value;
    const request = {
        "username": username,
        "password": password
    }
    $.ajax({
        url: 'http://localhost:8083/signin',
        type: 'POST',
        contentType: 'application/json',
		data: JSON.stringify(request),
        success: function (result) {
            let token = result.accessToken;
            localStorage.setItem('token', token);
            const username = result.username;
            let role = result.roles[0]; //ROLE_COFFEE_MAKER  ROLE_MANAGER
            console.log("User with role: " + role);
			document.getElementById ("loginSection").style.display = "none";
			document.getElementById ("mainMenu").style.display = "";
            
            if (role === "ROLE_MANAGER") {
				document.getElementById ("dashboardLi").style.display = "";
				document.getElementById ("reportLi").style.display = "";
				document.getElementById ("usersLi").style.display = "";
				document.getElementById ("salesLi").style.display = "";
			} else {
				document.getElementById ("dashboardLi").style.display = "";
				document.getElementById ("reportLi").style.display = "none";
				document.getElementById ("usersLi").style.display = "none";
				document.getElementById ("salesLi").style.display = "none";
			}

            //use roles to show, hide fields

        },
        error: function (xhr, status, error) {

        }
    });

}

function getOrders() {
     $.ajax({
            url: 'http://localhost:8083/orders',
            type: 'GET',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (orders) {
				hideAll();
			    document.getElementById ("dashboardDiv").style.display = "";
                console.log("LENGTH:  " + orders.length);
			    

				let datatable;
				if (orders.length === 0) {
					datatable = $('#tableAllOrders').DataTable({
					    "destroy": true
					});
				} else {
					datatable = $('#tableAllOrders').DataTable({
			        	"order": [[ 0, "asc" ]],
					    "destroy": true
			    	});
				    datatable.clear();
				    orders.forEach(function(order) {
						datatable.row.add([order.id, order.customerEmail, order.customerName, order.status]);
					});
				    datatable.draw();
				    addRowHandlers();
				}
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });
}

function addRowHandlers() {
    var table = document.getElementById("tableAllOrders");
    var rows = table.getElementsByTagName("tr");
    for (i = 0; i < rows.length; i++) {
        var currentRow = table.rows[i];
        var createClickHandler = 
            function(row) 
            {
                return function() { 
                    var cell = row.getElementsByTagName("td")[0];
                    var id = cell.innerHTML;
                    getOrderById(id);
				};
            };

        currentRow.onclick = createClickHandler(currentRow);
    }
}

function getOrderById(id) {
     $.ajax({
            url: 'http://localhost:8083/order/' + id ,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (order) {
                console.log("Successfully GET ORDER ");
				openOrderModal(order);
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });
}

function openOrderModal(order) {
    console.log("Alex openOrderModal :  " + JSON.stringify(order));
	let productQuantityArray = order.productsWithQuantities
    document.getElementById ("searchOrder").value = "";
    document.getElementById ("searchOrder").value = order.id;

	let datatable;
	if (productQuantityArray.length === 0) {
		datatable = $('#tableOneUser').DataTable({
		    "destroy": true
		});
	} else {
		datatable = $('#tableOneUser').DataTable({
        	"order": [[ 0, "asc" ]],
		    "destroy": true
    	});
	    datatable.clear();
	    productQuantityArray.forEach(function(productQuantity) {
			datatable.row.add([productQuantity.product.product_name, productQuantity.quantity]);
		});
	    datatable.draw();
	}
	findYourOrder();
}

function findYourOrder() {
    var header = document.getElementsByClassName("modal-header");
    header[0].style.backgroundColor = "#6E4D33";

    var modalHeaderText = document.getElementById("find-modal-header-text");
    modalHeaderText.innerHTML = "Order to be prepared or to update status";

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

function updateOrderState() {
    let id = document.getElementById ("searchOrder").value;
    let status = document.getElementById ("statusSelect").value;
    let request = {
		"id" : id,
		"status": status
	}
	$.ajax({
        url: 'http://localhost:8083/order/' + id + "/" + status,
        type: 'PUT',
        contentType: 'application/json',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function (result) {
            console.log("ALEX Successfully PUT ORDER ");
            document.getElementById("findModal").style.display = "none";
            getOrders();
        },
        error: function (xhr, status, error) {
            console.log("Something went wrong.");
            getOrders();
        }
    });
}

function showCreateUser() {
	hideAll();
    document.getElementById ("createUserInput").style.display = "";
    document.getElementById ("newUserDiv").style.display = "";
}

/*   Show password on hide password on admin add form        */
function show() {
  var a = document.getElementById("user-password");
  var b = document.getElementById("eye");
  if (a.type == "password") {
    a.type = "text";
  } else {
    a.type = "password";
  }
}

function createUser() {
    const username = document.getElementById ("username").value;
    const email = document.getElementById ("email").value;
    const password = document.getElementById ("user-password").value;
    const role = document.getElementById ("role").value;
    const request = {
        "username": username,
        "email": email,
        "password": password,
        "role": [role]
    }
     $.ajax({
            url: 'http://localhost:8083/signup',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
		    data: JSON.stringify(request),
            success: function (result) {
                console.log(JSON.stringify(result));
                
		    	createModalSuccess("User is created!");
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
		    	createModalFailure('Something went wrong. User could not be created. Status: ' + JSON.stringify(xhr.responseText));
            }
        });

}

function getUser() {
    const username = document.getElementById ("username").value;
    const email = document.getElementById ("email").value;
     $.ajax({
            url: 'http://localhost:8083/user/' + username + "/" + email,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (result) {
                console.log("Successfully GET USER " + result.username);
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });

}

function showDeleteUser() {
	hideAll();
    document.getElementById ("deleteUserDiv").style.display = "";
}

function deleteUser() {
    const username = document.getElementById ("usernameDelete").value;
    const email = document.getElementById ("emailDelete").value;
     $.ajax({
            url: 'http://localhost:8083/user/' + username + "/" + email,
            type: 'DELETE',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (result) {
                console.log("Successfully deleted");
                
		    	createModalSuccess("User is deleted!");
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
		    	createModalFailure('Something went wrong. User could not be deleted. Status: ' + JSON.stringify(xhr.responseText));
            }
        });

}

function showUpdateUser() {
	hideAll();
    document.getElementById ("updateUserInput").style.display = "";
    document.getElementById ("newUserDiv").style.display = "";
}

function updateUser() {
    const username = document.getElementById ("username").value;
    const email = document.getElementById ("email").value;
    const password = document.getElementById ("user-password").value;
    const role = document.getElementById ("role").value;
    const request = {
        "username": username,
        "email": email,
        "password": password,
        "role": [role]
    }
     $.ajax({
            url: 'http://localhost:8083/user/',
            type: 'PUT',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
		    data: JSON.stringify(request),
            success: function (result) {
                console.log("Successfully updated");
                
		    	createModalSuccess("User is deleted!");
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
		    	createModalFailure('Something went wrong. User could not be updated. Status: ' + JSON.stringify(xhr.responseText));
            }
        });

}

function getAllUsers() {
     $.ajax({
            url: 'http://localhost:8083/users',
            type: 'GET',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (users) {
                console.log("Successfully GET USERS ");
				hideAll();
                document.getElementById ("allUsersDiv").style.display = "";
				var datatable;
				if (users.length === 0) {
					datatable = $('#tableAllUsers').DataTable({
					    "destroy": true
					});
				} else {
					datatable = $('#tableAllUsers').DataTable({
			        	"order": [[ 0, "desc" ]],
					    "destroy": true
			    	});
				    datatable.clear();
				    users.forEach(function(user) {
						datatable.row.add([user.username, user.email, user.roles[0].name]);
					});
				    datatable.draw();
				}
				
				
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
		    	createModalFailure('Something went wrong. Could not get users from database. Status: ' + JSON.stringify(xhr.responseText));
            }
        });
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

function getStats() {
	$.ajax({
        url: 'http://localhost:8083/orders/week',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
		success: function (stats) {
			console.log("STATS : " + JSON.stringify(stats));
			hideAll();
			document.getElementById("salesDiv").style.display = "";
			init(stats);
		},
		error: function () {
			console.log("ALEX modal part");
		}
	}); 
}




