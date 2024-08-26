function submitForm() {

    const searchType = document.getElementById('search-type').value;
    const searchTitle = document.getElementById('searchTitle').value;
    const searchContent = document.getElementById('searchContent').value;
    const searchAuthor = document.getElementById('searchAuthor').value;
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

    const body = JSON.stringify({
        searchType: searchType,
        title: searchTitle,
        content: searchContent,
        author: searchAuthor,
        titleContent: {
            title: searchTitleWithContent,
            content: searchContentWithTitle
        },
        period: {
            startDate: searchDateStart,
            endDate: searchDateEnd
        }
    })

    // 비동기 요청 보내기
    fetch('/articles/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: body
    })
        .then(response => response.json())
        .then(data => {
            // 결과를 동적으로 표시
            const resultsDiv = document.getElementById('article-list');
            resultsDiv.innerHTML = ''; // 기존 결과를 비우기

            let count = 1;
            data.forEach(article => {
                const articleElement = document.createElement('tr');
                articleElement.innerHTML = `
                <td>${count++}</td>
                <td>
                    <a href="/articles/${article.id}">${article.title}</a>
                </td>
                <td>
                    ${new Date(article.createdAt).toLocaleDateString()}
                </td>
                <td>
                    ${article.author}
                </td>
            `;
                resultsDiv.appendChild(articleElement);
            });
        })
        .catch(error => {
            console.error('Fetch error:', error);
            alert('서버와의 통신 중 오류가 발생했습니다.');
        });
}