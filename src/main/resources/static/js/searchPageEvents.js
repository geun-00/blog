document.addEventListener('DOMContentLoaded', function () {

    const searchFields = {
        period: document.getElementById('search-date'),
        title: document.getElementById('search-title'),
        author: document.getElementById('search-author'),
        content: document.getElementById('search-content'),
        'title-content': document.getElementById('search-title-content')
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
            selectedType: searchTypeSelect.value,
            inputs: Object.keys(searchInputs).reduce((acc, key) => {
                acc[key] = searchInputs[key].value;
                return acc;
            }, {})
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

        saveState();
    }

    Object.values(searchInputs).forEach(input => {
        input.addEventListener('input', saveState);
    });

    searchTypeSelect.addEventListener('change', updateSearchFields);
    restoreState();
});

window.addEventListener('pageshow', function (event) {
    const type = performance.getEntriesByType('navigation')[0].type;
    if (window.performance) {
        if (type === 'back_forward' || type === 'reload') {
            const storedResults = sessionStorage.getItem('searchResults');
            if (storedResults) {
                renderData(storedResults);
            }
        } else if (type === 'navigate') {
            sessionStorage.removeItem('searchResults');
            sessionStorage.removeItem('searchState');
        }
    }
});