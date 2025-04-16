const passwordInput = document.getElementById("password");
const confirmInput = document.querySelector("input[name='confirmPassword']");
const submitBtn = document.getElementById("submitBtn");

function validatePasswordMatch() {
    const password = passwordInput.value.trim();
    const confirm = confirmInput.value.trim();

    if (confirm === "") {
        confirmInput.classList.remove("is-valid");
        confirmInput.classList.add("is-invalid");
        submitBtn.disabled = true;
        return;
    }

    if (password === confirm) {
        confirmInput.classList.remove("is-invalid");
        confirmInput.classList.add("is-valid");
        submitBtn.disabled = false;
    } else {
        confirmInput.classList.remove("is-valid");
        confirmInput.classList.add("is-invalid");
        submitBtn.disabled = true;
    }
}

passwordInput.addEventListener("input", validatePasswordMatch);
confirmInput.addEventListener("input", validatePasswordMatch);