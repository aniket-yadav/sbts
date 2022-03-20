package com.app.sbts.models;

public class Bus {
    String vehicleName;
    String vehicleModel;
    String vehicleCapacity;
    String vehicleColor;
    String vehicleNo;
    String vehicleDriver;

    public Bus() {
    }

    public Bus(String vehicleName, String vehicleModel, String vehicleCapacity, String vehicleColor, String vehicleNo, String vehicleDriver) {
        this.vehicleName = vehicleName;
        this.vehicleModel = vehicleModel;
        this.vehicleCapacity = vehicleCapacity;
        this.vehicleColor = vehicleColor;
        this.vehicleNo = vehicleNo;
        this.vehicleDriver = vehicleDriver;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleDriver() {
        return vehicleDriver;
    }

    public void setVehicleDriver(String vehicleDriver) {
        this.vehicleDriver = vehicleDriver;
    }




}
