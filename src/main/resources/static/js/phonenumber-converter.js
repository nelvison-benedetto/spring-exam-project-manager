document.addEventListener('DOMContentLoaded', () => {
    const phoneInput = document.getElementById('phoneNumberRendered');
    const phoneHidden = document.getElementById('phoneNumber');

    // Funzione di formattazione
    function formatPhone(rawValue) {
        const digits = rawValue.replace(/\D/g, ''); // Solo numeri
        let formatted = '';

        if (digits.length >= 1) formatted = '+' + digits.slice(0, 2);
        if (digits.length >= 3) formatted += ' ' + digits.slice(2, 5);
        if (digits.length >= 6) formatted += ' ' + digits.slice(5, 8);
        if (digits.length >= 9) formatted += ' ' + digits.slice(8, 12);

        return formatted;
    }

    // All'avvio: se hidden ha un valore, formatta e mostra nel visibile
    if (phoneHidden && phoneHidden.value) {
        phoneInput.value = formatPhone(phoneHidden.value);
    }

    // Durante l'input
    phoneInput.addEventListener('input', () => {
        let raw = phoneInput.value.replace(/[^+\d]/g, '');

        // Gestione del "+" come primo carattere
        if (raw.includes('+')) {
            raw = '+' + raw.replace(/\+/g, '');
        }

        if (!raw.startsWith('+') && raw.length > 0) {
            raw = '+' + raw;
        }

        // Se è solo "+", svuota tutto
        if (raw === '+') {
            phoneInput.value = '';
            phoneHidden.value = '';
            return;
        }

        const formatted = formatPhone(raw);
        phoneInput.value = formatted;
        phoneHidden.value = formatted.replace(/\s+/g, ''); // senza spazi
    });

    // Blocca caratteri non validi
    phoneInput.addEventListener('keypress', (e) => {
        const char = e.key;
        const value = phoneInput.value;

        // Solo numeri o "+" come primo carattere
        if (!/^\d$/.test(char) && !(char === '+' && value.indexOf('+') === -1 && value.length === 0)) {
            e.preventDefault();
        }
    });

    // Blocca incolla
    phoneInput.addEventListener('paste', (e) => {
        e.preventDefault();
    });
});
