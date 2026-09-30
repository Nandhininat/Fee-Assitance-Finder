document.addEventListener("DOMContentLoaded", function () {

    const params = new URLSearchParams(window.location.search);
    const schemeId = params.get("id");

    const loading = document.getElementById("loading");
    const details = document.getElementById("details");
    const error = document.getElementById("error");

    const schemeName = document.getElementById("schemeName");
    const benefits = document.getElementById("benefits");
    const eligibility = document.getElementById("eligibility");
    const category = document.getElementById("category");
    const course = document.getElementById("course");
    const location = document.getElementById("location");
    const incomeLimit = document.getElementById("incomeLimit");
    const deadline = document.getElementById("deadline");
    const documents = document.getElementById("documents");
    const procedure = document.getElementById("procedure");
    const applyLink = document.getElementById("applyLink");

    const bookmarkButton =
        document.querySelector(".bookmark");


    // =========================
    // CHECK SCHEME ID
    // =========================

    if (!schemeId) {
        showError("Scheme ID is missing.");
        return;
    }


    // =========================
    // LOAD SCHEME
    // =========================

    loadScheme(schemeId);


    async function loadScheme(id) {

        try {

            const response =
                await fetch(`/scheme/${id}`);

            if (!response.ok) {
                throw new Error(
                    "Scholarship scheme not found."
                );
            }

            const scheme =
                await response.json();

            console.log("Scheme loaded:", scheme);

            displayScheme(scheme);

        } catch (err) {

            console.error(
                "Error loading scheme:",
                err
            );

            showError(
                "Unable to load scholarship details."
            );
        }
    }


    // =========================
    // DISPLAY SCHEME
    // =========================

    function displayScheme(scheme) {

        schemeName.textContent =
            scheme.schemeName ||
            "Scholarship Scheme";

        benefits.textContent =
            scheme.benefits ||
            "Not available";

        eligibility.textContent =
            scheme.eligibility ||
            "Not available";

        category.textContent =
            scheme.eligibleCategory ||
            "Not specified";

        course.textContent =
            scheme.eligibleCourse ||
            "Not specified";

        location.textContent =
            scheme.eligibleLocation ||
            "Not specified";


        if (
            scheme.incomeLimit !== undefined &&
            scheme.incomeLimit !== null &&
            Number(scheme.incomeLimit) > 0
        ) {

            incomeLimit.textContent =
                "₹" +
                Number(scheme.incomeLimit)
                    .toLocaleString("en-IN");

        } else {

            incomeLimit.textContent =
                "As per official scheme rules";
        }


        if (scheme.applicationDeadline) {

            deadline.textContent =
                formatDate(
                    scheme.applicationDeadline
                );

        } else {

            deadline.textContent =
                "Not specified";
        }


        documents.textContent =
            scheme.requiredDocuments ||
            "Not specified";


        procedure.textContent =
            scheme.applicationProcedure ||
            "Please follow the instructions on the official portal.";


        setupApplyLink(
            scheme.applicationLink
        );


        loading.style.display = "none";

        details.style.display = "block";

        error.style.display = "none";


        // Check bookmark status
        checkBookmarkStatus(schemeId);
    }


    // =========================
    // APPLY LINK
    // =========================

    function setupApplyLink(applicationLink) {

        if (
            !applicationLink ||
            applicationLink.trim() === ""
        ) {

            applyLink.textContent =
                "Application Link Unavailable";

            applyLink.removeAttribute("href");

            applyLink.style.pointerEvents =
                "none";

            applyLink.style.opacity =
                "0.6";

            return;
        }


        try {

            const url =
                new URL(applicationLink.trim());

            if (
                url.protocol !== "http:" &&
                url.protocol !== "https:"
            ) {
                throw new Error(
                    "Invalid URL"
                );
            }


            applyLink.textContent =
                "Apply Now →";

            applyLink.href =
                url.href;

            applyLink.target =
                "_blank";

            applyLink.rel =
                "noopener noreferrer";

            applyLink.style.pointerEvents =
                "auto";

            applyLink.style.opacity =
                "1";

        } catch (err) {

            console.error(
                "Invalid application link:",
                err
            );

            applyLink.textContent =
                "Invalid Application Link";

            applyLink.removeAttribute("href");

            applyLink.style.pointerEvents =
                "none";

            applyLink.style.opacity =
                "0.6";
        }
    }


    // =========================
    // GET STUDENT ID
    // =========================

    function getStudentId() {

        return (
            localStorage.getItem("studentId") ||
            localStorage.getItem("studentID") ||
            localStorage.getItem("studentid") ||
            sessionStorage.getItem("studentId") ||
            sessionStorage.getItem("studentID") ||
            sessionStorage.getItem("studentid")
        );
    }


    // =========================
    // SAVE BOOKMARK
    // =========================

    window.bookmarkScheme = async function () {

        const studentId =
            getStudentId();


        if (!studentId) {

            alert(
                "Please login as a student first."
            );

            window.location.href =
                "login.html";

            return;
        }


        if (!schemeId) {

            alert(
                "Scheme ID is missing."
            );

            return;
        }


        if (bookmarkButton) {

            bookmarkButton.disabled =
                true;

            bookmarkButton.textContent =
                "Saving...";
        }


        try {

            const response =
                await fetch("/bookmark", {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({

                        studentId:
                            Number(studentId),

                        schemeId:
                            Number(schemeId)
                    })
                });


            if (!response.ok) {

                const errorText =
                    await response.text();

                throw new Error(
                    errorText ||
                    "Unable to save bookmark."
                );
            }


            const bookmark =
                await response.json();

            console.log(
                "Bookmark saved:",
                bookmark
            );


            if (bookmarkButton) {

                bookmarkButton.textContent =
                    "🔖 Saved";

                bookmarkButton.disabled =
                    true;
            }


            alert(
                "Scholarship saved successfully!"
            );


        } catch (error) {

            console.error(
                "Bookmark error:",
                error
            );


            if (bookmarkButton) {

                bookmarkButton.disabled =
                    false;

                bookmarkButton.textContent =
                    "🔖 Save Scholarship";
            }


            alert(
                "Unable to save scholarship."
            );
        }
    };


    // =========================
    // CHECK BOOKMARK STATUS
    // =========================

    async function checkBookmarkStatus(id) {

        const studentId =
            getStudentId();


        if (!studentId) {
            return;
        }


        try {

            const response =
                await fetch(
                    `/bookmark/student/${studentId}/scheme/${id}`
                );


            if (!response.ok) {
                return;
            }


            const bookmark =
                await response.json();


            if (
                bookmark &&
                bookmark.bookmarkId
            ) {

                if (bookmarkButton) {

                    bookmarkButton.textContent =
                        "🔖 Saved";

                    bookmarkButton.disabled =
                        true;
                }
            }

        } catch (error) {

            console.error(
                "Bookmark status error:",
                error
            );
        }
    }


    // =========================
    // DATE FORMAT
    // =========================

    function formatDate(dateString) {

        const date =
            new Date(dateString);

        if (
            isNaN(date.getTime())
        ) {

            return dateString;
        }


        return date.toLocaleDateString(
            "en-IN",
            {
                day: "2-digit",
                month: "long",
                year: "numeric"
            }
        );
    }


    // =========================
    // ERROR
    // =========================

    function showError(message) {

        loading.style.display =
            "none";

        details.style.display =
            "none";

        error.style.display =
            "block";

        error.textContent =
            message;
    }

});