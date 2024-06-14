package com.ffi.backofficeho.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.backofficeho.dao.ProcessDao;
import com.ffi.backofficeho.service.ValidationService;
import com.ffi.backofficeho.util.TableAliasUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TableAliasUtil tableAliasUtil;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, TableAliasUtil tableAliasUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableAliasUtil = tableAliasUtil;
    }

    @Autowired
    private ValidationService validationService;

    ////////// Insert master Branch
    @Override
    public void insertBranch(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_BRANCH (KODE_GROUP, KODE_CABANG, KODE_SINGKAT, NAMA_CABANG, ALAMAT1, ALAMAT2, " +
                "KOTA, KODE_POS, TELPON1, TELPON2, FAX1, FAX2, KIRIM_DATA, ALAMAT_EMAIL, ALAMAT_IP, ALAMAT_PORT, " +
                "FTP_ADDRESS, FTP_USER, FTP_PASSWORD, KODE_RSC, KODE_REGION, KODE_AREA, CONTACT_PERSON, " +
                "CONTACT_PHONE, TIPE_CABANG, STATUS_AKTIF, KETERANGAN, CAD1, CAD2, USER_CREATE, DATE_CREATE, " +
                "TIME_CREATE, USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) " +
                "VALUES(:kodeGroup, :kodeCabang, :kodeSingkat, :namaCabang, :alamat1, :alamat2, :kota, :kodePos, " +
                ":telpon1, :telpon2, :fax1, :fax2, :kirimData, :alamatEmail, :alamatIp, :alamatPort, :ftpAddress, " +
                ":ftpUser, :ftpPassword, :kodeRsc, :kodeRegion, :kodeArea, :contactPerson, :contactPhone, " +
                ":tipeCabang, :statusAktif, :keterangan, :cad1, :cad2, :userCreate, :dateCreate, :timeCreate, " +
                ":userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeGroup", balance.get("kodeGroup"));
        param.put("kodeCabang", balance.get("kodeCabang"));
        param.put("kodeSingkat", balance.get("kodeSingkat"));
        param.put("namaCabang", balance.get("namaCabang"));
        param.put("alamat1", balance.get("alamat1"));
        param.put("alamat2", balance.get("alamat2"));
        param.put("kota", balance.get("kota"));
        param.put("kodePos", balance.get("kodePos"));
        param.put("telpon1", balance.get("telpon1"));
        param.put("telpon2", balance.get("telpon2"));
        param.put("fax1", balance.get("fax1"));
        param.put("fax2", balance.get("fax2"));
        param.put("kirimData", balance.get("kirimData"));
        param.put("alamatEmail", balance.get("alamatEmail"));
        param.put("alamatIp", balance.get("alamatIp"));
        param.put("alamatPort", balance.get("alamatPort"));
        param.put("ftpAddress", balance.get("ftpAddress"));
        param.put("ftpUser", balance.get("ftpUser"));
        param.put("ftpPassword", balance.get("ftpPassword"));
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("kodeRegion", balance.get("kodeRegion"));
        param.put("kodeArea", balance.get("kodeArea"));
        param.put("contactPerson", balance.get("contactPerson"));
        param.put("contactPhone", balance.get("contactPhone"));
        param.put("tipeCabang", balance.get("tipeCabang"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("keterangan", balance.get("keterangan"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }
    ////////// end insert master branch

    ///////// update master branch
    @Override
    public void updateBranch(Map<String, String> balance) {
        String sql = "UPDATE WMS_BRANCH " +
                "SET KODE_GROUP=:kodeGroup, KODE_SINGKAT=:kodeSingkat, NAMA_CABANG=:namaCabang, ALAMAT1=:alamat1, " +
                "ALAMAT2=:alamat2, KOTA=:kota, KODE_POS=:kodePos, TELPON1=:telpon1, TELPON2=:telpon2, FAX1=:fax1, " +
                "FAX2=:fax2, KIRIM_DATA=:kirimData, ALAMAT_EMAIL=:alamatEmail, ALAMAT_IP=:alamatIp, " +
                "ALAMAT_PORT=:alamatPort, FTP_ADDRESS=:ftpAddress, FTP_USER=:ftpUser, FTP_PASSWORD=:ftpPassword," +
                " KODE_RSC=:kodeRsc, KODE_REGION=:kodeRegion, KODE_AREA=:kodeArea, CONTACT_PERSON=:contactPerson," +
                " CONTACT_PHONE=:contactPhone, TIPE_CABANG=:tipeCabang, STATUS_AKTIF=:statusAktif, " +
                "KETERANGAN=:keterangan, CAD1=:cad1, CAD2=:cad2, " +
                "USER_UPDATE=:userUpdate, DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, " +
                "STATUS_SYNC=:statusSync " +
                "WHERE KODE_CABANG=:kodeCabang";
        Map param = new HashMap();
        param.put("kodeGroup", balance.get("kodeGroup"));
        param.put("kodeCabang", balance.get("kodeCabang"));
        param.put("kodeSingkat", balance.get("kodeSingkat"));
        param.put("namaCabang", balance.get("namaCabang"));
        param.put("alamat1", balance.get("alamat1"));
        param.put("alamat2", balance.get("alamat2"));
        param.put("kota", balance.get("kota"));
        param.put("kodePos", balance.get("kodePos"));
        param.put("telpon1", balance.get("telpon1"));
        param.put("telpon2", balance.get("telpon2"));
        param.put("fax1", balance.get("fax1"));
        param.put("fax2", balance.get("fax2"));
        param.put("kirimData", balance.get("kirimData"));
        param.put("alamatEmail", balance.get("alamatEmail"));
        param.put("alamatIp", balance.get("alamatIp"));
        param.put("alamatPort", balance.get("alamatPort"));
        param.put("ftpAddress", balance.get("ftpAddress"));
        param.put("ftpUser", balance.get("ftpUser"));
        param.put("ftpPassword", balance.get("ftpPassword"));
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("kodeRegion", balance.get("kodeRegion"));
        param.put("kodeArea", balance.get("kodeArea"));
        param.put("contactPerson", balance.get("contactPerson"));
        param.put("contactPhone", balance.get("contactPhone"));
        param.put("tipeCabang", balance.get("tipeCabang"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("keterangan", balance.get("keterangan"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertArea(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_AREA (KODE_AREA, KETERANGAN_AREA, USER_CREATE, DATE_CREATE, TIME_CREATE, " +
                "USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) VALUES(:kodeArea, :keteranganArea, :userCreate," +
                " :dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeArea", balance.get("kodeArea"));
        param.put("keteranganArea", balance.get("keteranganArea"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateArea(Map<String, String> balance) {
        String sql = "UPDATE WMS_AREA SET KETERANGAN_AREA=:keteranganArea, USER_UPDATE=:userUpdate, " +
                "DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync WHERE KODE_AREA=:kodeArea";
        Map param = new HashMap();
        param.put("kodeArea", balance.get("kodeArea"));
        param.put("keteranganArea", balance.get("keteranganArea"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertCity(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_CITY (KODE_KOTA, KETERANGAN_KOTA, USER_CREATE, DATE_CREATE, TIME_CREATE," +
                " USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) VALUES(:kodeKota, :keteranganKota, " +
                ":userCreate, :dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeKota", balance.get("kodeKota"));
        param.put("keteranganKota", balance.get("keteranganKota"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateCity(Map<String, String> balance) {
        String sql = "UPDATE WMS_CITY SET KETERANGAN_KOTA=:keteranganKota, USER_UPDATE=:userUpdate, " +
                "DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync WHERE KODE_KOTA=:kodeKota";
        Map param = new HashMap();
        param.put("kodeKota", balance.get("kodeKota"));
        param.put("keteranganKota", balance.get("keteranganKota"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertLocation(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_LOCATION (KODE_LOCATION, KODE_INISIAL, KETERANGAN_LOKASI, LOKASI_GUDANG, " +
                "DEFAULT_RSC, SUPPORT_TO, MAIN_MENU, USER_CREATE, DATE_CREATE, TIME_CREATE, USER_UPDATE, DATE_UPDATE," +
                " TIME_UPDATE, STATUS_SYNC) VALUES(:kodeLocation, :kodeInisial, :keteranganLokasi, " +
                ":lokasiGudang, :defaultRsc, :supportTo, :mainMenu, :userCreate, :dateCreate, :timeCreate, " +
                ":userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeLocation", balance.get("kodeLocation"));
        param.put("kodeInisial", balance.get("kodeInisial"));
        param.put("keteranganLokasi", balance.get("keteranganLokasi"));
        param.put("lokasiGudang", balance.get("lokasiGudang"));
        param.put("defaultRsc", balance.get("defaultRsc"));
        param.put("supportTo", balance.get("supportTo"));
        param.put("mainMenu", balance.get("mainMenu"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateLocation(Map<String, String> balance) {
        String sql = "UPDATE WMS_LOCATION SET KODE_INISIAL=:kodeInisial, KETERANGAN_LOKASI=:keteranganLokasi, " +
                "LOKASI_GUDANG=:lokasiGudang, DEFAULT_RSC=:defaultRsc, SUPPORT_TO=:supportTo, MAIN_MENU=:mainMenu, " +
                "USER_UPDATE=:userUpdate, DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync " +
                "WHERE KODE_LOCATION=:kodeLocation";
        Map param = new HashMap();
        param.put("kodeLocation", balance.get("kodeLocation"));
        param.put("kodeInisial", balance.get("kodeInisial"));
        param.put("keteranganLokasi", balance.get("keteranganLokasi"));
        param.put("lokasiGudang", balance.get("lokasiGudang"));
        param.put("defaultRsc", balance.get("defaultRsc"));
        param.put("supportTo", balance.get("supportTo"));
        param.put("mainMenu", balance.get("mainMenu"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertUom(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_UOM (KODE_UOM, KETERANGAN_UOM, USER_CREATE, DATE_CREATE, TIME_CREATE, " +
                "USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) VALUES(:kodeUom, :keteranganUom, :userCreate, " +
                ":dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeUom", balance.get("kodeUom"));
        param.put("keteranganUom", balance.get("keteranganUom"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateUom(Map<String, String> balance) {
        String sql = "UPDATE WMS_UOM SET KETERANGAN_UOM=:keteranganUom, USER_UPDATE=:userUpdate, " +
                "DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync WHERE KODE_UOM=:kodeUom";
        Map param = new HashMap();
        param.put("kodeUom", balance.get("kodeUom"));
        param.put("keteranganUom", balance.get("keteranganUom"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertRsc(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_RSC (KODE_RSC, KETERANGAN_RSC, USER_CREATE, DATE_CREATE, TIME_CREATE, " +
                "USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) VALUES(:kodeRsc, :keteranganRsc, :userCreate, " +
                ":dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("keteranganRsc", balance.get("keteranganRsc"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateRsc(Map<String, String> balance) {
        String sql = "UPDATE WMS_RSC SET KETERANGAN_RSC=:keteranganRsc, USER_UPDATE=:userUpdate, " +
                "DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync WHERE KODE_RSC=:kodeRsc";
        Map param = new HashMap();
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("keteranganRsc", balance.get("keteranganRsc"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertRegion(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_REGION (KODE_REGION, KETERANGAN_REGION, USER_CREATE, DATE_CREATE, TIME_CREATE, " +
                "USER_UPDATE, DATE_UPDATE, TIME_UPDATE, STATUS_SYNC) VALUES(:kodeRegion, :keteranganRegion, :userCreate, " +
                ":dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, :statusSync)";
        Map param = new HashMap();
        param.put("kodeRegion", balance.get("kodeRegion"));
        param.put("keteranganRegion", balance.get("keteranganRegion"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateRegion(Map<String, String> balance) {
        String sql = "UPDATE WMS_REGION SET KETERANGAN_REGION=:keteranganRegion, USER_UPDATE=:userUpdate, " +
                "DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync WHERE KODE_REGION=:kodeRegion";
        Map param = new HashMap();
        param.put("kodeRegion", balance.get("kodeRegion"));
        param.put("keteranganRegion", balance.get("keteranganRegion"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertSupplier(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_SUPPLIER (KODE_SUPPLIER, NAMA_SUPPLIER, ALAMAT1, ALAMAT2, KOTA, KODE_POS," +
                " TELPON1, TELPON2, FAX1, FAX2, KIRIM_DATA, ALAMAT_EMAIL, FTP_ADDRESS, FTP_USER, FTP_PASSWORD, " +
                "DEFAULT_GUDANG, KODE_RSC, CONTACT_PERSON, CONTACT_PHONE, TERIMA_CN, STATUS_AKTIF, KETERANGAN," +
                " CAD1, CAD2, USER_CREATE, DATE_CREATE, TIME_CREATE, USER_UPDATE, DATE_UPDATE, TIME_UPDATE," +
                " STATUS_SYNC) VALUES(:kodeSupplier, :namaSupplier, :alamat1, :alamat2, :kota, :kodePos, :telpon1, " +
                ":telpon2, :fax1, :fax2, :kirimData, :alamatEmail, :ftpAddress, :ftpUser, :ftpPassword, " +
                ":defaultGudang, :kodeRsc, :contactPerson, :contactPhone, :terimaCn, :statusAktif, :keterangan, " +
                ":cad1, :cad2, :userCreate, :dateCreate, :timeCreate, :userUpdate, :dateUpdate, :timeUpdate, " +
                ":statusSync)";
        Map param = new HashMap();
        param.put("kodeSupplier", balance.get("kodeSupplier"));
        param.put("namaSupplier", balance.get("namaSupplier"));
        param.put("alamat1", balance.get("alamat1"));
        param.put("alamat2", balance.get("alamat2"));
        param.put("kota", balance.get("kota"));
        param.put("kodePos", balance.get("kodePos"));
        param.put("telpon1", balance.get("telpon1"));
        param.put("telpon2", balance.get("telpon2"));
        param.put("fax1", balance.get("fax1"));
        param.put("fax2", balance.get("fax2"));
        param.put("kirimData", balance.get("kirimData"));
        param.put("alamatEmail", balance.get("alamatEmail"));
        param.put("ftpAddress", balance.get("ftpAddress"));
        param.put("ftpUser", balance.get("ftpUser"));
        param.put("ftpPassword", balance.get("ftpPassword"));
        param.put("defaultGudang", balance.get("defaultGudang"));
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("contactPerson", balance.get("contactPerson"));
        param.put("contactPhone", balance.get("contactPhone"));
        param.put("terimaCn", balance.get("terimaCn"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("keterangan", balance.get("keterangan"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }
    ////////// end insert master branch

    ///////// update master branch
    @Override
    public void updateSupplier(Map<String, String> balance) {
        String sql = "UPDATE WMS_SUPPLIER SET NAMA_SUPPLIER=:namaSupplier, ALAMAT1=:alamat1, ALAMAT2=:alamat2, " +
                "KOTA=:kota, KODE_POS=:kodePos, TELPON1=:telpon1, TELPON2=:telpon2, FAX1=:fax1, FAX2=:fax1, " +
                "KIRIM_DATA=:kirimData, ALAMAT_EMAIL=:alamatEmail, FTP_ADDRESS=:ftpAddress, FTP_USER=:ftpUser, " +
                "FTP_PASSWORD=:ftpPassword, DEFAULT_GUDANG=:defaultGudang, KODE_RSC=:kodeRsc, " +
                "CONTACT_PERSON=:contactPerson, CONTACT_PHONE=:contactPhone, TERIMA_CN=:terimaCn, " +
                "STATUS_AKTIF=:statusAktif, KETERANGAN=:keterangan, CAD1=:cad1, CAD2=:cad2, USER_UPDATE=:userUpdate," +
                " DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate, STATUS_SYNC=:statusSync " +
                "WHERE KODE_SUPPLIER=:kodeSupplier";
        Map param = new HashMap();
        param.put("kodeSupplier", balance.get("kodeSupplier"));
        param.put("namaSupplier", balance.get("namaSupplier"));
        param.put("alamat1", balance.get("alamat1"));
        param.put("alamat2", balance.get("alamat2"));
        param.put("kota", balance.get("kota"));
        param.put("kodePos", balance.get("kodePos"));
        param.put("telpon1", balance.get("telpon1"));
        param.put("telpon2", balance.get("telpon2"));
        param.put("fax1", balance.get("fax1"));
        param.put("fax2", balance.get("fax2"));
        param.put("kirimData", balance.get("kirimData"));
        param.put("alamatEmail", balance.get("alamatEmail"));
        param.put("ftpAddress", balance.get("ftpAddress"));
        param.put("ftpUser", balance.get("ftpUser"));
        param.put("ftpPassword", balance.get("ftpPassword"));
        param.put("defaultGudang", balance.get("defaultGudang"));
        param.put("kodeRsc", balance.get("kodeRsc"));
        param.put("contactPerson", balance.get("contactPerson"));
        param.put("contactPhone", balance.get("contactPhone"));
        param.put("terimaCn", balance.get("terimaCn"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("keterangan", balance.get("keterangan"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("statusSync", balance.get("statusSync"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void insertProduct(Map<String, String> balance) {
        String sql = "INSERT INTO WMS_PRODUCT (KODE_BARANG, NAMA_BARANG, KONVERSI, SATUAN_KECIL, SATUAN_BESAR, " +
                "DEFAULT_GUDANG, DEFAULT_SUPPLIER, FLAG_COM, FLAG_DRY, FLAG_PRD, FLAG_MKT, FLAG_CTR, FLAG_HRA, " +
                "FLAG_FSD, FLAG_IT, OTHERS_1, OTHERS_2, FLAG_CONVERSION, FLAG_RESEP_PRODUKSI, FLAG_EXPIRED, " +
                "FLAG_ALTERNATE, FLAG_COGS, FLAG_BRG_BEKAS, STOCK_OPNAME, WARNING_EXPIRED, MIN_STOCK, MAX_STOCK, " +
                "MIN_ORDER, SATUAN_MIN_ORDER, STATUS_AKTIF, PANJANG, LEBAR, TINGGI, VOLUME, BERAT, LOKASI_BARANG, " +
                "UNIT_PRICE, TIPE_BARANG, KETERANGAN_BRG, DESCR2, GROUP_BARANG, SUB_GROUP, CAD1, CAD2, CAD3, " +
                "USER_CREATE, DATE_CREATE, TIME_CREATE, USER_UPDATE, DATE_UPDATE, TIME_UPDATE) VALUES( :kodeBarang, " +
                ":namaBarang, :konversi, :satuanKecil, :satuanBesar, :defaultGudang, :defaultSupplier, :flagCom, " +
                ":flagDry, :flagPrd, :flagMkt, :flagCtr, :flagHra, :flagFsd, :flagIt, :other1, :other2, " +
                ":flagConversion, :flagResepProduksi, :flagExpired, :flagAlternate, :flagCogs, :flagBrgBekas, " +
                ":stockOpname, :warningExpired, :minStock, :maxStock, :minOrder, :satuanMinOrder, :statusAktif, " +
                ":panjang, :lebar, :tinggi, :volume, :berat, :lokasiBarang, :unitPrice, :tipeBarang, :keteranganBrg, " +
                ":descr2, :groupBarang, :subGroup, :cad1, :cad2, :cad3, :userCreate, :dateCreate, :timeCreate, " +
                ":userUpdate, :dateUpdate, :timeUpdate)";
        Map param = new HashMap();
        param.put("kodeBarang", balance.get("kodeBarang"));
        param.put("namaBarang", balance.get("namaBarang"));
        param.put("konversi", balance.get("konversi"));
        param.put("satuanKecil", balance.get("satuanKecil"));
        param.put("satuanBesar", balance.get("satuanBesar"));
        param.put("defaultGudang", balance.get("defaultGudang"));
        param.put("defaultSupplier", balance.get("defaultSupplier"));
        param.put("flagCom", balance.get("flagCom"));
        param.put("flagDry", balance.get("flagDry"));
        param.put("flagPrd", balance.get("flagPrd"));
        param.put("flagMkt", balance.get("flagMkt"));
        param.put("flagCtr", balance.get("flagCtr"));
        param.put("flagHra", balance.get("flagHra"));
        param.put("flagFsd", balance.get("flagFsd"));
        param.put("flagIt", balance.get("flagIt"));
        param.put("other1", balance.get("other1"));
        param.put("other2", balance.get("other2"));
        param.put("flagConversion", balance.get("flagConversion"));
        param.put("flagResepProduksi", balance.get("flagResepProduksi"));
        param.put("flagExpired", balance.get("flagExpired"));
        param.put("flagAlternate", balance.get("flagAlternate"));
        param.put("flagCogs", balance.get("flagCogs"));
        param.put("flagBrgBekas", balance.get("flagBrgBekas"));
        param.put("stockOpname", balance.get("stockOpname"));
        param.put("warningExpired", balance.get("warningExpired"));
        param.put("minStock", balance.get("minStock"));
        param.put("maxStock", balance.get("maxStock"));
        param.put("minOrder", balance.get("minOrder"));
        param.put("satuanMinOrder", balance.get("satuanMinOrder"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("panjang", balance.get("panjang"));
        param.put("lebar", balance.get("lebar"));
        param.put("tinggi", balance.get("tinggi"));
        param.put("volume", balance.get("volume"));
        param.put("berat", balance.get("berat"));
        param.put("lokasiBarang", balance.get("lokasiBarang"));
        param.put("unitPrice", balance.get("unitPrice"));
        param.put("tipeBarang", balance.get("tipeBarang"));
        param.put("keteranganBrg", balance.get("keteranganBrg"));
        param.put("descr2", balance.get("descr2"));
        param.put("groupBarang", balance.get("groupBarang"));
        param.put("subGroup", balance.get("subGroup"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("cad3", balance.get("cad3"));
        param.put("userCreate", balance.get("userCreate"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", balance.get("dateUpdate"));
        param.put("timeUpdate", balance.get("timeUpdate"));
        param.put("dateCreate", LocalDateTime.now().format(dateFormatter));
        param.put("timeCreate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }
    ////////// end insert master branch

    ///////// update master branch
    @Override
    public void updateProduct(Map<String, String> balance) {
        String sql = "UPDATE WMS_PRODUCT SET NAMA_BARANG=:namaBarang, KONVERSI=:konversi, SATUAN_KECIL=:satuanKecil, " +
                "SATUAN_BESAR=:satuanBesar, DEFAULT_GUDANG=:defaultGudang, DEFAULT_SUPPLIER=:defaultSupplier, " +
                "FLAG_COM=:flagCom, FLAG_DRY=:flagDry, FLAG_PRD=:flagPrd, FLAG_MKT=:flagMkt, FLAG_CTR=:flagCtr, " +
                "FLAG_HRA=:flagHra, FLAG_FSD=:flagFsd, FLAG_IT=:flagIt, OTHERS_1=:other1, OTHERS_2=:other2, " +
                "FLAG_CONVERSION=:flagConversion, FLAG_RESEP_PRODUKSI=:flagResepProduksi, FLAG_EXPIRED=:flagExpired, " +
                "FLAG_ALTERNATE=:flagAlternate, FLAG_COGS=:flagCogs, FLAG_BRG_BEKAS=:flagBrgBekas, " +
                "STOCK_OPNAME=:stockOpname, WARNING_EXPIRED=:warningExpired, MIN_STOCK=:minStock, " +
                "MAX_STOCK=:maxStock, MIN_ORDER=:minOrder, SATUAN_MIN_ORDER=:satuanMinOrder, STATUS_AKTIF=:statusAktif, " +
                "PANJANG=:panjang, LEBAR=:lebar, TINGGI=:tinggi, VOLUME=:volume, BERAT=:berat, LOKASI_BARANG=:lokasiBarang, " +
                "UNIT_PRICE=:unitPrice, TIPE_BARANG=:tipeBarang, KETERANGAN_BRG=:keteranganBrg, DESCR2=:descr2, " +
                "GROUP_BARANG=:groupBarang, SUB_GROUP=:subGroup, CAD1=:cad1, CAD2=:cad2, CAD3=:cad3, " +
                "USER_UPDATE=:userUpdate, DATE_UPDATE=:dateUpdate, TIME_UPDATE=:timeUpdate " +
                "WHERE KODE_BARANG=:kodeBarang";
        Map param = new HashMap();
        param.put("kodeBarang", balance.get("kodeBarang"));
        param.put("namaBarang", balance.get("namaBarang"));
        param.put("konversi", balance.get("konversi"));
        param.put("satuanKecil", balance.get("satuanKecil"));
        param.put("satuanBesar", balance.get("satuanBesar"));
        param.put("defaultGudang", balance.get("defaultGudang"));
        param.put("defaultSupplier", balance.get("defaultSupplier"));
        param.put("flagCom", balance.get("flagCom"));
        param.put("flagDry", balance.get("flagDry"));
        param.put("flagPrd", balance.get("flagPrd"));
        param.put("flagMkt", balance.get("flagMkt"));
        param.put("flagCtr", balance.get("flagCtr"));
        param.put("flagHra", balance.get("flagHra"));
        param.put("flagFsd", balance.get("flagFsd"));
        param.put("flagIt", balance.get("flagIt"));
        param.put("other1", balance.get("other1"));
        param.put("other2", balance.get("other2"));
        param.put("flagConversion", balance.get("flagConversion"));
        param.put("flagResepProduksi", balance.get("flagResepProduksi"));
        param.put("flagExpired", balance.get("flagExpired"));
        param.put("flagAlternate", balance.get("flagAlternate"));
        param.put("flagCogs", balance.get("flagCogs"));
        param.put("flagBrgBekas", balance.get("flagBrgBekas"));
        param.put("stockOpname", balance.get("stockOpname"));
        param.put("warningExpired", balance.get("warningExpired"));
        param.put("minStock", balance.get("minStock"));
        param.put("maxStock", balance.get("maxStock"));
        param.put("minOrder", balance.get("minOrder"));
        param.put("satuanMinOrder", balance.get("satuanMinOrder"));
        param.put("statusAktif", balance.get("statusAktif"));
        param.put("panjang", balance.get("panjang"));
        param.put("lebar", balance.get("lebar"));
        param.put("tinggi", balance.get("tinggi"));
        param.put("volume", balance.get("volume"));
        param.put("berat", balance.get("berat"));
        param.put("lokasiBarang", balance.get("lokasiBarang"));
        param.put("unitPrice", balance.get("unitPrice"));
        param.put("tipeBarang", balance.get("tipeBarang"));
        param.put("keteranganBrg", balance.get("keteranganBrg"));
        param.put("descr2", balance.get("descr2"));
        param.put("groupBarang", balance.get("groupBarang"));
        param.put("subGroup", balance.get("subGroup"));
        param.put("cad1", balance.get("cad1"));
        param.put("cad2", balance.get("cad2"));
        param.put("cad3", balance.get("cad3"));
        param.put("userUpdate", balance.get("userUpdate"));
        param.put("dateUpdate", LocalDateTime.now().format(dateFormatter));
        param.put("timeUpdate", LocalDateTime.now().format(timeFormatter));
        validationService.validate(param);
        jdbcTemplate.update(sql, param);
    }
}
