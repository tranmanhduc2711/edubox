package com.example.edubox.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.edubox.constant.EventType;
import com.example.edubox.security.jwt.AuthService;
import com.example.edubox.socket.message.EventMessage;
import com.example.edubox.socket.message.receive.ReceiveAnswerPayload;
import com.example.edubox.socket.message.send.PresentCodePayload;
import com.example.edubox.socket.message.send.SendAnswerPayload;
import com.example.edubox.socket.message.send.SendResultPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {
    @Autowired
    private SocketIOServer server;

    @Autowired
    private AuthService authService;

    @Autowired
    private SocketService socketService;

    public SocketModule(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", EventMessage.class, onChatReceived());
        // start game
        server.addEventListener(EventType.START_GAME.getValue(), PresentCodePayload.class, onStartGame());
        // join game
        server.addConnectListener(onClientJoinRoom());
        server.addEventListener(EventType.JOIN_GAME.getValue(), PresentCodePayload.class, onJoinGame());
        // send answer
        server.addEventListener(EventType.SEND_ANSWER.getValue(), SendAnswerPayload.class, onAnswerSend());

        // send result
        server.addEventListener(EventType.SEND_RESULT.getValue(), SendResultPayload.class, onSendResult());

        // next slide
        server.addEventListener(EventType.NEXT_SLIDE.getValue(), PresentCodePayload.class, onNextSlide());

        // countdown
        server.addEventListener(EventType.COUNTDOWN.getValue(), PresentCodePayload.class, onCountDown());

        // leave game
        server.addEventListener(EventType.LEAVE_GAME.getValue(), PresentCodePayload.class, onLeaveGame());

        // end game
        server.addEventListener(EventType.END_GAME.getValue(), String.class, onEndGame());

    }


    private ConnectListener onConnected() {
        return (client) -> {
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

    private DataListener<EventMessage> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data.getPayload());

        };
    }

    /*
     *   Join game
     * */
    private ConnectListener onClientJoinRoom() {
        return (client) -> {
            String presentation = client.getHandshakeData().getSingleUrlParam("presentCode");
            client.joinRoom(presentation);
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DataListener<PresentCodePayload> onJoinGame() {
        return (senderClient, data, ackSender) -> {
            log.info(data.getPresentCode());
            socketService.sendMessage(data.getPresentCode(), EventType.JOIN_GAME.getValue(), senderClient, data);
        };
    }

    /*
     *   Start game
     * */
    private DataListener<PresentCodePayload> onStartGame() {
        return (senderClient, data, ackSender) -> {
            log.info(data.getPresentCode());
            socketService.sendMessage(data.getPresentCode(), EventType.START_GAME.getValue(), senderClient, data);
        };
    }

    /*
     *   Send answer
     * */
    private DataListener<SendAnswerPayload> onAnswerSend() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            String user = authService.getFullNameByToken(data.getToken());
            ReceiveAnswerPayload sendMessage = new ReceiveAnswerPayload();
            sendMessage.setUsername(user);
            sendMessage.setPresentCode(data.getPresentCode());
            sendMessage.setChoices(data.getChoices());
            sendMessage.setIsCorrect(data.getIsCorrect());

            socketService.sendMessage(data.getPresentCode(), EventType.RECEIVE_ANSWER.getValue(), senderClient, sendMessage);
        };
    }

    /*
     *   Send result
     * */
    private DataListener<SendResultPayload> onSendResult() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessage(data.getPresentCode(), EventType.RECEIVE_RESULT.getValue(), senderClient, data);
        };
    }

    /*
     *   Next Slide
     * */
    private DataListener<PresentCodePayload> onNextSlide() {
        return (senderClient, data, ackSender) -> {
            log.info(data.getPresentCode());
            socketService.sendMessage(data.getPresentCode(), EventType.NEXT_SLIDE.getValue(), senderClient, data);
        };
    }

    /*
     *   Countdown
     * */
    private DataListener<PresentCodePayload> onCountDown() {
        return (senderClient, data, ackSender) -> {
            log.info(data.getPresentCode());
            socketService.sendMessage(data.getPresentCode(), EventType.COUNTDOWN.getValue(), senderClient, data);
        };
    }

    /*
     *   Leave Game
     * */
    private DataListener<PresentCodePayload> onLeaveGame() {
        return (senderClient, data, ackSender) -> {
            log.info(data.getPresentCode());
            socketService.sendMessage(data.getPresentCode(), EventType.LEAVE_GAME.getValue(), senderClient, data);
        };
    }

    /*
     *   End Game
     * */
    private DataListener<String> onEndGame() {
        return (senderClient, data, ackSender) -> {
            log.info(data);
            socketService.sendMessage(data, EventType.END_GAME.getValue(), senderClient, data);
        };
    }
}
