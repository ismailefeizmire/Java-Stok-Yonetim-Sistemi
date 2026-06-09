import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class GirisEkrani extends JDialog {
    private JTextField txtSicil;
    private JPasswordField txtSifre;
    private JButton btnGiris, btnKayitOl, btnIptal;
    private boolean girisBasarili = false;
    private String adSoyad = "";
    private String sicilKodu = ""; 
    
    private HashMap<String, String[]> personelHaritasi = new HashMap<>();
    private final String personelDosyaAdi = "personeller.txt";

    public GirisEkrani(JFrame parent) {
        super(parent, "AREF Otomasyon - Kurumsal Giriş", true);
        personelVerileriniYukle();
        
        setSize(440, 320);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        setResizable(false);

        // --- MODERN PREMIUM RENKLER ---
        Color anaKoyu = new Color(30, 41, 59);       
        Color canliMavi = new Color(14, 116, 144);   
        Color softArkaPlan = new Color(248, 250, 252); 

        // Üst Alan: Premium Logo/Başlık Bandı
        JPanel baslikPanel = new JPanel(new BorderLayout());
        baslikPanel.setBackground(anaKoyu);
        baslikPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblBaslik = new JLabel("AREF KURUMSAL STOK PANELİ", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBaslik.setForeground(Color.WHITE);
        
        JLabel lblAltSatir = new JLabel("Güvenli Envanter ve Yetkilendirme Girişi", SwingConstants.CENTER);
        lblAltSatir.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAltSatir.setForeground(new Color(148, 163, 184));
        
        baslikPanel.add(lblBaslik, BorderLayout.CENTER);
        baslikPanel.add(lblAltSatir, BorderLayout.SOUTH);

        // Orta Alan: Giriş Formu Kartı
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(softArkaPlan);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 15, 40));

        txtSicil = new JTextField();
        txtSicil.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSicil.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        txtSifre = new JPasswordField();
        txtSifre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSifre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        JLabel lblS1 = new JLabel("6 Haneli Sicil Kodu:", SwingConstants.RIGHT);
        lblS1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblS1.setForeground(new Color(71, 85, 105));
        
        JLabel lblS2 = new JLabel("Sistem Şifresi:", SwingConstants.RIGHT);
        lblS2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblS2.setForeground(new Color(71, 85, 105));

        formPanel.add(lblS1); formPanel.add(txtSicil);
        formPanel.add(lblS2); formPanel.add(txtSifre);
        
        JLabel lblKunya = new JLabel("Geliştiriciler: Arda K. Fırtına & İsmail E. İzmire", SwingConstants.CENTER);
        lblKunya.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblKunya.setForeground(new Color(100, 116, 139));
        
        formPanel.add(new JLabel()); 
        formPanel.add(lblKunya);

        // Alt Panel: Buton Grubu
        JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        butonPanel.setBackground(softArkaPlan);
        butonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        btnIptal = new JButton("İptal");
        btnIptal.setFocusPainted(false);
        btnIptal.setBackground(new Color(241, 245, 249));
        btnIptal.setForeground(new Color(15, 23, 42));

        btnKayitOl = new JButton("Yeni Personel Kaydı");
        btnKayitOl.setFocusPainted(false);
        btnKayitOl.setBackground(new Color(5, 150, 105)); 
        btnKayitOl.setForeground(Color.WHITE);

        btnGiris = new JButton("Giriş Yap");
        btnGiris.setFocusPainted(false);
        btnGiris.setBackground(canliMavi);
        btnGiris.setForeground(Color.WHITE);
        
        butonPanel.add(btnIptal);
        butonPanel.add(btnKayitOl);
        butonPanel.add(btnGiris);

        add(baslikPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(butonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnGiris);

        addButtonHover(btnGiris, canliMavi);
        addButtonHover(btnKayitOl, new Color(5, 150, 105));

        btnGiris.addActionListener(e -> {
            String sicil = txtSicil.getText().trim();
            String sifre = new String(txtSifre.getPassword()).trim();

            if (sicil.isEmpty() || sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kutular boş bırakılamaz!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (personelHaritasi.containsKey(sicil)) {
                String[] veriler = personelHaritasi.get(sicil);
                if (veriler[1].equals(sifre)) {
                    this.adSoyad = veriler[0];
                    this.sicilKodu = sicil;
                    this.girisBasarili = true;
                    dispose(); 
                    hosGeldinizEkraniniAc(); 
                } else {
                    JOptionPane.showMessageDialog(this, "HATA: Şifre yanlış!", "Erişim Engellendi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "HATA: Sicil bulunamadı!", "Kayıt Yok", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnKayitOl.addActionListener(e -> personelKayitDialogunuAc());
        btnIptal.addActionListener(e -> System.exit(0));
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { System.exit(0); }
        });
    }

    private void addButtonHover(JButton btn, Color baseColor) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(baseColor.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(baseColor); }
        });
    }

    private void personelKayitDialogunuAc() {
        JDialog kayitDialog = new JDialog(this, "Yeni Personel Kayıt Formu", true);
        kayitDialog.setSize(400, 250);
        kayitDialog.setLayout(new GridLayout(4, 2, 10, 15));
        kayitDialog.setLocationRelativeTo(this);
        kayitDialog.getContentPane().setBackground(new Color(248, 250, 252));

        JTextField txtYeniAd = new JTextField();
        JTextField txtYeniSifre = new JTextField();
        
        Random rand = new Random();
        String yeniSicilNo;
        do {
            yeniSicilNo = String.valueOf(100000 + rand.nextInt(900000));
        } while (personelHaritasi.containsKey(yeniSicilNo));

        // HATA ÇÖZÜMÜ: Lambda ifadesinin erişebilmesi için sicil numarasını final olarak kilitledik!
        final String sabitSicilNo = yeniSicilNo;

        JLabel lblUretilenSicil = new JLabel("  " + sabitSicilNo);
        lblUretilenSicil.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUretilenSicil.setForeground(new Color(5, 150, 105));

        kayitDialog.add(new JLabel("  Otomatik Sicil Kodunuz:")); kayitDialog.add(lblUretilenSicil);
        kayitDialog.add(new JLabel("  Adınız Soyadınız:")); kayitDialog.add(txtYeniAd);
        kayitDialog.add(new JLabel("  Giriş Şifreniz:")); kayitDialog.add(txtYeniSifre);

        JButton btnKaydet = new JButton("Personeli Kaydet");
        btnKaydet.setBackground(new Color(5, 150, 105)); btnKaydet.setForeground(Color.WHITE);
        kayitDialog.add(new JLabel()); kayitDialog.add(btnKaydet);

        btnKaydet.addActionListener(ev -> {
            String ad = txtYeniAd.getText().trim();
            String sifre = txtYeniSifre.getText().trim();
            if (ad.isEmpty() || sifre.isEmpty()) return;

            // Kilitli olan sabitSicilNo hafızaya kaydediliyor
            personelHaritasi.put(sabitSicilNo, new String[]{ad, sifre});
            personelVerileriniKaydet();
            JOptionPane.showMessageDialog(this, "Kayıt Başarılı! Sicil No: " + sabitSicilNo);
            kayitDialog.dispose();
            txtSicil.setText(sabitSicilNo); 
        });
        kayitDialog.setVisible(true);
    }

    private void hosGeldinizEkraniniAc() {
        JDialog welcomeDialog = new JDialog((Frame)null, "Yükleniyor...", false);
        welcomeDialog.setSize(450, 180);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setLocationRelativeTo(null);
        welcomeDialog.setUndecorated(true); 
        
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(BorderFactory.createLineBorder(new Color(6, 182, 212), 2));

        JLabel lblWelcome = new JLabel("Sisteme Başarıyla Bağlanıldı. Hoş Geldiniz,", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcome.setForeground(new Color(203, 213, 225));
        
        JLabel lblUser = new JLabel(this.adSoyad, SwingConstants.CENTER);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUser.setForeground(new Color(6, 182, 212)); 

        JPanel textGroup = new JPanel(new GridLayout(2, 1));
        textGroup.setBackground(new Color(30, 41, 59));
        textGroup.add(lblWelcome); textGroup.add(lblUser);

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setForeground(new Color(6, 182, 212));
        progress.setBackground(new Color(71, 85, 105));

        panel.add(textGroup);
        panel.add(progress);
        welcomeDialog.add(panel, BorderLayout.CENTER);

        new Thread(() -> {
            welcomeDialog.setVisible(true);
            try {
                for (int i = 0; i <= 100; i += 5) {
                    final int p = i;
                    SwingUtilities.invokeLater(() -> progress.setValue(p));
                    Thread.sleep(30); 
                }
            } catch (Exception ex) {}
            welcomeDialog.dispose();
        }).start();

        while(welcomeDialog.isVisible()) {
            try { Thread.sleep(30); } catch(Exception e){}
        }
    }

    private void personelVerileriniKaydet() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(personelDosyaAdi))) {
            for (String searchSicil : personelHaritasi.keySet()) {
                String[] veriler = personelHaritasi.get(searchSicil);
                writer.println(searchSicil + ";" + veriler[0] + ";" + veriler[1]);
            }
        } catch (IOException e) {}
    }

    private void personelVerileriniYukle() {
        File f = new File(personelDosyaAdi);
        if (!f.exists()) {
            personelHaritasi.put("100001", new String[]{"Arda Kasım Fırtına", "aref123"});
            personelHaritasi.put("100002", new String[]{"İsmail Efe İzmire", "aref123"});
            personelVerileriniKaydet();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(personelDosyaAdi))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] p = s.split(";");
                if(p.length == 3) personelHaritasi.put(p[0], new String[]{p[1], p[2]});
            }
        } catch (Exception e) {}
    }

    public boolean isGirisBasarili() { return girisBasarili; }
    public void setGirisBasarili(boolean b) { this.girisBasarili = b; } 
    public String getAdSoyad() { return adSoyad; } 
    public String getSicilKodu() { return sicilKodu; } 
}