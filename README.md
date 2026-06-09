# AREF Kurumsal Stok Yönetim Otomasyonu

AREF Otomasyon, işletmelerin tekstil ve giyim stoklarını (Alt Giyim ve Üst Giyim) güvenli bir şekilde yönetmesini sağlayan, Nesne Yönelimli Programlama (OOP) mimarisiyle geliştirilmiş masaüstü Java uygulamasıdır. Uygulama, modern ve kullanıcı dostu bir arayüz (GUI) sunarken, verileri yerel dosya sisteminde kalıcı olarak saklar.

## 🚀 Projenin Öne Çıkan Özellikleri

* **Güvenli Personel Girişi ve Yetkilendirme:** Sisteme erişim, 6 haneli sicil kodu ve özel şifre ile sağlanır. Yeni personeller için sistem tarafından otomatik sicil kodu üretilir ve çakışmalar engellenir.
* **Gelişmiş Veri Yapıları:** Stok ve personel verilerinin hızlı bir şekilde aranması, eklenmesi ve silinmesi için `HashMap` koleksiyon yapısı (O(1) karmaşıklığında) kullanılmıştır.
* **Nesne Yönelimli Mimari (OOP):** * Sistemdeki ürünler Kalıtım (Inheritance) prensibiyle `Urun` üst sınıfından türetilen `AltGiyim` ve `UstGiyim` sınıflarına ayrılmıştır. 
  * Stok işlemleri `IStoklanabilir` arayüzü (Interface) üzerinden standartlaştırılarak Çok Biçimlilik (Polymorphism) sağlanmıştır.
* **Dosya Tabanlı Veri Kalıcılığı (File I/O):** Tüm stok kayıtları `aref_stok_audit.txt` dosyasına, personel giriş bilgileri ise `personeller.txt` dosyasına otomatik olarak kaydedilir ve sistem açılışında geri yüklenir.
* **Modern Swing Arayüzü (GUI):** Kurumsal kimliğe uygun, premium renk paleti ve özel kenarlıklarla (BorderFactory) tasarlanmış grafiksel kullanıcı arayüzü sunulur. Dinamik Progress Bar içeren hoş geldin ekranı ile kullanıcı deneyimi artırılmıştır.

## 🛠️ Kullanılan Teknolojiler ve Konseptler

* **Dil:** Java (JDK)
* **Arayüz (GUI):** Java Swing (JFrame, JDialog, JProgressBar vb.)
* **Veri Yönetimi:** Java File I/O (`BufferedReader`, `PrintWriter`, `FileWriter`)
* **Mimari:** OOP (Encapsulation, Inheritance, Polymorphism, Interfaces)

## ⚙️ Kurulum ve Çalıştırma

Projeyi yerel bilgisayarınızda çalıştırmak için aşağıdaki adımları izleyebilirsiniz:

1. Bu depoyu (repository) bilgisayarınıza klonlayın veya indirin.
2. Projeyi Eclipse, IntelliJ IDEA veya NetBeans gibi bir Java IDE'sinde açın.
3. Proje dizininde `aref_stok_audit.txt` ve `personeller.txt` dosyalarının okuma/yazma izinlerinin açık olduğundan emin olun (Bu dosyalar yoksa, ilk çalıştırmada sistem tarafından otomatik oluşturulacaktır).
4. `GirisEkrani` veya uygulamanın ana başlatıcı (Main) sınıfını çalıştırın.
5. Varsayılan yönetici bilgileriyle sisteme giriş yapabilir veya "Yeni Personel Kaydı" butonu üzerinden kendi profilinizi oluşturabilirsiniz.

