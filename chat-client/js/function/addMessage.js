import {drawIcon} from 'drawIcon';

export function addMessage(json) {

    var date = new Date(json.response.message.date);

    var temp = "<div class=\"d-flex justify-content-end mb-4\" id='message-" + json.response.message.id +"'>\n" +
        "                <div class=\"msg_cotainer_send\">\n" +
        json.response.message.value +
        "                    <span class=\"msg_time_send\">" + date.toDateString() + "</span>\n" +
        "                </div>\n" +
        "                <div class=\"img_cont_msg\">\n" +
        "                    <canvas class='rounded-circle user_img_msg' id='message-photo-" + json.response.message.id  + "'> </canvas>\n" +
        "                </div>\n" +
        "            </div>";


    $(temp).appendTo('#new-message');
    drawIcon("message-photo-" + json.response.message.id, json.response.message.user.name.charAt(0));
    $("#input-message").val("");
}
