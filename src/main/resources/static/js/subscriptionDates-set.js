
//<input type="text" id="subscriptionStartDate" class="form-control" readonly th:field="*{subscriptionStartDate}" value="" />

document.addEventListener("DOMContentLoaded", function () {
    const startInput = document.getElementById("subscriptionStartDate");
    const endInput = document.getElementById("subscriptionEndDate");
    const typeSelect = document.getElementById("subscriptionType");

    if (startInput && !startInput.value) {
        const today = new Date().toISOString().split("T")[0];
        startInput.value = today;
    }

    const plansDurations = {
        "Free": 1,
        "Basic": 3,
        "Standard": 6,
        "Premium": 12,
        "Enterprise": 24
    };

    if (typeSelect && endInput && startInput) {
        typeSelect.addEventListener("change", function () {
            const months = plansDurations[this.value];
            if (months) {
                const startDate = new Date(startInput.value || new Date());
                const endDate = new Date(startDate);
                endDate.setMonth(endDate.getMonth() + months);
                const formattedEnd = endDate.toISOString().split("T")[0];
                endInput.value = formattedEnd;
            } else {
                endInput.value = "";
            }
        });
    }
});
