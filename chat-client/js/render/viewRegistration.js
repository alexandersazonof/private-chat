// registration html code
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