package com.example.marek.zstv3;


import java.io.Serializable;

public class RequestModel implements Serializable {
    private Header header;
    private String machineAddress;
    private String communityRead;
    private SystemField systemField;
    private TableType tableType;

    public RequestModel(Header header, String machineAddress, String communityRead,SystemField systemField,TableType tableType)
    {
        this.setHeader(header);
        this.setMachineAddress(machineAddress);
        this.setCommunityRead(communityRead);
        this.setSystemField(systemField);
        this.setTableType(tableType);
    }
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
    public String getMachineAddress() {
        return machineAddress;
    }
    public void setMachineAddress(String machineAddress) {
        this.machineAddress = machineAddress;
    }
    public String getCommunityRead() {
        return communityRead;
    }
    public void setCommunityRead(String communityRead) {
        this.communityRead = communityRead;
    }
    public SystemField getSystemField() {
        return systemField;
    }
    public void setSystemField(SystemField systemField) {
        this.systemField = systemField;
    }
    public TableType getTableType() {
        return tableType;
    }
    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }


}