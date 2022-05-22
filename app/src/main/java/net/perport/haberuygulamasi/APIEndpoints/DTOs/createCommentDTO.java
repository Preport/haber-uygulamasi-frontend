package net.perport.haberuygulamasi.APIEndpoints.DTOs;

public class createCommentDTO {
    public String haberID;

    public String yorum;

    public String ustYorum;

    public createCommentDTO(String haberID, String yorum, String ustYorum){
        this(haberID, yorum);
        this.ustYorum = ustYorum;
    }

    public createCommentDTO(String haberID, String yorum){
        this.haberID = haberID;
        this.yorum = yorum;
    }
}
