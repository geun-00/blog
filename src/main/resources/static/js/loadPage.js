function loadPage(pageNumber) {
    event.preventDefault();

    const url = new URL(window.location.href);
    const params = new URLSearchParams(url.search);
    const pageSize = params.get('size') || '10';

    sessionStorage.setItem('pageNumber', pageNumber);

    const searchState = sessionStorage.getItem('searchState');
    if (searchState) {
        loadAllBySearchData(pageNumber, pageSize, searchState);
    } else {
        loadAllData(pageNumber, pageSize);
    }
}

function loadAllData(pageNumber, pageSize) {

    fetch(`/api/articles/page?page=${pageNumber - 1}&size=${pageSize}`, {
        method: 'get'
    }).then(response => response.json())
        .then(data => renderArticles(data, pageNumber, pageSize))
        .catch(error => {
            console.log(error);
            alert('오류가 발생했습니다.');
        })

}

function loadAllBySearchData(pageNumber, pageSize, body) {

    fetch(`/api/articles/search?page=${pageNumber - 1}&size=${pageSize}`, {
        method: 'post',
        headers: {
            'Content-type': 'application/json'
        },
        body: body
    }).then(response => response.json())
        .then(data => renderArticles(data, pageNumber, pageSize))
        .catch(error => {
            console.log(error);
            alert('오류가 발생했습니다.');
        })
}

function renderArticles(data, pageNumber, pageSize) {
    const resultsDiv = document.getElementById('article-list');
    resultsDiv.innerHTML = '';

    const today = new Date();
    const formattedToday = `${today.getFullYear()}. ${today.getMonth() + 1}. ${today.getDate()}.`;
    let count = (pageNumber - 1) * pageSize + 1;

    data.dataList.forEach(article => {
        const articleElement = document.createElement('tr');
        const articleDate = new Date(article.createdAt);
        const articleDateString = articleDate.toLocaleDateString();
        const articleTimeString = articleDate.toTimeString().split(' ')[0].substring(0, 5);

        articleElement.innerHTML = `
                <td>${count++}</td>
                <td>
                    <div class="article-container">
                        <a href="/articles/${article.id}">${article.title}</a>
                        <span>[${article.countComment}]</span> <!-- 댓글 수 표시 -->
                    </div>
                </td>
                <td>
                    ${articleDateString === formattedToday ? articleTimeString : articleDateString}
                </td>
                <td>
                    ${article.author}
                </td>
                <td>
                    ${article.views}
                </td>
            `;

        resultsDiv.appendChild(articleElement);
    });

    updatePagination(data);
}

function updatePagination(data) {
    const paginationDiv = document.getElementById('pagination');
    paginationDiv.innerHTML = '';

    if (data.prev) {
        const prevLink = document.createElement('a');
        prevLink.href = "javascript:void(0);";
        prevLink.setAttribute('data-page', data.prevPage);
        prevLink.className = 'prev';
        prevLink.onclick = () => loadPage(data.prevPage);
        prevLink.textContent = "이전";
        paginationDiv.appendChild(prevLink);
    }

    data.pageNumList.forEach(pageNum => {
        const pageLink = document.createElement('a');
        pageLink.href = "javascript:void(0);";
        pageLink.setAttribute('data-page', pageNum);
        pageLink.className = pageNum === data.currentPage ? 'active' : '';
        pageLink.onclick = () => loadPage(pageNum);
        pageLink.textContent = pageNum;
        paginationDiv.appendChild(pageLink);
    });

    if (data.next) {
        const nextLink = document.createElement('a');
        nextLink.href = "javascript:void(0);";
        nextLink.setAttribute('data-page', data.nextPage);
        nextLink.className = 'next';
        nextLink.onclick = () => loadPage(data.nextPage);
        nextLink.textContent = "다음";
        paginationDiv.appendChild(nextLink);
    }
}