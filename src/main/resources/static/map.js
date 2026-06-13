ymaps.ready(function () {
    var myMap = new ymaps.Map('map', {
        center: [55.75, 37.61],
        zoom: 4,
        controls: ['zoomControl']
    });

    var markersMap = {}; // хранилище маркеров по id

    // Загружаем сохранённые города
    function loadSavedCities() {
        if (savedCities && savedCities.length > 0) {
            savedCities.forEach(function(city) {
                var preset = city.visited ? 'islands#redDotIcon' : 'islands#blueDotIcon';
                var placemark = new ymaps.Placemark([city.lat, city.lng], {
                    balloonContent: city.city
                }, { preset: preset });
                myMap.geoObjects.add(placemark);
                markersMap[city.id] = placemark;
            });
            if (myMap.geoObjects.getLength() > 0) {
                myMap.setBounds(myMap.geoObjects.getBounds(), { zoomMargin: 20 });
            }
        }
    }
    loadSavedCities();

    // Поиск и добавление нового города
    var addressInput = document.getElementById('addressInput');
    var searchButton = document.getElementById('searchButton');

    function findAddress() {
        var address = addressInput.value.trim();
        if (!address) return;

        ymaps.geocode(address, { results: 1 }).then(function (res) {
            var geo = res.geoObjects.get(0);
            if (geo) {
                var coords = geo.geometry.getCoordinates();
                var cityName = geo.getAddressLine();

                fetch('/wishlist/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ city: cityName, lat: coords[0], lng: coords[1] })
                })
                    .then(res => res.json())
                    .then(data => {
                        // Новый город всегда не посещён — синяя иконка
                        var placemark = new ymaps.Placemark([data.lat, data.lng], {
                            balloonContent: data.city
                        }, { preset: 'islands#blueDotIcon' });
                        myMap.geoObjects.add(placemark);
                        markersMap[data.id] = placemark;

                        myMap.setBounds(myMap.geoObjects.getBounds(), { zoomMargin: 20 });
                        addCityToList(data.id, data.city, data.visited);
                        addressInput.value = '';
                    })
                    .catch(err => {
                        alert('Ошибка сохранения города');
                        console.error(err);
                    });
            } else {
                alert('Город не найден');
            }
        });
    }

    searchButton.addEventListener('click', findAddress);
    addressInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') findAddress();
    });

    // Функция добавления <li> в #cityList
    function addCityToList(id, cityName, visited) {
        var cityList = document.getElementById('cityList');
        if (!cityList) return;
        var li = document.createElement('li');
        li.innerHTML =
            '<input type="checkbox" class="visited-checkbox" data-id="' + id + '"' +
            (visited ? ' checked' : '') + '> ' +
            '<span>' + cityName + '</span> ' +
            '<button class="delete-city-btn" data-id="' + id + '" ' +
            'style="margin-left: 8px; cursor: pointer; background: none; border: none; color: red; font-weight: bold;" ' +
            'title="Удалить город">✕</button>';
        cityList.appendChild(li);
    }

    // Обработка изменения чекбоксов (отметка "посетил")
    document.getElementById('cityList').addEventListener('change', function(e) {
        if (e.target.classList.contains('visited-checkbox')) {
            var id = e.target.dataset.id;
            var isChecked = e.target.checked;

            fetch('/wishlist/' + id + '/toggle', { method: 'PATCH' })
                .then(() => {
                    // Меняем иконку маркера
                    if (markersMap[id]) {
                        var newPreset = isChecked ? 'islands#redDotIcon' : 'islands#blueDotIcon';
                        markersMap[id].options.set('preset', newPreset);
                    }
                })
                .catch(err => console.error('Ошибка переключения:', err));
        }
    });

    document.getElementById('cityList').addEventListener('click', function(e) {
        if (e.target.classList.contains('delete-city-btn')) {
            var id = e.target.dataset.id;
            if (!confirm('Удалить город из вишлиста?')) return;

            fetch('/wishlist/' + id, { method: 'DELETE' })
                .then(function(response) {
                    if (response.ok) {
                        // Удаляем маркер с карты
                        if (markersMap[id]) {
                            myMap.geoObjects.remove(markersMap[id]);
                            delete markersMap[id];
                        }
                        // Удаляем элемент списка
                        e.target.closest('li').remove();
                    } else {
                        alert('Ошибка при удалении');
                    }
                })
                .catch(function(err) {
                    console.error('Ошибка удаления:', err);
                });
        }
    });
});