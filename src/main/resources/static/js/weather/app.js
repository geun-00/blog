let currentPage = 0;
let weatherData = [];

window.onload = function() {
    if (navigator.geolocation) {
        showLoading();
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        console.log("Geolocation is not supported by this browser.");
    }
    addPaginationButtons();
};

function showPosition(position) {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;

    showLoading();

    fetch('/api/weather?latitude=' + latitude + '&longitude=' + longitude, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            weatherData = data.data;
            renderWeather(weatherData);
            hideLoading();
        })
        .catch(error => {
            console.error('Error fetching weather data:', error);
            hideLoading();
            showErrorMessage();
        });
}

function showErrorMessage() {
    document.getElementById('error-message').style.display = 'block'; // 오류 메시지 표시
}

function showError(error) {
    showErrorMessage();
    hideLoading();
    console.error(error.message);
}