

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
    $("body").on('click', '#chat_li', function () {
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
    });

    $('body').on('click', '#create-chat', function () {
        chatTools();
    });

    $('body').on('click', 'i[id^="message-delete-"]', function () {
        var id = $(this).attr('id').split('-')[2];

        var chatId = $('#ThisChatId').val();
        deleteMessage(id, chatId);
    });

    $('body').on('click', 'i[id^="message-edit-"]', function () {

        var id = $(this).attr('id').split('-')[2];
        var value = $('#message-value-'+id).text();

        editMessage(value, id)
    });

    $('body').on('click', '#update-message', function () {
       var id = $('#input-message').attr('name');
       var value = $('#input-message').val();
       var chatId = $('#ThisChatId').val();

        updateMessage(value, chatId, id);

    });

    $('body').on('click', "#save-chat", function () {
        var name = $('#chat-name').val();

        var members = [];

        $('.contacts input:checked').each(function() {
            members.push($(this).attr('name'));
        });

        createChat(name, members);

    });
});

function updateMessage(value, chatId, messageId) {
    var token = Cookies.get('token');

    var userId = localStorage.getItem('id');

    var xhr = new XMLHttpRequest();

    var json = JSON.stringify({
        value: value,
        userId: userId
    });

    xhr.open('PUT', 'http://10.6.103.13:8000/api/v1/chats/' + chatId + "/messages/" + messageId, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send(json);

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                dangerAlert(JSON.parse(xhr.responseText).message);

                return false;
            } else {
                getChatInfo(chatId);
                successAlert('Success update');
            }
        }
    };
}


function editMessage(value, id) {
    general = "        <div class=\"card-footer\" id='general-message'>\n" +
        "            <div class=\"input-group\">\n" +
        "                <div class=\"input-group-append\">\n" +
        "                    <span class=\"input-group-text attach_btn\"><i class=\"fas fa-paperclip\"></i></span>\n" +
        "                </div>\n" +
        "                <input type='text' name='" + id +"' id='input-message' value='" + value + "' class=\"form-control type_msg\" ></input>\n" +
        "                <div class=\"input-group-append\">\n" +
        "                    <span class=\"input-group-text send_btn\" id='update-message'><i class=\"fas fa-location-arrow\" ></i></span>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" ;

    $('#general-message').replaceWith(general);
}

function deleteMessage(id, chatId) {
    var token = Cookies.get('token');


    var xhr = new XMLHttpRequest();


    xhr.open('DELETE', 'http://10.6.103.13:8000/api/v1/chats/' + chatId + "/messages/" + id, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                dangerAlert(JSON.parse(xhr.responseText).message);

                return false;
            } else {
                successAlert('Success deleted');
                $('#message-' + id).remove();
            }
        }
    };
}

function createChat(name, members) {
    var token = Cookies.get('token');

    var id = localStorage.getItem('id');

    members.push(id);
    var xhr = new XMLHttpRequest();

    var json = JSON.stringify({
        name: name,
        members: members
    });

    xhr.open('POST', 'http://10.6.103.13:8000/api/v1/chats', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send(json);

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                dangerAlert(JSON.parse(xhr.responseText).message);

                return false;
            } else {
                checkToken();
                successAlert('Success created');
            }
        }
    };
}

function chatTools() {
    var token = Cookies.get('token');

    var xhr = new XMLHttpRequest();

    xhr.open('GET', 'http://10.6.103.13:8000/api/v1/users', true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                dangerAlert(xhr.responseText);

                return false;
            } else {
                var json = JSON.parse(xhr.responseText);

                createChatView(json);
            }
        }
    };
}


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

