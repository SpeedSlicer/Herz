    window.addEventListener("DOMContentLoaded", () => {
    const box = document.getElementById("dragBox");
    const handle = document.getElementById("dragHandle");
    const button = document.getElementById("myButton");
    const textInput = document.getElementById("textInput");
    const opacitySlider = document.getElementById("opacitySlider");
    const opacityValue = document.getElementById("opacityValue");
    const blurSlider = document.getElementById("blurSlider");
    const blurValue = document.getElementById("blurValue");
    const colors = document.querySelectorAll(".color");

    let dragging = false;
    let offsetX = 0;
    let offsetY = 0;

    handle.addEventListener("pointerdown", (event) => {
    event.preventDefault();
    event.stopPropagation();

    if (document.pointerLockElement) {
    document.exitPointerLock();
}

    const rect = box.getBoundingClientRect();

    dragging = true;
    offsetX = event.clientX - rect.left;
    offsetY = event.clientY - rect.top;

    handle.setPointerCapture(event.pointerId);
});

    handle.addEventListener("pointermove", (event) => {
    if (!dragging) return;

    const maxX = Math.max(0, window.innerWidth - box.offsetWidth);
    const maxY = Math.max(0, window.innerHeight - box.offsetHeight);

    const x = Math.max(0, Math.min(event.clientX - offsetX, maxX));
    const y = Math.max(0, Math.min(event.clientY - offsetY, maxY));

    box.style.left = `${x}px`;
    box.style.top = `${y}px`;
});

    function stopDragging(event) {
    dragging = false;

    if (handle.hasPointerCapture(event.pointerId)) {
    handle.releasePointerCapture(event.pointerId);
}
}

    handle.addEventListener("pointerup", stopDragging);
    handle.addEventListener("pointercancel", stopDragging);

    opacitySlider.addEventListener("input", () => {
    const opacity = Number(opacitySlider.value);

    box.style.setProperty("--panel-opacity", opacity / 100);
    opacityValue.textContent = `${opacity}%`;
});

    blurSlider.addEventListener("input", () => {
    const blur = Number(blurSlider.value);

    box.style.setProperty("--panel-blur", `${blur}px`);
    blurValue.textContent = `${blur}px`;
});

    colors.forEach((colorButton) => {
    colorButton.addEventListener("click", () => {
    const hex = colorButton.dataset.color;
    const rgb = hexToRgb(hex);

    box.style.setProperty(
    "--panel-rgb",
    `${rgb.r}, ${rgb.g}, ${rgb.b}`
    );

    colors.forEach((item) => item.classList.remove("active"));
    colorButton.classList.add("active");
});
});

    button.addEventListener("click", () => {
    alert(textInput.value || "Button clicked!");
});

    function hexToRgb(hex) {
    const value = hex.replace("#", "");

    return {
    r: parseInt(value.substring(0, 2), 16),
    g: parseInt(value.substring(2, 4), 16),
    b: parseInt(value.substring(4, 6), 16)
};
}
});
