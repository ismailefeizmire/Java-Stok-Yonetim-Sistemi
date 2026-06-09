import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    public static DepoYonetimi depo = new DepoYonetimi();
    private static DefaultTableModel tableModel;
    private static JTable tablo;
    private static String secilenBarkod = "";
    private static JTextField txtAramaCubugu;

    private static JLabel lblPersonelBilgi, lblToplamCesit, lblToplamStok, lblKritikDurum;
    private static JPanel kartCesit, kartStok, kartKritik; 
    private static JTabbedPane sekmePaneli;
    private static JPanel raporSekmesi;
    
    private static int grafikUstGiyimAdet = 0;
    private static int grafikAltGiyimAdet = 0;
    private static String anlikGirisYapanIsim = ""; 

    public Main() {
        // --- RENKLİ PREMIUM PALET ---
        Color koyuMavi = new Color(15, 23, 42);       
        Color SoftArkaPlan = new Color(241, 245, 249); 
        Color royalMavi = new Color(37, 99, 235);    
        Color emeraldYesil = new Color(16, 185, 129);  
        Color mercanKirmizi = new Color(239, 68, 68); 
        Color duzenleTuruncu = new Color(245, 158, 11); 
        
        Font anaFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font baslikFont = new Font("Segoe UI", Font.BOLD, 14);

        UIManager.put("Label.font", anaFont);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Table.font", anaFont);
        UIManager.put("TableHeader.font", baslikFont);

        setTitle("AREF Premium Envanter Yönetim Paneli v16.5");
        setSize(1250, 720); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(SoftArkaPlan);
        setLayout(new BorderLayout(15, 15));

        // --- MENÜ ÇUBUĞU ---
        JMenuBar menuBar = new JMenuBar(); menuBar.setBackground(koyuMavi); menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JMenu menuDosya = new JMenu("Dosya"); menuDosya.setForeground(Color.WHITE);
        JMenu menuRaporlar = new JMenu("Raporlar"); menuRaporlar.setForeground(Color.WHITE);
        JMenu menuHakkinda = new JMenu("Hakkında"); menuHakkinda.setForeground(Color.WHITE);
        JMenuItem itemYenile = new JMenuItem("Yenile"); JMenuItem itemKapat = new JMenuItem("Kapat");
        JMenuItem itemExcel = new JMenuItem("Excel'e Aktar (Simüle)"); JMenuItem itemAref = new JMenuItem("Proje Hakkında");
        menuDosya.add(itemYenile); menuDosya.add(itemKapat); menuRaporlar.add(itemExcel); menuHakkinda.add(itemAref);
        menuBar.add(menuDosya); menuBar.add(menuRaporlar); menuBar.add(menuHakkinda); setJMenuBar(menuBar);

        // --- ÜST ALAN: PERSONEL BİLGİ BANDI ---
        JPanel ustBant = new JPanel(new BorderLayout());
        ustBant.setBackground(koyuMavi);
        ustBant.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        lblPersonelBilgi = new JLabel("Aktif Personel: Yükleniyor...");
        lblPersonelBilgi.setForeground(new Color(56, 189, 248)); // Açık Gökyüzü Mavisi
        lblPersonelBilgi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ustBant.add(lblPersonelBilgi, BorderLayout.WEST);
        add(ustBant, BorderLayout.NORTH);

        // --- RENKLENDİRİLMİŞ İSTATİSTİK KARTLARI (DASHBOARD) ---
        JPanel dashboardPaneli = new JPanel(new GridLayout(1, 3, 20, 0));
        dashboardPaneli.setBackground(SoftArkaPlan);
        dashboardPaneli.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Border kartKenarlik = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        // 1. Çeşit Kartı (Neon/Yumuşak Mavi Panel)
        kartCesit = new JPanel(new GridLayout(2, 1)); kartCesit.setBackground(new Color(239, 246, 255)); kartCesit.setBorder(kartKenarlik);
        lblToplamCesit = new JLabel("0"); lblToplamCesit.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblToplamCesit.setForeground(royalMavi);
        JLabel txt1 = new JLabel("Toplam Ürün Çeşidi"); txt1.setForeground(new Color(71, 85, 105));
        kartCesit.add(lblToplamCesit); kartCesit.add(txt1);

        // 2. Stok Kartı (Yumuşak Yeşil Panel)
        kartStok = new JPanel(new GridLayout(2, 1)); kartStok.setBackground(new Color(240, 253, 250)); kartStok.setBorder(kartKenarlik);
        lblToplamStok = new JLabel("0 Adet"); lblToplamStok.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblToplamStok.setForeground(emeraldYesil);
        JLabel txt2 = new JLabel("Depodaki Toplam Envanter"); txt2.setForeground(new Color(71, 85, 105));
        kartStok.add(lblToplamStok); kartStok.add(txt2);

        // 3. Kritik Kartı (Yumuşak Kırmızı Panel)
        kartKritik = new JPanel(new GridLayout(2, 1)); kartKritik.setBackground(Color.WHITE); kartKritik.setBorder(kartKenarlik);
        lblKritikDurum = new JLabel("0 Ürün"); lblKritikDurum.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblKritikDurum.setForeground(koyuMavi);
        JLabel txt3 = new JLabel("Kritik Eşik Uyarıları"); txt3.setForeground(new Color(71, 85, 105));
        kartKritik.add(lblKritikDurum); kartKritik.add(txt3);

        dashboardPaneli.add(kartCesit); dashboardPaneli.add(kartStok); dashboardPaneli.add(kartKritik);

        JPanel merkezPanel = new JPanel(new BorderLayout(0, 15));
        merkezPanel.setBackground(SoftArkaPlan);
        merkezPanel.add(dashboardPaneli, BorderLayout.NORTH);

        sekmePaneli = new JTabbedPane(); sekmePaneli.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ================= SEKME 1: TABLO ALANI =================
        JPanel stokListesiSekmesi = new JPanel(new BorderLayout(0, 15)); stokListesiSekmesi.setBackground(SoftArkaPlan);
        stokListesiSekmesi.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel aramaPanel = new JPanel(new BorderLayout(15, 0));
        aramaPanel.setBackground(Color.WHITE);
        aramaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtAramaCubugu = new JTextField();
        txtAramaCubugu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAramaCubugu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JLabel lblAraSimge = new JLabel("🔍 Hızlı Ürün Süzgeci: ");
        lblAraSimge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        aramaPanel.add(lblAraSimge, BorderLayout.WEST);
        aramaPanel.add(txtAramaCubugu, BorderLayout.CENTER);
        stokListesiSekmesi.add(aramaPanel, BorderLayout.NORTH);

        txtAramaCubugu.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { tabloyuYenile(); }
            @Override public void removeUpdate(DocumentEvent e) { tabloyuYenile(); }
            @Override public void changedUpdate(DocumentEvent e) { tabloyuYenile(); }
        });

        String[] sutunlar = {"Barkod / ID", "Ürün Adı", "Kategori", "Stok Detayı (S, M, L, XL)", "Fiyat", "Renk", "Ekleyen Personel"};
        tableModel = new DefaultTableModel(sutunlar, 0);
        tablo = new JTable(tableModel);
        tablo.setRowHeight(38); 
        tablo.setShowGrid(true); tablo.setGridColor(new Color(241, 245, 249));
        tablo.setSelectionBackground(new Color(219, 234, 254)); 
        tablo.setSelectionForeground(Color.BLACK);
        tablo.setDefaultRenderer(Object.class, new OzelRenklendirici());

        JTableHeader header = tablo.getTableHeader(); 
        header.setBackground(new Color(241, 245, 249)); header.setForeground(koyuMavi); header.setPreferredSize(new Dimension(header.getWidth(), 42)); 
        JScrollPane scrollPane = new JScrollPane(tablo); scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        stokListesiSekmesi.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout()); buttonPanel.setBackground(SoftArkaPlan);
        JButton btnCikisYap = new JButton(" 🚪 Güvenli Oturumu Kapat "); btnCikisYap.setBackground(new Color(100, 116, 139)); btnCikisYap.setForeground(Color.WHITE); btnCikisYap.setPreferredSize(new Dimension(240, 42));
        
        JPanel crudButonlarPaneli = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); crudButonlarPaneli.setBackground(SoftArkaPlan);
        JButton btnEkleFormu = new JButton(" + Yeni Ürün Ekle "); btnEkleFormu.setBackground(royalMavi); btnEkleFormu.setForeground(Color.WHITE); btnEkleFormu.setPreferredSize(new Dimension(160, 42));
        JButton btnDuzenleFormu = new JButton(" 📝 Ürünü Düzenle "); btnDuzenleFormu.setBackground(duzenleTuruncu); btnDuzenleFormu.setForeground(Color.WHITE); btnDuzenleFormu.setPreferredSize(new Dimension(160, 42));
        JButton btnSil = new JButton(" ❌ Envanterden Sil "); btnSil.setBackground(mercanKirmizi); btnSil.setForeground(Color.WHITE); btnSil.setPreferredSize(new Dimension(160, 42));
        
        addButtonHoverEffect(btnEkleFormu, royalMavi);
        addButtonHoverEffect(btnDuzenleFormu, duzenleTuruncu);
        addButtonHoverEffect(btnSil, mercanKirmizi);
        addButtonHoverEffect(btnCikisYap, new Color(100, 116, 139));

        crudButonlarPaneli.add(btnSil); crudButonlarPaneli.add(btnDuzenleFormu); crudButonlarPaneli.add(btnEkleFormu);
        buttonPanel.add(btnCikisYap, BorderLayout.WEST); buttonPanel.add(crudButonlarPaneli, BorderLayout.CENTER);
        stokListesiSekmesi.add(buttonPanel, BorderLayout.SOUTH);

        // ================= SEKME 2: GRAFİK ALANI =================
        raporSekmesi = new OzelSutunGrafigiPanel();
        raporSekmesi.setBackground(Color.WHITE);

        sekmePaneli.addTab("📦 Stok Yönetim Listesi", stokListesiSekmesi); 
        sekmePaneli.addTab("📊 Kategori Dağılım Raporu", raporSekmesi);
        merkezPanel.add(sekmePaneli, BorderLayout.CENTER);
        
        add(merkezPanel, BorderLayout.CENTER);

        // --- DİNLEYİCİLER ---
        tablo.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int seciliSatir = tablo.getSelectedRow();
                if (seciliSatir != -1) { secilenBarkod = tableModel.getValueAt(seciliSatir, 0).toString().split(" ")[0]; }
            }
        });

        btnEkleFormu.addActionListener(e -> new UrunEklePenceresi(this, anlikGirisYapanIsim).setVisible(true));
        btnDuzenleFormu.addActionListener(e -> {
            if (tablo.getSelectedRow() == -1 || secilenBarkod.isEmpty()) { JOptionPane.showMessageDialog(this, "Lütfen önce bir ürün seçin!"); return; }
            new UrunDuzenlePenceresi(this, secilenBarkod).setVisible(true);
        });
        btnSil.addActionListener(e -> {
            if (tablo.getSelectedRow() == -1 || secilenBarkod.isEmpty()) { JOptionPane.showMessageDialog(this, "Lütfen önce bir ürün seçin!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (depo.urunSil(secilenBarkod)) { tabloyuYenile(); secilenBarkod = ""; }
            }
        });
        btnCikisYap.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Oturum kapatılsın mı?", "Güvenli Çıkış", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                depo.verileriKaydet(); this.dispose(); Main.main(null);
            }
        });

        itemYenile.addActionListener(e -> { txtAramaCubugu.setText(""); tabloyuYenile(); });
        itemKapat.addActionListener(e -> { depo.verileriKaydet(); System.exit(0); });
        itemExcel.addActionListener(e -> JOptionPane.showMessageDialog(this, "Excel simüle edildi."));
        itemAref.addActionListener(e -> JOptionPane.showMessageDialog(this, "AREF Premium Paneli\n\nGeliştiriciler:\n• Arda Kasım Fırtına\n• İsmail Efe İzmire"));

        addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { depo.verileriKaydet(); } });
        setLocationRelativeTo(null); 
    }

    private void addButtonHoverEffect(JButton button, Color originalColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(originalColor.darker()); button.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(originalColor); }
        });
    }

    public void setAktifPersonelYazisi(String isim, String sql) {
        anlikGirisYapanIsim = isim; 
        lblPersonelBilgi.setText("💼 Aktif Kurumsal Oturum: " + isim + " (Sicil No: " + sql + ")");
    }

    public static void tabloyuYenile() {
        tableModel.setRowCount(0);
        String arananKelime = (txtAramaCubugu != null) ? txtAramaCubugu.getText().toLowerCase().trim() : "";

        int tStokSayaci = 0; int kStokSayaci = 0; 
        grafikUstGiyimAdet = 0; 
        grafikAltGiyimAdet = 0;

        for (Urun u : depo.getTumUrunler()) {
            String kategori = (u instanceof UstGiyim) ? "Üst Giyim" : "Alt Giyim";
            tStokSayaci += u.miktarSorgula();
            if (u.miktarSorgula() <= u.getKritikStokSeviyesi()) kStokSayaci++; 
            if (u instanceof UstGiyim) grafikUstGiyimAdet += u.miktarSorgula(); else grafikAltGiyimAdet += u.miktarSorgula();

            if (arananKelime.isEmpty() || u.getIsim().toLowerCase().contains(arananKelime) || u.getBarkod().toLowerCase().contains(arananKelime)) {
                String stokBedenDetayFormat = u.miktarSorgula() + " (S:" + u.getStokS() + ", M:" + u.getStokM() + ", L:" + u.getStokL() + ", XL:" + u.getStokXL() + ")";
                Object[] satir = { u.getBarkod() + " (ID:" + u.getId() + ")", u.getIsim(), kategori, stokBedenDetayFormat, u.getBirimFiyat() + " TL", u.getRenkKodu(), u.getEkleyenPersonel() };
                tableModel.addRow(satir);
            }
        }
        
        lblToplamCesit.setText(String.valueOf(depo.urunSayisi()));
        lblToplamStok.setText(tStokSayaci + " Adet");
        lblKritikDurum.setText(kStokSayaci + " Ürün");
        
        // Kritik durum alarmı (Hafif soft kırmızı arka plan)
        if (kStokSayaci > 0) {
            kartKritik.setBackground(new Color(254, 226, 226));
            lblKritikDurum.setForeground(new Color(220, 38, 38));
        } else {
            kartKritik.setBackground(new Color(240, 253, 244)); // Güvenli durum (Soft Yeşil)
            lblKritikDurum.setForeground(new Color(22, 163, 74));
        }

        if (raporSekmesi != null) {
            raporSekmesi.repaint();
        }
        
        tablo.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablo.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablo.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablo.getColumnModel().getColumn(3).setPreferredWidth(210); 
        tablo.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablo.getColumnModel().getColumn(5).setPreferredWidth(60); 
        tablo.getColumnModel().getColumn(6).setPreferredWidth(140); 
    }

    private static class OzelRenklendirici extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) c; 
            String barkod = table.getValueAt(row, 0).toString().split(" ")[0];
            Urun bulunanUrun = null;
            for (Urun u : depo.getTumUrunler()) { if (u.getBarkod().equals(barkod)) { bulunanUrun = u; break; } }

            if (bulunanUrun != null && bulunanUrun.miktarSorgula() <= bulunanUrun.getKritikStokSeviyesi()) { 
                renderer.setBackground(new Color(254, 226, 226)); // Kritik Alarm Satırı
            } else {
                if (isSelected) {
                    renderer.setBackground(new Color(219, 234, 254));
                } else {
                    if (row % 2 == 0) {
                        renderer.setBackground(new Color(248, 250, 252)); // Zebra
                    } else {
                        renderer.setBackground(Color.WHITE);
                    }
                }
            }
            renderer.setForeground(Color.BLACK);

            if (column == 5 && value != null) {
                try {
                    Color urunOzelRengi = Color.decode(value.toString()); renderer.setBackground(urunOzelRengi); renderer.setText(""); 
                    if(value.toString().equals("#FFFFFF")) renderer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                } catch (Exception ex) {}
            } else { renderer.setBorder(null); }
            return renderer;
        }
    }

    // ================= SÜTUN GRAFİĞİ GELİŞTİRİLDİ (GRID VE DEGRADELİ) =================
    private static class OzelSutunGrafigiPanel extends JPanel {
        public OzelSutunGrafigiPanel() {
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelGenislik = getWidth();
            int panelYukseklik = getHeight();

            g2.setColor(new Color(15, 23, 42));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString("Kategori Bazlı Envanter Analiz Grafiği", 50, 40);

            int grafikSolKenar = 100;
            int grafikTabanKenar = panelYukseklik - 120;
            int grafikTavanKenar = 80;
            int grafikMaksimumBoy = grafikTabanKenar - grafikTavanKenar;

            // Arka Plan Yardımcı Grid Çizgileri (Görsel Derinlik)
            g2.setColor(new Color(241, 245, 249));
            for (int i = 1; i <= 4; i++) {
                int yCizgi = grafikTabanKenar - (grafikMaksimumBoy * i / 4);
                g2.drawLine(grafikSolKenar, yCizgi, panelGenislik - 100, yCizgi);
            }

            g2.setColor(new Color(148, 163, 184));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(grafikSolKenar, grafikTavanKenar, grafikSolKenar, grafikTabanKenar); 
            g2.drawLine(grafikSolKenar, grafikTabanKenar, panelGenislik - 100, grafikTabanKenar); 

            int toplamUrun = grafikUstGiyimAdet + grafikAltGiyimAdet;
            int enYuksekDeger = Math.max(grafikUstGiyimAdet, grafikAltGiyimAdet);
            if (enYuksekDeger == 0) enYuksekDeger = 1; 

            int sutunGenislik = 110;
            int bosluk = 90;

            // ---- 1. SÜTUN: ÜST GİYİM (Degradeli Mavi) ----
            int ustGiyimSutunBoyu = (int) (((double) grafikUstGiyimAdet / enYuksekDeger) * grafikMaksimumBoy);
            int ustX = grafikSolKenar + bosluk;
            int ustY = grafikTabanKenar - ustGiyimSutunBoyu;

            GradientPaint gradMavi = new GradientPaint(ustX, ustY, new Color(59, 130, 246), ustX, grafikTabanKenar, new Color(29, 78, 216));
            g2.setPaint(gradMavi);
            g2.fillRect(ustX, ustY, sutunGenislik, ustGiyimSutunBoyu);
            g2.setColor(new Color(29, 78, 216));
            g2.drawRect(ustX, ustY, sutunGenislik, ustGiyimSutunBoyu);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString(grafikUstGiyimAdet + " Adet", ustX + 30, ustY - 10);
            g2.drawString("👕 Üst Giyim", ustX + 15, grafikTabanKenar + 25);

            // ---- 2. SÜTUN: ALT GİYİM (Degradeli Turuncu) ----
            int altGiyimSutunBoyu = (int) (((double) grafikAltGiyimAdet / enYuksekDeger) * grafikMaksimumBoy);
            int altX = ustX + sutunGenislik + bosluk;
            int altY = grafikTabanKenar - altGiyimSutunBoyu;

            GradientPaint gradTuruncu = new GradientPaint(altX, altY, new Color(251, 146, 60), altX, grafikTabanKenar, new Color(234, 88, 12));
            g2.setPaint(gradTuruncu);
            g2.fillRect(altX, altY, sutunGenislik, altGiyimSutunBoyu);
            g2.setColor(new Color(234, 88, 12));
            g2.drawRect(altX, altY, sutunGenislik, altGiyimSutunBoyu);

            g2.setColor(Color.BLACK);
            g2.drawString(grafikAltGiyimAdet + " Adet", altX + 30, altY - 10);
            g2.drawString("👖 Alt Giyim", altX + 15, grafikTabanKenar + 25);

            // ---- ALT LEJANT BİLGİ ALANI ----
            int lejantY = panelYukseklik - 50;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            g2.setColor(new Color(37, 99, 235));
            g2.fillRect(grafikSolKenar, lejantY, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawString("Üst Giyim Segmenti", grafikSolKenar + 25, lejantY + 13);

            g2.setColor(new Color(234, 88, 12));
            g2.fillRect(grafikSolKenar + 180, lejantY, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawString("Alt Giyim Segmenti", grafikSolKenar + 205, lejantY + 13);
            
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString("📊 Toplam Dağılım Hacmi: " + toplamUrun + " Adet Ürün", grafikSolKenar + 380, lejantY + 13);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main anaPencere = new Main();
            GirisEkrani login = new GirisEkrani(anaPencere); login.setVisible(true); 
            if (login.isGirisBasarili()) {
                anaPencere.setAktifPersonelYazisi(login.getAdSoyad(), login.getSicilKodu());
                depo.verileriYukle(); anaPencere.setVisible(true); Main.tabloyuYenile();        
            } else { System.exit(0); }
        });
    }
}