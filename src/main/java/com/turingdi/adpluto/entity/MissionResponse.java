package com.turingdi.adpluto.entity;

/*
 * Created by leibniz on 16-11-29.
 */
public class MissionResponse {
    private String cheaterID;
    private String message;

    public MissionResponse(String cheaterID) {
        this.cheaterID = cheaterID;
    }

    public MissionResponse(String cheaterID, String message) {

        this.cheaterID = cheaterID;
        this.message = message;
    }

    @Override
    public String toString() {
        return "MissionResponse{" +
                "cheaterID='" + cheaterID + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCheaterID() {
        return cheaterID;
    }

    public void setCheaterID(String cheaterID) {
        this.cheaterID = cheaterID;
    }

    public String getJsonString() {
        return null;
    }

    public byte[] getJsonBytes() {
        return getJsonString().getBytes();
    }
}
