package com.hnu.entity;

import javax.xml.crypto.Data;
import java.awt.*;

public class imageData {
    private int deviceid;
    private Data backtime;
    private Image image;
    private int imid;

    public int getDeviceid() {
        return deviceid;
    }

    public Data getBacktime() {
        return backtime;
    }

    public Image getImage() {
        return image;
    }

    public int getImid() {
        return imid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setBacktime(Data backtime) {
        this.backtime = backtime;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImid(int imid) {
        this.imid = imid;
    }
}
