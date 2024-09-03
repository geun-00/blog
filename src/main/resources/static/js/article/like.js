let isLiked = false;

export function handleIsLiked() {
    const articleId = document.getElementById('article-id').value;

    fetch(`/api/articles/${articleId}/liked`)
        .then(response => response.json())
        .then(isLikedResponse => {
            if (isLikedResponse) {
                const likeIcon = document.getElementById('like-icon');
                likeIcon.src = '/images/icon_like_on.png';
                isLiked = true;
            }
        })
        .catch(error => {
            console.error('Error fetching like status:', error);
        });
}

export function handleLike() {
    if (isLiked) return;

    const articleId = document.getElementById('article-id').value;

    fetch(`/api/articles/like/${articleId}`, {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                isLiked = true;
                const likeIcon = document.getElementById('like-icon');
                const likeCount = document.getElementById('like-count');

                likeIcon.src = '/images/icon_like_on.png';
                likeCount.textContent = parseInt(likeCount.textContent) + 1;
            } else {
                alert('오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}
