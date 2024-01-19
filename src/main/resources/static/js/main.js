'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var regButton = document.querySelector('#regButton');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var registrationPage = document.querySelector('#registration-page');
var registrationForm = document.querySelector('#registrationForm');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var password = null;
var email = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#login_auth').value.trim();
    password = document.querySelector('#password_auth').value.trim();

    if(username && password) {

        var dataToSend = {
            login: username,
            password: password
        };

        var requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataToSend)
        };

        fetch('/authUser', requestOptions)
            .then(async response => {
                if (!response.ok) {
                    throw new Error(await response.text());
                }
                return response.text();
            })
            .then(data => {
                console.log('Ответ от сервера:', data);
                usernamePage.classList.add('hidden');
                registrationPage.classList.add('hidden');
                chatPage.classList.remove('hidden');
            })
            .catch(error => {
                alert(error);
                console.error(error);
            });
    }
    event.preventDefault();
}

function showRegForm(event) {
    usernamePage.classList.add('hidden');
    chatPage.classList.add('hidden');
    registrationPage.classList.remove('hidden');
    event.preventDefault();
}

function backReg() {
    usernamePage.classList.remove('hidden');
    chatPage.classList.add('hidden');
    registrationPage.classList.add('hidden');
}

function registration(event) {
    username = document.querySelector('#login_reg').value.trim();
    password = document.querySelector('#password_reg').value.trim();
    email = document.querySelector('#email_reg').value.trim();

    if(username && password && email) {
        var dataToSend = {
            login: username,
            password: password,
            email: email
        };

        var requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataToSend)
        };

        fetch('/registerUser', requestOptions)
            .then(async response => {
                if (!response.ok) {
                    throw new Error(await response.text());
                }
                return response.text();
            })
            .then(data => {
                console.log('Ответ от сервера:', data);
                usernamePage.classList.remove('hidden');
                chatPage.classList.add('hidden');
                registrationPage.classList.add('hidden');
            })
            .catch(error => {
                alert(error);
                console.error(error);
            });
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.authUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' присоединился!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' покинул нас!';
    } else {
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender + ':');
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

regButton.addEventListener('click', showRegForm);
usernameForm.addEventListener('submit', connect, true);
registrationForm.addEventListener('submit', registration, true);
messageForm.addEventListener('submit', sendMessage, true);
