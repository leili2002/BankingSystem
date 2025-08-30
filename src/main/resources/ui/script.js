const API_BASE = "http://localhost:8080";

// Load menu buttons dynamically from /menu
async function loadMenu() {
    try {
        const res = await fetch(API_BASE + "/menu");
        const data = await res.json();

        const container = document.getElementById("menu-buttons");
        container.innerHTML = ""; // Clear existing buttons

        data.options.forEach(option => {
            const button = document.createElement("button");
            button.textContent = option;
            button.onclick = () => callApi("/" + option.toLowerCase().replace(" ", ""));
            container.appendChild(button);
        });
    } catch (error) {
        document.getElementById("response").textContent = "Error loading menu: " + error.message;
    }
}

// Call API endpoints
async function callApi(endpoint) {
    try {
        const res = await fetch(API_BASE + endpoint);
        const data = await res.json();
        document.getElementById("response").textContent = JSON.stringify(data, null, 2);
    } catch (error) {
        document.getElementById("response").textContent = "Error: " + error.message;
    }
}

// Load menu on page load
window.onload = loadMenu;
