function initScreenToggle() {
    let isSignup = false;

    const links = $(".links").find("li");
    let signup = links.find("#signup");
    let signin = links.find("#signin");
    let reset = links.find("#reset");

    const form = $("#authForm");
    let first_input = form.find(".first-input");
    let hidden_input = form.find(".input__block").find("#repeat__password");
    let signin_btn = form.find(".signin__btn");
    let findBtn = $(".find_btn");

    //----------- 회원가입 화면 전환 ---------------------
    signup.on("click", function (e) {
        e.preventDefault();
        isSignup = true;

        $(this).parent().parent().siblings("h1").text("회원가입");

        $(this).parent().css("opacity", "1");
        $(this).parent().siblings().css("opacity", ".6");

        first_input.removeClass("first-input__block").addClass("signup-input__block");
        hidden_input.css({
            "opacity": "1",
            "display": "block"
        });

        signin_btn.text("회원가입");

        form.find("#phoneNumber, #nickname, #passwordRequirements, #passwordError").show();
        form.find("#loginError").hide();

        form.attr("action", "javascript:void(0);");

        findBtn.hide();
    });

    //----------- 로그인 화면 전환 ---------------------
    signin.on("click", function (e) {
        e.preventDefault();
        isSignup = false;

        $(this).parent().parent().siblings("h1").text("로그인");

        $(this).parent().css("opacity", "1");
        $(this).parent().siblings().css("opacity", ".6");

        first_input.addClass("first-input__block").removeClass("signup-input__block");
        hidden_input.css({
            "opacity": "0",
            "display": "none"
        });

        signin_btn.text("로그인");

        form.find("#phoneNumber, #nickname, #passwordRequirements, #passwordError").hide();

        form.attr("action", "/loginProc");

        findBtn.show();
    });

    findBtn.show();
    form.find("#phoneNumber, #emailError, #nickname, #passwordRequirements, #passwordError").hide();

    //----------- 초기화 버튼 동작 ---------------------
    reset.on("click", function (e) {
        e.preventDefault();

        $(this).parent().parent().siblings("form")
            .find(".input__block").find(".input").val("");
    });

    //----------- form submit ---------------------
    form.on("submit", function (e) {
        if (isSignup) {
            e.preventDefault();

            const email = $("#email").val().trim();
            const password = $("#password").val().trim();
            const repeatPassword = $("#repeat__password").val().trim();
            const phoneNumber = $("#phoneNumber").val().trim();
            const nickname = $("#nickname").val().trim();

            if (!email) {
                alert("이메일을 입력해 주세요.");
                return;
            }
            if (!password) {
                alert("비밀번호를 입력해 주세요.");
                return;
            }
            if (!repeatPassword) {
                alert("비밀번호 확인을 입력해 주세요.");
                return;
            }
            if (!phoneNumber) {
                alert("전화번호를 입력해 주세요.");
                return;
            }
            if (!nickname) {
                alert("닉네임을 입력해 주세요.");
                return;
            }

            if (!isValid) {
                alert("정보를 정확하게 입력해 주세요");
                return;
            }

            const signupData = {
                email: email,
                password: password,
                phoneNumber: phoneNumber,
                nickname: nickname
            };

            fetch('/api/formUser', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(signupData)
            }).then(response => response.json())
                .then(data => {
                    if (data.code === 201) {
                        alert(data.data + ' 님 가입을 환영합니다! 서비스 사용을 위해 로그인을 해주세요');
                        window.location.href = '/login';
                    } else {
                        alert('정보를 정확하게 입력해주세요');
                    }
                }).catch(error => {
                console.log(error);
                alert('오류가 발생했습니다.')
            })
        }
    });
}

$(document).ready(function () {
    initScreenToggle();
});