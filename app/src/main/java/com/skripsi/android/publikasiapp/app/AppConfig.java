package com.skripsi.android.publikasiapp.app;

/**
 * Created by Harits on 2/24/2018.
 */

public class AppConfig {
    //URL untuk server User login
    public static String URL_LOGIN = "http://192.168.43.64/publikasi_api/login.php";

    //URL untuk server User register
    public static String URL_REGISTER = "http://192.168.43.64/publikasi_api/register.php";

    //URL untuk server fetching json publikasi
    public static String URL_PUBLIKASI2 = "http://192.168.43.64/publikasi_api/get_publikasi.php";
    public static String URL_PUBLIKASI = "http://192.168.43.64/publikasi_api/get_all_publikasi.php?id=";

    public static String URL_CARI = "http://192.168.43.64/publikasi_api/publikasi_search.php?search=";
    public static String URL_ACTIVITY_USER = "http://192.168.43.64/publikasi_api/add_activity.php";



}
