const phoneInput = document.getElementById('phoneNumber');
const phoneHidden = document.getElementById('phoneHidden');

phoneInput.addEventListener('input', function () {
    let raw = phoneInput.value;

    // Rimuove tutti i caratteri tranne numeri e +
    raw = raw.replace(/[^+\d]/g, '');

    // Permetti il "+" solo come primo carattere
    if (raw.includes('+')) {
        raw = '+' + raw.replace(/\+/g, '');
    }

    // Se inizia con numeri e NON con "+", non aggiungerlo forzatamente
    if (!raw.startsWith('+') && raw.length > 0) {
        raw = '+' + raw;
    }

    // Se è vuoto, lascia vuoto (non reimpostare +)
    if (raw === '+') {
        phoneInput.value = '';
        phoneHidden.value = '';
        return;
    }

    // Estrarre solo i numeri (senza il "+")
    const digits = raw.replace(/\D/g, '');

    // Format italiano: +39 333 456 7890
    let formatted = '';
    if (digits.length >= 1) formatted = '+' + digits.slice(0, 2);
    if (digits.length >= 3) formatted += ' ' + digits.slice(2, 5);
    if (digits.length >= 6) formatted += ' ' + digits.slice(5, 8);
    if (digits.length >= 9) formatted += ' ' + digits.slice(8, 12);

    phoneInput.value = formatted;

    // Hidden value senza spazi
    phoneHidden.value = formatted.replace(/\s+/g, '');
});

// Solo numeri e '+' all'inizio
phoneInput.addEventListener('keypress', function (e) {
    const char = e.key;
    const value = phoneInput.value;

    // Consenti solo numeri o + come primo carattere (e solo una volta)
    if (!/^\d$/.test(char) && !(char === '+' && value.indexOf('+') === -1 && value.length === 0)) {
        e.preventDefault();
    }
});

// Blocca incolla
phoneInput.addEventListener('paste', function (e) {
    e.preventDefault();
});
