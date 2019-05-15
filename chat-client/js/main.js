


$( document ).ready(function() {

    // check token load chat or login html code
    checkToken();

    // after click on sign up , appears registration form
    $('body').on('click', '#btn-view-signup', function () {

        getRegistrationPage();
    });

    // after click on login , appears login form
    $('body').on('click', '#btn-view-login',function () {
        getLoginPage();
    });

    // registration logic, after success appears login form
    $('body').on('click', '#btn-signup', function () {
        if (signup()) {
            successAlert("User was successful created");
            getLoginPage();
        }
    });

    // after click on chat y will show this messages in this chat
    $("body").on('click', 'li', function () {
        var id = $(this).find('.chatId').val();
        console.log("click li: " + id);
        getChatInfo(id);
    });

    // login logic, after success appears chat and token save in cookie
    $("body").on('click', '#btn-login', function () {
        var json = login();

        if (json != null) {
            successAlert("Success sign in");

            checkToken();

            var token = json.response.token;
            var id = json.response.id;

            localStorage.setItem("id", id);
            Cookies.set("token", token);
            checkToken();
        }
    });

    // click logout , token will remove
    $('body').on('click', '#logout', function () {
        Cookies.remove('token');
        checkToken();
    });


    $('body').on('click', '#send-message', function () {
        var value = $('#input-message').val();
        var chatId = $('#ThisChatId').val();

        sendMessage(value, chatId);
    })
});



function sendMessage(value, chatId) {
    var xhr = new XMLHttpRequest();

    var userId = localStorage.getItem("id");
    var token = Cookies.get('token');

    var json = JSON.stringify({
        value: value,
        userId: userId
    });

    xhr.open('POST', 'http://10.6.103.13:8000/api/v1/chats/' + chatId + '/messages', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send(json);

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                dangerAlert('Can not send message');
                //getLoginPage();
                return false;
            } else {
                var json = JSON.parse(xhr.responseText);

                addMessage(json);
            }
        }
    };
}


// login request
function login() {
    var xhr = new XMLHttpRequest();
    var login = document.getElementById('login').value;
    var password  = document.getElementById("password").value;

    var json = JSON.stringify({
        login: login,
        password: password
    });

    xhr.open('POST', 'http://10.6.103.13:8000/api/v1/auth/signin', false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(json);

    if (xhr.status != 200) {

        var mes = JSON.parse(xhr.responseText).message;

        dangerAlert(mes);

        return null;
    } else {
        var json = JSON.parse(xhr.responseText);

        return json;
    }
}


// signup request
function signup() {
    var xhr = new XMLHttpRequest();
    var login = document.getElementById('login').value;
    var password  = document.getElementById("password").value;
    var passwordConfirm = document.getElementById("passwordConfirm").value;
    var name = document.getElementById("name").value;


    var json = JSON.stringify({
        login: login,
        password: password,
        passwordConfirm: passwordConfirm,
        name: name
    });

    xhr.open('POST', 'http://10.6.103.13:8000/api/v1/auth/signup', false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(json);

    if (xhr.status != 200) {
        var mes = JSON.parse(xhr.responseText).message;

        dangerAlert(mes);
        return false;
    } else {
        return true;
    }
}







function getChatInfo(id) {
    var xhr = new XMLHttpRequest();

    var token = Cookies.get('token');
    var url = 'http://10.6.103.13:8000/api/v1/chats/' + id + "/messages/";
    xhr.open('GET', url, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                //getLoginPage();
                return false;
            } else {

                var json = JSON.parse(xhr.responseText);
                viewMessages(json);
            }
        }
    };
}





