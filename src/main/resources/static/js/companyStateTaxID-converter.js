const taxInput = document.getElementById('companyStateTaxID');
const taxHidden = document.getElementById('companyStateTaxIDHidden');

taxInput.addEventListener('input', function () {
    let raw = taxInput.value.replace(/\D/g, ''); // solo numeri

    // Formattazione: XXX-XXXX-X
    let formatted = '';
    if (raw.length >= 1) formatted += raw.slice(0, 3);
    if (raw.length >= 4) formatted += '-' + raw.slice(3, 7);
    if (raw.length >= 8) formatted += '-' + raw.slice(7, 8);

    // Limita la lunghezza massima a 9 caratteri numerici + 2 trattini
    if (formatted.length > 11) formatted = formatted.slice(0, 11);

    taxInput.value = formatted;
    taxHidden.value = formatted;
});

taxInput.addEventListener('keypress', function (e) {
    const char = e.key;
    if (!/^\d$/.test(char)) {
        e.preventDefault();
    }
});

taxInput.addEventListener('paste', function (e) {
    e.preventDefault();
});