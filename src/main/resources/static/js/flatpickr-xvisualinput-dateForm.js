document.addEventListener('DOMContentLoaded', function () {
    if (typeof flatpickr !== 'undefined') {
        flatpickr(".applyflatpickr", {  //code applied to classes css '.applyflatpickr' (cioe i miei input x le dates)
            dateFormat: "Y/m/d",  //maschera solamente x render, in db rimane format yyyy-MM-dd(top x sql queries)
            allowInput: false
        });
    } else {
        console.log("Flatpickr library is not available.");
    }
});

//ogni tanto ho notato che torna su gg/MM/aaa disbilitando flatpickr...