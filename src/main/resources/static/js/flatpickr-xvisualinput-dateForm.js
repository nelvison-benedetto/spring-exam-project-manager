document.addEventListener('DOMContentLoaded', function () {
    if (typeof flatpickr !== 'undefined') {
        flatpickr(".applyflatpickr", {
            dateFormat: "Y/m/d",
            allowInput: false
        });
    } else {
        console.log("Flatpickr library is not available.");
    }
});

//ogni tanto ho notato che torna su gg/MM/aaa disbilitando flatpickr...