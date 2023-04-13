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
            document.getElementById ("content").style.display = "";
            document.getElementById ("login").style.display = "none";
            let token = result.accessToken;
            localStorage.setItem('token', token);
            const username = result.username;
            let rolesArray = result.roles;

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
            success: function (result) {
                result.forEach(function(order) {
                    console.log(order.customer_name);
                });
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });
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
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });

}

function deleteUser() {
    const username = document.getElementById ("username").value;
    const email = document.getElementById ("email").value;
     $.ajax({
            url: 'http://localhost:8083/user/' + username + "/" + email,
            type: 'DELETE',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (result) {
                console.log("Successfully deleted");
            },
            error: function (xhr, status, error) {
                console.log("Something went wrong.");
            }
        });

}