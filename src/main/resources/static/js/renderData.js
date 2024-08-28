// console.log(performance.getEntriesByType("navigation")[0].type)

function renderData(storedResults) {
    const data = JSON.parse(storedResults);
    const resultsDiv = document.getElementById('article-list');
    resultsDiv.innerHTML = '';

    const today = new Date();
    const formattedToday = `${today.getFullYear()}. ${today.getMonth() + 1}. ${today.getDate()}.`;

    let count = sessionStorage.getItem('currentPage');

    data.forEach(article => {
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
}