package com.ffi.backofficehq.dao;

import java.util.Map;

public interface ProcessDao {

    //////////// CRUD master
    public void insertBranch(Map<String, String> mapping);

    public void updateBranch(Map<String, String> mapping);

    public void insertArea(Map<String, String> mapping);

    public void updateArea(Map<String, String> mapping);

    public void insertCity(Map<String, String> mapping);

    public void updateCity(Map<String, String> mapping);

    public void insertLocation(Map<String, String> mapping);

    public void updateLocation(Map<String, String> mapping);

    public void insertUom(Map<String, String> mapping);

    public void updateUom(Map<String, String> mapping);

    public void insertRsc(Map<String, String> mapping);

    public void updateRsc(Map<String, String> mapping);

    public void insertRegion(Map<String, String> mapping);

    public void updateRegion(Map<String, String> mapping);

    public void insertSupplier(Map<String, String> mapping);

    public void updateSupplier(Map<String, String> mapping);

    public void insertProduct(Map<String, String> mapping);

    public void updateProduct(Map<String, String> mapping);

    //////////// end CRUD master
}
