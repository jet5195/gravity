
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
    static double g = 1;

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
            //double accel[] = {0, 0};
            for (int j = i + 1; j < entities.size(); j++) {
                double newAcceli[] = computeNewAcceleration(entities.get(i), entities.get(j), g);
                double newAccelj[] = computeNewAcceleration(entities.get(j), entities.get(i), g);
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

    static double[] computeNewAcceleration(Entity eMove, Entity eStatic, double g) {//entity eMove being acted on by entity eStatic
        
        double magnitude = 1;
        double distance = distanceBetweenPoints(eMove.x, eMove.y, eStatic.x, eStatic.y);
        Angles angleObject = new Angles();
        double angle = angleObject.computeVectorAngle(eMove.x, eMove.y, eStatic.x, eStatic.y);
        
        if (distance != 0) {//didn't fix NaN
            magnitude = (g * eStatic.getMass()) / (distance * distance); //distance
        }
        
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
    }

    static void presets(KeyboardInputClass input) {
        double x;
        double y;
        double vx;
        double vy;
        double r;
        System.out.println("Presets: Default is 1");
        System.out.println("1. Quadrants");
        System.out.println("2. Overtake");
        System.out.println("3. Extremely fast X collision");
        System.out.println("4. Solar Systems");
        int preset = input.getInteger(true, 1, 1, 4, "");
        int p = 0;
        switch (preset) {
            case 1:
                p = size / 4;
                entities.add(new Entity(-1 * p, p, 0, 0, p / 4, size));
                entities.add(new Entity(p, p, 0, 0, p / 4, size));
                entities.add(new Entity(p, -1 * p, 0, 0, p / 4, size));
                entities.add(new Entity(-1 * p, -1 * p, 0, 0, p / 4, size));
                break;
            case 2:
                p = size/4;
                entities.add(new Entity(-p, 0, 180, 0, p/8, size));
                entities.add(new Entity(0, 0, 50, 0, p/10, size));
            //entities.add(new Entity(x, y, vx, vy, r))
            case 3:
                entities.add(new Entity(-400,0 ,2000, 0, 10, size));
                entities.add(new Entity(365,0 ,-2000, 0, 10, size));
            case 4:
                entities.add(new Entity(0, 0, 0, 0, 150, size));//sun
                entities.add(new Entity(200, 0, 0, 20, 1.44, size));//mercury
                entities.add(new Entity(300, 0, 0, 15, 3.052, size));//venus
                entities.add(new Entity(500, 0, 0, 12, 3.371, size));//earth
                entities.add(new Entity(750, 0, 0, 9, 1.89, size));//mars
                entities.add(new Entity(900, 0, 0, 6.5, 11.441, size));//jupiter
                entities.add(new Entity(1100, 0, 0, 5, 9.184, size));//saturn
                entities.add(new Entity(1200, 0, 0, 2.5, 6.362, size));//uranus
                entities.add(new Entity(1400, 0, 0, 1, 6.622, size));//neptune
                entities.add(new Entity(1700, 0, 0, .5, .5, size));//pluto
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
        double distanceBetween = distanceBetweenPoints(entity1x, entity1y, entity2x, entity2y);
        //System.out.println("Distance between: " + distanceBetween + " < combinedRadii: " + combinedRadii);

        if (distanceBetween <= combinedRadii) {
            System.out.println("There was a collision at ts: " + 0);
            collisions.add(new Collision(i, j));
            return true;
        }
        //if there isn't already a collision, loop through until you find one or get out of the timestep
        for (double ts = .01; ts < timeStep; ts += .01) {
            entity1x = entity1.x + (entity1.vx * ts) + (.5 * entity1.nax * ts * ts);
            entity1y = entity1.y + (entity1.vy * ts) + (.5 * entity1.nay * ts * ts);
            entity2x = entity2.x + (entity2.vx * ts) + (.5 * entity2.nax * ts * ts);
            entity2y = entity2.y + (entity2.vy * ts) + (.5 * entity2.nay * ts * ts);

            distanceBetween = distanceBetweenPoints(entity1x, entity1y, entity2x, entity2y);
            if (distanceBetween <= combinedRadii) {
                System.out.println("There was a collision at ts: " + ts);
                //System.out.println("Distance Between " + i + " & " + j + " : " + distanceBetween);
                System.out.println("CombinedRadii:" + combinedRadii);
                collisions.add(new Collision(i, j));
                return true;
            }
        }
        return false;
    }
    

    static double computeSlope(Entity entity1) {
        return (entity1.ny - entity1.y) / (entity1.nx - entity1.x);
    }

    static void newCollide() {
        ArrayList<ArrayList<Integer>> collidingIndexes = new ArrayList();//the 0th value will ALWAYS CONTAIN the i value
        //find out if any entities collide with more than one other entity
        System.out.println("Number of Collisions is: " + collisions.size());
        int iValue = -1;
        //get all collisions and put their indexes in collidingIndexes
        for (int i = 0; i < collisions.size(); i++) {
            if (collisions.get(i).i != iValue) {
                iValue = collisions.get(i).i;
                collidingIndexes.add(new ArrayList<>());
                collidingIndexes.get(collidingIndexes.size() - 1).add(iValue);
            }
            collidingIndexes.get(collidingIndexes.size() - 1).add(collisions.get(i).j);
        }

        findMutualCollisions(collidingIndexes);

        ArrayList<BigCollisions> newCollisions = new ArrayList();
        //TIME TO ACTUALLY MAKE THEM DO THINGS WHEN THEY COLLIDE
        for (int i = 0; i < collidingIndexes.size(); i++) {
            iValue = collidingIndexes.get(i).get(0);
            double x = 0;
            double y = 0;
            double mass = 0;
            double radius = 0;
            double vx = 0;
            double vy = 0;
            double ax = 0;
            double ay = 0;

            for (int j = 0; j < collidingIndexes.get(i).size(); j++) {
                int jValue = collidingIndexes.get(i).get(j);
                Entity jEntity = entities.get(jValue);
                mass += jEntity.getMass();
                vx += jEntity.getMass() * jEntity.vx;
                vy += jEntity.getMass() * jEntity.vy;
                ax += jEntity.getMass() * jEntity.ax;
                ay = jEntity.getMass() * jEntity.ay;
                x += jEntity.x * jEntity.getMass();
                y += jEntity.y * jEntity.getMass();

            }
            x /= mass;
            y /= mass;
              
            vx /= mass;
            vy /= mass;
                
            x = x + (vx * timeStep);
            y = y + (vy * timeStep);
            
           //test this a bit and if it is too much then disregard a
            newCollisions.add(new BigCollisions(collidingIndexes.get(i), x, y, vx, vy, mass));
        }

        removeAndCombineEntities(newCollisions);
    }

    static void removeAndCombineEntities(ArrayList<BigCollisions> newCollisions) {
        ArrayList<Integer> deadIndexes = new ArrayList<>();
        ArrayList<Entity> babyEntities = new ArrayList<>();

        for (int i = 0; i < newCollisions.size(); i++) {
            for (int j = newCollisions.get(i).indexes.size() - 1; j >= 0; j--) {
                int deadIndex = newCollisions.get(i).indexes.get(j);
                //Entity dead = entities.remove(deadIndex);
                deadIndexes.add(deadIndex);
                System.out.println("'removed an entity'");

            }
            //entities.add(new Entity(newCollisions.get(i).x, newCollisions.get(i).y,newCollisions.get(i).vx, newCollisions.get(i).vy, newCollisions.get(i).radius, size));
            babyEntities.add(new Entity(newCollisions.get(i).x, newCollisions.get(i).y,
                    newCollisions.get(i).vx, newCollisions.get(i).vy, newCollisions.get(i).radius, size));
            System.out.println("'Added an entity'");
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

    static void findMutualCollisions(ArrayList<ArrayList<Integer>> collidingIndexes) {
        printArrayListArrayListInt(collidingIndexes);
        for (int i = 0; i < collidingIndexes.size(); i++) {
            
            int iValue = collidingIndexes.get(i).get(0);
            //iCollisions = collidingIndexes.get(i);
            for (int j = 0; j < collidingIndexes.get(i).size(); j++) {//for all newCollisions of i
                int jValue = collidingIndexes.get(i).get(j);
                for (int k = i + 1; k < collidingIndexes.size(); k++) {//check what they collide to
                    int kValue = collidingIndexes.get(k).get(0);//there's nothing left.. since you deleted it
                    for (int l = 0; l < collidingIndexes.get(k).size(); l++) {
                        int lValue = collidingIndexes.get(k).get(l);
                        if (jValue == lValue) {
                            for (int m = collidingIndexes.get(k).size() - 1; m >= 0; m--) {
                                int mValue = collidingIndexes.get(k).get(m);
                                if (!collidingIndexes.get(i).contains(mValue)) {
                                    //iCollisions.add(lValue);
                                    collidingIndexes.get(i).add(mValue);
                                    System.out.println("Added " + iValue + "| " + mValue);
                                    collidingIndexes.get(k).remove(m);
                                    System.out.println("Removed " + kValue + "| " + mValue);
                                } else {//already contained
                                    collidingIndexes.get(k).remove(m);
                                    System.out.println("Removed " + kValue + "| " + mValue);
                                }
                            }
                        }
                    }
                    if (collidingIndexes.get(k).isEmpty()){
                        collidingIndexes.remove(k);
                    }
                }
            }
            if (collidingIndexes.get(i).size() == 1) {
                collidingIndexes.remove(i);
                i--;
            }

        }
        printArrayListArrayListInt(collidingIndexes);
    }

    static void printArrayListArrayListInt(ArrayList<ArrayList<Integer>> collidingIndexes) {
        for (int i = 0; i < collidingIndexes.size(); i++) {
            System.out.print(collidingIndexes.get(i).get(0) + "| ");
            for (int j = 1; j < collidingIndexes.get(i).size(); j++) {
                System.out.print(collidingIndexes.get(i).get(j) + ", ");
            }
            System.out.println("");
        }
    }

//    
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

    static void drawEntity(Entity entity) {
        image.insertCircle(entity.x, entity.y, entity.getRadius(), entity.color[0], entity.color[1], entity.color[2], fill);
    }
}
//****************************************************************************
//****************************************************************************

