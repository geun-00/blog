document.addEventListener('DOMContentLoaded', function () {

    const sizeSelector = document.getElementById('size-selector');

    const urlParams = new URLSearchParams(window.location.search);
    const size = urlParams.get('size');

    if (size) {
        sizeSelector.textContent = `${size}개씩`;
    } else {
        sizeSelector.textContent = '10개씩';
    }

    const searchFields = {
        period: document.getElementById('search-date'),
        title: document.getElementById('search-title'),
        author: document.getElementById('search-author'),
        content: document.getElementById('search-content'),
        'title_content': document.getElementById('search-title-content')
    };

    const searchInputs = {
        searchTitleInput: document.getElementById('searchTitle'),
        searchContentInput: document.getElementById('searchContent'),
        searchAuthorInput: document.getElementById('searchAuthor'),
        searchTitleWithContentInput: document.getElementById('searchTitleWithContent'),
        searchContentWithTitleInput: document.getElementById('searchContentWithTitle'),
        searchDateStartInput: document.getElementById('search-date-start'),
        searchDateEndInput: document.getElementById('search-date-end')
    };

    const searchTypeSelect = document.getElementById('search-type');

    function saveState() {
        const state = {
            searchType: searchTypeSelect.value,
            title: searchInputs.searchTitleInput.value,
            content: searchInputs.searchContentInput.value,
            author: searchInputs.searchAuthorInput.value,
            titleContent: {
                title: searchInputs.searchTitleWithContentInput.value,
                content: searchInputs.searchContentWithTitleInput.value
            },
            period: {
                startDate: searchInputs.searchDateStartInput.value,
                endDate: searchInputs.searchDateEndInput.value
            }
        };

        sessionStorage.setItem('searchState', JSON.stringify(state));
    }

    function restoreState() {
        const storedState = sessionStorage.getItem('searchState');
        if (storedState) {
            const state = JSON.parse(storedState);

            searchTypeSelect.value = state.selectedType;

            Object.keys(state.inputs).forEach(key => {
                if (searchInputs[key]) {
                    searchInputs[key].value = state.inputs[key];
                }
            });

            updateSearchFields();
        }
    }

    function clearInputs() {
        Object.values(searchInputs).forEach(input => input.value = '');
    }

    function updateSearchFields() {
        const selectedValue = searchTypeSelect.value;

        clearInputs();

        Object.values(searchFields).forEach(field => field.style.display = 'none');

        if (searchFields[selectedValue]) {
            searchFields[selectedValue].style.display = 'block';
        }
    }

    searchTypeSelect.addEventListener('change', updateSearchFields);
    restoreState();

    // 검색 버튼 클릭 시 상태 저장 및 검색
    const searchButton = document.getElementById('search-button');
    searchButton.addEventListener('click', function () {
        saveState();
    });
});

window.addEventListener('pageshow', function (event) {
    const type = performance.getEntriesByType('navigation')[0].type;
    if (window.performance) {
        if (type === 'back_forward' || type === 'reload') {
            const pageNumber = sessionStorage.getItem('pageNumber');
            if (pageNumber) {
                loadPage(pageNumber);
            }
        }
        else if (type === 'navigate') {
            sessionStorage.removeItem('pageNumber');
            sessionStorage.removeItem('searchState');
        }
    }
});