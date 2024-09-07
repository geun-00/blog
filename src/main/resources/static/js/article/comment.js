import {formatDateTime, updateCommentCount} from '../utils.js';

export function setupCommentSubmit() {
    const submitButton = document.getElementById('submit-btn');

    submitButton.addEventListener('click', function(event) {
        const articleId = document.getElementById('article-id').value;
        const commentText = document.querySelector('.comment_inbox_text').value.trim();

        if (commentText === '') {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        const body = JSON.stringify({
            comment: commentText
        });

        fetch(`/api/comments/${articleId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: body
        })
            .then(response => response.json())
            .then(data => {
                addCommentToList(data);
                document.querySelector('.comment_inbox_text').value = '';
                updateCommentCount(1);
            })
            .catch(error => {
                console.error('Error:', error.message);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });
    });
}

function addCommentToList(data) {
    const commentList = document.querySelector('.comment_list');

    const newComment = document.createElement('div');
    newComment.className = 'comment-item mb-3';
    const isAuthor = data.isAuthor;

    newComment.innerHTML = `
        <input type="hidden" id="comment-id" value="${data.id}">
        
        <div class="comment-header d-flex align-items-start">
            <div class="d-flex align-items-start flex-grow-1">
                <img src="${data.profileImageUrl}" alt="Profile Image"
                     class="rounded-circle img-fluid">
                <a class="author-name fw-bold">${data.author}</a>
                ${isAuthor ? '<span class="badge bg-success text-white ms-2">작성자</span>' : ''}
            </div>
        </div>
        
        <div class="d-flex align-items-center position-relative">
            <div class="comment-content fs-5 flex-grow-1">${data.comment}</div>
            <input type="text" class="form-control d-none" style="flex-grow: 1;"/>
            <button class="btn btn-link p-0 ms-3" style="margin-left: auto" 
                    type="button" onclick="toggleOptions(this)">
                <img src="/images/menu.png" alt="options" style="width: 20px; height: 20px;">
            </button>
            <ul class="options-menu list-unstyled position-absolute" style="display: none; left: 100%; top: 0; margin-left: 5px;">
                <li><button class="dropdown-item" 
                            onclick="editComment(this)">수정하기</button></li>
                <li><button class="dropdown-item text-danger" 
                            onclick="deleteComment(this)">삭제하기</button></li>
            </ul>
            <div class="edit-buttons d-none" style="margin-top: 5px;">
                <button class="btn btn-primary btn-sm" onclick="saveEdit(this)">수정</button>
                <button class="btn btn-secondary btn-sm" onclick="cancelEdit(this)">취소</button>
            </div>
        </div>
        <div class="text-muted fst-italic">${formatDateTime(data.createdAt)}</div>
        <hr class="comment-divider">
        `;
    commentList.appendChild(newComment);

    const authorElement = newComment.querySelector('.author-name');
    authorElement.onclick = function() {
        window.location.href = `/userPage?username=${data.author}`;
    };
    const profileImage = newComment.querySelector('.img-fluid');
    profileImage.onclick = function() {
        window.location.href = `/userPage?username=${data.author}`;
    };
}
