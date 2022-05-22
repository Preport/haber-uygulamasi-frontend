package net.perport.haberuygulamasi.APIEndpoints.DTOs;

public class createUserDTO {
    String kullaniciAdi;
    String eposta;
    String sifre;

    public createUserDTO(String kullaniciAdi, String eposta, String sifre){
        this.kullaniciAdi = kullaniciAdi;
        this.eposta = eposta;
        this.sifre = sifre;
    }
}
