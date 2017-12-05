package RollCallTelBot;

import java.util.ArrayList;

public class ChanGroup {

  private String name;
  private String id;

    public String getName() {
        return name;
    }

    public String getId(){
        return id;
    }

    public ChanGroup(String name, String id){
        this.name = name;
        this.id = id;
    }
    ArrayList<ClassSession> sessions = new ArrayList<ClassSession>();
}
