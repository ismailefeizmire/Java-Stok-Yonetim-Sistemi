import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class UrunEklePenceresi extends JDialog {
    private JTextField txtStok, txtFiyat, txtKritikStok, txtEkleyen;
    private JComboBox<String> comboAnaKategori, comboUrunModeli, comboDetay, comboRenk;
    private JButton btnKaydet;

    private String[] ustGiyimModelleri = {"Tişört", "Sweatshirt", "Gömlek", "Kazak", "Hırka", "Mont/Ceket"};
    private String[] altGiyimModelleri = {"Pantolon", "Kot Pantolon", "Eşofman Altı", "Şort", "Kargo Pantolon", "Tayt"};
    private String[] yakaTipleri = {"V Yaka", "Bisiklet Yaka", "Polo Yaka", "Dik Yaka", "Hakim Yaka"};
    private String[] pacaTipleri = {"Boru Paça", "Dar Paça", "Geniş Paça", "İspanyol Paça", "Lastikli Paça"};
    
    // YENİLENEN GENİŞ KURUMSAL RENK PALETİ (12 RENK)
    private String[] renkIsimleri = {
        "Siyah", "Beyaz", "Kırmızı", "Lacivert", "Haki", "Gri", 
        "Antrasit", "Bordo", "Bej", "Hardal", "Havuç Turuncusu", "Asker Yeşili"
    };
    private String[] renkKodlari = {
        "#212121", "#FFFFFF", "#D32F2F", "#1A237E", "#2E7D32", "#757575", 
        "#37474F", "#880E4F", "#F5F5DC", "#F57F17", "#E65100", "#1B5E20"
    };

    public UrunEklePenceresi(JFrame anaEkran, String aktifPersonelIsmi) {
        super(anaEkran, "Yeni Ürün Ekle", true); 
        setSize(460, 440);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(anaEkran); 
        
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtStok = new JTextField();
        txtFiyat = new JTextField();
        txtKritikStok = new JTextField("5");
        
        txtEkleyen = new JTextField(aktifPersonelIsmi);
        txtEkleyen.setEnabled(false); 
        
        comboAnaKategori = new JComboBox<>(new String[]{"Üst Giyim", "Alt Giyim"});
        comboUrunModeli = new JComboBox<>(ustGiyimModelleri);
        comboDetay = new JComboBox<>(yakaTipleri);
        comboRenk = new JComboBox<>(renkIsimleri); // 12 Renk otomatik yüklendi

        comboAnaKategori.addActionListener(e -> {
            comboUrunModeli.removeAllItems(); comboDetay.removeAllItems();
            if (comboAnaKategori.getSelectedIndex() == 0) {
                for (String model : ustGiyimModelleri) { comboUrunModeli.addItem(model); }
                for (String yaka : yakaTipleri) { comboDetay.addItem(yaka); }
            } else {
                for (String model : altGiyimModelleri) { comboUrunModeli.addItem(model); }
                for (String paca : pacaTipleri) { comboDetay.addItem(paca); }
            }
        });

        formPanel.add(new JLabel("Ana Kategori:")); formPanel.add(comboAnaKategori);
        formPanel.add(new JLabel("Ürün Modeli:")); formPanel.add(comboUrunModeli);
        formPanel.add(new JLabel("Seçmeli Özellik:")); formPanel.add(comboDetay);
        formPanel.add(new JLabel("Ürün Rengi:")); formPanel.add(comboRenk);
        formPanel.add(new JLabel("Stok Adedi:")); formPanel.add(txtStok);
        formPanel.add(new JLabel("Birim Fiyat:")); formPanel.add(txtFiyat);
        formPanel.add(new JLabel("Kritik Stok Eşiği:")); formPanel.add(txtKritikStok);
        formPanel.add(new JLabel("İşlemi Yapan Personel:")); formPanel.add(txtEkleyen);

        btnKaydet = new JButton("Sisteme Kaydet");
        btnKaydet.setBackground(new Color(28, 124, 212)); btnKaydet.setForeground(Color.WHITE);
        btnKaydet.setPreferredSize(new Dimension(0, 40));
        
        add(formPanel, BorderLayout.CENTER); add(btnKaydet, BorderLayout.SOUTH);

        btnKaydet.addActionListener(e -> {
            try {
                int stok = Integer.parseInt(txtStok.getText().trim());
                double fiyat = Double.parseDouble(txtFiyat.getText().trim());
                int kritikStok = Integer.parseInt(txtKritikStok.getText().trim());
                
                if (stok < 0 || fiyat <= 0 || kritikStok < 0) return;

                Random rand = new Random();
                String barkod = "AREF-" + (100000 + rand.nextInt(900000));
                String secilenRenkKodu = renkKodlari[comboRenk.getSelectedIndex()];

                Urun yeniUrun;
                if (comboAnaKategori.getSelectedIndex() == 0) {
                    yeniUrun = new UstGiyim(Main.depo.urunSayisi() + 1, barkod, comboUrunModeli.getSelectedItem().toString(), stok, fiyat, comboDetay.getSelectedItem().toString(), kritikStok, secilenRenkKodu, aktifPersonelIsmi);
                } else {
                    yeniUrun = new AltGiyim(Main.depo.urunSayisi() + 1, barkod, comboUrunModeli.getSelectedItem().toString(), stok, fiyat, comboDetay.getSelectedItem().toString(), kritikStok, secilenRenkKodu, aktifPersonelIsmi);
                }

                if (Main.depo.urunEkle(yeniUrun)) { dispose(); Main.tabloyuYenile(); }
            } catch (Exception ex) {}
        });
    }
}