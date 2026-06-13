document.addEventListener('DOMContentLoaded', function() {
    let depDatalist = document.createElement('datalist');
    depDatalist.id = 'cityListDeparture';
    document.body.appendChild(depDatalist);
    let arrDatalist = document.createElement('datalist');
    arrDatalist.id = 'cityListArrival';
    document.body.appendChild(arrDatalist);

    // Привязываем поля
    document.getElementById('departureInput').setAttribute('list', 'cityListDeparture');
    document.getElementById('arrivalInput').setAttribute('list', 'cityListArrival');

    setupCityField('departureInput', 'departureCity', 'cityListDeparture');
    setupCityField('arrivalInput', 'arrivalCity', 'cityListArrival');

    updateDatalist('departureInput', 'cityListDeparture', 'departureCity');
    updateDatalist('arrivalInput', 'cityListArrival', 'arrivalCity');
});
/*]]>*/
(function() {
    const dep = document.getElementById('departureDate');
    const ret = document.getElementById('returnDate');

    function syncDates() {

        if (dep.type === 'date' && dep.value) {
            ret.min = dep.value;

            if (ret.value && ret.value < dep.value) ret.value = '';
        } else {
            ret.min = '';
        }

        if (ret.type === 'date' && ret.value) {
            dep.max = ret.value;
            if (dep.value && dep.value > ret.value) dep.value = '';
        } else {
            dep.max = '';
        }
    }

    dep.addEventListener('change', syncDates);
    dep.addEventListener('blur', syncDates);
    ret.addEventListener('change', syncDates);
    ret.addEventListener('blur', syncDates);

    dep.addEventListener('focus', function() {
        if (this.type !== 'date') this.type = 'date';
        syncDates();
    });
    ret.addEventListener('focus', function() {
        if (this.type !== 'date') this.type = 'date';
        syncDates();
    });

    syncDates();
})();