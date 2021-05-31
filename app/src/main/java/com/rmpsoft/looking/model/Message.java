package com.rmpsoft.looking.model;

public class Message {

    private String idMessage;
    private String message;
    private String idUser;
    private String idTeam;
    private String chatPath;

    public Message() {
    }


    public Message(String idMessage, String idUser, String idTeam, String message, String chatPath) {
        this.idMessage = idMessage;
        this.idUser = idUser;
        this.idTeam = idTeam;
        this.message = message;
        this.chatPath = chatPath;
    }

    public String getChatPath() {
        return chatPath;
    }

    public void setChatPath(String chatPath) {
        this.chatPath = chatPath;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
}
