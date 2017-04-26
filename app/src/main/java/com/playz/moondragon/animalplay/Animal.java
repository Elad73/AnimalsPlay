package com.playz.moondragon.animalplay;

/**
 * Created by Elad on 24/04/2017.
 */

public class Animal {

    private String _name;
    private String _imagePath;
    private String _soundPath;
    protected String _type;
    private  String _objectKey;


   /* public Animal(String imagePath, String soundPath) {
        _imagePath = imagePath;
        _soundPath = soundPath;
        _name = extractNameFromPath();
        _objectKey = setKey();
    }*/

    public Animal(String animalName, String animalType) {
        _name = animalName;
        _type = animalType;
    }

    public String getKey() { return  _objectKey;}

    public String getImagePath() {
        return _imagePath;
    }

    public String getType() { return _type; }

    public String getName() { return _name; }

    private String setKey() { return getName() + getType(); }

    public String getSoundPath() {
        return _soundPath;
    }

    public void setImagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }

    public void setSoundPath(String _soundPath) {
        this._soundPath = _soundPath;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    private String extractNameFromPath() {
        return  getImagePath().substring(getImagePath().indexOf('-') + 1).replace('_',' ').replace(".png", "");
    }

    public String getAssetsImageFolder() {
        return getImagePath().substring(0, getImagePath().indexOf('/'));
    }
}

