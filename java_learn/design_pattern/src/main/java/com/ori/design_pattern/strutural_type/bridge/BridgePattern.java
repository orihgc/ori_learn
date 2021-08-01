package com.ori.design_pattern.strutural_type.bridge;

import java.util.ArrayList;
import java.util.List;

public class BridgePattern {

    public static void main(String[] args) {
        ArrayList<String> telephones = new ArrayList<>();
        TelephoneMsgSender telephoneMsgSender = new TelephoneMsgSender(telephones);
        SeverNotification severNotification = new SeverNotification(telephoneMsgSender);
        severNotification.notify("server");
    }
}

interface MsgSender {
    void send(String message);
}

class TelephoneMsgSender implements MsgSender {

    private List<String> telephones;

    public TelephoneMsgSender(List<String> telephones) {
        this.telephones = telephones;
    }

    @Override
    public void send(String message) {
    }
}


abstract class Notification {
    protected MsgSender msgSender;

    public Notification(MsgSender msgSender) {
        this.msgSender = msgSender;
    }

    public abstract void notify(String message);
}

class SeverNotification extends Notification {

    public SeverNotification(MsgSender msgSender) {
        super(msgSender);
    }

    @Override
    public void notify(String message) {
        msgSender.send(message);
    }
}