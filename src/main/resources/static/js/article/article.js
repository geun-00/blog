// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton != null) {
    deleteButton.addEventListener('click', event => {

        if (confirm('정말 삭제하시겠습니까?')) {
            let id = document.getElementById('article-id').value;
            function success() {
                alert('삭제가 완료되었습니다.');
                location.replace('/articles');
            }

            function fail() {
                alert('삭제 실패했습니다.');
                location.replace('/articles');
            }

            httpRequest('DELETE','/api/articles/' + id, null, success, fail);
        }
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton != null) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        const content = quill.root.innerHTML;

        const body = JSON.stringify({
            title: document.getElementById('title').value,
            content: content
        })

        function success() {
            alert('수정 완료되었습니다.');
            location.replace('/articles/' + id);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace('/articles/' + id);
        }

        httpRequest('PUT','/api/articles/' + id, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton != null) {
    createButton.addEventListener('click', event => {

        const content = quill.root.innerHTML.trim();
        const title = document.getElementById('title').value;

        const cleanedContent = content.replace(/<p>\s*<\/p>/g, "")
                                             .replace(/<p><br><\/p>/g, "")
                                             .replace(/<p><\/p>/g, "")
                                             .trim();
        if (title === '') {
            alert('제목을 작성해주세요.');
            return;
        }
        if (cleanedContent === '') {
            alert('내용을 작성해주세요.');
            return;
        }

        fetch('/api/articles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        }).then(response => response.json())
            .then(data => {
                if (data.code === 201) {
                    alert('등록되었습니다.');
                    location.replace('/articles');
                } else {
                    alert('오류가 발생했습니다.');
                    location.replace('/new-article');
                }
            })
    });
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            return fail();
        }
    }).catch(error => fail());
}

// 쿠키를 가져오는 함수
function getCookie(key) {

    var result = null;

    var cookie = document.cookie.split(';');
    console.log(cookie)

    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}