function toggleDropdown() {
    const dropdownMenu = document.querySelector('.dropdown-menu');
    dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
}

function setPageSize(size) {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.set('size', size);
    window.location.href = currentUrl.toString();
}

function loadPage(pageNumber) {
    event.preventDefault();

    const url = new URL(window.location.href);
    const params = new URLSearchParams(url.search);
    const pageSize = params.get('size') || '10';

    fetch(`/page/articles?page=${pageNumber - 1}&size=${pageSize}`, {
        method: 'GET'
    }).then(res => res.json())
        .then(data => {
            sessionStorage.setItem('pagingResults', JSON.stringify(data.dataList));

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
                    <a href="/articles/${article.id}">${article.title}</a>
                </td>
                <td>
                    ${articleDateString === formattedToday ? articleTimeString : articleDateString}
                </td>
                <td>
                    ${article.author}
                </td>
            `;
                resultsDiv.appendChild(articleElement);
            });
            updatePagination(data)
        })

    function updatePagination(data) {
        const paginationDiv = document.getElementById('pagination');
        paginationDiv.innerHTML = '';

        if (data.prev) {
            const prevLink = document.createElement('a');
            prevLink.href = "javascript:void(0);";
            prevLink.setAttribute('data-page', data.prevPage);
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
            nextLink.onclick = () => loadPage(data.nextPage);
            nextLink.textContent = "다음";
            paginationDiv.appendChild(nextLink);
        }
    }
}