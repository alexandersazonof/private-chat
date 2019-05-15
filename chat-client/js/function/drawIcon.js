export function drawIcon(id, text) {
    const canvas = document.getElementById(id);
    canvas.width = 64;
    canvas.height = 64;
    const context = canvas.getContext("2d");
    // Draw background
    context.fillStyle = '#f4f5f7';
    context.fillRect(0, 0, canvas.width, canvas.height);
    // Draw text
    context.fillStyle = "#FFFFFF";
    context.font = "30px Helvetica";
    context.textBaseline = "middle";
    context.textAlign = "center";
    const x = canvas.width / 2;
    const y = canvas.height / 2;
    context.fillText(text, x, y);
}