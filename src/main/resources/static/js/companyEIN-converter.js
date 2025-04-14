const einInput = document.getElementById('companyEIN');
const einHidden = document.getElementById('companyEINHidden');

einInput.addEventListener('input', function () {
    let raw = einInput.value.replace(/\D/g, ''); // Solo numeri

    // Format: XX-XXXXXXX
    let formatted = '';
    if (raw.length >= 2) formatted += raw.slice(0, 2);
    if (raw.length >= 3) formatted += '-' + raw.slice(2, 9);
    if (raw.length > 9) formatted = formatted.slice(0, 10); // blocca extra

    einInput.value = formatted;
    einHidden.value = formatted;
});

einInput.addEventListener('keypress', function (e) {
    const char = e.key;
    if (!/^\d$/.test(char)) {
        e.preventDefault();
    }
});

// Blocca incolla
einInput.addEventListener('paste', function (e) {
    e.preventDefault();
});
