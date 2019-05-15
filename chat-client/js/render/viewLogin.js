// login html code
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