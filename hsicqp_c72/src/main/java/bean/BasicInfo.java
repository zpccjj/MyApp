package bean;

import java.util.List;

public class BasicInfo {
    private List<BasicUnit> CQDW;//产权单位列表

    private List<BasicQP> QpList;//气瓶类型

    private List<BasicUnit> ZZDW;//制造单位列表

    private List<BasicUnit> JYDW;//检验单位列表

    public List<BasicUnit> getCQDW() {
        return CQDW;
    }

    public void setCQDW(List<BasicUnit> cQDW) {
        CQDW = cQDW;
    }

    public List<BasicQP> getQpList() {
        return QpList;
    }

    public void setQpList(List<BasicQP> qpList) {
        QpList = qpList;
    }

    public List<BasicUnit> getZZDW() {
        return ZZDW;
    }

    public void setZZDW(List<BasicUnit> zZDW) {
        ZZDW = zZDW;
    }

    public List<BasicUnit> getJYDW() {
        return JYDW;
    }

    public void setJYDW(List<BasicUnit> jYDW) {
        JYDW = jYDW;
    }


}
