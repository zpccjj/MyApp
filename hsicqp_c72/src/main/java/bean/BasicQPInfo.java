package bean;

import java.util.List;

public class BasicQPInfo{
    private String Id;
    private String Name;
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    private List<BasicQPInfo2> list;

    public List<BasicQPInfo2> getList() {
        return list;
    }

    public void setList(List<BasicQPInfo2> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Name;
    }
}
