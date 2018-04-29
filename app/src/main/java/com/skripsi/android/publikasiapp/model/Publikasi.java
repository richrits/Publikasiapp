package com.skripsi.android.publikasiapp.model;

public class Publikasi {
    private int pub_id;
    private String title;
    private String kat_no;
    private String pub_no;
    private String issn;
    private String _abstract;
    private String sch_date;
    private String rl_date;
    private String updt_date;
    private String cover;
    private String pdf;
    private String size;

    public Publikasi(int pub_id, String title, String kat_no, String pub_no, String issn, String _abstract, String sch_date, String rl_date, String updt_date, String cover, String pdf, String size) {
        this.pub_id = pub_id;
        this.title = title;
        this.kat_no = kat_no;
        this.pub_no = pub_no;
        this.issn = issn;
        this._abstract = _abstract;
        this.sch_date = sch_date;
        this.rl_date = rl_date;
        this.updt_date = updt_date;
        this.cover = cover;
        this.pdf = pdf;
        this.size = size;
    }

    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKat_no() {
        return kat_no;
    }

    public void setKat_no(String kat_no) {
        this.kat_no = kat_no;
    }

    public String getPub_no() {
        return pub_no;
    }

    public void setPub_no(String pub_no) {
        this.pub_no = pub_no;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getSch_date() {
        return sch_date;
    }

    public void setSch_date(String sch_date) {
        this.sch_date = sch_date;
    }

    public String getRl_date() {
        return rl_date;
    }

    public void setRl_date(String rl_date) {
        this.rl_date = rl_date;
    }

    public String getUpdt_date() {
        return updt_date;
    }

    public void setUpdt_date(String updt_date) {
        this.updt_date = updt_date;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
