let editingId = null;


async function adminLogin() {

    const username =
        prompt("Admin username:");

    const password =
        prompt("Admin password:");


    if (!username || !password) {
        return;
    }


    try {

        const response =
            await fetch(
                "/admin/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify({

                        username: username,

                        password: password

                    })
                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.message ||
                "Admin login failed"
            );
        }


        sessionStorage.setItem(
            "adminLoggedIn",
            "true"
        );


        loadSchemes();


    } catch (error) {

        alert(error.message);
    }
}


async function loadSchemes() {

    try {

        const response =
            await fetch(
                "/scheme",
                {
                    credentials: "include"
                }
            );


        if (response.status === 401 ||
            response.status === 403) {

            await adminLogin();

            return;
        }


        const schemes =
            await response.json();


        renderSchemes(schemes);


    } catch (error) {

        console.error(error);
    }
}


function renderSchemes(schemes) {

    const list =
        document.getElementById(
            "schemeList"
        );


    list.innerHTML = "";


    schemes.forEach(scheme => {

        const div =
            document.createElement(
                "div"
            );


        div.className =
            "admin-scheme";


        div.innerHTML = `

            <h3>
                ${escapeHtml(
                    scheme.schemeName
                )}
            </h3>

            <p>
                ${escapeHtml(
                    scheme.benefits || ""
                )}
            </p>

            <p>
                Deadline:
                ${scheme.applicationDeadline || "Not specified"}
            </p>

            <div class="admin-actions">

                <button
                    class="edit"
                    onclick="editScheme(${scheme.schemeID})">

                    Edit

                </button>

                <button
                    class="delete"
                    onclick="deleteScheme(${scheme.schemeID})">

                    Delete

                </button>

            </div>
        `;


        list.appendChild(div);
    });
}


document.getElementById(
    "schemeForm"
).addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();


        const scheme = {

            schemeName:
                document.getElementById(
                    "schemeName"
                ).value.trim(),

            benefits:
                document.getElementById(
                    "benefits"
                ).value.trim(),

            eligibility:
                document.getElementById(
                    "eligibility"
                ).value.trim(),

            applicationDeadline:
                document.getElementById(
                    "applicationDeadline"
                ).value || null,

            applicationLink:
                document.getElementById(
                    "applicationLink"
                ).value.trim(),

            requiredDocuments:
                document.getElementById(
                    "requiredDocuments"
                ).value.trim(),

            applicationProcedure:
                document.getElementById(
                    "applicationProcedure"
                ).value.trim(),

            incomeLimit:
                Number(
                    document.getElementById(
                        "incomeLimit"
                    ).value
                ),

            eligibleCategory:
                document.getElementById(
                    "eligibleCategory"
                ).value.trim(),

            eligibleCourse:
                document.getElementById(
                    "eligibleCourse"
                ).value.trim(),

            eligibleLocation:
                document.getElementById(
                    "eligibleLocation"
                ).value.trim()
        };


        try {

            let url = "/scheme";
            let method = "POST";


            if (editingId) {

                url =
                    "/scheme/"
                    + editingId;

                method = "PUT";
            }


            const response =
                await fetch(
                    url,
                    {
                        method: method,

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        credentials: "include",

                        body: JSON.stringify(
                            scheme
                        )
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Unable to save scholarship"
                );
            }


            showMessage(
                "Scholarship saved successfully!",
                "success"
            );


            resetForm();

            loadSchemes();


        } catch (error) {

            showMessage(
                error.message,
                "error"
            );
        }

    }
);


async function editScheme(id) {

    try {

        const response =
            await fetch(
                "/scheme/" + id
            );


        const scheme =
            await response.json();


        editingId =
            id;


        document.getElementById(
            "formTitle"
        ).textContent =
            "Edit Scholarship";


        document.getElementById(
            "schemeName"
        ).value =
            scheme.schemeName || "";


        document.getElementById(
            "benefits"
        ).value =
            scheme.benefits || "";


        document.getElementById(
            "eligibility"
        ).value =
            scheme.eligibility || "";


        document.getElementById(
            "applicationDeadline"
        ).value =
            scheme.applicationDeadline || "";


        document.getElementById(
            "applicationLink"
        ).value =
            scheme.applicationLink || "";


        document.getElementById(
            "requiredDocuments"
        ).value =
            scheme.requiredDocuments || "";


        document.getElementById(
            "applicationProcedure"
        ).value =
            scheme.applicationProcedure || "";


        document.getElementById(
            "incomeLimit"
        ).value =
            scheme.incomeLimit || 0;


        document.getElementById(
            "eligibleCategory"
        ).value =
            scheme.eligibleCategory || "";


        document.getElementById(
            "eligibleCourse"
        ).value =
            scheme.eligibleCourse || "";


        document.getElementById(
            "eligibleLocation"
        ).value =
            scheme.eligibleLocation || "";


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });


    } catch (error) {

        alert(
            "Unable to load scholarship"
        );
    }
}


async function deleteScheme(id) {

    if (!confirm(
        "Delete this scholarship?"
    )) {

        return;
    }


    try {

        const response =
            await fetch(
                "/scheme/" + id,
                {
                    method: "DELETE",
                    credentials: "include"
                }
            );


        if (!response.ok) {

            throw new Error(
                "Delete failed"
            );
        }


        loadSchemes();


    } catch (error) {

        alert(error.message);
    }
}


function resetForm() {

    document.getElementById(
        "schemeForm"
    ).reset();


    editingId = null;


    document.getElementById(
        "formTitle"
    ).textContent =
        "Add Scholarship";
}


function showMessage(
    message,
    type
) {

    document.getElementById(
        "message"
    ).innerHTML =
        `<p class="${type}">
            ${message}
        </p>`;
}


function adminLogout() {

    fetch(
        "/admin/logout",
        {
            method: "POST",
            credentials: "include"
        }
    ).finally(() => {

        sessionStorage.clear();

        window.location.href =
            "login.html";
    });
}


function escapeHtml(value) {

    return String(value || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


loadSchemes();