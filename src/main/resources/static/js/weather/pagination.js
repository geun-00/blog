function changePage(increment) {
    currentPage += increment;
    renderWeather(weatherData);
}

function addPaginationButtons() {
    const paginationContainer = document.getElementById('pagination-container');

    const prevButton = paginationContainer.getElementsByClassName('prev-button')[0];
    prevButton.onclick = () => {
        if (currentPage > 0) {
            changePage(-1);
        }
    };

    const nextButton = paginationContainer.getElementsByClassName('next-button')[0];
    nextButton.onclick = () => {
        if (currentPage < 1) {
            changePage(1);
        }
    };
}