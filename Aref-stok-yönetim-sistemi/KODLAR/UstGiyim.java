public class UstGiyim extends Urun {
    private String yakaTipi;

    public UstGiyim(int id, String barkod, String isim, int stokAdedi, double birimFiyat, String yakaTipi, int kritikStokSeviyesi, String renkKodu, String ekleyenPersonel) {
        super(id, barkod, isim, stokAdedi, birimFiyat, kritikStokSeviyesi, renkKodu, ekleyenPersonel);
        this.yakaTipi = yakaTipi;
    }

    @Override
    public String etiketYazdir() {
        return "UST GIYIM [Yaka: " + yakaTipi + "] - Stok: " + stokAdedi;
    }
}