function createChatView(json) {

    var users = json.response.users;
    var general = "";

    var name_array = [];
    var id_array = [];

    for (var i =0; i < users.length ;i++) {

        var id = users[i].id;
        var name = users[i].name;

        name_array.push(name.charAt(0));
        id_array.push(id);

        var li = "<li>\n" +
            "                            <div class=\"d-flex bd-highlight\">\n" +
            '<div class="my-4 mr-3">\n' +
            '<label class="container" >\n' +
            '  <input type="checkbox" name="' + id + '">\n' +
            '  <span class="checkmark"></span>\n' +
            '</label>'+
            '  </div>'+
            "                                <div class=\"img_cont\">\n" +
            "<canvas class='rounded-circle user_img' id='user-" + id + "'></canvas> " +
            "                                </div>\n" +
            "                                <div class=\"user_info\">\n" +
            "                                    <span class='chatName'>" + name + "</span>\n" +
            "<input type='hidden' class='userId' value=' " + id + "'>" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </li>";

        general += li;
    }

    var general = "<div class=\"col-md-8 col-xl-6 chat\" id=\"message\">\n" +
        "                <div class=\"card\">\n" +
        "                <div class=\"card-header\">\n" +
        "                    <div class=\"input-group\">\n" +
        "                        <input type=\"text\" placeholder=\"Chat name\" name=\"\" class=\"form-control search\" id='chat-name'>\n" +
        "                        <div class=\"input-group-prepend\">\n" +
        "                            <span class=\"input-group-text search_btn\"></span>\n" +
        '<i class="fas fa-user-plus h4 my-1 ml-2" id="save-chat" style="color:#DDDDDD;"></i>' +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "                        <div class=\"card-body contacts_body\">\n" +
        "<ui class=\"contacts\">\n" +
        general +
        "  </ui>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "</div>\n" +
        "</div>";

    $("#message").replaceWith(general);

    for (var i = 0; i < name_array.length ; i++) {
        drawIcon('user-' + id_array[i], name_array[i]);
    }

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

function addMessage(json) {

    var date = new Date(json.response.message.date);

    var temp = "<div class=\"d-flex justify-content-end mb-4\" id='message-" + json.response.message.id +"'>\n" +
        "                <div class=\"msg_cotainer_send\">\n" +
        json.response.message.value +
        "                    <span class=\"msg_time_send\">" + date.toDateString() + "</span>\n" +
        "                </div>\n" +
        "                <div class=\"img_cont_msg\">\n" +
        "                    <canvas class='rounded-circle user_img_msg' id='message-photo-" + json.response.message.id  + "'> </canvas>\n" +
        "                </div>\n" +
        '<i class="fas fa-trash blackiconcolor my-2 ml-2" id="message-delete-' + json.response.message.id + '"></i>' +
        '<i class="fas fa-pen blackiconcolor my-2 ml-2" id="message-edit-' + json.response.message.id + '"></i>' +
        "            </div>";


    $(temp).appendTo('#new-message');
    drawIcon("message-photo-" + json.response.message.id, json.response.message.user.name.charAt(0));
    $("#input-message").val("");
}

function checkToken() {
    var xhr = new XMLHttpRequest();

    var token = Cookies.get('token');
    var id = localStorage.getItem("id");
    var url = 'http://10.6.103.13:8000/api/v1/chats';
    var param = 'userId=' + id;

    xhr.open('GET', url + "?" + param, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if(xhr.status != 200) {
                getLoginPage();
                return false;
            } else {


                var json = JSON.parse(xhr.responseText);
                chat(json);
            }
        }
    };
}

function drawIcon(id, text) {
    const canvas = document.getElementById(id);
    canvas.width = 64;
    canvas.height = 64;
    const context = canvas.getContext("2d");
    // Draw background
    context.fillStyle = '#f4f5f7';
    context.fillRect(0, 0, canvas.width, canvas.height);
    // Draw text
    context.fillStyle = "#080808";
    context.font = "30px Helvetica";
    context.textBaseline = "middle";
    context.textAlign = "center";
    const x = canvas.width / 2;
    const y = canvas.height / 2;
    context.fillText(text, x, y);
}

