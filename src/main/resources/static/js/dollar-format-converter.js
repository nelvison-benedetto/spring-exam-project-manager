const budgetInput = document.getElementById('budgetRendered');
const budgetHidden = document.getElementById('budget');

function formatBudgetInput(rawValue) {
    // Rimuove tutti i caratteri non numerici
    const digits = rawValue.replace(/\D/g, '');

    let cents = 0;

    if (digits) {
        cents = parseInt(digits, 10);
    }

    const value = (cents / 100).toFixed(2);

    // Formatta come "$1,234.56"
    budgetInput.value = '$' + parseFloat(value).toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });

    // Campo nascosto (numerico puro)
    budgetHidden.value = value;
}

// Formatter in input live
budgetInput.addEventListener('input', function () {
    formatBudgetInput(budgetInput.value);
});

// Blocca caratteri non numerici
budgetInput.addEventListener('keypress', function (e) {
    if (!/[0-9]/.test(e.key)) {
        e.preventDefault();
    }
});

// Previeni incolla
budgetInput.addEventListener('paste', function (e) {
    e.preventDefault();
});

// Formatter su load, anche se è zero
window.addEventListener('DOMContentLoaded', function () {
    const existingValue = budgetHidden.value;

    if (existingValue) {
        const cents = Math.round(parseFloat(existingValue) * 100);
        formatBudgetInput(String(cents));
    }
    // } else {
    //     // Se non c'è valore, inizializza a 0.00
    //     formatBudgetInput('0');
    // }
});
