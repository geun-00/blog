function showLoading() {
    document.getElementById('loading-message').style.display = 'block';
    document.getElementById('weather-container').style.display = 'none';
    document.getElementById('pagination-container').style.display = 'none';
}

function hideLoading() {
    document.getElementById('loading-message').style.display = 'none';
    document.getElementById('weather-container').style.display = 'flex';
    document.getElementById('pagination-container').style.display = 'block';
}

function getCurrentDate() {
    const today = new Date();
    const month = today.getMonth() + 1;
    const day = today.getDate();
    return `${month}월 ${day}일`;
}

function renderWeather(weatherData) {
    const container = document.getElementById('weather-container');
    container.innerHTML = ''; // 초기화

    const currentDateElement = document.getElementById('cur-date');
    if (currentDateElement) {
        currentDateElement.textContent = getCurrentDate();
    }

    const groupedData = groupByTime(weatherData);
    let tmx = '';
    let tmn = '';

    const filteredData = groupedData.filter(item => {
        if (item.TMN) {
            tmn = item.TMN.fcstValue;
            return false;
        }
        if (item.TMX) {
            tmx = item.TMX.fcstValue;
        }

        const hour = parseInt(item.fcstTime.substring(0, 2), 10);
        if (currentPage === 0) {
            return hour >= 8 && hour <= 15;
        } else {
            return hour >= 16 && hour <= 23;
        }
    });

    const weatherTitle = document.querySelector('#tmx_tmn');
    if (weatherTitle) {
        weatherTitle.innerHTML = `
            <strong>최고</strong>: <span class="max-temp">${tmx}°C</span> 
            <strong>최저</strong>: <span class="min-temp">${tmn}°C</span>
        `;
    }

    filteredData.forEach(item => {
        const hour = item.fcstTime.substring(0, 2) + "시";
        const weatherInfo = `
        <div class="weather-item">
            <p><strong>${hour}</strong> </p>
            <div class="weather-category">
                <img src="/images/tmp.png" alt="TMP 이미지" class="weather-icon" />
                <p>${item.TMP ? item.TMP.fcstValue + '°C' : 'N/A'}</p>
            </div>
            <div class="weather-category">
                <img src="/images/rain.png" alt="POP 이미지" class="weather-icon" />
                <p>${item.POP ? item.POP.fcstValue + '%' : 'N/A'}</p>
            </div>
        </div>
        `;
        container.innerHTML += weatherInfo;
    });
}

function groupByTime(weatherData) {
    const groupedData = [];

    weatherData.forEach(item => {
        const existing = groupedData.find(group => group.fcstTime === item.fcstTime);

        if (existing) {
            existing[item.category] = item;
        } else {
            const newGroup = {
                fcstTime: item.fcstTime,
            };
            newGroup[item.category] = item;
            groupedData.push(newGroup);
        }
    });

    return groupedData;
}
