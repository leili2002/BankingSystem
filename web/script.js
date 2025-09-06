const API_BASE = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
    // Helper functions
    const show = (id) => document.getElementById(id).style.display = "block";
    const hide = (id) => document.getElementById(id).style.display = "none";
    const clearResponse = () => {
        const resp = document.getElementById("response");
        if (resp) resp.textContent = "";
    };

    // ======= Navigation =======
    function showSignUp() {
        hide("main-menu");
        hide("login-form");
        show("signup-form");
        clearResponse();
    }

    function showLogin() {
        hide("main-menu");
        hide("signup-form");
        show("login-form");
        clearResponse();
    }

    function backToMain() {
        hide("signup-form");
        hide("login-form");
        hide("dashboard");
        hide("create-account-form");
        hide("accounts-table");
        show("main-menu");
        clearResponse();
    }

    function showDashboard(username) {
        hide("signup-form");
        hide("login-form");
        hide("main-menu");
        show("dashboard");
        document.getElementById("welcome-text").textContent = `Welcome, ${username}!`;
        document.getElementById("dashboard-response").textContent = "";
        hide("accounts-table");
        hide("create-account-form");
    }

    function showAddAccount() {
        show("create-account-form");
        hide("accounts-table");
        document.getElementById("dashboard-response").textContent = "";
    }

    // ======= Sign Up =======
    async function submitSignUp() {
        const national_id = document.getElementById("signup-id").value.trim();
        const name = document.getElementById("signup-name").value.trim();
        const lastname = document.getElementById("signup-lastname").value.trim();
        const password = document.getElementById("signup-password").value.trim();

        if (!national_id || !name || !lastname || !password) {
            document.getElementById("response").textContent = "❌ Please fill all fields.";
            return;
        }

        const payload = { national_id: parseInt(national_id), name, lastname, password };

        try {
            const res = await fetch(`${API_BASE}/signup`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            const text = await res.text();
            document.getElementById("response").textContent = text;

            if (res.ok) {
                // Clear fields
                document.getElementById("signup-id").value = "";
                document.getElementById("signup-name").value = "";
                document.getElementById("signup-lastname").value = "";
                document.getElementById("signup-password").value = "";
            }

        } catch (err) {
            document.getElementById("response").textContent = "Network error: " + err.message;
        }
    }

    // ======= Login =======
    async function submitLogin() {
        const national_id = parseInt(document.getElementById("login-id").value.trim(), 10);
        const password = document.getElementById("login-password").value.trim();

        if (isNaN(national_id) || !password) {
            document.getElementById("response").textContent = "❌ Enter a valid ID and password.";
            return;
        }

        try {
            const res = await fetch(`${API_BASE}/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ national_id, password })
            });

            const contentType = res.headers.get("Content-Type") || "";
            const data = contentType.includes("application/json") ? await res.json() : await res.text();

            if (res.ok) {
                localStorage.setItem("jwt", data.token);
                alert("Login successful!");
                showDashboard(data.name);
                document.getElementById("login-id").value = "";
                document.getElementById("login-password").value = "";
            } else if (res.status === 400) {
                document.getElementById("response").textContent = "❌ Bad request.";
            } else if (res.status === 401) {
                document.getElementById("response").textContent = "❌ Unauthorized. Wrong ID or password.";
            } else {
                document.getElementById("response").textContent = `❌ Login failed (HTTP ${res.status})`;
            }

        } catch (err) {
            document.getElementById("response").textContent = "Network error: " + err.message;
        }
    }

    // ======= Create Account =======
    async function submitAccount() {
        const data = {
            name: document.getElementById("name").value.trim(),
            lastname: document.getElementById("lastname").value.trim(),
            type: document.getElementById("type").value.trim(),
            balance: parseFloat(document.getElementById("balance").value)
        };

        if (!data.name || !data.lastname || !data.type || isNaN(data.balance)) {
            document.getElementById("response").textContent = "❌ Fill all account fields correctly.";
            return;
        }

        try {
            const token = localStorage.getItem("jwt");
            const res = await fetch(`${API_BASE}/account/signup`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });

            const text = await res.text();
            document.getElementById("response").textContent = text;

        } catch (err) {
            document.getElementById("response").textContent = "Error: " + err.message;
        }
    }

    // ======= Fetch Accounts =======
    async function getAccounts() {
        const token = localStorage.getItem("jwt");
        if (!token) {
            document.getElementById("dashboard-response").textContent = "❌ You are not logged in.";
            return;
        }

        try {
            const res = await fetch(`${API_BASE}/accounts`, {
                method: "GET",
                headers: { "Authorization": `Bearer ${token}` }
            });

            if (res.ok) {
                const accounts = await res.json();
                const tbody = document.getElementById("accounts-tbody");
                tbody.innerHTML = "";

                if (accounts.length === 0) {
                    document.getElementById("dashboard-response").textContent = "No accounts found.";
                    hide("accounts-table");
                    return;
                }

                accounts.forEach(acc => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${acc.id}</td>
                        <td>${acc.name}</td>
                        <td>${acc.lastname}</td>
                        <td>${acc.type}</td>
                        <td>${acc.balance}</td>
                    `;
                    tbody.appendChild(row);
                });

                show("accounts-table");
                document.getElementById("dashboard-response").textContent = "";

            } else if (res.status === 401) {
                document.getElementById("dashboard-response").textContent = "❌ Unauthorized. Log in again.";
            } else {
                document.getElementById("dashboard-response").textContent = `❌ Failed (HTTP ${res.status})`;
            }

        } catch (err) {
            document.getElementById("dashboard-response").textContent = "Network error: " + err.message;
        }
    }

    // ======= Logout =======
    function logout() {
        localStorage.removeItem("jwt");
        backToMain();
        alert("You have been logged out.");
    }

    // ======= Expose globally =======
    window.showSignUp = showSignUp;
    window.showLogin = showLogin;
    window.backToMain = backToMain;
    window.submitSignUp = submitSignUp;
    window.submitLogin = submitLogin;
    window.showDashboard = showDashboard;
    window.showAddAccount = showAddAccount;
    window.submitAccount = submitAccount;
    window.getAccounts = getAccounts;
    window.logout = logout;
});
