public class AltGiyim extends Urun {
    private String pacaTipi;

    public AltGiyim(int id, String barkod, String isim, int stokAdedi, double birimFiyat, String pacaTipi, int kritikStokSeviyesi, String renkKodu, String ekleyenPersonel) {
        super(id, barkod, isim, stokAdedi, birimFiyat, kritikStokSeviyesi, renkKodu, ekleyenPersonel);
        this.pacaTipi = pacaTipi;
    }

    @Override
    public String etiketYazdir() {
        return "ALT GIYIM [Paca: " + pacaTipi + "] - Stok: " + stokAdedi;
    }
}