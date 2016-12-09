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
    int color[];
    private static int[][] colors = {{255,255,255},{0,255,255},{30,144,255},{0,0,255},
            {138,43,226},{192,192,192},{0,255,0},{244,164,96},{255,0,0},{255,255,0}};
    
    Entity(double x, double y,double vx, double vy, double r,int size){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
     
        this.nx = x;
        this.ny = y;
        this.nvx = vx;
        this.nvy = vy;
     
        this.setRadiusAndMass(r);
        double ratio = this.getMass()/(size*size);
        //System.out.println(ratio);
        if(ratio<=.00001){
            color = colors[0];
        }
        else if(ratio<=.00004){
            color = colors[1];
        }
        else if(ratio<=.00016){
            color = colors[2];
        }
        else if(ratio<=.00064){
            color = colors[3];
        }
        else if(ratio<=.00256){
            color = colors[4];
        }
        else if(ratio<=.001024){
            color = colors[5];
        }
        else if(ratio<=.04096){
            color = colors[6];
        }
        else if(ratio<=.16384){
            color = colors[7];
        }
        else if(ratio<=.65536){
            color = colors[8];
        }
        else{
            color = colors[9];
        }
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