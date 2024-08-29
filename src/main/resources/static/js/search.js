function submitForm() {

    const searchType = document.getElementById('search-type').value;
    const searchTitle = document.getElementById('searchTitle').value;
    const searchContent = document.getElementById('searchContent').value;
    const searchAuthor = document.getElementById('searchAuthor').value;
    const searchViews = document.getElementById('searchViews').value;
    const searchTitleWithContent = document.getElementById('searchTitleWithContent').value;
    const searchContentWithTitle = document.getElementById('searchContentWithTitle').value;
    const searchDateStart = document.getElementById('search-date-start').value;
    const searchDateEnd = document.getElementById('search-date-end').value;

    switch (searchType) {
        case 'title':
            if (!searchTitle) {
                sendAlert('제목');
                return;
            }
            break;
        case 'content':
            if (!searchContent) {
                sendAlert('내용');
                return;
            }
            break;
        case 'author':
            if (!searchAuthor) {
                sendAlert('작성자');
                return;
            }
            break;
        case 'views':
            if (!searchViews) {
                sendAlert('조회수');
                return;
            }
            break;
        case 'title-content':
            if (!searchTitleWithContent) {
                sendAlert('제목');
                return;
            }
            if (!searchContentWithTitle) {
                sendAlert('내용');
                return;
            }
            break;
        case 'period':
            if (!searchDateStart || !searchDateEnd) {
                sendAlert('날짜');
                return;
            }
            if (searchDateStart > searchDateEnd) {
                alert('기간을 정확하게 입력해주세요.');
                return;
            }
            break;
    }

    function sendAlert(message) {
        alert(message + '(을)를 입력해주세요');
    }

    const searchState = JSON.stringify({
        searchType: searchType,
        title: searchTitle,
        content: searchContent,
        author: searchAuthor,
        views: searchViews,
        titleContent: {
            title: searchTitleWithContent,
            content: searchContentWithTitle
        },
        period: {
            startDate: searchDateStart,
            endDate: searchDateEnd
        }
    })

    sessionStorage.setItem('searchState', searchState);

    const pageNumber = sessionStorage.getItem('pageNumber') || '1';

    loadPage(pageNumber)
}