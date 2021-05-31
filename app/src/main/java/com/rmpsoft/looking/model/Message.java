package com.rmpsoft.looking.model;

public class Message {

    private String idMessage;
    private String message;
    private String idSender;
    private String idReceiver;
    private String chatPath;
    private String date;
    private String time;

    public Message() {
    }


    public Message(String idMessage, String idSender, String idReceiver, String message, String chatPath, String date, String time) {
        this.idMessage = idMessage;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.chatPath = chatPath;
        this.date = date;
        this.time = time;
    }

    public String getChatPath() {
        return chatPath;
    }

    public void setChatPath(String chatPath) {
        this.chatPath = chatPath;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
