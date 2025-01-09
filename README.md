# **PlanPal - A Project Management Application**

PlanPal adalah aplikasi **manajemen proyek berbasis GUI** yang dirancang untuk memudahkan tim dalam mengelola proyek menggunakan pendekatan **Kanban Board**. Aplikasi ini dibangun dengan **Java Swing** sebagai antarmuka utama, serta menggunakan **MySQL** untuk sistem manajemen basis data.

Repository ini berisi **source code** aplikasi, dokumentasi, serta panduan lengkap untuk menjalankan proyek secara lokal.

---

## **📋 Fitur Utama**
PlanPal menyediakan fitur-fitur berikut untuk memudahkan manajemen proyek:

- Pendaftaran Admin dan Anggota
- Dashboard Proyek
- Manajemen Tugas dengan Kanban Board (*To-Do*, *In Progress*, *Done*)
- Notifikasi Otomatis
- Komentar Langsung
- Laporan Progres Mingguan (format Excel)
- Otentikasi Pengguna (Login dan Sign Up)

---

## **🛠️ Teknologi yang Digunakan**
- **Java Swing** – untuk antarmuka pengguna (frontend)
- **Java** – untuk logika aplikasi (backend)
- **MySQL** – untuk sistem manajemen basis data
- **Apache POI** – untuk ekspor laporan dalam format Excel

---

## **📂 Struktur Proyek**
```
PlanPal/
├── src/
│   ├── controller/
│   ├── model/
│   ├── view/
│   └── utils/
├── database/
│   └── planpal.sql
└── README.md
```

- **`controller/`** – Berisi logika backend untuk menangani request dari pengguna.
- **`model/`** – Berisi class untuk memodelkan data seperti Proyek, Tugas, dan Komentar.
- **`view/`** – Berisi antarmuka pengguna yang interaktif.
- **`utils/`** – Berisi utility class seperti **Database Helper**, **Session Manager**, dan **Login Service**.
- **`database/`** – Berisi file SQL untuk menyiapkan basis data.

---

## **📦 Cara Menjalankan Proyek Secara Lokal**

### **1. Clone Repository**
Jalankan perintah berikut di terminal untuk meng-clone repository ini:

```bash
git clone https://github.com/jopi183/planpal.git
cd planpal
```

### **2. Siapkan Basis Data**
- Import file SQL yang ada di folder **`database/planpal.sql`** ke dalam MySQL.
- Pastikan konfigurasi database di kode sesuai dengan pengaturan MySQL lokal Anda.

### **3. Jalankan Proyek**
1. Buka proyek di **IDE** seperti **IntelliJ IDEA** atau **Eclipse**.
2. Jalankan file **`Main.java`** untuk memulai aplikasi.

---

## **🧪 Panduan Pengujian**
- **Username Admin:** `admin`
- **Password Admin:** `admin123`

Untuk menambahkan pengguna baru, gunakan fitur **Sign Up** di aplikasi.

---

## **🧩 Kontribusi Anggota Tim**
| **Nama Anggota**           | **Peran/Role**                     | **Kontribusi**                                          |
|----------------------------|-----------------------------------|--------------------------------------------------------|
| Joshua Pinem               | Backend Developer & Architect     | Desain arsitektur aplikasi, pengembangan logika backend |
| Umar Khairur Rahman        | QA & Code Integrator              | Quality assurance, pengujian aplikasi, penyatuan kode  |
| Nathan Dava Arkananta      | Document Manager &                | Pengelolaan dokumen proyek, ide, dan solusi masalah    |
| Embun Nawang Sari          | Front-End Developer & Designer    | Pengembangan antarmuka aplikasi dan desain UI          |
| Anila Dwi Lestari          | Front-End Developer & Designer    | Desain visual antarmuka dan pengembangan halaman Sign Up |
| Yasmina Arethaya Hanjani   | Front-End Developer & Database    | Pengelolaan database, ekspor laporan, dan pengembangan dashboard |

---

## **📄 Lisensi**
Proyek ini dilisensikan di bawah **MIT License** – bebas digunakan dan dikembangkan lebih lanjut.

---

## **📎 Link Repository**
- GitHub: [PlanPal Repository](https://github.com/jopi183/planpal)

---

## **💻 Panduan Pengembangan Lanjutan**
Jika Anda ingin mengembangkan aplikasi ini lebih lanjut, berikut adalah langkah-langkah yang disarankan:

1. **Tambahkan Fitur Baru:**  
   Misalnya, integrasi dengan **cloud storage** atau penambahan **chat system** untuk kolaborasi real-time.

2. **Perbaiki Desain UI/UX:**  
   Gunakan library seperti **JavaFX** untuk meningkatkan tampilan antarmuka.

3. **Integrasikan dengan API Eksternal:**  
   Contohnya, gunakan API untuk **email notifikasi** atau **kalender proyek**.

---

## **📧 Kontak**
Jika Anda memiliki pertanyaan atau masukan, silakan hubungi kami melalui:

- **Joshua Pinem** – jopisan@student.telkomuniversity.ac.id
- **Umar Khairur Rahman** – fbnajis@student.telkomuniversity.ac.id 
- **Nathan Dava Arkananta** – nathandava@student.telkomuniversity.ac.id 
- **Embun Nawang Sari** – embunns@student.telkomuniversity.ac.id 
- **Anila Dwi Lestari** – aniladwilestari@student.telkomuniversity.ac.id  
- **Yasmina Arethaya Hanjani** – yasminaarethaya@student.telkomuniversity.ac.id

Kami dengan senang hati menerima umpan balik dan kontribusi dari Anda! 🎉
