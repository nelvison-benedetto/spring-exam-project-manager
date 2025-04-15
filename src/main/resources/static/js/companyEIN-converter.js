const einInput = document.getElementById('companyEINRendered');
const einHidden = document.getElementById('companyEIN');

function formatEIN(raw) {
    raw = raw.replace(/\D/g, ''); // solo numeri

    // Formattazione: XX-XXXXXXX
    let formatted = '';
    if (raw.length >= 1) formatted += raw.slice(0, 2);
    if (raw.length >= 3) formatted += '-' + raw.slice(2, 9);

    // Max lunghezza: 9 cifre + 1 trattino
    return formatted.slice(0, 10);
}

einInput.addEventListener('input', function () {
    const formatted = formatEIN(einInput.value);
    einInput.value = formatted;
    einHidden.value = formatted;
});

einInput.addEventListener('keypress', function (e) {
    if (!/^\d$/.test(e.key)) {
        e.preventDefault();
    }
});

einInput.addEventListener('paste', function (e) {
    e.preventDefault();
});

// Formattazione automatica su caricamento pagina
document.addEventListener('DOMContentLoaded', function () {
    if (einHidden && einHidden.value) {
        einInput.value = formatEIN(einHidden.value);
    }
});
