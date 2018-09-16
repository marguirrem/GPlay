package gplay.marlonaguirre.ml.gplay.pojos;

public class Folder {
    private String name;
    private String path;

    public Folder(){

    }
    public Folder(String name,String path){
        this.path = path;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
