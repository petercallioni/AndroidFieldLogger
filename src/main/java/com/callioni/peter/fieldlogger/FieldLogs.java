package com.callioni.peter.fieldlogger;

public class FieldLogs {
    private String time;
    private String field;
    private float conductivity;
    private float ph;
    private int moisture;
    private int dissolvedOxygen;

    public FieldLogs(String field, String time, float conductivity, float ph, int moisture, int dissolvedOxygen)
    {
        setTime(time);
        setFieldNumber(field);
        setConductivity(conductivity);
        setPh(ph);
        setMoisture(moisture);
        setDissolvedOxygen(dissolvedOxygen);
    }

    public String getTime() {
        return time;
    }

    private void setTime(String time) {
        this.time = time;
    }

    public String getFieldName() {
        return field;
    }

    private void setFieldNumber(String field) {
        this.field = field;
    }

    public int getDissolvedOxygen() {
        return dissolvedOxygen;
    }

    private void setDissolvedOxygen(int dissolvedOxygen) {
        this.dissolvedOxygen = dissolvedOxygen;
    }

    public float getPh() {
        return ph;
    }

    private void setPh(float ph) {
        this.ph = ph;
    }

    public int getMoisture() {
        return moisture;
    }

    private void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public float getConductivity() {
        return conductivity;
    }

    private void setConductivity(float conductivity) {
        this.conductivity = conductivity;
    }
}
