const loginForm = document.querySelector(".card.login-form#login-form");
const loginID = document.querySelector("#reg-id");
const loginPW = document.querySelector("#reg-pass");

function login(event){
    event.preventDefault();
    console.log(loginID.value);
    console.log(loginPW.value);
}

console.log(loginForm);
loginForm.addEventListener('click',login);