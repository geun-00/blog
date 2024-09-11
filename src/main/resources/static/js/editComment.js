let openMenu = null;

function toggleOptions(button) {
    const menu = button.nextElementSibling;

    if (menu.style.display === 'block') {
        menu.style.display = 'none';
        openMenu = null;
    } else {
        if (openMenu) {
            openMenu.style.display = 'none';
        }
        menu.style.display = 'block';
        openMenu = menu;
    }
}

function editComment(element) {
    // 현재 클릭한 댓글 요소를 찾음
    const commentDiv = element.closest('.d-flex');
    const commentTextDiv = commentDiv.querySelector('.comment-content');
    const inputField = commentDiv.querySelector('input[type="text"]');
    const editButtons = commentDiv.querySelector('.edit-buttons');

    // 기존 댓글 내용을 input 필드에 설정
    inputField.value = commentTextDiv.textContent.trim();

    // 댓글 내용 숨기고, input 필드와 수정/취소 버튼 보여주기
    commentTextDiv.classList.add('d-none');
    inputField.classList.remove('d-none');
    editButtons.classList.remove('d-none');
}

function saveEdit(button) {
    const commentDiv = button.closest('.d-flex');
    const inputField = commentDiv.querySelector('input[type="text"]');
    const commentTextDiv = commentDiv.querySelector('.comment-content');
    const editButtons = commentDiv.querySelector('.edit-buttons');
    const commentId = commentDiv.closest('.comment-item').querySelector('#comment-id').value;

    const comment = inputField.value;
    if (comment === '') {
        alert('댓글을 입력해주세요.')
        return;
    }

    fetch(`/api/comments/${commentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ comment: comment }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                commentTextDiv.textContent = inputField.value;
                inputField.classList.add('d-none');
                commentTextDiv.classList.remove('d-none');
                editButtons.classList.add('d-none');
            } else {
                console.log(data);
                alert('오류가 발생했습니다.');
            }
        });
}

function cancelEdit(button) {
    const commentDiv = button.closest('.d-flex');
    const commentTextDiv = commentDiv.querySelector('.comment-content');
    const inputField = commentDiv.querySelector('input[type="text"]');
    const editButtons = commentDiv.querySelector('.edit-buttons');

    // 댓글 내용 복원하고, input 필드와 수정/취소 버튼 숨기기
    inputField.classList.add('d-none');
    commentTextDiv.classList.remove('d-none');
    editButtons.classList.add('d-none');
}

function deleteComment(element) {
    const commentId = element.closest('.comment-item').querySelector('#comment-id').value;

    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        fetch(`/api/comments/${commentId}`, {
            method: 'DELETE',
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    element.closest('.comment-item').remove();
                    document.querySelector('.comment_inbox_text').value = '';
                    updateCommentCount(-1);
                } else {
                    console.log(data);
                    alert('오류가 발생했습니다.');
                }
            })
            .catch(error => console.error('Error:', error));
    }
}

function updateCommentCount(increment) {
    const commentCountElement = document.querySelector('#comment-count');
    const currentCount = parseInt(commentCountElement.textContent);
    commentCountElement.textContent = currentCount + increment;
}