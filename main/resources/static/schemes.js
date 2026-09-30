const studentId =
    localStorage.getItem("studentId");

let allSchemes = [];
let currentSchemes = [];


if (!studentId) {
    window.location.href = "login.html";
}


async function loadSchemes() {

    try {

        showLoading(true);

        const response =
            await fetch("/scheme");

        if (!response.ok) {
            throw new Error("Unable to load scholarships");
        }

        allSchemes =
            await response.json();

        currentSchemes =
            allSchemes;

        renderSchemes(
            currentSchemes
        );

    } catch (error) {

        console.error(error);

        showEmpty(
            "Unable to load scholarships."
        );

    } finally {

        showLoading(false);
    }
}


async function showEligible() {

    try {

        showLoading(true);

        const response =
            await fetch(
                "/eligible/" + studentId
            );

        if (!response.ok) {
            throw new Error(
                "Unable to find eligible scholarships"
            );
        }

        currentSchemes =
            await response.json();

        renderSchemes(
            currentSchemes
        );

        document.getElementById(
            "resultTitle"
        ).textContent =
            "Scholarships Eligible For You";

        activateFilter(1);

    } catch (error) {

        showEmpty(error.message);

    } finally {

        showLoading(false);
    }
}


async function showActive() {

    try {

        showLoading(true);

        const response =
            await fetch("/scheme/active");

        currentSchemes =
            await response.json();

        renderSchemes(
            currentSchemes
        );

        document.getElementById(
            "resultTitle"
        ).textContent =
            "Active Scholarships";

        activateFilter(2);

    } catch (error) {

        showEmpty(error.message);

    } finally {

        showLoading(false);
    }
}


async function showExpired() {

    try {

        showLoading(true);

        const response =
            await fetch("/scheme/expired");

        currentSchemes =
            await response.json();

        renderSchemes(
            currentSchemes
        );

        document.getElementById(
            "resultTitle"
        ).textContent =
            "Expired Scholarships";

        activateFilter(3);

    } catch (error) {

        showEmpty(error.message);

    } finally {

        showLoading(false);
    }
}


function showAll() {

    currentSchemes =
        allSchemes;

    document.getElementById(
        "resultTitle"
    ).textContent =
        "All Scholarships";

    renderSchemes(
        currentSchemes
    );

    activateFilter(0);
}


async function searchSchemes() {

    const name =
        document.getElementById(
            "searchInput"
        ).value.trim();


    if (!name) {

        showAll();

        return;
    }


    try {

        showLoading(true);

        const response =
            await fetch(
                "/scheme/search?name="
                + encodeURIComponent(name)
            );


        if (!response.ok) {
            throw new Error("Search failed");
        }


        currentSchemes =
            await response.json();


        document.getElementById(
            "resultTitle"
        ).textContent =
            "Search Results";


        renderSchemes(
            currentSchemes
        );


    } catch (error) {

        showEmpty(error.message);

    } finally {

        showLoading(false);
    }
}


function renderSchemes(schemes) {

    const container =
        document.getElementById(
            "schemeContainer"
        );

    container.innerHTML = "";


    document.getElementById(
        "resultCount"
    ).textContent =
        schemes.length
        + " scholarship"
        + (schemes.length !== 1 ? "s" : "");


    if (!schemes.length) {

        showEmpty(
            "No scholarships found."
        );

        return;
    }


    document.getElementById(
        "emptyState"
    ).style.display =
        "none";


    schemes.forEach(scheme => {

        const card =
            document.createElement("article");

        card.className =
            "scheme-card";


        card.innerHTML = `

            <div class="scheme-top">

                <div class="scheme-icon">
                    🎓
                </div>

                <button
                    class="bookmark-btn"
                    onclick="bookmarkScheme(${scheme.schemeID})"
                    title="Save scholarship">

                    🔖

                </button>

            </div>


            <h3>
                ${escapeHtml(
                    scheme.schemeName
                )}
            </h3>


            <p>
                ${escapeHtml(
                    scheme.benefits || "Government assistance"
                )}
            </p>


            <div class="info-row">

                <span class="tag">
                    ${escapeHtml(
                        scheme.eligibleCategory || "All Categories"
                    )}
                </span>

                <span class="tag">
                    ${escapeHtml(
                        scheme.eligibleCourse || "All Courses"
                    )}
                </span>

                <span class="tag">
                    ${escapeHtml(
                        scheme.eligibleLocation || "All Locations"
                    )}
                </span>

            </div>


            <div class="deadline">
                📅 Deadline:
                ${scheme.applicationDeadline || "Not specified"}
            </div>


            <div class="card-actions">

                <a
                    class="view-btn"
                    href="scheme-details.html?id=${scheme.schemeID}">

                    View Details

                </a>

            </div>

        `;


        container.appendChild(card);
    });
}


async function bookmarkScheme(schemeId) {

    try {

        const response =
            await fetch(
                "/bookmark",
                {
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
                }
            );


        if (!response.ok) {

            const data =
                await response.json();

            throw new Error(
                data.message ||
                "Unable to save bookmark"
            );
        }


        alert(
            "Scholarship saved successfully!"
        );


    } catch (error) {

        alert(error.message);
    }
}


function activateFilter(index) {

    const buttons =
        document.querySelectorAll(
            ".filter-btn"
        );

    buttons.forEach(
        button =>
            button.classList.remove(
                "active"
            )
    );

    if (buttons[index]) {

        buttons[index]
            .classList.add("active");
    }
}


function showLoading(show) {

    document.getElementById(
        "loading"
    ).style.display =
        show ? "block" : "none";
}


function showEmpty(message) {

    const empty =
        document.getElementById(
            "emptyState"
        );

    empty.style.display =
        "block";

    empty.querySelector("p")
        .textContent =
        message;


    document.getElementById(
        "schemeContainer"
    ).innerHTML = "";


    document.getElementById(
        "resultCount"
    ).textContent =
        "0 scholarships";
}


function escapeHtml(value) {

    if (value === null ||
        value === undefined) {

        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


document.getElementById(
    "searchInput"
).addEventListener(
    "keydown",
    function(event) {

        if (event.key === "Enter") {

            searchSchemes();
        }
    }
);


loadSchemes();