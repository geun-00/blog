document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.getElementById('submit-btn');

    submitButton.addEventListener('click', function(event) {
        event.preventDefault();

        const articleId = document.getElementById('article-id').value;
        const commentText = document.querySelector('.comment_inbox_text').value.trim();

        if (commentText === '') {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        const body = JSON.stringify({
            articleId: articleId,
            comment: commentText
        });

        fetch('/api/comments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: body
        })
            .then(response => response.json())
            .then(data => {
                const commentList = document.querySelector('.comment_list');

                const newComment = document.createElement('div');
                newComment.className = 'comment-item mb-3';
                const isAuthor = data.isAuthor;

                function formatDateTime(dateString) {
                    const date = new Date(dateString);
                    const year = date.getFullYear();
                    const month = String(date.getMonth() + 1).padStart(2, '0');
                    const day = String(date.getDate()).padStart(2, '0');
                    const hours = String(date.getHours()).padStart(2, '0');
                    const minutes = String(date.getMinutes()).padStart(2, '0');

                    return `${year}.${month}.${day}. ${hours}:${minutes}`;
                }

                newComment.innerHTML = `
                    <div class="comment-header">
                        <div class="d-flex align-items-center">
                            <a class="author-name fw-bold">${data.author}</a>
                            ${isAuthor ? '<span class="badge bg-success text-white ms-2">작성자</span>' : ''}
                        </div>
                        <div class="text-muted fst-italic">${formatDateTime(data.createdAt)}</div>
                    </div>
                    <div class="comment-content fs-5">${data.comment}</div>
                    <hr class="comment-divider">
                `;
                commentList.appendChild(newComment);

                const authorElement = newComment.querySelector('.author-name');
                authorElement.onclick = function() {
                    window.location.href = `/myPage?author=${data.author}`;
                };

                document.querySelector('.comment_inbox_text').value = '';
            })
            .catch(error => {
                console.error('Error:', error.message);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });
    });
});