window.addEventListener('DOMContentLoaded', function () {
    dayjs.extend(dayjs_plugin_isToday);
    dayjs.extend(dayjs_plugin_isYesterday);
    dayjs.extend(dayjs_plugin_relativeTime);
    dayjs.extend(dayjs_plugin_advancedFormat);
    dayjs.extend(dayjs_plugin_customParseFormat);

    document.querySelectorAll('.updated-at').forEach(function (el) {  //loop su tutti gli elementi
        const dateStr = el.getAttribute('data-date');  //read the html attribute e.g.<span class="updated-at" data-date="2025/07/09 14:30:00"></span>
        if (!dateStr) return;

        const d = dayjs(dateStr,"YYYY/MM/DD HH:mm:ss");  //conversion new format and in obj dayjs 
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
