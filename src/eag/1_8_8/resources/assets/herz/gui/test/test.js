(function () {
    const box = document.getElementById("dragBox");
    const handle = document.getElementById("dragHandle");
    const button = document.getElementById("myButton");
    const textInput = document.getElementById("textInput");
    const opacitySlider = document.getElementById("opacitySlider");
    const opacityValue = document.getElementById("opacityValue");
    const blurSlider = document.getElementById("blurSlider");
    const blurValue = document.getElementById("blurValue");
    const colors = document.querySelectorAll("#palette .color");

    if (!box || !handle) {
        console.error("Drag panel elements were not found.");
        return;
    }


    let dragging = false;
    let offsetX = 0;
    let offsetY = 0;

    handle.addEventListener("pointerdown", (event) => {
        event.preventDefault();
        event.stopPropagation();

        if (document.pointerLockElement && document.exitPointerLock) {
            document.exitPointerLock();
        }

        const rect = box.getBoundingClientRect();

        dragging = true;
        offsetX = event.clientX - rect.left;
        offsetY = event.clientY - rect.top;

        handle.setPointerCapture?.(event.pointerId);
    });

    handle.addEventListener("pointermove", (event) => {
        if (!dragging) return;

        event.preventDefault();
        event.stopPropagation();

        const maxX = Math.max(0, window.innerWidth - box.offsetWidth);
        const maxY = Math.max(0, window.innerHeight - box.offsetHeight);

        const x = Math.max(
            0,
            Math.min(event.clientX - offsetX, maxX)
        );

        const y = Math.max(
            0,
            Math.min(event.clientY - offsetY, maxY)
        );

        box.style.left = `${x}px`;
        box.style.top = `${y}px`;
    });

    function stopDragging(event) {
        dragging = false;

        if (
            handle.hasPointerCapture &&
            handle.hasPointerCapture(event.pointerId)
        ) {
            handle.releasePointerCapture(event.pointerId);
        }
    }

    handle.addEventListener("pointerup", stopDragging);
    handle.addEventListener("pointercancel", stopDragging);

    if (opacitySlider && opacityValue) {
        function updateOpacity() {
            const opacity = Number(opacitySlider.value);

            box.style.setProperty(
                "--panel-opacity",
                String(opacity / 100)
            );

            opacityValue.textContent = `${opacity}%`;
        }

        opacitySlider.addEventListener("input", updateOpacity);
        updateOpacity();
    }

    if (blurSlider && blurValue) {
        function updateBlur() {
            const blur = Number(blurSlider.value);

            box.style.setProperty("--panel-blur", `${blur}px`);
            blurValue.textContent = `${blur}px`;
        }

        blurSlider.addEventListener("input", updateBlur);
        updateBlur();
    }

    function hexToRgb(hex) {
        const normalized = hex.replace("#", "");

        if (!/^[0-9a-fA-F]{6}$/.test(normalized)) {
            return null;
        }

        return {
            r: parseInt(normalized.substring(0, 2), 16),
            g: parseInt(normalized.substring(2, 4), 16),
            b: parseInt(normalized.substring(4, 6), 16)
        };
    }

    colors.forEach((colorButton) => {
        colorButton.addEventListener("click", (event) => {
            event.preventDefault();
            event.stopPropagation();

            const hex = colorButton.dataset.color;
            const rgb = hexToRgb(hex);

            if (!rgb) return;

            box.style.setProperty(
                "--panel-rgb",
                `${rgb.r}, ${rgb.g}, ${rgb.b}`
            );

            colors.forEach((item) => {
                item.classList.remove("active");
            });

            colorButton.classList.add("active");
        });
    });

    if (button && textInput) {
        button.addEventListener("click", (event) => {
            event.preventDefault();
            event.stopPropagation();

            const text = textInput.value.trim();
        });
    }

    if (textInput) {
        const stopGameInput = (event) => {
            event.stopPropagation();
        };

        textInput.addEventListener("pointerdown", (event) => {
            event.stopPropagation();

            if (document.pointerLockElement && document.exitPointerLock) {
                document.exitPointerLock();
            }

            textInput.focus();
        });

        textInput.addEventListener("click", (event) => {
            event.stopPropagation();
            textInput.focus();
        });

        textInput.addEventListener("keydown", stopGameInput);
        textInput.addEventListener("keyup", stopGameInput);
        textInput.addEventListener("keypress", stopGameInput);
        textInput.addEventListener("input", stopGameInput);
        textInput.addEventListener("wheel", stopGameInput);
    }
    box.addEventListener("pointerdown", (event) => {
        event.stopPropagation();
    });

    box.addEventListener("click", (event) => {
        event.stopPropagation();
    });
}());