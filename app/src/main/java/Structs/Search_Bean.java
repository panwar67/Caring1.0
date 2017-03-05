package Structs;

/**
 * Created by Panwar on 05/03/17.
 */
public class Search_Bean
{

    private  String TAG ;
    private String coloumn;
    private String type;

    public String getColoumn() {
        return this.coloumn;
    }

    public String getTAG() {
        return this.TAG;
    }
    public String getType() {
        return this.type;
    }
    public void setColoumn(String coloumn) {
        this.coloumn = coloumn;
    }
    public void setTAG(String TAG) {
        this.TAG = TAG;
    }
    public void setType(String type) {
        this.type = type;
    }
}
