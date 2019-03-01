package bean;

import java.util.List;

public class Weather {
    private String status;
    private int count;
    private String info;
    private int infocode;
    private List<WeatherLives> lives;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getInfocode() {
        return infocode;
    }

    public void setInfocode(int infocode) {
        this.infocode = infocode;
    }

    public List<WeatherLives> getLives() {
        return lives;
    }

    public void setLives(List<WeatherLives> lives) {
        this.lives = lives;
    }
}
