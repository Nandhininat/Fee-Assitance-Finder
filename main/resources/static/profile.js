const studentId =
    localStorage.getItem("studentId");


if (!studentId) {
    window.location.href = "login.html";
}


let currentStudent = null;


async function loadProfile() {

    try {

        const response =
            await fetch("/student");


        const students =
            await response.json();


        currentStudent =
            students.find(
                student =>
                    Number(student.studentid)
                    === Number(studentId)
            );


        if (!currentStudent) {

            throw new Error(
                "Student profile not found"
            );
        }


        document.getElementById(
            "studentname"
        ).value =
            currentStudent.studentname || "";


        document.getElementById(
            "username"
        ).value =
            currentStudent.username || "";


        document.getElementById(
            "course"
        ).value =
            currentStudent.course || "";


        document.getElementById(
            "income"
        ).value =
            currentStudent.income || 0;


        document.getElementById(
            "academicDetails"
        ).value =
            currentStudent.academicDetails || "";


        document.getElementById(
            "category"
        ).value =
            currentStudent.category || "General";


        document.getElementById(
            "location"
        ).value =
            currentStudent.location || "";


    } catch (error) {

        showMessage(
            error.message,
            "error"
        );
    }
}


document.getElementById(
    "profileForm"
).addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();


        if (!currentStudent) {
            return;
        }


        const student = {

            studentid:
                Number(studentId),

            studentname:
                document.getElementById(
                    "studentname"
                ).value.trim(),

            username:
                currentStudent.username,

            /*
             * Keep existing password.
             * The backend will receive the stored
             * password from the current object.
             */
            password:
                currentStudent.password || "",

            course:
                document.getElementById(
                    "course"
                ).value.trim(),

            income:
                Number(
                    document.getElementById(
                        "income"
                    ).value
                ),

            academicDetails:
                document.getElementById(
                    "academicDetails"
                ).value.trim(),

            category:
                document.getElementById(
                    "category"
                ).value,

            location:
                document.getElementById(
                    "location"
                ).value.trim()
        };


        /*
         * Because password is WRITE_ONLY,
         * GET /student does not return it.
         *
         * Therefore profile update through the
         * current POST /student endpoint should
         * NOT be used blindly.
         *
         * This page currently updates the profile
         * only if the backend accepts the existing
         * password.
         */


        try {

            const response =
                await fetch(
                    "/student",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify(
                            student
                        )
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Unable to update profile"
                );
            }


            localStorage.setItem(
                "studentName",
                data.studentname
            );


            showMessage(
                "Profile updated successfully!",
                "success"
            );


        } catch (error) {

            showMessage(
                error.message,
                "error"
            );
        }

    });


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


loadProfile();