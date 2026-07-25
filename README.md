# Adik App 💗

Aplikasi Android pribadi (100% **offline**, tanpa server, tanpa internet) untuk membantu mengelola:

- 💰 **Keuangan** — saldo, akumulasi pemasukan & pengeluaran
- 🎯 **Target Belajar** — target semester dengan progress bar
- 🛍️ **Wishlist** — daftar barang impian + prioritas
- 📅 **Jadwal Kuliah** — jadwal mingguan per hari
- ✅ **Kegiatan / Daily Activity** — agenda harian
- 📔 **Note Harian** — jurnal harian + foto (kamera/galeri, tersimpan lokal)
- 📝 **Note Tugas** — daftar tugas kuliah dengan **notifikasi otomatis H-2 sebelum deadline**

Dibangun dengan **Kotlin + Jetpack Compose + Room (SQLite lokal) + WorkManager**. Semua data tersimpan di HP, tidak ada permission internet sama sekali.

---

## 1. Struktur Proyek

```
AdikApp/
├── app/
│   └── src/main/java/com/salwa/adikapp/
│       ├── data/           # Entity + DAO + Room Database
│       ├── notification/   # Reminder H-2 (WorkManager)
│       ├── ui/
│       │   ├── screens/    # Layar tiap fitur
│       │   ├── navigation/ # Navigasi antar layar
│       │   └── theme/      # Warna & tipografi
│       ├── viewmodel/      # ViewModel tiap fitur
│       └── util/           # Helper (tanggal, penyimpanan foto)
├── .github/workflows/android-build.yml   # CI build otomatis
├── gradlew / gradlew.bat / gradle/        # Gradle wrapper (siap pakai)
├── build.gradle.kts, settings.gradle.kts
└── README.md
```

## 2. Menyiapkan Repository di GitHub

1. Buat repository baru di GitHub (misal `adik-app`), **jangan** centang "Add README" supaya tidak bentrok.
2. Di terminal, masuk ke folder proyek ini lalu jalankan:

   ```bash
   git init
   git add .
   git commit -m "Initial commit: Adik App"
   git branch -M main
   git remote add origin https://github.com/USERNAME/adik-app.git
   git push -u origin main
   ```

3. Setelah push pertama, buka tab **Actions** di repo GitHub kamu — workflow **"Android CI Build"** akan otomatis berjalan.

## 3. Cara Kerja Workflow (GitHub Actions)

File: `.github/workflows/android-build.yml`

| Trigger | Yang terjadi |
|---|---|
| Push ke branch `main` | Build **debug APK** otomatis, hasilnya bisa diunduh di tab *Actions → run terkait → Artifacts* |
| Pull request ke `main` | Build & test dijalankan untuk validasi sebelum merge |
| Push tag `v*` (misal `v1.0`) | Build tambahan **release APK** (unsigned) |
| Manual (`workflow_dispatch`) | Bisa dijalankan manual lewat tombol "Run workflow" di tab Actions |

Setelah build selesai (biasanya 3–6 menit), buka:
`Actions → pilih run terbaru → bagian Artifacts → download "adikapp-debug-apk"`

File `.apk` di dalamnya tinggal di-transfer ke HP Android dan diinstall langsung (aktifkan "Install dari sumber tidak dikenal" jika diminta).

## 4. Build Manual di Komputer Sendiri (opsional)

Kalau punya Android Studio:

1. Buka folder `AdikApp` lewat **File → Open**.
2. Tunggu Gradle sync selesai (butuh koneksi internet **hanya saat build/download dependency**, aplikasi hasil jadinya tetap offline).
3. Klik **Run ▶** untuk jalankan di emulator/HP, atau **Build → Build Bundle(s)/APK(s) → Build APK(s)**.

Lewat terminal (tanpa Android Studio, asal ada JDK 17 & Android SDK terpasang):

```bash
./gradlew assembleDebug
# hasil APK ada di: app/build/outputs/apk/debug/app-debug.apk
```

## 5. Membuat APK Release yang Ditandatangani (Signed) — opsional

APK dari GitHub Actions defaultnya **unsigned** (untuk testing). Kalau mau publish/pakai jangka panjang dan install ulang tanpa uninstall, sebaiknya sign APK-nya:

1. Buat keystore: `keytool -genkey -v -keystore adikapp.keystore -alias adikapp -keyalg RSA -keysize 2048 -validity 10000`
2. Simpan `adikapp.keystore` sebagai **GitHub Secret** (encode base64), tambahkan step signing di workflow menggunakan `r0adkll/sign-android-release` action.
3. Atau paling simpel: build & sign langsung dari Android Studio (**Build → Generate Signed Bundle/APK**).

## 6. Kenapa Offline?

- Tidak ada `INTERNET` permission di `AndroidManifest.xml`.
- Semua data disimpan di database lokal **Room/SQLite** (`adikapp.db`) di penyimpanan internal aplikasi.
- Foto note harian disimpan sebagai file di `files/diary_photos/` di internal storage HP — bukan diunggah kemana pun.
- Notifikasi H-2 dijadwalkan dengan **WorkManager**, berjalan sepenuhnya di perangkat, otomatis dijadwalkan ulang setelah restart HP (`BootReceiver`).

## 7. Fitur yang Bisa Dikembangkan Lagi

- Export data ke PDF/Excel untuk laporan keuangan
- Backup/restore database ke file lokal
- Widget home screen untuk jadwal hari ini
- Grafik pengeluaran per kategori

---

Dibuat dengan sayang untuk adik 💗
