
import java.util.ArrayList;

public class Boleyn {
    
    final static double pi = 3.14159265358979323846;
    static ImageConstruction image;
    static int size;
    static ArrayList<Entity> entities;
    static double timeStep = 1;
    static long graphicsDelay;
    static boolean fill;//false = no fill, true = fill
    
    public static void main(String[] args) {
        KeyboardInputClass input = new KeyboardInputClass();
        size = 800;
        while (true) {
            graphicsDelay = 0;
            fill = false;
            entities = new ArrayList<>();
            size = input.getInteger(true, 800, 0, 5000, "Enter the size for the window (Default is 800):");
            createImage();
            image.displayImage(false, "Simulation", false);
            change(input);
               
            menu(input);
        }
    }
    
    static void menu(KeyboardInputClass input) {
        int exit = 0;
        double panFactorTxt = 1;
        double zoomFactor = 2;
        double panFactor=1;
        char tsc = 'H';
        while (exit == 0) {
            updateImage(0);
            System.out.println("Menu");
            System.out.println("______");
            System.out.println("Iterate:  I (Default)");
            System.out.println("Zoom:     ZI: Zoom In; ZO: Zoom Out");
            System.out.println("Pan:      PU: Pan Up; PD: Pan Down; PL: Pan Left; PR: Pan Right; H: Home");
            System.out.println("Time:     T");
            System.out.println("Change:   C");
            System.out.println("Show:     S");
            System.out.println("Restart:  R");
            System.out.println("Exit:     E");
            String txt = input.getString("I", "");
            txt = txt.toUpperCase();
           
            if (null != txt) {
                switch (txt) {
                    case "I":
                        int numOfIterations = 1; 
                        while(true){
                            numOfIterations = input.getInteger(true, 1, 0, 2147483647,
                                    "Specify number of iterations (0 stops, ENTER steps through)");
                            if(numOfIterations==0){
                                break;
                            }
                            while(numOfIterations>0){
                                iterate();
                                numOfIterations--;
                            }
                        }
                        break;
                    //zoom in
                    case "ZI":
                        image.xLeft+= (image.xRange/4);
                        image.xRight-= (image.xRange/4);
                        image.yBottom+= (image.yRange/4);
                        image.yTop-= (image.yRange/4);
                        image.xRange=image.xRight-image.xLeft;
                        image.yRange=image.yTop-image.yBottom;

//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //zoom out
                    case "ZO":
                        //y=.5yrange
                       image.xLeft-= (.5 * image.xRange);
                        image.xRight+= (.5 * image.xRange);
                        image.yBottom-= (.5 * image.yRange);
                        image.yTop+= (.5 * image.yRange);
                        image.xRange=image.xRight-image.xLeft;
                        image.yRange=image.yTop-image.yBottom;
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        
                        //image.displayImage(false, null, false);
                        break;
                    //pan up
                    case "PU":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY, 
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor=1/panFactorTxt;
                        image.yTop+=(image.yRange/panFactor);
                        image.yBottom+=(image.yRange/panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //pan down
                    case "PD":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY, 
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor=1/panFactorTxt;
                        image.yTop-=(image.yRange/panFactor);
                        image.yBottom-=(image.yRange/panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    case "PL":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY, 
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor=1/panFactorTxt;
                        image.xLeft-=(image.yRange/panFactor);
                        image.xRight-=(image.yRange/panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //pan right
                    case "PR":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY, 
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor=1/panFactorTxt;
                        image.xLeft+=(image.xRange/panFactor);
                        image.xRight+=(image.xRange/panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //time ddouble 
                    case "H":
                        //return pan to home
                        image.xLeft= image.xRange/-2;
                        image.xRight = image.xRange/2;
                        image.yTop= image.yRange/2;
                        image.yBottom = image.yRange/-2;
//                    case "TD":                        
//                        timeStep *= 2;
//                        break;
//                    //time half    
//                    case "TH":
//                        timeStep /= 2;
                        break;
                    case "T":
                        tsc = input.getCharacter(true, 'H', "H, D",1, "Enter 'H' to half the timestep, 'D' to double. Default is: '" + tsc + "', current timestep is: " + timeStep);
                        if(tsc=='D'){
                            timeStep*=2;
                        }else if(tsc=='H'){
                            timeStep/=2;
                        }
                        break;
                    //change
                    case "C":
                        change(input);
                        break;
                    //restart
                    case "S":
                        show();
                        break;
                    case "R":
                        exit = 1;
                        //image.clearImage(0, 0, 0);
                        image.closeDisplay();
                        break;
                    case "E":
                        System.exit(0);
                        break;
                    default:
                        System.out.println(txt + " is not a valid entry, try again");
                        break;
                }
            }
            //updateImage();
        }
    }
    
    static void pan(String direction, KeyboardInputClass input){
        input.getDouble(true, 1, .00000001, Double.POSITIVE_INFINITY, direction);
        switch(direction){
            case "PU":
            case "PD":
            case "PL":
            case "PR":
        }
    }

    static void change(KeyboardInputClass input) {
        int exit = 0;
        int menu = 2;
        while (exit == 0) {
            double x = 0;
            double y = 0;
            double vx = 0;
            double vy = 0;
            double r = 0;
            System.out.println("Press 0 to stop entering entities");
            System.out.println("Press 1 to randomize a new entity");
            System.out.println("Press 2 to select a situation");
            System.out.println("Press 3 to specify your own coordinates");
            System.out.println("Press 4 to specify the time delay");
            System.out.println("Press 5 for extras");
            menu = input.getInteger(true, menu, 0, 5, "Default is: " + menu);
            switch (menu) {
                case 0:
                    if (entities.isEmpty()) {
                        System.out.println("Error, there must be at least 1 entity, try again.");
                    } else {
                        exit = 1;
                        break;
                    }//range with defaults.. for max radius etc.. 1/10th size of window
                case 1:
                    int numRandEntities = input.getInteger(true, 1, 0, 1000000, "How many random entities? (Default is 1):");
                    while (numRandEntities > 0) {
                        x =  ((Math.random() * image.xRange) + image.xLeft);
                        y =  ((Math.random() * image.yRange) + image.yBottom);
                        //System.out.println("X is: " + x + ", Y is: " + y);
                        vx =  (Math.random() * size) - (size / 2);
                        vy =  (Math.random() * size) - (size / 2);
                        r =  (Math.random() * size) / 10;//max 
                        entities.add(new Entity(x, y, vx, vy, r,size));
                        numRandEntities--;
                    }
                    break;
                case 2:
                    presets(input);
                    break;
                case 3:
                    x = input.getDouble(true, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "Enter an x coordinate (Default is 0):");
                    y = input.getDouble(true, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "Enter a y coordinate (Default is 0):");
                    vx = input.getDouble(true, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "Enter velocity on the x axis (Default is 0):");
                    vy = input.getDouble(true, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "Enter velocity on the y axis (Default is 0):");
                    r = input.getDouble(true, 10, 1, Double.POSITIVE_INFINITY, "Enter the radius (Default is 10):");
                    entities.add(new Entity(x, y, vx, vy, r,size));
                    break;
                case 4:
                    graphicsDelay = (long)(input.getDouble(true, graphicsDelay, 0, 2147483647,
                            "Enter the graphics dealy in milliseconds (Default is 0):"));
                    break;
                case 5:
                    extras(input);
            }
            updateImage(0);
        }
    }
    //****************************************************************************

    static void createImage() {
        image = new ImageConstruction(size, size, size/-2, size/2, size/-2, size/2, 1);
        image.displaySetup();
    }
    
    static void show() {   
        System.out.printf("%12s%10s%10s%10s%10s%10s\n","X","Y","VX","VY","AX","AY");
        //System.out.printf("%d.\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n",i, a.x, a.y, a.vx, a.vy, a.ax, a.ay);
        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            //System.out.println(i+".\t"+a.x+"\t"+a.y+"\t"+a.vx+"\t"+a.vy+"\t"+a.ax+"\t"+a.ay);    
            System.out.printf("%d.%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f\n",i, a.x, a.y, a.vx, a.vy, a.ax, a.ay);
        }
    }
    //****************************************************************************

    static void iterate() {
            for (int i = 0; i < entities.size(); i++) {
                double accel[] = {0,0};
                for (int j = i+1; j < entities.size(); j++) {
                    double newAcceli[] = computeNewAcceleration(entities.get(i), entities.get(j), 1);
                    double newAccelj[] = computeNewAcceleration(entities.get(j), entities.get(i), 1);
                    //System.out.println("newAccel = " + newAccel[0] + " " + newAccel[1]);
                    entities.get(i).nax+=newAcceli[0];
                    entities.get(i).nay+=newAcceli[1];
                    entities.get(j).nax+=newAccelj[0];
                    entities.get(j).nay+=newAccelj[1];
                }
                //this is where I should compute new location?
                //entities.get(i).nax=accel[0];
                //entities.get(i).nay=accel[1];
                computeNewVelocity(entities.get(i));
                computeNewPosition(entities.get(i));
            }
            for (int i = 0; i < entities.size(); i++) {
                setNewValuesAsOldValues(entities.get(i));
            }
            updateImage(1);
        
    }
    
    static void setNewValuesAsOldValues(Entity entity1){
        entity1.x = entity1.nx;
        entity1.y = entity1.ny;
        entity1.ax = entity1.nax;
        entity1.ay = entity1.nay;
        entity1.vx = entity1.nvx;
        entity1.vy = entity1.nvy;
        //System.out.println("Set new as old");
        //System.out.println("entity.vy:" + entity1.vy);
        entity1.nx=0;
        entity1.ny=0;
        entity1.nvx=0;
        entity1.nvy=0;
        entity1.nax=0;
        entity1.nay=0;
        //System.out.println("entity.vy:" + entity1.vy);
        
    }
    
    
    static double[] computeNewAcceleration(Entity eMove, Entity eStatic, int g){//entity eMove being acted on by entity eStatic
        double magnitude = g * eStatic.getMass()/distanceBetweenPoints(eMove, eStatic); //distance
        //System.out.println("magnitude: " + magnitude);
        //double slope = (eStatic.y-eMove.y)/(eStatic.x-eMove.x);// probs useless
        Angles angleObject = new Angles();
        double angle = angleObject.computeVectorAngle(eMove.x,eMove.y,eStatic.x,eStatic.y);//I think this is backwards
        //then multiply the magnitude of the acceleration times the cosine and
        //sine of that angle to get the acceleration components in the x and y directions, respectively
        //System.out.println("angle: "+ angle);
        double xAccel = Math.cos(angle)*magnitude;
        double yAccel = Math.sin(angle)*magnitude;
        double[] accel = {xAccel, yAccel};
        return accel;
        //consideration in the x direction is ax = aax + abx + acx 
    }
    
    
    static double distanceBetweenPoints(Entity entity1, Entity entity2){
        double xDistance = (entity2.x-entity1.x);
        xDistance = xDistance * xDistance;
        double yDistance = (entity2.y-entity1.y);
        yDistance = yDistance * yDistance;
        return Math.sqrt(xDistance + yDistance);
        
    }
    
     static void computeNewPosition(Entity entity){
        entity.nx=entity.x+(entity.vx*timeStep)+(.5*entity.nax*timeStep*timeStep);
        entity.ny=entity.y+(entity.vy*timeStep)+(.5*entity.nay*timeStep*timeStep);   
    }
     
     static void computeNewVelocity(Entity entity){
         entity.nvx = entity.vx+(entity.nax*timeStep);
         entity.nvy = entity.vy+(entity.nay*timeStep);
         //System.out.println("Compute new velocity");
         //System.out.println("Entity velocity is: " + entity.nvx + " & " + entity.nvy);
     }
     
     static void presets(KeyboardInputClass input){
         double x;
         double y;
         double vx;
         double vy;
         double r;
         System.out.println("Presets: Default is 1");
         System.out.println("1. Quadrants");
         System.out.println("2. Planets?");
         int preset = input.getInteger(true, 1, 1, 2, "");
         switch(preset){
             case 1:
                 int p = size/4;
                 entities.add(new Entity(p, p, 0, 0, p/4,size));
                 entities.add(new Entity(-1*p, p, 0, 0, p/4,size));
                 entities.add(new Entity(p, -1*p, 0, 0, p/4,size));
                 entities.add(new Entity(-1*p, -1*p, 0, 0, p/4,size));
                 break;
             case 2:
                 //entities.add(new Entity(x, y, vx, vy, r))
             case 3:
         }
     }
     
     static void extras(KeyboardInputClass input){
         System.out.println("Extras: Default is 1");
         if(!fill){
             System.out.println("1. Fill the entities");
         } else{
             System.out.println("1. Don't fill the entities");
         }
         input.getInteger(true, 1, 1, 1, "");
         if(!fill){
             fill=true;
         } else{
             fill=false;
         }
     }
    
    static void updateImage(int isIterate) {
        image.clearImage(0, 0, 0);
        for (int i = 0; i < entities.size(); i++) {
            drawEntity(entities.get(i));
        }
        if(isIterate==1){
            try {                                 //wait a moment for smoother graphics
                Thread.sleep(graphicsDelay);
            }
            catch (Exception e) {
            }
        }
        image.setPixelValues();
    }

    //****************************************************************************
    static void drawEntity(Entity entity) {
        image.insertCircle(entity.x, entity.y, entity.getRadius(), entity.color[0], entity.color[1], entity.color[2], fill);
    }
}
//****************************************************************************
//****************************************************************************

