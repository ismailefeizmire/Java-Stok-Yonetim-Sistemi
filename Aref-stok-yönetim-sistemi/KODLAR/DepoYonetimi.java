import java.util.HashMap;
import java.util.Collection;
import java.io.*;

public class DepoYonetimi {
    private HashMap<String, Urun> urunHaritasi = new HashMap<>();
    private final String dosyaAdi = "aref_stok_audit.txt";

    public boolean urunEkle(Urun yeniUrun) {
        if (urunHaritasi.containsKey(yeniUrun.getBarkod())) return false;
        urunHaritasi.put(yeniUrun.getBarkod(), yeniUrun);
        return true;
    }

    public Collection<Urun> getTumUrunler() { return urunHaritasi.values(); }
    public boolean urunSil(String barkod) {
        if (urunHaritasi.containsKey(barkod)) { urunHaritasi.remove(barkod); return true; }
        return false;
    }

    public void verileriKaydet() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dosyaAdi))) {
            for (Urun u : urunHaritasi.values()) {
                String tip = (u instanceof UstGiyim) ? "UST" : "ALT";
                String ekBilgi = (u instanceof UstGiyim) ? ((UstGiyim) u).etiketYazdir().split(": ")[1].replace("]", "") 
                                                        : ((AltGiyim) u).etiketYazdir().split(": ")[1].replace("]", "");
                // En sona u.getEkleyenPersonel() alanı eklendi
                writer.println(tip + ";" + u.getId() + ";" + u.getBarkod() + ";" + u.getIsim() + ";" + u.miktarSorgula() + ";" + u.getBirimFiyat() + ";" + ekBilgi.split(" -")[0] + ";" + u.getKritikStokSeviyesi() + ";" + u.getRenkKodu() + ";" + u.getEkleyenPersonel());
            }
        } catch (IOException e) {}
    }

    public void verileriYukle() {
        File f = new File(dosyaAdi);
        if (!f.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] p = s.split(";");
                Urun u;
                int kStok = Integer.parseInt(p[7]);
                String renk = p[8];
                String ekleyen = p[9]; // Dosyadan ekleyen kişiyi oku
                if (p[0].equals("UST")) {
                    u = new UstGiyim(Integer.parseInt(p[1]), p[2], p[3], Integer.parseInt(p[4]), Double.parseDouble(p[5]), p[6], kStok, renk, ekleyen);
                } else {
                    u = new AltGiyim(Integer.parseInt(p[1]), p[2], p[3], Integer.parseInt(p[4]), Double.parseDouble(p[5]), p[6], kStok, renk, ekleyen);
                }
                urunHaritasi.put(u.getBarkod(), u);
            }
        } catch (Exception e) {}
    }

    public int urunSayisi() { return urunHaritasi.size(); }
}