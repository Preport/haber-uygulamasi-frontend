package net.perport.haberuygulamasi.APIEndpoints.Models;


public class PostYorum {
    public String _id;

    public String haberID;

    public String kullaniciID;

    public String yorum;

    public String zaman;

    public boolean editlenmiş;

    public boolean silinmiş;

    public String ustYorum;

    public Yorum toYorum(String kullaniciAdi){
        Yorum y = new Yorum();
        y._id = _id;
        y.ustYorum = ustYorum;
        y.yorum = yorum;
        y.zaman = zaman;
        y.editlenmiş = editlenmiş;
        y.silinmiş = silinmiş;
        y.altYorumlar = null;
        y.kullaniciAdi = kullaniciAdi;
        y.haberID = haberID;
        return y;
    }
}
