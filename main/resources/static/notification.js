// ========================================
// STUDENT ID
// ========================================

const studentId = localStorage.getItem("studentId");


// ========================================
// CHECK LOGIN
// ========================================

if (!studentId) {
    window.location.href = "login.html";
}


// ========================================
// LOAD ALL NOTIFICATIONS
// ========================================

async function loadNotifications() {

    const container =
        document.getElementById("notificationContainer");

    const loading =
        document.getElementById("loading");

    const empty =
        document.getElementById("empty");

    try {

        if (loading) {
            loading.style.display = "block";
        }

        if (empty) {
            empty.style.display = "none";
        }

        const response = await fetch(
            "/notification/student/" + studentId
        );

        console.log(
            "Notification API status:",
            response.status
        );

        if (!response.ok) {
            throw new Error(
                "Unable to load notifications"
            );
        }

        const notifications =
            await response.json();

        console.log(
            "Notifications received:",
            notifications
        );

        renderNotifications(notifications);

        await loadUnreadCount();

    } catch (error) {

        console.error(
            "Notification error:",
            error
        );

        if (container) {
            container.innerHTML = `
                <div class="error">
                    <h3>Unable to load notifications</h3>
                    <p>${error.message}</p>
                </div>
            `;
        }

    } finally {

        if (loading) {
            loading.style.display = "none";
        }
    }
}


// ========================================
// LOAD UNREAD NOTIFICATIONS
// ========================================

async function loadUnread() {

    const container =
        document.getElementById("notificationContainer");

    const empty =
        document.getElementById("empty");

    try {

        if (empty) {
            empty.style.display = "none";
        }

        const response = await fetch(
            "/notification/student/" +
            studentId +
            "/unread"
        );

        if (!response.ok) {
            throw new Error(
                "Unable to load unread notifications"
            );
        }

        const notifications =
            await response.json();

        console.log(
            "Unread notifications:",
            notifications
        );

        renderNotifications(notifications);

        await loadUnreadCount();

    } catch (error) {

        console.error(
            "Unread notification error:",
            error
        );
    }
}


// ========================================
// LOAD UNREAD COUNT
// ========================================

async function loadUnreadCount() {

    try {

        const response = await fetch(
            "/notification/student/" +
            studentId +
            "/unread/count"
        );

        if (!response.ok) {
            throw new Error(
                "Unable to load unread count"
            );
        }

        const count =
            await response.json();

        const countElement =
            document.getElementById("unreadCount");

        if (countElement) {

            countElement.textContent = count;

            if (count > 0) {

                countElement.style.display =
                    "inline-block";

            } else {

                countElement.style.display =
                    "none";
            }
        }

        console.log(
            "Unread count:",
            count
        );

    } catch (error) {

        console.error(
            "Unread count error:",
            error
        );
    }
}


// ========================================
// RENDER NOTIFICATIONS
// ========================================

function renderNotifications(notifications) {

    const container =
        document.getElementById(
            "notificationContainer"
        );

    const empty =
        document.getElementById("empty");


    if (!container) {
        console.error(
            "notificationContainer not found"
        );
        return;
    }


    container.innerHTML = "";


    // No notifications

    if (!notifications ||
        notifications.length === 0) {

        if (empty) {
            empty.style.display = "block";
        }

        return;
    }


    if (empty) {
        empty.style.display = "none";
    }


    notifications.forEach(notification => {

        const div =
            document.createElement("div");


        div.className =
            "notification " +
            (
                notification.read
                    ? "read"
                    : "unread"
            );


        div.innerHTML = `

            <div class="notification-content">

                <h3>
                    ${escapeHtml(
                        notification.title ||
                        "Notification"
                    )}
                </h3>

                <p>
                    ${escapeHtml(
                        notification.message ||
                        ""
                    )}
                </p>

                <div class="time">
                    ${formatDateTime(
                        notification.createdAt
                    )}
                </div>

            </div>


            <div class="notification-actions">

                ${
                    notification.read

                    ? `

                        <button
                            class="read-btn"
                            onclick="markUnread(
                                ${notification.notificationId}
                            )">

                            Mark Unread

                        </button>

                    `

                    : `

                        <button
                            class="read-btn"
                            onclick="markRead(
                                ${notification.notificationId}
                            )">

                            Mark Read

                        </button>

                    `
                }


                <button
                    class="remove-btn"
                    onclick="deleteNotification(
                        ${notification.notificationId}
                    )">

                    Delete

                </button>

            </div>

        `;


        container.appendChild(div);

    });
}


// ========================================
// MARK AS READ
// ========================================

async function markRead(id) {

    try {

        const response = await fetch(
            "/notification/" +
            id +
            "/read",
            {
                method: "PUT"
            }
        );


        if (!response.ok) {
            throw new Error(
                "Unable to mark notification as read"
            );
        }


        await loadNotifications();

    } catch (error) {

        console.error(error);

        alert(error.message);
    }
}


// ========================================
// MARK AS UNREAD
// ========================================

async function markUnread(id) {

    try {

        const response = await fetch(
            "/notification/" +
            id +
            "/unread",
            {
                method: "PUT"
            }
        );


        if (!response.ok) {
            throw new Error(
                "Unable to mark notification as unread"
            );
        }


        await loadNotifications();

    } catch (error) {

        console.error(error);

        alert(error.message);
    }
}


// ========================================
// DELETE NOTIFICATION
// ========================================

async function deleteNotification(id) {

    if (!confirm(
        "Delete this notification?"
    )) {
        return;
    }


    try {

        const response = await fetch(
            "/notification/" + id,
            {
                method: "DELETE"
            }
        );


        if (!response.ok) {
            throw new Error(
                "Unable to delete notification"
            );
        }


        await loadNotifications();

    } catch (error) {

        console.error(error);

        alert(error.message);
    }
}


// ========================================
// DELETE ALL NOTIFICATIONS
// ========================================

async function deleteAll() {

    if (!confirm(
        "Delete all notifications?"
    )) {
        return;
    }


    try {

        const response = await fetch(
            "/notification/student/" +
            studentId,
            {
                method: "DELETE"
            }
        );


        if (!response.ok) {
            throw new Error(
                "Unable to delete notifications"
            );
        }


        await loadNotifications();

    } catch (error) {

        console.error(error);

        alert(error.message);
    }
}


// ========================================
// DATE & TIME
// ========================================

function formatDateTime(dateString) {

    if (!dateString) {
        return "";
    }


    const date =
        new Date(dateString);


    if (isNaN(date.getTime())) {
        return dateString;
    }


    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}


// ========================================
// HTML SECURITY
// ========================================

function escapeHtml(value) {

    return String(value || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


// ========================================
// PAGE LOAD
// ========================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        console.log(
            "Student ID:",
            studentId
        );

        loadNotifications();

        loadUnreadCount();

    }
);