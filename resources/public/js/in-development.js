function disable_form() {
    $(".email-submission").replaceWith("<div class=\"already-submitted\"><p>We've got your email address, and will keep you posted.</p></div>")
}

function submit_email(email) {
    console.log("Submitting email", email, "...")
    $.post("/api/v0/submit-email", {"email": email})
	.done(function (res) {
	    console.log("GOT BACK", res)
	    localStorage.setItem("recorded", "true")
	    disable_form();
	})
	.fail(function (res) {
	    console.log("Something screwy happened...")
	})
}

window.onload = function () {
    if (typeof(Storage)!== "undefined" && "true" == localStorage.getItem("recorded")) {
	disable_form() ;
    } else {
	var email_input = document.querySelector(".email-input");
	var submit = document.querySelector("button");

	email_input.onkeyup = function (e) {
	    if (e.keyCode == 13) {
		submit_email(email_input.value);
	    }
	}
	submit.onclick = function (e) {
	    submit_email(email_input.value);
	}
    }
};
