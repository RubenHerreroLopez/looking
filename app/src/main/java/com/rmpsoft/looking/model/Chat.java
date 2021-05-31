package com.rmpsoft.looking.model;

public class Chat {

    private String idUser;
    private String idTeam;
    private String userName;
    private String teamName;
    private String userImage;
    private String teamImage;
    private String chatPath;

    public Chat() {
    }

    public Chat(String idUser, String idTeam, String userName, String teamName, String userImage, String teamImage, String chatPath) {
        this.idUser = idUser;
        this.idTeam = idTeam;
        this.userName = userName;
        this.teamName = teamName;
        this.userImage = userImage;
        this.teamImage = teamImage;
        this.chatPath = chatPath;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTeamImage() {
        return teamImage;
    }

    public void setTeamImage(String teamImage) {
        this.teamImage = teamImage;
    }

    public String getChatPath() {
        return chatPath;
    }

    public void setChatPath(String chatPath) {
        this.chatPath = chatPath;
    }

}
