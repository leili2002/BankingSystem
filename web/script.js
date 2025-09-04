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
// make json to send server
    const payload = {
        national_id: parseInt(national_id),
        name: name,
        lastname: lastname,
        password: password
    };
// send and get a response from server (response is  answer of server)
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


//login form
async function submitLogin() {
    const national_id = parseInt(document.getElementById("login-id").value.trim(), 10);
    const password = document.getElementById("login-password").value.trim();

    if (isNaN(national_id) || !password) {
        document.getElementById("response").textContent = "❌ Please enter a valid ID and password.";
        return;
    }

    const payload = { national_id, password };
    console.log("Sending payload to server:", payload);

    try {
        const response = await fetch(API_BASE + "/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const contentType = response.headers.get("Content-Type") || "";
        const data = contentType.includes("application/json")
            ? await response.json()
            : await response.text();

        // Success based on HTTP status code
        if (response.status === 200) {
            // Server should return { token, name }
            localStorage.setItem("jwt", data.token);
            alert("Login successful!");
            // Hide login form, show dashboard

            // Clear login fields
            document.getElementById("login-id").value = "";
            document.getElementById("login-password").value = "";

            // Show dashboard using your existing function
            showDashboard(data.name);
            // Store user info
            window.userName = data.name;
            // Update welcome message
            // document.getElementById("welcome-text").textContent = `Welcome, ${window.userName}!`;

        } else if (response.status === 400) {
            document.getElementById("response").textContent = "❌ Bad request. Please check your input.";

        } else if (response.status === 401) {
            document.getElementById("response").textContent = "❌ Unauthorized. Wrong ID or password.";

        } else {
            document.getElementById("response").textContent = `❌ Login failed (HTTP ${response.status})`;
        }

        console.log("Server response:", data);

    } catch (error) {
        document.getElementById("response").textContent = "Network error: " + error.message;
        console.error("Login error:", error);
    }
}

// account menu
//
//     try {
//         const token = localStorage.getItem("jwt");
//         if (!token) {
//             document.getElementById("dashboard-response").textContent = "❌ No token found. Please login first.";
//             return;
//         }
//
//         const response = await fetch("http://localhost:8080/account-menu", {
//             method: "GET",
//             headers: {
//                 "Content-Type": "application/json",
//                 "Authorization": "Bearer " + token
//             }
//         });
//
//         const contentType = response.headers.get("Content-Type") || "";
//         const data = contentType.includes("application/json")
//             ? await response.json()
//             : await response.text();
//
//         if (response.status === 200) {
//             document.getElementById("dashboard-response").textContent = `✅ Success: ${JSON.stringify(data)}`;
//         } else if (response.status === 401) {
//             document.getElementById("dashboard-response").textContent = "❌ Unauthorized. Token invalid or expired.";
//         } else {
//             document.getElementById("dashboard-response").textContent = `❌ Failed (HTTP ${response.status}): ${JSON.stringify(data)}`;
//         }
//     } catch (error) {
//         document.getElementById("dashboard-response").textContent = "Network error: " + error.message;
//         console.error(error);
//     }
// });
