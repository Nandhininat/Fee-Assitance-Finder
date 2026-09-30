const studentId =
    localStorage.getItem("studentId");

const studentName =
    localStorage.getItem("studentName");


// =========================
// CHECK LOGIN
// =========================

if (!studentId) {
    window.location.href = "login.html";
}


// =========================
// SHOW STUDENT NAME
// =========================

document.getElementById("studentName").textContent =
    studentName || "Student";


// =========================
// LOAD DASHBOARD
// =========================

async function loadDashboard() {

    try {

        // =========================
        // TOTAL SCHOLARSHIPS
        // =========================

        const schemesResponse =
            await fetch("/scheme");

        if (!schemesResponse.ok) {
            throw new Error(
                "Unable to load scholarships"
            );
        }

        const schemes =
            await schemesResponse.json();

        document.getElementById("schemeCount")
            .textContent = schemes.length;


        // =========================
        // ELIGIBLE SCHOLARSHIPS
        // =========================

        const eligibleResponse =
            await fetch(
                "/eligible/" + studentId
            );

        if (!eligibleResponse.ok) {
            throw new Error(
                "Unable to load eligible scholarships"
            );
        }

        const eligible =
            await eligibleResponse.json();

        document.getElementById("eligibleCount")
            .textContent = eligible.length;


        // =========================
        // BOOKMARK COUNT
        // =========================

        const bookmarksResponse =
            await fetch(
                "/bookmark/student/" + studentId
            );

        if (!bookmarksResponse.ok) {
            throw new Error(
                "Unable to load bookmarks"
            );
        }

        const bookmarks =
            await bookmarksResponse.json();

        document.getElementById("bookmarkCount")
            .textContent = bookmarks.length;


        // =========================
        // UNREAD NOTIFICATION COUNT
        // =========================

        const notificationResponse =
            await fetch(
                "/notification/student/"
                + studentId
                + "/unread/count"
            );

        if (!notificationResponse.ok) {
            throw new Error(
                "Unable to load notifications"
            );
        }

        const notificationCount =
            await notificationResponse.json();

        document.getElementById(
            "notificationCount"
        ).textContent = notificationCount;


    } catch (error) {

        console.error(
            "Dashboard loading error:",
            error
        );

    }
}


// =========================
// LOGOUT
// =========================

function logout() {

    localStorage.removeItem("studentId");
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentUsername");

    window.location.href =
        "login.html";
}


// =========================
// START DASHBOARD
// =========================

loadDashboard();