function successAlert(data) {
    $("<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
        "  <strong>"+ data + "</strong>\n" +
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>").prependTo('body');
}

function dangerAlert(data) {
    $("<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
        "  <strong>"+ data + "</strong>\n" +
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>").prependTo('body');
}

function chat(json) {

    var chat_li = "";
    var id_array = [];
    var name_array = [];

    for (var i = 0; i < json.response.chats.length; i++) {
        var name = json.response.chats[i].name;
        var id = json.response.chats[i].id;

        id_array.push(id);
        name_array.push(name.charAt(0));

        var li = "<li id='chat_li'>\n" +
            "                            <div class=\"d-flex bd-highlight\">\n" +
            "                                <div class=\"img_cont\">\n" +
            "<canvas class='rounded-circle user_img' id='chat-" + id + "'></canvas> " +
            "                                </div>\n" +
            "                                <div class=\"user_info\">\n" +
            "                                    <span class='chatName'>" + name + "</span>\n" +
            "<input type='hidden' class='chatId' value=' " + id + "'>" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </li>";

        chat_li += li;
    }


    $("#general").replaceWith("<div id=\"general\">\n" +
        "    <div class=\"container-fluid h-100\">\n" +
        "        <div class=\"row justify-content-center h-100\">\n" +
        "            <div class=\"col-md-4 col-xl-3 chat\"><div class=\"card mb-sm-3 mb-md-0 contacts_card\">\n" +
        "                <div class=\"card-header\">\n" +
        "                    <div class=\"input-group\">\n" +
        "                        <input type=\"text\" placeholder=\"Search...\" name=\"\" class=\"form-control search\">\n" +
        "                        <div class=\"input-group-prepend\">\n" +
        "                            <span class=\"input-group-text search_btn\"><i class=\"fas fa-search\"></i></span>\n" +
        "                        </div>\n" +
        "<a id='logout'> <i class=\"fas fa-sign-out-alt my-1 ml-3 h4\" id='my-icon' style=\"color:#DDDDDD;\"></i> </a>" +
        "<a id='create-chat'><i class=\"fas fa-plus  my-1 ml-3 h4\" style=\"color:#DDDDDD;\"></i></a>" +
        "                    </div>\n" +
        "                </div>\n" +
        "                <div class=\"card-body contacts_body\">\n" +
        "                    <ui class=\"contacts\">\n" +
        chat_li +
        "                    </ui>\n" +
        "                </div>\n" +
        "                <div class=\"card-footer\"></div>\n" +
        "            </div></div>\n" +
        "            <div id=\"message\">\n" +

        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>");

    for (var i = 0; i < id_array.length; i++) {
        drawIcon("chat-" + id_array[i], name_array[i]);
    }
}

function getLoginPage() {
    $("#general").replaceWith('<div id="general">\n' +
        '    <div class="row my-5">\n' +
        '        <div class="col-md-4"></div>\n' +
        '        <div class="col-md-2">\n' +
        '            <h4 class="text-center">Sign in</h4>\n' +
        '            <form name="user" >\n' +
        '                <label>Login</label>\n' +
        '                <input type="text" class="form-control" name="login" id="login" value="user">\n' +
        '                <label>Password</label>\n' +
        '                <input type="password" class="form-control" name="password" id="password" value="password">\n' +
        '\n' +
        '            </form>\n' +
        '            <button id="btn-login" class="btn btn-light my-3">Submit</button>\n' +
        '            <button id="btn-view-signup" class="btn btn-primary my-1">Sign up</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>');
}

function viewMessages(json) {

    var messages = json.response.messages;

    if (messages.length == 0) {
        var general = "<div class=\"col-md-8 col-xl-6 chat\" id=\"message\">\n" +
            "                <div class=\"card\">\n" +
            "                    <div class=\"card-header msg_head\">\n" +
            "                        <div class=\"d-flex bd-highlight\">\n" +
            "                            <div class=\"img_cont\">\n" +
            "                                <canvas id='main-chat-photo' class=\"rounded-circle user_img\"> </canvas>\n" +

            "                            </div>\n" +
            "                            <div class=\"user_info\">\n" +
            "                                <span>"+ json.response.chat.name +"</span>\n" +
            "<input type=\"hidden\" id=\"ThisChatId\" value='" + json.response.chat.id +"'>" +
            "                                <p>1767 Messages</p>\n" +
            "                            </div>\n" +
            "                            <div class=\"video_cam\">\n" +
            "                                <span><i class=\"fas fa-video\"></i></span>\n" +
            "                                <span><i class=\"fas fa-phone\"></i></span>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"card-body msg_card_body\" id='new-message'>"+
            "</div>\n" +
            "        <div class=\"card-footer\">\n" +
            "            <div class=\"input-group\">\n" +
            "                <div class=\"input-group-append\">\n" +
            "                    <span class=\"input-group-text attach_btn\"><i class=\"fas fa-paperclip\"></i></span>\n" +
            "                </div>\n" +
            "                <textarea id='input-message' name=\"\" class=\"form-control type_msg\" placeholder=\"Type your message...\"></textarea>\n" +
            "                <div class=\"input-group-append\">\n" +
            "                    <span class=\"input-group-text send_btn\" id='send-message'><i class=\"fas fa-location-arrow\" ></i></span>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";

        $("#message").replaceWith(general);
        drawIcon("main-chat-photo", json.response.chat.name.charAt(0));


    } else {
        var id_array = [];
        var name_array = [];

        var general = "<div class=\"col-md-8 col-xl-6 chat\" id=\"message\">\n" +
            "                <div class=\"card\">\n" +
            "                    <div class=\"card-header msg_head\">\n" +
            "                        <div class=\"d-flex bd-highlight\">\n" +
            "                            <div class=\"img_cont\">\n" +
            "                                <canvas id='main-chat-photo' class=\"rounded-circle user_img\"> </canvas>\n" +

            "                            </div>\n" +
            "                            <div class=\"user_info\">\n" +
            "                                <span>"+ json.response.messages[0].chat.name +"</span>\n" +
            "<input type=\"hidden\" id=\"ThisChatId\" value='" + messages[0].chat.id +"'>" +
            "                                <p>1767 Messages</p>\n" +
            "                            </div>\n" +
            "                            <div class=\"video_cam\">\n" +
            "                                <span><i class=\"fas fa-video\" ></i></span>\n" +
            "                                <span><i class=\"fas fa-phone\"></i></span>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"card-body msg_card_body\" id='new-message'>";




        for (var i = 0; i < messages.length; i++) {
            var temp = "";
            var userId = localStorage.getItem("id");
            var date = new Date(messages[i].date);
            var value = messages[i].value ;
            var id = messages[i].id;

            id_array.push(messages[i].id);

            name_array.push(messages[i].user.name.charAt(0));

            if(messages[i].user.id != userId) {

                temp += "<div class=\"d-flex justify-content-start mb-4\" id='message-" + messages[i].id +"'>\n" +
                    "                            <div class=\"img_cont_msg\">\n" +
                    "                                <canvas class='rounded-circle user_img_msg' id='message-photo-" +messages[i].id  + "'> </canvas>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"msg_cotainer\">\n" +
                    value +
                    "                                <span class=\"msg_time\">" + date.toDateString() + "</span>\n" +
                    "                            </div>\n" +
                    "                        </div>";
            } else {

                temp += "<div class=\"d-flex justify-content-end mb-4\" id='message-" + messages[i].id +"'>\n" +
                    "                <div class=\"msg_cotainer_send\" >\n" +
                    "<div id='message-value-" + id +"'>" +
                    value +
                    "</div>" +
                    "                    <span class=\"msg_time_send\">" + date.toDateString() + "</span>\n" +
                    "                </div>\n" +
                    "                <div class=\"img_cont_msg\">\n" +
                    "                    <canvas class='rounded-circle user_img_msg' id='message-photo-" + messages[i].id  + "'> </canvas>\n" +
                    "                </div>\n" +
                    '<i class="fas fa-trash blackiconcolor my-2 ml-2" id="message-delete-' + id + '"></i>' +
                    '<i class="fas fa-pen blackiconcolor my-2 ml-2" id="message-edit-' + id + '"></i>' +
                    "            </div>";
            }

            general += temp;
        }

        general += "</div>\n" +
            "        <div class=\"card-footer\" id='general-message'>\n" +
            "            <div class=\"input-group\">\n" +
            "                <div class=\"input-group-append\">\n" +
            "                    <span class=\"input-group-text attach_btn\"><i class=\"fas fa-paperclip\"></i></span>\n" +
            "                </div>\n" +
            "                <input type='text' id='input-message' name=\"\" class=\"form-control type_msg\" placeholder=\"Type your message...\"></input>\n" +
            "                <div class=\"input-group-append\">\n" +
            "                    <span class=\"input-group-text send_btn\" id='send-message'><i class=\"fas fa-location-arrow\" ></i></span>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";

        $("#message").replaceWith(general);

        for(var i = 0;i<name_array.length;i++) {
            drawIcon("message-photo-" + id_array[i], name_array[i]);
        }
        drawIcon("main-chat-photo", json.response.messages[0].chat.name.charAt(0));
    }

}

function getRegistrationPage() {
    $("#general").replaceWith('<div id="general">\n' +
        '    <div class="row my-5">\n' +
        '        <div class="col-md-4"></div>\n' +
        '        <div class="col-md-2">\n' +
        '            <h4 class="text-center">Sign up</h4>\n' +
        '            <form name="user" >\n' +
        '                <label>Login</label>\n' +
        '                <input type="text" class="form-control" id="login">\n' +
        '                <label>Password</label>\n' +
        '                <input type="password" class="form-control"  id="password">\n' +
        '                <label>Password confirm</label>\n' +
        '                <input type="password" class="form-control"  id="passwordConfirm" >\n' +
        '                <label>Name</label>\n' +
        '                <input type="text" class="form-control"  id="name">\n' +
        '            </form>\n' +
        '            <button id="btn-signup" class="btn btn-light my-3">Submit</button>\n' +
        '            <button id="btn-view-login" class="btn btn-primary my-1">Login</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>');
}





