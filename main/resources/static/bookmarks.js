const studentId =
    localStorage.getItem("studentId");


if (!studentId) {

    window.location.href =
        "login.html";

}


// =========================
// LOAD BOOKMARKS
// =========================

async function loadBookmarks() {

    const container =
        document.getElementById(
            "bookmarkContainer"
        );

    const loading =
        document.getElementById(
            "loading"
        );

    const empty =
        document.getElementById(
            "empty"
        );


    try {

        loading.style.display =
            "block";

        empty.style.display =
            "none";


        const response =
            await fetch(
                "/bookmark/student/" +
                studentId
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load bookmarks."
            );
        }


        const bookmarks =
            await response.json();


        if (!bookmarks.length) {

            showEmpty();
            return;
        }


        container.innerHTML = "";


        // =========================
        // LOAD EACH SCHEME
        // =========================

        for (const bookmark of bookmarks) {

            try {

                const response =
                    await fetch(
                        "/scheme/" +
                        bookmark.schemeId
                    );


                if (!response.ok) {

                    throw new Error(
                        "Scheme not found"
                    );
                }


                const scheme =
                    await response.json();


                // =========================
                // CREATE CARD
                // =========================

                const card =
                    document.createElement(
                        "div"
                    );


                card.className =
                    "card";


                card.innerHTML = `

                    <h3>
                        ${escapeHtml(
                            scheme.schemeName ||
                            "Scholarship Scheme"
                        )}
                    </h3>


                    <p>
                        ${escapeHtml(
                            scheme.benefits ||
                            "Benefits not specified"
                        )}
                    </p>


                    <div class="deadline">

                        📅 Deadline:

                        ${scheme.applicationDeadline
                            ? formatDate(
                                scheme.applicationDeadline
                              )
                            : "Not specified"}

                    </div>


                    <div class="documents">

                        📄 Required Documents:

                        ${escapeHtml(
                            scheme.requiredDocuments ||
                            "Not specified"
                        )}

                    </div>


                    <div class="eligibility">

                        🎓 Eligibility:

                        ${escapeHtml(
                            scheme.eligibility ||
                            "Not specified"
                        )}

                    </div>


                    <div class="actions">

                        <a
                            class="view"
                            href="scheme-details.html?id=${scheme.schemeID}">
                            View Details
                        </a>


                        <button
                            class="delete"
                            onclick="deleteBookmark(${bookmark.bookmarkId})">
                            Remove
                        </button>

                    </div>
                `;


                container.appendChild(card);


            } catch (error) {

                console.error(
                    "Error loading scheme:",
                    error
                );
            }
        }


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <p class="error">
                Unable to load bookmarks.
            </p>
        `;


    } finally {

        loading.style.display =
            "none";
    }
}


// =========================
// DELETE BOOKMARK
// =========================

async function deleteBookmark(
    bookmarkId
) {

    const confirmDelete =
        confirm(
            "Remove this scholarship from bookmarks?"
        );


    if (!confirmDelete) {
        return;
    }


    try {

        const response =
            await fetch(
                "/bookmark/" +
                bookmarkId,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to remove bookmark."
            );
        }


        alert(
            "Scholarship removed from bookmarks."
        );


        // Reload bookmarks
        await loadBookmarks();


    } catch (error) {

        console.error(error);

        alert(
            error.message
        );
    }
}


// =========================
// EMPTY MESSAGE
// =========================

function showEmpty() {

    document.getElementById(
        "loading"
    ).style.display = "none";


    document.getElementById(
        "empty"
    ).style.display = "block";


    document.getElementById(
        "bookmarkContainer"
    ).innerHTML = "";
}


// =========================
// DATE FORMAT
// =========================

function formatDate(
    dateString
) {

    const date =
        new Date(dateString);


    if (
        isNaN(
            date.getTime()
        )
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
// HTML SECURITY
// =========================

function escapeHtml(value) {

    return String(value || "")

        .replaceAll(
            "&",
            "&amp;"
        )

        .replaceAll(
            "<",
            "&lt;"
        )

        .replaceAll(
            ">",
            "&gt;"
        )

        .replaceAll(
            '"',
            "&quot;"
        )

        .replaceAll(
            "'",
            "&#039;"
        );
}


// =========================
// START
// =========================

loadBookmarks();