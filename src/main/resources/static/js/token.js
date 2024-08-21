const token = getCookie('access_token')

if (token) {
    localStorage.setItem("access_token", token);
}