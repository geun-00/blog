let isValid = true;

document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('password');
    const repeatPasswordInput = document.getElementById('repeat__password');
    const emailInput = document.getElementById('email');
    const phoneNumberInput = document.getElementById('phoneNumber');
    const nicknameInput = document.getElementById('nickname');

    const emailError = document.getElementById('emailError');
    const phoneNumberError = document.getElementById('phoneNumberError');
    const nicknameError = document.getElementById('nicknameError');

    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const phonePattern = /^010\d{8}$/;
    const nicknamePattern = /^.{2,20}$/;

    const passwordRequirements = {
        length: document.getElementById('lengthRequirement'),
        number: document.getElementById('numberRequirement'),
        specialChar: document.getElementById('specialCharRequirement')
    };

    // 이메일 유효성 검사
    function validateEmail() {
        const email = emailInput.value.trim();
        if (emailPattern.test(email)) {
            emailError.textContent = '';
            isValid = true;
        } else {
            emailError.textContent = '유효하지 않은 이메일 주소입니다.';
            isValid = false;
        }
    }

    // 전화번호 유효성 검사
    function validatePhoneNumber() {
        const phoneNumber = phoneNumberInput.value.trim();
        if (phonePattern.test(phoneNumber)) {
            phoneNumberError.textContent = '';
            isValid = true;
        } else {
            phoneNumberError.textContent = '- 를 제외하고 전화번호 형식에 맞게 입력해주세요.';
            isValid = false;
        }
    }

    // 닉네임 유효성 검사
    function validateNickname() {
        const nickname = nicknameInput.value.trim();
        if (nicknamePattern.test(nickname)) {
            nicknameError.textContent = '';
            isValid = true;
        } else {
            nicknameError.textContent = '닉네임은 2~20자이어야 합니다.';
            isValid = false;
        }
    }

    // 비밀번호 유효성 검사
    function validatePassword() {
        const password = passwordInput.value.trim();
        const repeatPassword = repeatPasswordInput.value.trim();

        // 비밀번호 길이 검사
        if (password.length >= 8 && password.length <= 16) {
            passwordRequirements.length.classList.add('valid');
            passwordRequirements.length.classList.remove('invalid');
            isValid = true;
        } else {
            passwordRequirements.length.classList.add('invalid');
            passwordRequirements.length.classList.remove('valid');
            isValid = false;
        }

        // 숫자 포함 여부 검사
        if (/\d/.test(password)) {
            passwordRequirements.number.classList.add('valid');
            passwordRequirements.number.classList.remove('invalid');
            isValid = true;
        } else {
            passwordRequirements.number.classList.add('invalid');
            passwordRequirements.number.classList.remove('valid');
            isValid = false;
        }

        // 특수문자 포함 여부 검사
        if (/[@#$%^&*]/.test(password)) {
            passwordRequirements.specialChar.classList.add('valid');
            passwordRequirements.specialChar.classList.remove('invalid');
            isValid = true;
        } else {
            passwordRequirements.specialChar.classList.add('invalid');
            passwordRequirements.specialChar.classList.remove('valid');
            isValid = false;
        }

        if (repeatPasswordInput) {
            if (repeatPassword === password) {
                document.getElementById('passwordError').textContent = '';
            } else {
                document.getElementById('passwordError').textContent = '비밀번호가 일치하지 않습니다.';
                isValid = false;
            }
        }
    }

    function handleURLParameters() {
        const urlParams = new URLSearchParams(window.location.search);
        const error = urlParams.get('error');
        const message = urlParams.get('exception');

        if (error === 'true' && message) {
            document.getElementById('loginError').textContent = decodeURIComponent(message);
        }
    }

    passwordInput.addEventListener('input', validatePassword);
    repeatPasswordInput.addEventListener('input', validatePassword);
    emailInput.addEventListener('input', validateEmail);
    phoneNumberInput.addEventListener('input', validatePhoneNumber);
    nicknameInput.addEventListener('input', validateNickname);

    handleURLParameters();
});
