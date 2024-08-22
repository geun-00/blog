window.addEventListener('load', () => {
    const token = getCookie('access_token')

    if (token) {
        localStorage.setItem("access_token", token);

        fetch('/token', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(r => r.json())
    }
});