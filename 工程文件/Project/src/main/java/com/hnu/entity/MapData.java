package com.hnu.entity;


public class MapData {
    private int id;
    private int xCoordinate;
    private int yCoordinate;
    private String description;
    private boolean isFlyable;
    private boolean isParkable;
    private int blockId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFlyable() {
        return isFlyable;
    }

    public void setFlyable(boolean flyable) {
        isFlyable = flyable;
    }

    public boolean isParkable() {
        return isParkable;
    }

    public void setParkable(boolean parkable) {
        isParkable = parkable;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}
