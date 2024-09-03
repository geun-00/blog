let isLiked = false;

export function handleIsLiked() {
    const articleId = document.getElementById('article-id').value;

    fetch(`/api/articles/${articleId}/liked`)
        .then(response => response.json())
        .then(data => {
            isLiked = data.liked;
            updateLikeUI(isLiked, data.likesCount);
        })
        .catch(error => {
            console.error('Error fetching like status:', error);
        });
}

export function handleLike() {

    const articleId = document.getElementById('article-id').value;
    const method = isLiked ? 'DELETE' : 'POST';

    fetch(`/api/articles/like/${articleId}`, {
        method: method
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                isLiked = !isLiked;
                updateLikeUI(isLiked, data.data);
            } else {
                alert('오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

function updateLikeUI(isLiked, likesCount) {
    const likeIcon = document.getElementById('like-icon');
    const likeCount = document.getElementById('like-count');

    likeIcon.src = isLiked ? '/images/icon_like_on.png' : '/images/icon_like_off.png';
    likeCount.textContent = likesCount;
}