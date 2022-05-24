package net.perport.haberuygulamasi.APIEndpoints.DTOs;

public class updateUserDTO {
    String kullaniciAdi;
    String sifre;
    int kategoriSecimleri;

    public updateUserDTO(String kullaniciAdi, String sifre, int kategoriSecimleri){
        this.kullaniciAdi = ifNotEmpty(kullaniciAdi);
        this.sifre = ifNotEmpty(sifre);
        this.kategoriSecimleri = kategoriSecimleri;
    }

    private String ifNotEmpty(String data){
        return data.equals("") ? null : data;
    }
}
