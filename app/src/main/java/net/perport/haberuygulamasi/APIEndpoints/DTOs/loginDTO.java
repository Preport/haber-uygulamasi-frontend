package net.perport.haberuygulamasi.APIEndpoints.DTOs;

public class loginDTO {
    public final String kullaniciAdi;
    public final String sifre;
    public loginDTO(String kullaniciAdi, String sifre){
        this.kullaniciAdi=kullaniciAdi;
        this.sifre=sifre;
    }
}
