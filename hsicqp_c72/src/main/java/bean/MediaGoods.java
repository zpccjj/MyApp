package bean;

import java.util.ArrayList;
import java.util.List;

public class MediaGoods {
    private String MediaCode;//介质代码
    private String MediaName;
    private List<QPGoods> Goods = new ArrayList<QPGoods>();//商品代码
    public String getMediaCode() {
        return MediaCode;
    }
    public void setMediaCode(String mediaCode) {
        MediaCode = mediaCode;
    }
    public String getMediaName() {
        return MediaName;
    }
    public void setMediaName(String mediaName) {
        MediaName = mediaName;
    }
    public List<QPGoods> getGoods() {
        return Goods;
    }
    public void setGoods(List<QPGoods> goods) {
        Goods = goods;
    }
    @Override
    public String toString(){
        return MediaName;
    }

}
