(function () {

    const root =
        document.getElementById("herz-clickgui-root");

    if (!root) {
        console.error(
            "[Herz] ClickGUI root missing"
        );

        return;
    }


    /* =====================================================
       STATE
       ===================================================== */

    let guiOpen = false;

    let currentCategory =
        "combat";

    let captureTarget =
        null;

    let dragging =
        false;

    let dragStartX =
        0;

    let dragStartY =
        0;

    let windowStartX =
        0;

    let windowStartY =
        0;

    let windowX =
        0;

    let windowY =
        0;


    const STORAGE_GUI_BIND =
        "herz.clickgui.bind";

    const STORAGE_MODULE_BINDS =
        "herz.clickgui.moduleBinds";


    let guiBind =
        localStorage.getItem(
            STORAGE_GUI_BIND
        ) || "ShiftRight";


    let moduleBinds = {};

    try {

        moduleBinds =
            JSON.parse(
                localStorage.getItem(
                    STORAGE_MODULE_BINDS
                ) || "{}"
            );

    } catch (e) {

        moduleBinds = {};

    }


    /* =====================================================
       ELEMENTS
       ===================================================== */

    const windowElement =
        document.getElementById(
            "hz-window"
        );

    const dragHandle =
        document.getElementById(
            "hz-drag-handle"
        );

    const closeButton =
        document.getElementById(
            "hz-close"
        );

    const search =
        document.getElementById(
            "hz-search"
        );

    const categoryTitle =
        document.getElementById(
            "hz-category-title"
        );

    const categoryDescription =
        document.getElementById(
            "hz-category-description"
        );

    const visibleCount =
        document.getElementById(
            "hz-visible-count"
        );

    const guiBindButton =
        document.getElementById(
            "hz-open-bind"
        );

    const footerGuiBind =
        document.getElementById(
            "hz-footer-open-bind"
        );

    const keyCapture =
        document.getElementById(
            "hz-key-capture"
        );

    const keyCaptureTitle =
        document.getElementById(
            "hz-key-capture-title"
        );


    const categoryButtons =
        Array.from(
            root.querySelectorAll(
                ".hz-category"
            )
        );


    const moduleCards =
        Array.from(
            root.querySelectorAll(
                ".hz-module-card"
            )
        );


    const toggles =
        Array.from(
            root.querySelectorAll(
                ".hz-toggle"
            )
        );


    const bindButtons =
        Array.from(
            root.querySelectorAll(
                ".hz-bind-button"
            )
        );


    /* =====================================================
       KEY DISPLAY NAMES
       ===================================================== */

    function keyName(code) {

        const aliases = {
            "ShiftRight": "RSHIFT",
            "ShiftLeft": "LSHIFT",

            "ControlRight": "RCTRL",
            "ControlLeft": "LCTRL",

            "AltRight": "RALT",
            "AltLeft": "LALT",

            "Space": "SPACE",

            "Escape": "ESC",

            "Backspace": "BACKSPACE",

            "Enter": "ENTER",

            "Tab": "TAB",

            "ArrowUp": "↑",
            "ArrowDown": "↓",
            "ArrowLeft": "←",
            "ArrowRight": "→",

            "CapsLock": "CAPS",

            "BracketLeft": "[",
            "BracketRight": "]",

            "Semicolon": ";",

            "Quote": "'",

            "Comma": ",",

            "Period": ".",

            "Slash": "/",

            "Backslash": "\\",

            "Minus": "-",

            "Equal": "=",

            "Backquote": "`"
        };


        if (aliases[code]) {
            return aliases[code];
        }


        if (
            code.startsWith(
                "Key"
            )
        ) {
            return code.substring(3);
        }


        if (
            code.startsWith(
                "Digit"
            )
        ) {
            return code.substring(5);
        }


        if (
            code.startsWith(
                "Numpad"
            )
        ) {
            return "NUM " +
                code.substring(6);
        }


        return code.toUpperCase();

    }


    /* =====================================================
       GUI OPEN / CLOSE
       ===================================================== */
    function setOpen(value) {

        guiOpen = value;

        root.classList.toggle(
            "open",
            guiOpen
        );

        if (guiOpen) {

            captureGameInput();

        } else {

            search.blur();

            releaseGameInput();

        }
    }


    function toggleGUI() {

        setOpen(
            !guiOpen
        );

    }


    closeButton.addEventListener(
        "click",
        function () {
            setOpen(false);
        }
    );


    /* =====================================================
       KEY CAPTURE
       ===================================================== */

    function beginKeyCapture(
        target
    ) {

        captureTarget =
            target;


        let title =
            target === "gui"
                ? "ClickGUI key"
                : target + " key";


        keyCaptureTitle.textContent =
            title;


        keyCapture.classList.add(
            "active"
        );

    }


    function cancelKeyCapture() {

        captureTarget =
            null;


        keyCapture.classList.remove(
            "active"
        );

    }


    function assignCapturedKey(
        code
    ) {

        if (
            !captureTarget
        ) {
            return;
        }


        if (
            captureTarget === "gui"
        ) {

            guiBind =
                code;


            localStorage.setItem(
                STORAGE_GUI_BIND,
                guiBind
            );


            refreshGuiBind();

        } else {

            moduleBinds[
                captureTarget
                ] = code;


            saveModuleBinds();

            refreshModuleBinds();

        }


        cancelKeyCapture();

    }


    function clearCapturedKey() {

        if (
            !captureTarget
        ) {
            return;
        }


        if (
            captureTarget === "gui"
        ) {

            /*
             * Keep ClickGUI reachable.
             */

            guiBind =
                "ShiftRight";


            localStorage.setItem(
                STORAGE_GUI_BIND,
                guiBind
            );


            refreshGuiBind();

        } else {

            delete moduleBinds[
                captureTarget
                ];


            saveModuleBinds();

            refreshModuleBinds();

        }


        cancelKeyCapture();

    }


    function saveModuleBinds() {

        localStorage.setItem(
            STORAGE_MODULE_BINDS,
            JSON.stringify(
                moduleBinds
            )
        );

    }


    /* =====================================================
       KEYBOARD
       ===================================================== */

    document.addEventListener(
        "keydown",
        function (event) {

            /*
             * KEY CAPTURE MODE
             */

            if (
                captureTarget
            ) {

                if (
                    event.repeat
                ) {
                    return;
                }


                event.preventDefault();


                if (
                    event.code ===
                    "Escape"
                ) {

                    cancelKeyCapture();

                    return;
                }


                if (
                    event.code ===
                    "Backspace"
                ) {

                    clearCapturedKey();

                    return;
                }


                assignCapturedKey(
                    event.code
                );

                return;
            }


            /*
             * Ignore repeated bind presses.
             */

            if (
                event.repeat
            ) {
                return;
            }


            /*
             * GUI BIND
             */

            if (
                event.code ===
                guiBind
            ) {

                event.preventDefault();

                toggleGUI();

                return;
            }


            /*
             * ESCAPE
             */

            if (
                guiOpen &&
                event.code ===
                "Escape"
            ) {

                event.preventDefault();

                setOpen(false);

                return;
            }


            /*
             * SEARCH SHORTCUT
             */

            if (
                guiOpen &&
                event.code ===
                "Slash" &&
                document.activeElement !==
                search
            ) {

                event.preventDefault();

                search.focus();

                search.select();

                return;
            }


            /*
             * Don't run module binds while
             * typing in an input.
             */

            const active =
                document.activeElement;


            if (
                active &&
                (
                    active.tagName ===
                    "INPUT" ||

                    active.tagName ===
                    "TEXTAREA"
                )
            ) {
                return;
            }


            /*
             * MODULE BINDS
             */

            for (
                const moduleId
                in moduleBinds
                ) {

                if (
                    moduleBinds[
                        moduleId
                        ] !==
                    event.code
                ) {
                    continue;
                }


                const toggle =
                    root.querySelector(
                        '.hz-toggle[data-module="' +
                        moduleId +
                        '"]'
                    );


                if (
                    toggle
                ) {

                    event.preventDefault();

                    /*
                     * This triggers both:
                     *
                     * JS click handler
                     * AND
                     * your Java addClickListener()
                     */

                    toggle.click();

                }

            }

        },
        true
    );


    /* =====================================================
       GUI BIND BUTTONS
       ===================================================== */

    guiBindButton.addEventListener(
        "click",
        function () {

            beginKeyCapture(
                "gui"
            );

        }
    );


    footerGuiBind.addEventListener(
        "click",
        function () {

            beginKeyCapture(
                "gui"
            );

        }
    );


    function refreshGuiBind() {

        const name =
            keyName(
                guiBind
            );


        guiBindButton.textContent =
            name;


        footerGuiBind.textContent =
            name;

    }


    /* =====================================================
       MODULE BINDS
       ===================================================== */

    bindButtons.forEach(
        function (button) {

            button.addEventListener(
                "click",
                function (event) {

                    event.stopPropagation();


                    beginKeyCapture(
                        button.dataset.bindTarget
                    );

                }
            );

        }
    );


    function refreshModuleBinds() {

        bindButtons.forEach(
            function (button) {

                const id =
                    button.dataset.bindTarget;


                const key =
                    moduleBinds[id];


                const label =
                    button.querySelector(
                        "strong"
                    );


                label.textContent =
                    key
                        ? keyName(key)
                        : "NONE";

            }
        );

    }


    /* =====================================================
       TOGGLE VISUALS
       ===================================================== */

    toggles.forEach(
        function (toggle) {

            toggle.addEventListener(
                "click",
                function () {

                    const enabled =
                        !toggle.classList.contains(
                            "enabled"
                        );


                    toggle.classList.toggle(
                        "enabled",
                        enabled
                    );


                    toggle.setAttribute(
                        "aria-pressed",
                        enabled
                    );


                    const moduleId =
                        toggle.dataset.module;


                    const card =
                        root.querySelector(
                            '.hz-module-card[data-module="' +
                            moduleId +
                            '"]'
                        );


                    if (
                        card
                    ) {

                        card.classList.toggle(
                            "enabled",
                            enabled
                        );

                    }

                }
            );

        }
    );


    /* =====================================================
       CATEGORY DATA
       ===================================================== */

    const categoryInfo = {

        combat: {
            title: "Combat",
            description:
                "Combat automation and interaction modules."
        },

        movement: {
            title: "Movement",
            description:
                "Movement, speed and navigation modules."
        },

        player: {
            title: "Player",
            description:
                "Player utilities and inventory behavior."
        },

        render: {
            title: "Render",
            description:
                "Visual modules and world rendering."
        },

        client: {
            title: "Client",
            description:
                "Customize Herz itself."
        }

    };


    function selectCategory(
        category
    ) {

        currentCategory =
            category;


        categoryButtons.forEach(
            function (button) {

                button.classList.toggle(
                    "active",
                    button.dataset.category ===
                    category
                );

            }
        );


        categoryTitle.classList.add(
            "transitioning"
        );


        setTimeout(
            function () {

                const info =
                    categoryInfo[
                        category
                        ];


                categoryTitle.textContent =
                    info.title;


                categoryDescription.textContent =
                    info.description;


                categoryTitle.classList.remove(
                    "transitioning"
                );

            },
            100
        );


        filterModules();

    }


    categoryButtons.forEach(
        function (button) {

            button.addEventListener(
                "click",
                function () {

                    selectCategory(
                        button.dataset.category
                    );

                }
            );

        }
    );


    /* =====================================================
       FILTER
       ===================================================== */

    function filterModules() {

        const query =
            search.value
                .toLowerCase()
                .trim();


        let count =
            0;


        moduleCards.forEach(
            function (
                card,
                index
            ) {

                const categoryMatch =
                    card.dataset.category ===
                    currentCategory;


                const searchMatch =
                    !query ||
                    card.dataset.name.includes(
                        query
                    );


                const visible =
                    categoryMatch &&
                    searchMatch;


                card.classList.remove(
                    "visible"
                );


                if (
                    visible
                ) {

                    count++;


                    /*
                     * Small stagger makes category
                     * switching feel much nicer.
                     */

                    setTimeout(
                        function () {

                            card.classList.add(
                                "visible"
                            );

                        },
                        index * 28
                    );

                }

            }
        );


        visibleCount.textContent =
            count;

    }


    search.addEventListener(
        "input",
        filterModules
    );


    /* =====================================================
       DRAGGING
       ===================================================== */

    dragHandle.addEventListener(
        "mousedown",
        function (event) {

            if (
                event.target.closest(
                    "button"
                ) ||

                event.target.closest(
                    "input"
                )
            ) {
                return;
            }


            dragging =
                true;


            dragStartX =
                event.clientX;


            dragStartY =
                event.clientY;


            windowStartX =
                windowX;


            windowStartY =
                windowY;


            event.preventDefault();

        }
    );


    document.addEventListener(
        "mousemove",
        function (event) {

            if (
                !dragging
            ) {
                return;
            }


            windowX =
                windowStartX +
                (
                    event.clientX -
                    dragStartX
                );


            windowY =
                windowStartY +
                (
                    event.clientY -
                    dragStartY
                );


            const maxX =
                Math.max(
                    0,
                    window.innerWidth / 2 -
                    150
                );


            const maxY =
                Math.max(
                    0,
                    window.innerHeight / 2 -
                    100
                );


            windowX =
                Math.max(
                    -maxX,
                    Math.min(
                        maxX,
                        windowX
                    )
                );


            windowY =
                Math.max(
                    -maxY,
                    Math.min(
                        maxY,
                        windowY
                    )
                );


            windowElement.style.setProperty(
                "--hz-x",
                windowX + "px"
            );


            windowElement.style.setProperty(
                "--hz-y",
                windowY + "px"
            );

        }
    );


    document.addEventListener(
        "mouseup",
        function () {

            dragging =
                false;

        }
    );


    /* =====================================================
       INITIALIZE
       ===================================================== */

    refreshGuiBind();

    refreshModuleBinds();

    selectCategory(
        "combat"
    );


    console.log(
        "[Herz] Modern ClickGUI initialized"
    );
    /* =====================================================
       INPUT / CURSOR LOCKING
       ===================================================== */

    const gameFrame =
        document.getElementById("game_frame");

    function captureGameInput() {

        /*
         * Release Minecraft's mouse / pointer lock.
         */
        if (document.pointerLockElement) {
            document.exitPointerLock();
        }

        /*
         * Make absolutely sure the browser cursor is visible.
         */
        document.documentElement.style.cursor = "default";
        document.body.style.cursor = "default";
        root.style.cursor = "default";

        /*
         * Prevent the game DOM from receiving pointer events.
         *
         * Your game canvas lives inside #game_frame, so this
         * blocks clicks, mouse movement, wheel interaction, etc.
         */
        if (gameFrame) {
            gameFrame.style.pointerEvents = "none";
        }

        /*
         * Allow the ClickGUI itself to receive interactions.
         */
        root.style.pointerEvents = "auto";
    }


    function releaseGameInput() {

        /*
         * Re-enable the game.
         */
        if (gameFrame) {
            gameFrame.style.pointerEvents = "";
        }

        document.documentElement.style.cursor = "";
        document.body.style.cursor = "";
        root.style.cursor = "";

        /*
         * Don't request pointer lock here unless you specifically
         * want closing the GUI to instantly grab the mouse again.
         *
         * Minecraft can reacquire it when the player clicks.
         */
    }
})();
