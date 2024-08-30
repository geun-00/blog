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
                newComment.className = 'comment_item';
                newComment.innerHTML = `
                <span class="comment_author">${data.author}</span>
                <span class="comment_content">${data.comment}</span>
            `;
                commentList.appendChild(newComment);

                document.querySelector('.comment_inbox_text').value = '';
            })
            .catch(error => {
                console.error('Error:', error.message);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });
    });
});