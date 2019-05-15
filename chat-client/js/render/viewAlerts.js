
// danger alert html code
function dangerAlert(data) {
    $("<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
        "  <strong>"+ data + "</strong>\n" +
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>").prependTo('body');
}

// success alert html code
function successAlert(data) {
    $("<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
        "  <strong>"+ data + "</strong>\n" +
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>").prependTo('body');
}