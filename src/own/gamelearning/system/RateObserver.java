package own.gamelearning.system;

/**
 * Created by 挨踢狗 on 2017/4/25.
 */
public class RateObserver {

    private long current;
    private long last;
    private long delay;
    private int count;
    private int rate;

    public void initialize(){
        last = System.currentTimeMillis();
        delay = 0;
        rate = 0;
    }

    public void calculate(){
        current = System.currentTimeMillis();
        delay += current - last;
        last = current;
        count ++;
        if(delay > 1000){
            delay -= 1000;
            rate = count;
            count = 0;
        }
    }

    public String getRate(){
        return "FPS " + rate;
    }
}
