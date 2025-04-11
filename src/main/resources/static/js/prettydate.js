window.addEventListener('DOMContentLoaded', function () {
    dayjs.extend(dayjs_plugin_isToday);
    dayjs.extend(dayjs_plugin_isYesterday);
    dayjs.extend(dayjs_plugin_relativeTime);
    dayjs.extend(dayjs_plugin_advancedFormat);
    dayjs.extend(dayjs_plugin_customParseFormat);

    document.querySelectorAll('.updated-at').forEach(function (el) {
        const dateStr = el.getAttribute('data-date');
        if (!dateStr) return;

        const d = dayjs(dateStr,"YYYY/MM/DD HH:mm:ss");
        let formatted;
        if (d.isValid()) {
            if (d.isToday()) {
                formatted = "Today at " + d.format("h:mm A");
            } else if (d.isYesterday()) {
                formatted = "Yesterday at " + d.format("h:mm A");
            } else {
                formatted = d.format("MMM D, YYYY h:mm A");
            }
            el.textContent = formatted;
        } else {
            el.textContent = "Invalid Date";
        }
    });
});
