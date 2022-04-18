package at.moritzmusel.cluedo;

public class Network {
    private static Network instance = null;
    private Network(){
    }
    public static Network getInstance(){
        if(instance==null) instance = new Network();
        return instance;
    }


}
