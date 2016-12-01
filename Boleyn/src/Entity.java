public class Entity {


    double x;
    double y;

    double vx;
    double vy;

    double ax;
    double ay;
 
    double nx;
    double ny;
    double nvx;
    double nvy;
    double nax;
    double nay;
    private double radius;//maybe should be int?
    private double mass;// equal to area
    final double pi = 3.14159265358979323846;
    
    Entity(double x, double y,double vx, double vy, double r){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.setRadiusAndMass(r);
        //this.setMass(mass);
        
    }

    //****************************************************************************
    double getRadius(){
        return this.radius;
    }
    //****************************************************************************
//    void setRadius(double r){
//        this.radius = r;
//    }
    void setRadiusAndMass(double r){
        this.radius = r;
        this.mass = pi * r * r;//a=pi*r^2
    }
    //****************************************************************************
    double getMass(){
        return this.mass;
    }
    //****************************************************************************  
    void setMass(double area){// shouldn't be needed except for when setRadius calls it.. private?
        this.mass = area;
    }
    //****************************************************************************
}
//****************************************************************************
//****************************************************************************