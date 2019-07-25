package bean;

public class BasicUnit {
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

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Name;
    }

}
