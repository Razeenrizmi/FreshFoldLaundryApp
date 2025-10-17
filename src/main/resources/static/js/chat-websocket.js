var stompClient = null;
function connectWebSocket(userId, onMessage) {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/public', function (messageOutput) {
            var msg = JSON.parse(messageOutput.body);
            onMessage(msg);
        });
    });
}
function sendWebSocketMessage(senderId, receiverId, message) {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({ senderId, receiverId, message, timestamp: new Date() }));
}

