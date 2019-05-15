import {drawIcon} from "../function/drawIcon";

export function viewMessages(json) {

    var messages = json.response.messages;
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
        "                                <span><i class=\"fas fa-video\"></i></span>\n" +
        "                                <span><i class=\"fas fa-phone\"></i></span>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                        <span id=\"action_menu_btn\"><i class=\"fas fa-ellipsis-v\"></i></span>\n" +
        "                        <div class=\"action_menu\">\n" +
        "                            <ul>\n" +
        "                                <li><i class=\"fas fa-user-circle\"></i> View profile</li>\n" +
        "                                <li><i class=\"fas fa-users\"></i> Add to close friends</li>\n" +
        "                                <li><i class=\"fas fa-plus\"></i> Add to group</li>\n" +
        "                                <li><i class=\"fas fa-ban\"></i> Block</li>\n" +
        "                            </ul>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                    <div class=\"card-body msg_card_body\" id='new-message'>";




    for (var i = 0; i < messages.length; i++) {
        var temp = "";
        var userId = localStorage.getItem("id");
        var date = new Date(messages[i].date);
        var value = messages[i].value ;

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

            temp += "<div class=\"d-flex justify-content-end mb-4\" id='message-'" + messages[i].id +">\n" +
                "                <div class=\"msg_cotainer_send\">\n" +
                value +
                "                    <span class=\"msg_time_send\">" + date.toDateString() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"img_cont_msg\">\n" +
                "                    <canvas class='rounded-circle user_img_msg' id='message-photo-" + messages[i].id  + "'> </canvas>\n" +
                "                </div>\n" +
                "            </div>";
        }

        general += temp;
    }

    general += "</div>\n" +
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

    for(var i = 0;i<name_array.length;i++) {
        drawIcon("message-photo-" + id_array[i], name_array[i]);
    }
    drawIcon("main-chat-photo", json.response.messages[0].chat.name.charAt(0));
}