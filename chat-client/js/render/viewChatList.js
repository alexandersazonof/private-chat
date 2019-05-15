function chat(json) {

    var chat_li = "";
    var id_array = [];
    var name_array = [];

    for (var i = 0 ; i < json.response.chats.length ; i++) {
        var name = json.response.chats[i].name;
        var id = json.response.chats[i].id;

        id_array.push(id);
        name_array.push(name.charAt(0));

        var li = "<li>\n" +
            "                            <div class=\"d-flex bd-highlight\">\n" +
            "                                <div class=\"img_cont\">\n" +
            "<canvas class='rounded-circle user_img' id='chat-"+ id + "'></canvas> "+
            "                                </div>\n" +
            "                                <div class=\"user_info\">\n" +
            "                                    <span class='chatName'>" + name + "</span>\n" +
            "<input type='hidden' class='chatId' value=' " + id + "'>"  +
            "                                    <p>Offline</p>\n" +
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
        "<a id='logout'> <i class=\"fas fa-sign-out-alt my-1 ml-3 h4\" id='my-icon'></i> </a>"+
        "<a id='create-chat'><i class=\"fas fa-plus  my-1 ml-3 h4\"></i></a>"+
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

    for (var i =0;i<id_array.length;i++) {
        drawIcon("chat-" + id_array[i], name_array[i]);
    }
}