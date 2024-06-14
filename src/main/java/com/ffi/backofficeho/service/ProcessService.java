package com.ffi.backofficeho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffi.backofficeho.dao.ProcessDao;

import java.util.Map;

@Service
public class ProcessService {

    @Autowired
    ProcessDao dao;

    public void insertBranch(Map<String, String> balance) {
        dao.insertBranch(balance);
    }

    public void updateBranch(Map<String, String> balance) {
        dao.updateBranch(balance);
    }

    public void insertArea(Map<String, String> balance) {
        dao.insertArea(balance);
    }

    public void updateArea(Map<String, String> balance) {
        dao.updateArea(balance);
    }

    public void insertCity(Map<String, String> balance) {
        dao.insertCity(balance);
    }

    public void updateCity(Map<String, String> balance) {
        dao.updateCity(balance);
    }

    public void insertLocation(Map<String, String> balance) {
        dao.insertLocation(balance);
    }

    public void updateLocation(Map<String, String> balance) {
        dao.updateLocation(balance);
    }

    public void insertUom(Map<String, String> balance) {
        dao.insertUom(balance);
    }

    public void updateUom(Map<String, String> balance) {
        dao.updateUom(balance);
    }

    public void insertRsc(Map<String, String> balance) { dao.insertRsc(balance); }

    public void updateRsc(Map<String, String> balance) {
        dao.updateRsc(balance);
    }

    public void insertRegion(Map<String, String> balance) { dao.insertRegion(balance); }

    public void updateRegion(Map<String, String> balance) {
        dao.updateRegion(balance);
    }

    public void insertSupplier(Map<String, String> balance) { dao.insertSupplier(balance); }

    public void updateSupplier(Map<String, String> balance) {
        dao.updateSupplier(balance);
    }

    public void insertProduct(Map<String, String> balance) { dao.insertProduct(balance); }

    public void updateProduct(Map<String, String> balance) {
        dao.updateProduct(balance);
    }
    
}
