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