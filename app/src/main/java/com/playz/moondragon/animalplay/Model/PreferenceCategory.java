package com.playz.moondragon.animalplay.Model;

/**
 * Created by Elad on 04/05/2017.
 */

public class PreferenceCategory {

    private Long defaultValue;
    private String entries;
    private String entryValues;
    private Boolean persistent;
    private String summary;
    private String title;

    public Long getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Long defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    public String getEntryValues() {
        return entryValues;
    }

    public void setEntryValues(String entryValues) {
        this.entryValues = entryValues;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getDefaultValue() + " " + getTitle() + " " +  getSummary() + " " +
                getEntries() + " " +  getEntryValues() + " " +  getPersistent();
    }
}
