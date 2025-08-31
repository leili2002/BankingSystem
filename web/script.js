const API_BASE = "http://localhost:8080";

// Show Sign Up form
function showSignUp() {
    document.getElementById("main-menu").style.display = "none";
    document.getElementById("signup-form").style.display = "block";
}

// Back to main menu
function backToMain() {
    document.getElementById("signup-form").style.display = "none";
    document.getElementById("main-menu").style.display = "block";
    document.getElementById("response").textContent = "";
}

// Submit Sign Up form
async function submitSignUp() {
    const national_id = document.getElementById("signup-id").value.trim();
    const name = document.getElementById("signup-name").value.trim();
    const lastname = document.getElementById("signup-lastname").value.trim();
    const password = document.getElementById("signup-password").value.trim();

    if (!national_id || !name || !lastname || !password) {
        document.getElementById("response").textContent = "❌ Please fill all fields.";
        return;
    }

    const payload = {
        national_id: parseInt(national_id),
        name: name,
        lastname: lastname,
        password: password
    };

    try {
        const response = await fetch(API_BASE + "/signup", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const text = await response.text();
        document.getElementById("response").textContent = text;
    } catch (error) {
        document.getElementById("response").textContent = "Error: " + error.message;
    }
}
 // login function
async function submitLogin() {
    const national_id = parseInt(document.getElementById("login-id").value.trim(), 10);
    const password = document.getElementById("login-password").value.trim();

    if (isNaN(national_id) || !password) {
        document.getElementById("response").textContent = "❌ Please enter a valid ID and password.";
        return;
    }

    const payload = {
        national_id: national_id,
        password: password
    };
    console.log("Sending payload to server:", payload);

    try {
        const response = await fetch(API_BASE + "/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const result = await response.json();
        console.log("Server response:", result);
        console.log(result);

        if (result.success) {
            // Hide login form, show dashboard
            document.getElementById("login-form").style.display = "none";
            document.getElementById("main-menu").style.display = "none";
            document.getElementById("dashboard").style.display = "block";

            // Store user info
            window.userId = result.national_id;
            window.userName = result.name;

            // Update welcome message
            document.getElementById("welcome-text").textContent = `Welcome, ${window.userName}!`;
        }
        else {
            document.getElementById("response").textContent = "❌ Login failed!";
        }
    } catch (error) {
        document.getElementById("response").textContent = "Error: " + error.message;
    }
}
