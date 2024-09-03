export function formatDateTime(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}.${month}.${day}. ${hours}:${minutes}`;
}

export function updateCommentCount(increment) {
    const commentCountElement = document.querySelector('#comment-count');
    const currentCount = parseInt(commentCountElement.textContent);
    commentCountElement.textContent = currentCount + increment;
}