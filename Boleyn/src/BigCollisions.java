import java.util.ArrayList;

public class BigCollisions {
    ArrayList<Integer> indexes;
    double mass;
    double vx;
    double vy;
    double x;
    double y;
    double radius;
    final double pi = 3.14159265358979323846;
    
    public BigCollisions(ArrayList<Integer> indexes, double x, double y, double vx, double vy,double mass){
        this.indexes=indexes;
        this.x=x;
        this.y=y;
        this.vx=vx;
        this.vy=vy;
        this.mass=mass;
        this.radius = Math.sqrt(mass / pi);
    }
    
    private void initialMove(){
        
    }
}
