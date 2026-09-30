document
    .getElementById("adminLoginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const username =
            document.getElementById("username").value.trim();

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("message");

        const loginBtn =
            document.getElementById("loginBtn");

        message.textContent = "";
        loginBtn.disabled = true;
        loginBtn.textContent = "Logging in...";

        try {

            const response = await fetch("/admin/login", {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(
                    data.message || "Invalid admin username or password"
                );
            }

            // Store admin details
            localStorage.setItem(
                "adminId",
                data.AdminID
            );

            localStorage.setItem(
                "adminUsername",
                data.username
            );

            localStorage.setItem(
                "adminRole",
                data.role
            );

            message.style.color = "green";
            message.textContent =
                "Admin login successful!";

            setTimeout(function () {
                window.location.href = "admin.html";
            }, 800);

        } catch (error) {

            message.style.color = "red";
            message.textContent = error.message;

        } finally {

            loginBtn.disabled = false;
            loginBtn.textContent = "Login";
        }
    });