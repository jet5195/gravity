
import java.util.ArrayList;
import java.util.Collections;

public class Boleyn {

    final static double pi = 3.14159265358979323846;
    static ImageConstruction image;
    static int size;
    static ArrayList<Entity> entities;
    static double timeStep = 1;
    static long graphicsDelay;
    static boolean fill;//false = no fill, true = fill
    static ArrayList<Collision> collisions = new ArrayList();

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
        double panFactor = 1;
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
                        while (true) {
                            numOfIterations = input.getInteger(true, 1, 0, 2147483647,
                                    "Specify number of iterations (0 stops, ENTER steps through)");
                            if (numOfIterations == 0) {
                                break;
                            }
                            while (numOfIterations > 0) {
                                iterate();
                                numOfIterations--;
                            }
                        }
                        break;
                    //zoom in
                    case "ZI":
                        image.xLeft += (image.xRange / 4);
                        image.xRight -= (image.xRange / 4);
                        image.yBottom += (image.yRange / 4);
                        image.yTop -= (image.yRange / 4);
                        image.xRange = image.xRight - image.xLeft;
                        image.yRange = image.yTop - image.yBottom;

//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //zoom out
                    case "ZO":
                        //y=.5yrange
                        image.xLeft -= (.5 * image.xRange);
                        image.xRight += (.5 * image.xRange);
                        image.yBottom -= (.5 * image.yRange);
                        image.yTop += (.5 * image.yRange);
                        image.xRange = image.xRight - image.xLeft;
                        image.yRange = image.yTop - image.yBottom;
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);

                        //image.displayImage(false, null, false);
                        break;
                    //pan up
                    case "PU":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.yTop += (image.yRange / panFactor);
                        image.yBottom += (image.yRange / panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //pan down
                    case "PD":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.yTop -= (image.yRange / panFactor);
                        image.yBottom -= (image.yRange / panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    case "PL":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.xLeft -= (image.yRange / panFactor);
                        image.xRight -= (image.yRange / panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //pan right
                    case "PR":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.xLeft += (image.xRange / panFactor);
                        image.xRight += (image.xRange / panFactor);
//                        System.out.println("Top: " + image.yTop + " , Bottom: " + image.yBottom);
//                        System.out.println("Left: " + image.xLeft + " , Right: " + image.xRight);
                        break;
                    //time ddouble 
                    case "H":
                        //return pan to home
                        image.xLeft = image.xRange / -2;
                        image.xRight = image.xRange / 2;
                        image.yTop = image.yRange / 2;
                        image.yBottom = image.yRange / -2;
//                    case "TD":                        
//                        timeStep *= 2;
//                        break;
//                    //time half    
//                    case "TH":
//                        timeStep /= 2;
                        break;
                    case "T":
                        tsc = input.getCharacter(true, 'H', "H, D", 1, "Enter 'H' to half the timestep, 'D' to double. Default is: '" + tsc + "', current timestep is: " + timeStep);
                        if (tsc == 'D') {
                            timeStep *= 2;
                        } else if (tsc == 'H') {
                            timeStep /= 2;
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

    static void pan(String direction, KeyboardInputClass input) {
        input.getDouble(true, 1, .00000001, Double.POSITIVE_INFINITY, direction);
        switch (direction) {
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
                        x = ((Math.random() * image.xRange) + image.xLeft);
                        y = ((Math.random() * image.yRange) + image.yBottom);
                        //System.out.println("X is: " + x + ", Y is: " + y);
                        vx = ((Math.random() * size) - (size / 2)) / 10;
                        vy = ((Math.random() * size) - (size / 2) / 10);
                        r = (Math.random() * size) / 10;//max 
                        entities.add(new Entity(x, y, vx, vy, r, size));
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
                    entities.add(new Entity(x, y, vx, vy, r, size));
                    break;
                case 4:
                    graphicsDelay = (long) (input.getDouble(true, graphicsDelay, 0, 2147483647,
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
        image = new ImageConstruction(size, size, size / -2, size / 2, size / -2, size / 2, 1);
        image.displaySetup();
    }

    static void show() {
        System.out.printf("%12s%10s%10s%10s%10s%10s%10s%10s\n", "R", "Mass", "X", "Y", "VX", "VY", "AX", "AY");
        //System.out.printf("%d.\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n",i, a.x, a.y, a.vx, a.vy, a.ax, a.ay);
        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            //System.out.println(i+".\t"+a.x+"\t"+a.y+"\t"+a.vx+"\t"+a.vy+"\t"+a.ax+"\t"+a.ay);    
            System.out.printf("%d.%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f\n",
                    i, a.getRadius(), a.getMass(), a.x, a.y, a.vx, a.vy, a.ax, a.ay);
        }
    }
    //****************************************************************************

    static void iterate() {
        for (int i = 0; i < entities.size(); i++) {
            double accel[] = {0, 0};
            for (int j = i + 1; j < entities.size(); j++) {
                double newAcceli[] = computeNewAcceleration(entities.get(i), entities.get(j), 1);
                double newAccelj[] = computeNewAcceleration(entities.get(j), entities.get(i), 1);
                //System.out.println("newAccel = " + newAccel[0] + " " + newAccel[1]);
                entities.get(i).nax += newAcceli[0];
                entities.get(i).nay += newAcceli[1];
                entities.get(j).nax += newAccelj[0];
                entities.get(j).nay += newAccelj[1];
            }
            //this is where I should compute new location?
            //entities.get(i).nax=accel[0];
            //entities.get(i).nay=accel[1];
            computeNewVelocity(entities.get(i));
            computeNewPosition(entities.get(i));
        }
        collisions = new ArrayList();
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                computeCollisions(i, j);
            }

        }
//        for (int i = 0; i < newCollisions.size(); i++) {
//            collide(newCollisions.get(i));
//
//        }
        if (!collisions.isEmpty()) {
            newCollide();
        }

        for (int i = 0; i < entities.size(); i++) {
            setNewValuesAsOldValues(entities.get(i));
        }
        updateImage(1);

    }

    static void setNewValuesAsOldValues(Entity entity1) {
        entity1.x = entity1.nx;
        entity1.y = entity1.ny;
        entity1.ax = entity1.nax;
        entity1.ay = entity1.nay;
        entity1.vx = entity1.nvx;
        entity1.vy = entity1.nvy;
        //System.out.println("Set new as old");
        //System.out.println("entity.vy:" + entity1.vy);
        entity1.nx = 0;
        entity1.ny = 0;
        entity1.nvx = 0;
        entity1.nvy = 0;
        entity1.nax = 0;
        entity1.nay = 0;
        //System.out.println("entity.vy:" + entity1.vy);

    }

    static double[] computeNewAcceleration(Entity eMove, Entity eStatic, int g) {//entity eMove being acted on by entity eStatic
        double magnitude = 1;
        double distance = distanceBetweenPoints(eMove.x, eMove.y, eStatic.x, eStatic.y);
        if (distance != 0) {//didn't fix NaN
            magnitude = g * eStatic.getMass() / distance; //distance
        }
        //System.out.println("magnitude: " + magnitude);
        //double slope = (eStatic.y-eMove.y)/(eStatic.x-eMove.x);// probs useless
        Angles angleObject = new Angles();
        double angle = angleObject.computeVectorAngle(eMove.x, eMove.y, eStatic.x, eStatic.y);//I think this is backwards
        //then multiply the magnitude of the acceleration times the cosine and
        //sine of that angle to get the acceleration components in the x and y directions, respectively
        //System.out.println("angle: "+ angle);
        double xAccel = Math.cos(angle) * magnitude;
        double yAccel = Math.sin(angle) * magnitude;
        double[] accel = {xAccel, yAccel};
        return accel;
        //consideration in the x direction is ax = aax + abx + acx 
    }

    static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
//        System.out.println("x1\ty1\tx2\ty2\t");
//        System.out.println(x1 +  "\t" +y1 + "\t" +x2 + "\t" +y2);

        double a = (x2 - x1);
        //System.out.println("x distance is: " + a);
        a = a * a;
        double b = (y2 - y1);
        //System.out.println("y distance is: " + b);
        b = b * b;
        return Math.sqrt(a + b);

    }

    static void computeNewPosition(Entity entity) {
        entity.nx = entity.x + (entity.vx * timeStep) + (.5 * entity.nax * timeStep * timeStep);
        entity.ny = entity.y + (entity.vy * timeStep) + (.5 * entity.nay * timeStep * timeStep);
    }

    static void computeNewVelocity(Entity entity) {
        entity.nvx = entity.vx + (entity.nax * timeStep);
        entity.nvy = entity.vy + (entity.nay * timeStep);
        //System.out.println("Compute new velocity");
        //System.out.println("Entity velocity is: " + entity.nvx + " & " + entity.nvy);
    }

    static void presets(KeyboardInputClass input) {
        double x;
        double y;
        double vx;
        double vy;
        double r;
        System.out.println("Presets: Default is 1");
        System.out.println("1. Quadrants");
        System.out.println("2. Planets?");
        int preset = input.getInteger(true, 1, 1, 2, "");
        switch (preset) {
            case 1:
                int p = size / 4;
                entities.add(new Entity(-1 * p, p, 0, 0, p / 4, size));
                entities.add(new Entity(p, p, 0, 0, p / 4, size));
                entities.add(new Entity(p, -1 * p, 0, 0, p / 4, size));
                entities.add(new Entity(-1 * p, -1 * p, 0, 0, p / 4, size));
                break;
            case 2:
            //entities.add(new Entity(x, y, vx, vy, r))
            case 3:
        }
    }

    static void extras(KeyboardInputClass input) {
        System.out.println("Extras: Default is 1");
        if (!fill) {
            System.out.println("1. Fill the entities");
        } else {
            System.out.println("1. Don't fill the entities");
        }
        input.getInteger(true, 1, 1, 1, "");
        if (!fill) {
            fill = true;
        } else {
            fill = false;
        }
    }

    static void updateImage(int isIterate) {
        image.clearImage(0, 0, 0);
        for (int i = 0; i < entities.size(); i++) {
            drawEntity(entities.get(i));
        }
        if (isIterate == 1) {
            try {                                 //wait a moment for smoother graphics
                Thread.sleep(graphicsDelay);
            } catch (Exception e) {
            }
        }
        image.setPixelValues();
    }

    //must be called B4 setting all old variables as the curretn new variables
    static void computeCollisions(int i, int j) {
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);
        double variance = .005;//amount of... for a slope to be the same
        double slope1 = computeSlope(entity1);
        double slope2 = computeSlope(entity2);

        case3(i, j, slope1, slope2);
//        if (Math.abs(slope1 - slope2) < variance) {
//            System.out.println("/////////////////////////////////");
//            System.out.println("case1");
//            System.out.println("i:" + i + ", j: " + j);
//            case1(i, j, slope1, slope2);//trajectories intersect
//
//        } else {
//            System.out.println("case2");
//            case2(i, j, slope1, slope2);
//
//        }
    }

    static void case1(int i, int j, double slope1, double slope2) {//different slopes
        //two entities collide if both of them are completely 
        //contained at the same time within a circular area of 
        //radius r1+r2 centered at the intersection point 
        //(where r1 and r2 are the radii of the two bodies).
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);
        //double radius = entity1.getRadius() + entity2.getRadius();
        //double area = pi * radius * radius;
        double x = ((slope1 * entity1.x) - (slope2 * entity2.x) + entity2.y - entity1.y) / (slope1 - slope2);
        if (x <= .000001 || x > .000001) {
            x = 0;
        }
        System.out.println(i + " slope is: " + slope1);
        System.out.println(j + " slope is: " + slope2);
        double y = slope1 * (x - entity1.x) + entity1.y;
        if (y <= .000001 || y > .000001) {
            y = 0;
        }

        // image.insertCircle(x, y, entity1.getRadius()+entity2.getRadius(),
        //        255, 255, 255, true);
        //image.setPixelValues();
        System.out.println("The intersection of " + i + " & " + j);
        System.out.println("x is: " + x);
        System.out.println("y is: " + y);
        //pythag theorem
        //double a = y - entity1.y;
        //double b = x - entity1.x;
        //double d1 = Math.sqrt((a * a) + (b * b));
        double d1x = entity1.x - x;
        double d1y = entity1.y - y;
        System.out.println("d1x is: " + d1x);
        System.out.println("d1y is: " + d1y);
        //a = y - entity2.y;
        //b = x - entity2.x;
        //double d2 = Math.sqrt((a * a) + (b * b));
        double d2x = entity2.x - x;
        double d2y = entity2.y - y;

        System.out.println("d2x is: " + d2x);
        System.out.println("d2y is: " + d2y);

        //double d1in = d1 - entity2.getRadius();
        double d1inX = Math.abs(d1x) - entity2.getRadius();
        double d1inY = Math.abs(d1y) - entity2.getRadius();
        System.out.println("d1inX: " + d1inX);
        System.out.println("d1inY: " + d1inY);

        //double d1out = d1 + entity2.getRadius();
        double d1outX = Math.abs(d1x) + entity2.getRadius();
        double d1outY = Math.abs(d1y) + entity2.getRadius();
        System.out.println("d1outX: " + d1outX);
        System.out.println("d1outY: " + d1outY);

        //double d2in = d2 - entity1.getRadius();
        double d2inX = Math.abs(d2x) - entity1.getRadius();
        double d2inY = Math.abs(d2y) - entity1.getRadius();
        System.out.println("d2inX: " + d2inX);
        System.out.println("d2inY: " + d2inY);

        //double d2out = d2 - entity1.getRadius();
        double d2outX = Math.abs(d2x) + entity1.getRadius();
        double d2outY = Math.abs(d2y) + entity1.getRadius();
        System.out.println("d2outX: " + d2outX);
        System.out.println("d2outY: " + d1outY);

        //dx = vxt + ½axt2 and dy = vyt + ½ayt2 
        //rewrite
        //½axt2 + vxt – dx = 0 and ½ayt2 + vyt – dy = 0 
        //do this for entity1 and entity2, maybe make this a method that passes in an entity
        double t1in = computeTime(entity1, d1inX, d1inY);
        double t1out = computeTime(entity1, d1outX, d1outY);
        double t2in = computeTime(entity2, d2inX, d2inY);
        double t2out = computeTime(entity2, d2outX, d2outY);

        //return true? if there is a collision maybe?
        if (((t1in >= t2in && t1out <= t2out) || (t2in >= t1in && t2out <= t1out)) && (t1in < timeStep && t2in < timeStep)) {
            System.out.println("THERE IS A COLLISION OMG");
            collisions.add(new Collision(i, j, x, y));
            //collide(i, j, x, y);
        }
        double timeAtWhichCollisionOccurs;
        if (t1in > t2in) {
            timeAtWhichCollisionOccurs = t1in;

        } else {
            timeAtWhichCollisionOccurs = t2in;
        }
        System.out.println("t1in:" + t1in);
        System.out.println("t1out:" + t1out);
        System.out.println("t2in:" + t2in);
        System.out.println("t2out:" + t2out);

        //one of these has to be true right? the inner 
    }

    static void case2(int i, int j, double slope1, double slope2) {
        //do case 2.. if slope is less than epsilon
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);
        double sumOfRadii = entity1.getRadius() + entity2.getRadius();
        //now compute the distance between
        //compute distance between the two trajectories
        //no idea what this means

    }

    static boolean case3(int i, int j, double slope1, double slope2) {
        //double ts = .1;
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);
        double combinedRadii = entity1.getRadius() + entity2.getRadius();
        double entity1x = entity1.x;
        double entity1y = entity1.y;
        double entity2x = entity2.x;
        double entity2y = entity2.y;

        //double a = entity1y - entity2y;
        //double b = entity1x - entity2x;
        //double distanceBetween = Math.sqrt(a * a + b * b);
        double distanceBetween = distanceBetweenPoints(entity1x, entity1y, entity2x, entity2y);

        double x = 0;
        double y = 0;
        //double x = (entity1x+entity2x)/2;
        //double y = (entity1y+entity2y)/2;
        //if there is already a collision
        if (distanceBetween <= combinedRadii) {
            //
            System.out.println("There was a collision at ts: " + 0);
            x = ((slope1 * entity1x) - (slope2 * entity2x) + entity2y - entity1y) / (slope1 - slope2);
            if (x <= .000001 && x > -.000001) {
                x = 0;
            }
            System.out.println(i + " slope is: " + slope1);
            System.out.println(j + " slope is: " + slope2);
            y = slope1 * (x - entity1.x) + entity1.y;
            if (y <= .000001 && y > -.000001) {
                y = 0;
            }
            System.out.println("The intersection of " + i + " & " + j);
            System.out.println("x is: " + x);
            System.out.println("y is: " + y);
            //
            //collide(i, j, x, y);

            collisions.add(new Collision(i, j, x, y));
            return true;
        }
        //if there isn't already a collision, loop through until you find one or get out of the timestep
        for (double ts = .1; ts < timeStep; ts += .1) {
            entity1x = entity1.x + (entity1.vx * ts) + (.5 * entity1.nax * ts * ts);
            entity1y = entity1.y + (entity1.vy * ts) + (.5 * entity1.nay * ts * ts);
            entity2x = entity2.x + (entity2.vx * ts) + (.5 * entity2.nax * ts * ts);
            entity2y = entity2.y + (entity2.vy * ts) + (.5 * entity2.nay * ts * ts);
            
//            if(ts>=.91){
//                System.out.println("Entity1x: " + entity1x);
//                System.out.println("Entity1y: " + entity1y);
//            }

            //a = entity1y - entity2y;
            //b = entity1x - entity2x;
            //distanceBetween = Math.sqrt(a * a + b * b);
            distanceBetween = distanceBetweenPoints(entity1x, entity1y, entity2x, entity2y);
            if (distanceBetween <= combinedRadii) {
                x = (entity1x + entity2x) / 2;
                y = (entity1y + entity2y) / 2;
                System.out.println("There was a collision at ts: " + ts);
                System.out.println("Distance Between " + i + " & " + j + " : " + distanceBetween);
                System.out.println("CombinedRadii:" + combinedRadii);
                collisions.add(new Collision(i, j, x, y));
                return true;
            }
        }
        return false;
    }

    static double computeTime(Entity e, double x, double y) {

        double time = -1;
        //double dx = e.x - x;
        //double dy = e.y - y;

        if (e.nax != 0) {
            time = quadraticFormula(.5 * e.nax, e.vx, -1 * x);
        } else if (e.vx != 0) {
            time = x / e.vx;
        } else if (e.nay != 0) {
            time = quadraticFormula(.5 * e.nay, e.vy, -1 * y);
        } else if (e.vy != 0) {
            time = y / e.vy;
        } else {
            System.out.println("NOTHING IS MOVING WHY TRY");
        }
        return time;
    }

    static double quadraticFormula(double a, double b, double c) {
        double result1 = 0;
        double result2 = 0;
        double sqrt = (b * b) - (4 * a * c);
        //if(sqrt>=0){
        result1 = ((-1 * b) + Math.sqrt(sqrt)) / (2 * a);
        result2 = ((-1 * b) - Math.sqrt(sqrt)) / (2 * a);
        //} else{
        //wtf do I do
        //}
        if (result1 >= 0 && result2 >= 0) {
            if (result1 < result2) {
                return result1;
            } else {
                return result2;
            }
        } else if (result1 > 0) {
            //System.out.println(result1);
            return result1;

        } else if (result2 > 0) {
            //System.out.println(result2);
            return result2;
        }
        System.out.println("This sets a variable to NaN... meaning they should never touch?");
        return Double.NaN;//maybe return -1 if it's impossible instead of Double.NaN.. test
    }

    static double computeSlope(Entity entity1) {
        return (entity1.ny - entity1.y) / (entity1.nx - entity1.x);
    }

    static void newCollide() {
        ArrayList<ArrayList<Integer>> entitiesColliding = new ArrayList();//the 0th value will ALWAYS CONTAIN the i value
        //find out if any entities collide with more than one other entity
        System.out.println("Number of Collisions is: " + collisions.size());
        int iValue = -1;
        for (int i = 0; i < collisions.size(); i++) {
            if (collisions.get(i).i != iValue) {
                iValue = collisions.get(i).i;
                entitiesColliding.add(new ArrayList<>());
                entitiesColliding.get(entitiesColliding.size() - 1).add(iValue);
            }
            entitiesColliding.get(entitiesColliding.size() - 1).add(collisions.get(i).j);
        }

        //at this point, entitiesColliding 
        for (int i = 0; i < entitiesColliding.size(); i++) {
            System.out.print(entitiesColliding.get(i).get(0) + "| ");
            for (int j = 1; j < entitiesColliding.get(i).size(); j++) {
                System.out.print(entitiesColliding.get(i).get(j) + ", ");

            }
            System.out.println("");
        }
        System.out.println(".....");
        //iValue = entitiesColliding.get(0).get(0);//now this will be used to refer
        //to each entity which collides
        //ie 0|1,2,3
        //it's 0
        //System.out.println("iValue b4 loop: " + iValue);
        //ArrayList<Integer> iCollisions = entitiesColliding.get(0);
        // iCollisions.remove(0);//this contains everyting iValue hits... without itself

        for (int i = 0; i < entitiesColliding.size(); i++) {
//            if (entitiesColliding.get(0).get(0) != 0) {
//                System.out.println("something weird happened. i is: " + i + "0Value is: " + entitiesColliding.get(0).get(0));
//            }
            iValue = entitiesColliding.get(i).get(0);
            //iCollisions = entitiesColliding.get(i);
            for (int j = 0; j < entitiesColliding.get(i).size(); j++) {//for all newCollisions of i
                int jValue = entitiesColliding.get(i).get(j);
                for (int k = i + 1; k < entitiesColliding.size(); k++) {//check what they collide to
                    int kValue = entitiesColliding.get(k).get(0);
//                    if (kValue == jValue) {//if ivalue collides with 
//                        //if kValue (k|2, 3, 5) is in iCollisions (i| k, 5, 7)
//                        for (int l = entitiesColliding.get(k).size() - 1; l > 0; l--) {//start at 1 to disregard k itself
//                            int lValue = entitiesColliding.get(k).get(l);
//                            if (!entitiesColliding.get(i).contains(lValue)) {
//                                //iCollisions.add(lValue);
//                                entitiesColliding.get(i).add(lValue);
//                                System.out.println("Added " + iValue + "| " + lValue);
//                                entitiesColliding.get(k).remove(l);
//                                System.out.println("Removed " + kValue + "| " + lValue);
//                            } else {//already contained
//                                entitiesColliding.get(k).remove(l);
//                                System.out.println("Removed " + kValue + "| " + lValue);
//                            }
//                        }
//                        if (entitiesColliding.get(k).size() == 1) {
//                            entitiesColliding.remove(k);
//                        }
//                        //maybe have to delete entitesColliding.get(k) if it is empty????
//                    } else {
                        for (int l = 0; l < entitiesColliding.get(k).size(); l++) {
                            int lValue = entitiesColliding.get(k).get(l);
                            if (jValue == lValue) {
                                for (int m = entitiesColliding.get(k).size() - 1; m > 0; m--) {
                                    int mValue = entitiesColliding.get(k).get(m);
                                    if (!entitiesColliding.get(i).contains(mValue)) {
                                        //iCollisions.add(lValue);
                                        entitiesColliding.get(i).add(mValue);
                                        System.out.println("Added " + iValue + "| " + mValue);
                                        entitiesColliding.get(k).remove(m);
                                        System.out.println("Removed " + kValue + "| " + mValue);
                                    } else {//already contained
                                        entitiesColliding.get(k).remove(m);
                                        System.out.println("Removed " + kValue + "| " + mValue);
                                    }

                                //}
                            }

                        }
                    }
                }
                //go through each arrayList and see if 0 collides with 1 and 1 collides with 2
                // then 0 collides with 1 & 2
                //add all newCollisions from entitiesColliding.get(?).get(0)iCollisions.get(j)

            }
            if(entitiesColliding.get(i).size()==1){
                entitiesColliding.remove(i);
                i--;
            }

        }
        for (int i = 0; i < entitiesColliding.size(); i++) {
            System.out.print(entitiesColliding.get(i).get(0) + "| ");
            for (int j = 1; j < entitiesColliding.get(i).size(); j++) {
                System.out.print(entitiesColliding.get(i).get(j) + ", ");

            }
            System.out.println("");
        }
        ArrayList<NewCollisions> newCollisions = new ArrayList();
        //TIME TO ACTUALLY MAKE THEM DO THINGS WHEN THEY COLLIDE
        for (int i = 0; i < entitiesColliding.size(); i++) {
            iValue = entitiesColliding.get(i).get(0);
            double x = 0;
            double y = 0;
            double mass = 0;
            double radius = 0;
            double vx = 0;
            double vy = 0;
           
            for (int j = 0; j < entitiesColliding.get(i).size(); j++) {
                int jValue = entitiesColliding.get(i).get(j);
                Entity jEntity = entities.get(jValue);
                mass += jEntity.getMass();
                vx += jEntity.getMass() * jEntity.vx;
                vy += jEntity.getMass() * jEntity.vy;
                x += jEntity.x;
                y += jEntity.y;
            }
            x/=entitiesColliding.get(i).size();
            y/=entitiesColliding.get(i).size();
            vx /= mass;
            vy /= mass;
            newCollisions.add(new NewCollisions(entitiesColliding.get(i), x, y, vx, vy, mass));
        }

        //System.out.println("Index for newCollision[0]" + newCollisions.get(0).indexes.toString());

        ArrayList<Integer> deadIndexes = new ArrayList<>();
        ArrayList<Entity> babyEntities = new ArrayList<>();

        /////////////////actually collide
        //System.out.println("newCollisions.size: " + newCollisions.size());
        for (int i = 0; i < newCollisions.size(); i++) {
            for (int j = newCollisions.get(i).indexes.size() - 1; j >= 0; j--) {
                int deadIndex = newCollisions.get(i).indexes.get(j);
                //Entity dead = entities.remove(deadIndex);
                deadIndexes.add(deadIndex);
                System.out.println("'removed an entity'");

            }
            //entities.add(new Entity(newCollisions.get(i).x, newCollisions.get(i).y,newCollisions.get(i).vx, newCollisions.get(i).vy, newCollisions.get(i).radius, size));
            babyEntities.add(new Entity(newCollisions.get(i).x, newCollisions.get(i).y, newCollisions.get(i).vx, newCollisions.get(i).vy, newCollisions.get(i).radius, size));
            System.out.println("'Added an entity'");
        }

        for (int i = 0; i < entitiesColliding.size(); i++) {
            System.out.print(entitiesColliding.get(i).get(0) + "| ");
            for (int j = 1; j < entitiesColliding.get(i).size(); j++) {
                System.out.print(entitiesColliding.get(i).get(j) + ", ");

            }
            System.out.println("");
        }

        deadIndexes = sort(deadIndexes);
        //largest index is now last
        System.out.println("deadIndexes" + deadIndexes.toString());
        for (int i = 0; i < deadIndexes.size(); i++) {
            entities.remove((int) deadIndexes.get(i));

        }
        for (int i = 0; i < babyEntities.size(); i++) {
            entities.add(babyEntities.get(i));

        }

    }

    static ArrayList<Integer> sort(ArrayList<Integer> list) {
        int temp = 0;
        for (int i = list.size(); i > 1; i--) {
            for (int j = list.size(); j > 1; j--) {

                if (list.get(j - 1) > list.get(j - 2)) {
                    temp = list.get(j - 1);
                    list.set(j - 1, list.get(j - 2));
                    list.set(j - 2, temp);

                }//if
            }//for
        }//for
        return list;
    }

    static void collide(Collision c) {
        int i = c.i;
        int j = c.j;
        double x = c.x;
        double y = c.y;
        //m3= m1+m2
        //calculate r via m3
        //area=pir^2
        //r = sqrt(a/pi)
        System.out.println("Entity " + i + " & " + j + "collide creating a new entity at position");
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);

        entities.remove(j);
        entities.remove(i);
        //vx3=(m1*vx1+m2*vx2)/m3 & same for y
        double mass = entity1.getMass() + entity2.getMass();
        System.out.println("mass is: " + mass);
        double radius = Math.sqrt(mass / pi);
        double vx = ((entity1.getMass() * entity1.vx) + (entity2.getMass() * entity2.vx)) / mass;
        double vy = ((entity1.getMass() * entity1.vy) + (entity2.getMass() * entity2.vy)) / mass;
        Entity combined = new Entity(x, y, vx, vy, radius, size);
        entities.add(0, combined);
        System.out.println("added an entity. ");
    }
//add smaller to 
    //****************************************************************************

    static void drawEntity(Entity entity) {
        image.insertCircle(entity.x, entity.y, entity.getRadius(), entity.color[0], entity.color[1], entity.color[2], fill);
    }
}
//****************************************************************************
//****************************************************************************

