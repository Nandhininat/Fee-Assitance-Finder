const API = "";

const loginForm = document.getElementById("loginForm");


// =========================
// STUDENT LOGIN
// =========================

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const username =
        document.getElementById("username").value.trim();

    const password =
        document.getElementById("password").value;


    try {

        const response = await fetch(
            API + "/login",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username: username,
                    password: password
                })
            }
        );


        const data = await response.json();


        if (!response.ok) {

            throw new Error(
                data.message ||
                "Invalid username or password"
            );
        }


        // =========================
        // SAVE LOGIN INFORMATION
        // =========================

        localStorage.setItem(
            "studentId",
            data.studentid
        );

        localStorage.setItem(
            "studentName",
            data.studentname
        );

        localStorage.setItem(
            "studentUsername",
            data.username
        );


        // Optional: save complete student object
        localStorage.setItem(
            "student",
            JSON.stringify(data)
        );


        // =========================
        // GO TO DASHBOARD
        // =========================

        window.location.href =
            "dashboard.html";


    } catch (error) {

        document.getElementById(
            "message"
        ).innerHTML =
            `<p class="error">${error.message}</p>`;
    }

});


// =========================
// SHOW REGISTER
// =========================

function showRegister() {

    document.getElementById(
        "registerModal"
    ).style.display = "flex";
}


// =========================
// CLOSE REGISTER
// =========================

function closeRegister() {

    document.getElementById(
        "registerModal"
    ).style.display = "none";
}


// =========================
// STUDENT REGISTRATION
// =========================

document.getElementById("registerForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();


        const student = {

            studentname:
                document.getElementById(
                    "regName"
                ).value.trim(),

            username:
                document.getElementById(
                    "regUsername"
                ).value.trim(),

            password:
                document.getElementById(
                    "regPassword"
                ).value,

            course:
                document.getElementById(
                    "regCourse"
                ).value.trim(),

            income:
                Number(
                    document.getElementById(
                        "regIncome"
                    ).value
                ),

            academicDetails:
                document.getElementById(
                    "regAcademic"
                ).value.trim(),

            category:
                document.getElementById(
                    "regCategory"
                ).value,

            location:
                document.getElementById(
                    "regLocation"
                ).value.trim()
        };


        try {

            const response =
                await fetch(
                    API + "/student",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(student)
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Registration failed"
                );
            }


            document.getElementById(
                "registerMessage"
            ).innerHTML =
                `<p class="success">
                    Registration successful. Please login.
                </p>`;


            document.getElementById(
                "registerForm"
            ).reset();


            setTimeout(function () {

                closeRegister();

            }, 1500);


        } catch (error) {

            document.getElementById(
                "registerMessage"
            ).innerHTML =
                `<p class="error">
                    ${error.message}
                </p>`;
        }

    });