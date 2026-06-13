function updateDatalist(inputId, datalistId, hiddenId) {
    let input = document.getElementById(inputId);
    let datalist = document.getElementById(datalistId);
    let hidden = document.getElementById(hiddenId);

    datalist.innerHTML = '';

    let searchValue = input.value.toLowerCase().trim();

    let filtered = cities.filter(city =>
        city.city_rus.toLowerCase().includes(searchValue) ||
        city.city.toLowerCase().includes(searchValue)
    );

    filtered.forEach(city => {
        let option = document.createElement('option');
        option.value = city.city_rus;
        option.setAttribute('data-code', city.city);
        option.setAttribute('data-city-ru', city.city_rus);
        datalist.appendChild(option);
    });

    if (searchValue === '') {
        datalist.innerHTML = '';
        cities.slice(0, 15).forEach(city => {
            let option = document.createElement('option');
            option.value = city.city_rus;
            option.setAttribute('data-code', city.city);
            datalist.appendChild(option);
        });
    }
}

function setupCityField(inputId, hiddenId, datalistId) {
    let input = document.getElementById(inputId);
    let hidden = document.getElementById(hiddenId);
    let datalist = document.getElementById(datalistId);

    input.addEventListener('input', function(e) {
        updateDatalist(inputId, datalistId, hiddenId);
        hidden.value = '';
    });

    input.addEventListener('change', function() {
        let selectedText = input.value;
        let found = cities.find(city => city.city_rus === selectedText);
        if (found) {
            hidden.value = found.city;
        } else {
            let match = cities.find(city => city.city_rus.toLowerCase() === selectedText.toLowerCase());
            if (match) {
                hidden.value = match.city;
                input.value = match.city_rus;
            } else {
                hidden.value = '';
            }
        }
    });

    document.getElementById('searchForm').addEventListener('submit', function(e) {
        if (!hidden.value) {
            e.preventDefault();
            alert('Пожалуйста, выберите город из списка');
            input.focus();
            return false;
        }
    });
}