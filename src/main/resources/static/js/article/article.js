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

        const articleData = validateArticle();
        if (!articleData) return;

        handleRequest('PUT', '/api/articles/' + id, articleData, '수정되었습니다.', '/articles/' + id, '/articles/' + id);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton != null) {
    createButton.addEventListener('click', event => {

        const articleData = validateArticle();
        if (!articleData) return;

        handleRequest('POST', '/api/articles', articleData, '등록되었습니다.', '/articles', '/new-article');
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
    }).then(response => response.json())
        .then(data => {
            if (data.code === 200 || data.code === 201) {
                return success();
            } else {
                return fail();
            }
        }).catch(error => {
            console.error(error);
            return fail();
    })
}

function validateArticle() {
    const title = document.getElementById('title').value;
    const content = quill.root.innerHTML.trim();
    const cleanedContent = content.replace(/<p>\s*<\/p>/g, "")
                                         .replace(/<p><br><\/p>/g, "")
                                         .replace(/<p><\/p>/g, "")
                                         .trim();

    if (title === '') {
        alert('제목을 작성해주세요.');
        return null;
    }
    if (cleanedContent === '') {
        alert('내용을 작성해주세요.');
        return null;
    }

    return {
        title: title,
        content: content
    };
}

function handleRequest(method, url, body, successMessage, successRedirectUrl, failRedirectUrl) {
    const bodyString = JSON.stringify(body);

    function success() {
        alert(successMessage);
        location.replace(successRedirectUrl);
    }

    function fail() {
        alert('오류가 발생했습니다.');
        location.replace(failRedirectUrl);
    }

    httpRequest(method, url, bodyString, success, fail);
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