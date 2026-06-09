public abstract class Urun implements IStoklanabilir {
    private int id;
    private String barkod;
    private String isim;
    protected int stokAdedi;
    private double birimFiyat;
    private int kritikStokSeviyesi;
    private String renkKodu;
    private String ekleyenPersonel; // YENİ: Denetim için ekleyen personel bilgisi

    private int stokS, stokM, stokL, stokXL;

    public Urun(int id, String barkod, String isim, int stokAdedi, double birimFiyat, int kritikStokSeviyesi, String renkKodu, String ekleyenPersonel) {
        this.id = id;
        this.barkod = barkod;
        this.isim = isim;
        this.stokAdedi = stokAdedi;
        this.birimFiyat = birimFiyat;
        this.kritikStokSeviyesi = kritikStokSeviyesi;
        this.renkKodu = renkKodu;
        this.ekleyenPersonel = ekleyenPersonel; // YENİ
        
        bedenleriHesaplaVeDagit(stokAdedi);
    }

    private void bedenleriHesaplaVeDagit(int toplamStok) {
        int taban = toplamStok / 4;
        int kalan = toplamStok % 4;
        this.stokS = taban; this.stokM = taban; this.stokL = taban; this.stokXL = taban;
        if (kalan == 1) this.stokM++;
        else if (kalan == 2) { this.stokM++; this.stokL++; }
        else if (kalan == 3) { this.stokM++; this.stokL++; this.stokS++; }
    }

    public int getId() { return id; }
    public String getBarkod() { return barkod; }
    public String getIsim() { return isim; }
    public double getBirimFiyat() { return birimFiyat; }
    public int getKritikStokSeviyesi() { return kritikStokSeviyesi; }
    public void setKritikStokSeviyesi(int seviye) { this.kritikStokSeviyesi = seviye; }
    public String getRenkKodu() { return renkKodu; }
    public void setRenkKodu(String renkKodu) { this.renkKodu = renkKodu; }
    public String getEkleyenPersonel() { return ekleyenPersonel; } // YENİ
    public void setEkleyenPersonel(String personel) { this.ekleyenPersonel = personel; } // YENİ

    public int getStokS() { return stokS; }
    public int getStokM() { return stokM; }
    public int getStokL() { return stokL; }
    public int getStokXL() { return stokXL; }

    public void stokEkle(int miktar) { this.stokAdedi += miktar; bedenleriHesaplaVeDagit(this.stokAdedi); }
    public void stokCikar(int miktar) { this.stokAdedi -= miktar; bedenleriHesaplaVeDagit(this.stokAdedi); }
    public int miktarSorgula() { return this.stokAdedi; }

    public abstract String etiketYazdir();
}