import javax.swing.*;
import java.awt.*;

public class UrunDuzenlePenceresi extends JDialog {
    private JTextField txtStok, txtFiyat, txtKritikStok;
    private JComboBox<String> comboDetay, comboRenk;
    private JButton btnGuncelle;
    private Urun hedefUrun;

    // AYNI 12 RENK BURAYA DA ENTEGRE EDİLDİ (Uyum Koruması)
    private String[] renkIsimleri = {
        "Siyah", "Beyaz", "Kırmızı", "Lacivert", "Haki", "Gri", 
        "Antrasit", "Bordo", "Bej", "Hardal", "Havuç Turuncusu", "Asker Yeşili"
    };
    private String[] renkKodlari = {
        "#212121", "#FFFFFF", "#D32F2F", "#1A237E", "#2E7D32", "#757575", 
        "#37474F", "#880E4F", "#F5F5DC", "#F57F17", "#E65100", "#1B5E20"
    };

    public UrunDuzenlePenceresi(JFrame anaEkran, String barkod) {
        super(anaEkran, "Ürünü Düzenle", true);
        
        for (Urun u : Main.depo.getTumUrunler()) {
            if (u.getBarkod().equals(barkod)) { hedefUrun = u; break; }
        }

        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(anaEkran);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtStok = new JTextField(String.valueOf(hedefUrun.miktarSorgula()));
        txtFiyat = new JTextField(String.valueOf(hedefUrun.getBirimFiyat()));
        txtKritikStok = new JTextField(String.valueOf(hedefUrun.getKritikStokSeviyesi()));
        
        if (hedefUrun instanceof UstGiyim) {
            comboDetay = new JComboBox<>(new String[]{"V Yaka", "Bisiklet Yaka", "Polo Yaka", "Dik Yaka", "Hakim Yaka"});
        } else {
            comboDetay = new JComboBox<>(new String[]{"Boru Paça", "Dar Paça", "Geniş Paça", "İspanyol Paça", "Lastikli Paça"});
        }
        comboDetay.setSelectedItem(hedefUrun.etiketYazdir().split("\\[")[1].split(": ")[1].split("\\]")[0]);

        comboRenk = new JComboBox<>(renkIsimleri);
        for (int i = 0; i < renkKodlari.length; i++) {
            if (renkKodlari[i].equals(hedefUrun.getRenkKodu())) { comboRenk.setSelectedIndex(i); break; }
        }

        formPanel.add(new JLabel("Ana Kategori:")); formPanel.add(new JLabel(hedefUrun instanceof UstGiyim ? "Üst Giyim" : "Alt Giyim"));
        formPanel.add(new JLabel("Ürün Modeli:")); formPanel.add(new JLabel(hedefUrun.getIsim()));
        formPanel.add(new JLabel("Seçmeli Özellik:")); formPanel.add(comboDetay);
        formPanel.add(new JLabel("Ürün Rengi:")); formPanel.add(comboRenk);
        formPanel.add(new JLabel("Stok Adedi:")); formPanel.add(txtStok);
        formPanel.add(new JLabel("Birim Fiyat:")); formPanel.add(txtFiyat);
        formPanel.add(new JLabel("Kritik Stok Eşiği:")); formPanel.add(txtKritikStok);

        btnGuncelle = new JButton("Değişiklikleri Kaydet");
        btnGuncelle.setBackground(new Color(242, 153, 74)); btnGuncelle.setForeground(Color.WHITE);

        add(formPanel, BorderLayout.CENTER); add(btnGuncelle, BorderLayout.SOUTH);

        btnGuncelle.addActionListener(e -> {
            try {
                int yeniStok = Integer.parseInt(txtStok.getText().trim());
                double yeniFiyat = Double.parseDouble(txtFiyat.getText().trim());
                int yeniKritik = Integer.parseInt(txtKritikStok.getText().trim());
                if (yeniStok < 0 || yeniFiyat <= 0 || yeniKritik < 0) return;

                Main.depo.urunSil(hedefUrun.getBarkod());
                Urun guncelUrun;
                String yeniRenk = renkKodlari[comboRenk.getSelectedIndex()];
                
                if (hedefUrun instanceof UstGiyim) {
                    guncelUrun = new UstGiyim(hedefUrun.getId(), hedefUrun.getBarkod(), hedefUrun.getIsim(), yeniStok, yeniFiyat, comboDetay.getSelectedItem().toString(), yeniKritik, yeniRenk, hedefUrun.getEkleyenPersonel());
                } else {
                    guncelUrun = new AltGiyim(hedefUrun.getId(), hedefUrun.getBarkod(), hedefUrun.getIsim(), yeniStok, yeniFiyat, comboDetay.getSelectedItem().toString(), yeniKritik, yeniRenk, hedefUrun.getEkleyenPersonel());
                }
                Main.depo.urunEkle(guncelUrun);
                dispose(); Main.tabloyuYenile();
            } catch (Exception ex) {}
        });
    }
}