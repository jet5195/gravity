//Program:      Boleyn.java
//Course:       COSC460
//Description:  Implements a simulation of Newtonian gravitational physics
//Author:       Jared Boleyn
//Revised:      12/9/16
//Language:     Java
//IDE:          NetBeans 8.2
//**************************************************************************
//**************************************************************************
import java.util.ArrayList;
//Class:        Boleyn
//Description:  This class contains the main method as well as the majority of
//              other methods for implementing physics
public class Boleyn {

    final static double PI = 3.14159265358979323846;
    static ImageConstruction image;                 //the image
    static int size;                                //the size of the image
    static ArrayList<Entity> entities;              //array of the entities
    static double timeStep = 1;                     //current timeStep
    static long graphicsDelay;                      //the graphics delay
    static boolean fill;                            //false = no fill, true = fill
    static ArrayList<Collision> collisions = new ArrayList();//array containing the collisions
    static double g = 1;                            //gravity

    //**************************************************************************
    //Method:       main
    //Description:  Creates an endless loop that only quits if you decide to exit
    //              the program. Calls the menus that the user uses.
    //Parameters:   String[] args
    //Returns:      nothing
    //Calls:        KeyboardInputClass, createImage(), change(), menu()
    public static void main(String[] args) {
        KeyboardInputClass input = new KeyboardInputClass();
        size = 800;               
        while (true) {
            graphicsDelay = 0;            //set this to 0 on start
            fill = false;                 //fill is false on start
            entities = new ArrayList<>(); //creates the array of entities
            size = input.getInteger(true, 800, 0, 5000, "Enter the size for the window (Default is 800):");
            createImage();
            image.displayImage(false, "Simulation", false);
            change(input);
            menu(input);
        }
    }
    //**************************************************************************
    //Method:       menu
    //Description:  The main menu for the program, also contains the algorithms for
    //              panning & zooming. Also allows the user to exit the program
    //Parameters:   KeyboardInputClass input
    //Returns:      nothing
    //Calls:        change(), show(), image.closeDisplay, system.exit

    static void menu(KeyboardInputClass input) {
        int exit = 0;                   
        double panFactorTxt = 1;
        double panFactor = 1;
        char tsc = 'H';             //timeStep character default
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

                        break;
                    //zoom out
                    case "ZO":
                        image.xLeft -= (.5 * image.xRange);
                        image.xRight += (.5 * image.xRange);
                        image.yBottom -= (.5 * image.yRange);
                        image.yTop += (.5 * image.yRange);
                        image.xRange = image.xRight - image.xLeft;
                        image.yRange = image.yTop - image.yBottom;
                        break;
                    //pan up
                    case "PU":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.yTop += (image.yRange / panFactor);
                        image.yBottom += (image.yRange / panFactor);
                        break;
                    //pan down
                    case "PD":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.yTop -= (image.yRange / panFactor);
                        image.yBottom -= (image.yRange / panFactor);
                        break;
                    //pan left
                    case "PL":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.xLeft -= (image.yRange / panFactor);
                        image.xRight -= (image.yRange / panFactor);
                        break;
                    //pan right
                    case "PR":
                        panFactorTxt = input.getDouble(true, panFactorTxt, .00000001, Double.POSITIVE_INFINITY,
                                "Enter panning factor (Default is: " + panFactorTxt + "):");
                        panFactor = 1 / panFactorTxt;
                        image.xLeft += (image.xRange / panFactor);
                        image.xRight += (image.xRange / panFactor);
                        break;
                    //return home 
                    case "H":
                        image.xLeft = image.xRange / -2;
                        image.xRight = image.xRange / 2;
                        image.yTop = image.yRange / 2;
                        image.yBottom = image.yRange / -2;
                        break;
                    //time step
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
        }
    }
    //**************************************************************************
    //Method:       change
    //Description:  Allows the user to enter entities, specify time delay, and to
    //Parameters:   none
    //Returns:      nothing
    //Calls:        setNeighbors, continueRun

    static void change(KeyboardInputClass input) {
        int exit = 0;
        int menu = 2;
        while (exit == 0) {
            //these are the values that are edited when an entity is created
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
            System.out.println("Press 5 to fill entities (or not fill if they are being filled)");
            menu = input.getInteger(true, menu, 0, 5, "Default is: " + menu);
            switch (menu) {
                case 0:
                    if (entities.isEmpty()) {
                        System.out.println("Error, there must be at least 1 entity, try again.");
                    } else {
                        exit = 1;
                        break;
                    }
                case 1:
                    int numRandEntities = input.getInteger(true, 1, 0, 1000000, "How many random entities? (Default is 1):");
                    while (numRandEntities > 0) {
                        x = ((Math.random() * image.xRange) + image.xLeft);
                        y = ((Math.random() * image.yRange) + image.yBottom);
                        vx = ((Math.random() * size) - (size / 2)) / 100;
                        vy = ((Math.random() * size) - (size / 2) / 100);
                        r = (Math.random() * size) / 5;//max  is
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
                    r = input.getDouble(true, 25, 1, Double.POSITIVE_INFINITY, "Enter the radius (Default is 25):");
                    entities.add(new Entity(x, y, vx, vy, r, size));
                    break;
                case 4:
                    graphicsDelay = (long) (input.getDouble(true, graphicsDelay, 0, 2147483647,
                            "Enter the graphics dealy in milliseconds (Default is 0):"));
                    break;
                case 5:
                    fill = !fill;
            }
            updateImage(0);
        }
    }
    //**************************************************************************
    //Method:       createImage
    //Description:  Creates the image, running image construction and display setup
    //Parameters:   none
    //Returns:      nothing
    //Calls:        imageConstruction, image.displaySetup
    static void createImage() {
        image = new ImageConstruction(size, size, size / -2, size / 2, size / -2, size / 2, 1);
        image.displaySetup();
    }
    //**************************************************************************
    //Method:       show
    //Description:  Displays the current coordinates of all of the entities
    //Parameters:   none
    //Returns:      nothing
    //Calls:        getRadius, getMass

    static void show() {
        System.out.printf("%12s%10s%10s%10s%10s%10s%10s%10s\n", "R", "Mass", "X", "Y", "VX", "VY", "AX", "AY");
        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            System.out.printf("%2d.%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f%10.2f\n",
                    i, a.getRadius(), a.getMass(), a.x, a.y, a.vx, a.vy, a.ax, a.ay);
        }
    }
    //**************************************************************************
    //Method:       iterate
    //Description:  Iterates through the time steps, call this multiple times to iterate
    //              mulitple times
    //Parameters:   none
    //Returns:      nothing
    //Calls:        computeNewAcceleration(), computeNewVelocity(), computeNewPosition()
    //              computeCollisions(), setNewValuesAsOldValues(), collide()

    static void iterate() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                double newAcceli[] = computeNewAcceleration(entities.get(i), entities.get(j));
                double newAccelj[] = computeNewAcceleration(entities.get(j), entities.get(i));
                entities.get(i).nax += newAcceli[0];
                entities.get(i).nay += newAcceli[1];
                entities.get(j).nax += newAccelj[0];
                entities.get(j).nay += newAccelj[1];
            }
            computeNewVelocity(entities.get(i));
            computeNewPosition(entities.get(i));
        }
        collisions = new ArrayList();
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                computeCollisions(i, j);
            }
        }
        //if there is a collision collide, if not don't
        if (!collisions.isEmpty()) {
            collide();
        }
        //set all of the old values to the new values and get rid of the old values
        for (int i = 0; i < entities.size(); i++) {
            setNewValuesAsOldValues(entities.get(i));
        }
        updateImage(1);
    }
    //**************************************************************************
    //Method:       setNewValuesAsOldValues
    //Description:  Gets rid of all the old values and sets all the new values to
    //              the current values
    //Parameters:   Entity entity1 (the entity to change all the values of
    //Returns:      nothing
    //Calls:        Nothing

    static void setNewValuesAsOldValues(Entity entity1) {
        entity1.x = entity1.nx;
        entity1.y = entity1.ny;
        entity1.ax = entity1.nax;
        entity1.ay = entity1.nay;
        entity1.vx = entity1.nvx;
        entity1.vy = entity1.nvy;
        entity1.nx = 0;
        entity1.ny = 0;
        entity1.nvx = 0;
        entity1.nvy = 0;
        entity1.nax = 0;
        entity1.nay = 0;
    }
    //**************************************************************************
    //Method:       computeNewAcceleration
    //Description:  Computes the new acceleration component for eMove being acted
    //              on by eStatic. Returns the new component for the acceleration
    //              created by these specific entities
    //Parameters:   Entity eMove, Entity eStatic
    //Returns:      double[] with 2 values, the first being the ax component,
    //              the second being hte ay component
    //Calls:        Angle.computeVectorAngle, distanceBetweenPoints

    static double[] computeNewAcceleration(Entity eMove, Entity eStatic) {//entity eMove being acted on by entity eStatic
        
        double magnitude = 1;
        double distance = distanceBetweenPoints(eMove.x, eMove.y, eStatic.x, eStatic.y);
        Angles angleObject = new Angles();
        double angle = angleObject.computeVectorAngle(eMove.x, eMove.y, eStatic.x, eStatic.y);
        
        if (distance != 0) {
            magnitude = (g * eStatic.getMass()) / (distance * distance);
        }
        
        //then multiply the magnitude of the acceleration times the cosine and
        //sine of that angle to get the acceleration components in the x and y directions, respectively
        double xAccel = Math.cos(angle) * magnitude;
        double yAccel = Math.sin(angle) * magnitude;
        double[] accel = {xAccel, yAccel};
        return accel;
    }
    //**************************************************************************
    //Method:       distanceBetweenPoints
    //Description:  Calculates the distance between any two points
    //Parameters:   double x1, double y1, double x2, double y2
    //Returns:      double, the distance
    //Calls:        Nothing
    
    static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        double a = (x2 - x1);
        a = a * a;
        double b = (y2 - y1);
        b = b * b;
        return Math.sqrt(a + b);
    }
    //**************************************************************************
    //Method:       computeNewPosition
    //Description:  computes the new position for a specific entity to be at
    //              essentially creates movement
    //Parameters:   Entity entity, the entity that moves
    //Returns:      Nothing
    //Calls:        Nothing


    static void computeNewPosition(Entity entity) {
        entity.nx = entity.x + (entity.vx * timeStep) + (.5 * entity.nax * timeStep * timeStep);
        entity.ny = entity.y + (entity.vy * timeStep) + (.5 * entity.nay * timeStep * timeStep);
    }
    //**************************************************************************
    //Method:       computeNewVelocity
    //Description:  computes new velocity, computes new velocity for a given entity
    //Parameters:   Entity entity
    //Returns:      Nothing
    //Calls:        Nothing

    static void computeNewVelocity(Entity entity) {
        entity.nvx = entity.vx + (entity.nax * timeStep);
        entity.nvy = entity.vy + (entity.nay * timeStep);
    }
    //**************************************************************************
    //Method:       presets
    //Description:  Contains the preset locations to start a run
    //Parameters:   KeyboardInputClass
    //Returns:      Nothing
    //Calls:        Nothing

    static void presets(KeyboardInputClass input) {
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
    //**************************************************************************
    //Method:       updateImage
    //Description:  clears image and draws all entities then sets pixel values
    //Parameters:   int isIterate (1 if being called from Iterate method, else a 0),
    //              allows iterate to have a delay but other methods to not have a
    //              delay if specified
    //Returns:      Nothing
    //Calls:        image.clearImage, drawEntities, Thread.sleep,image.setPixelValues
    static void updateImage(int isIterate) {
        image.clearImage(0, 0, 0);
        for (int i = 0; i < entities.size(); i++) {
            drawEntity(entities.get(i));
        }
        if (isIterate == 1) {
            try {                             //wait a moment for smoother graphics
                Thread.sleep(graphicsDelay);
            } catch (Exception e) {
            }
        }
        image.setPixelValues();
    }
    //**************************************************************************
    //Method:       computeCollisions
    //Description:  finds all the collisions and adds them to arrays in order for them
    //              to be acted on must be called before
    //              setting all old variables as the current new variables
    //Parameters:   int i, int j.. the location in entities where the entities are
    //Returns:      boolean.. only to break out of the method
    //Calls:        distanceBetweenPoints,

    static boolean computeCollisions(int i, int j) {
        Entity entity1 = entities.get(i);
        Entity entity2 = entities.get(j);

        double combinedRadii = entity1.getRadius() + entity2.getRadius();
        double entity1x = entity1.x;
        double entity1y = entity1.y;
        double entity2x = entity2.x;
        double entity2y = entity2.y;
        double distanceBetween = distanceBetweenPoints(entity1x, entity1y, entity2x, entity2y);

        if (distanceBetween <= combinedRadii) {
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
                collisions.add(new Collision(i, j));
                return true;
            }
        }
        return false;
    }
    //**************************************************************************
    //Method:       collide
    //Description:  sorts through collisions and calls other methods to act on
    //              the collisions
    //Parameters:   None
    //Returns:      Nothing
    //Calls:        findMutualCollisions, removeAndCombineEntities

    static void collide() {
        ArrayList<ArrayList<Integer>> collidingIndexes = new ArrayList();
        //find out if any entities collide with more than one other entity
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
        //call this twice to check if finding mutual collisions added any transitive
        //collisions
        findMutualCollisions(collidingIndexes);
        findMutualCollisions(collidingIndexes);

        ArrayList<BigCollisions> newCollisions = new ArrayList();
        //time to actually create the new entities
        for (int i = 0; i < collidingIndexes.size(); i++) {
            double x = 0;
            double y = 0;
            double mass = 0;
            double vx = 0;
            double vy = 0;
            
            for (int j = 0; j < collidingIndexes.get(i).size(); j++) {
                int jValue = collidingIndexes.get(i).get(j);
                Entity jEntity = entities.get(jValue);
                mass += jEntity.getMass();
                vx += jEntity.getMass() * jEntity.vx;
                vy += jEntity.getMass() * jEntity.vy;
                x += jEntity.x * jEntity.getMass();
                y += jEntity.y * jEntity.getMass();

            }
            x /= mass;
            y /= mass;
              
            vx /= mass;
            vy /= mass;
                
            x = x + (vx * timeStep);
            y = y + (vy * timeStep);
            
            newCollisions.add(new BigCollisions(collidingIndexes.get(i), x, y, vx, vy, mass));
        }
        removeAndCombineEntities(newCollisions);   
    }
    //**************************************************************************
    //Method:       removeAndCombineEntities
    //Description:  for all of the collisions in newCollisions, acts upon them by
    //              first removing all doomed entities and then adding the babyEntities
    //Parameters:   ArrayList<BigCollisions> newCollisions
    //              contains all the now sorted collisions
    //Returns:      Nothing
    //Calls:        sort, to sort the doomed entities in descending order
    static void removeAndCombineEntities(ArrayList<BigCollisions> newCollisions) {
        ArrayList<Integer> deadIndexes = new ArrayList<>();
        ArrayList<Entity> babyEntities = new ArrayList<>();

        for (int i = 0; i < newCollisions.size(); i++) {
            for (int j = newCollisions.get(i).indexes.size() - 1; j >= 0; j--) {
                int deadIndex = newCollisions.get(i).indexes.get(j);
                deadIndexes.add(deadIndex);

            }          
            babyEntities.add(new Entity(newCollisions.get(i).x, newCollisions.get(i).y,
                    newCollisions.get(i).vx, newCollisions.get(i).vy, newCollisions.get(i).radius, size));
        }
        deadIndexes = sort(deadIndexes);
        //largest index is now last
        for (int i = 0; i < deadIndexes.size(); i++) {
            entities.remove((int) deadIndexes.get(i));
        }
        for (int i = 0; i < babyEntities.size(); i++) {
            entities.add(babyEntities.get(i));
        }
    }
    //**************************************************************************
    //Method:       findMututalCollisions
    //Description:  makes it so if 0 hits 1 and 1 hits 5 then 0 hits one and 5
    //Parameters:   ArrayList<Integer>> collidingIndexes
    //Returns:      Nothing
    //Calls:        computeSlope, distanceBetweenPoints,

    static void findMutualCollisions(ArrayList<ArrayList<Integer>> collidingIndexes) {
        for (int i = 0; i < collidingIndexes.size(); i++) {         
            int iValue = collidingIndexes.get(i).get(0);
            for (int j = 0; j < collidingIndexes.get(i).size(); j++) {//for all newCollisions of i
                int jValue = collidingIndexes.get(i).get(j);
                for (int k = i + 1; k < collidingIndexes.size(); k++) {//check what they collide to
                    int kValue = collidingIndexes.get(k).get(0);//there's nothing left. since you deleted it
                    for (int l = 0; l < collidingIndexes.get(k).size(); l++) {
                        int lValue = collidingIndexes.get(k).get(l);
                        if (jValue == lValue) {
                            for (int m = collidingIndexes.get(k).size() - 1; m >= 0; m--) {
                                int mValue = collidingIndexes.get(k).get(m);
                                if (!collidingIndexes.get(i).contains(mValue)) {
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
    }
    //**************************************************************************
    //Method:       sort
    //Description:  sorts and returns an arraylist in descending order
    //Parameters:   ArrayList<Integer> that is sorted
    //Returns:      sorted arraylist
    //Calls:        Nothing
    static ArrayList<Integer> sort(ArrayList<Integer> list) {
        int temp = 0;
        for (int i = list.size(); i > 1; i--) {
            for (int j = list.size(); j > 1; j--) {
                if (list.get(j - 1) > list.get(j - 2)) {
                    temp = list.get(j - 1);
                    list.set(j - 1, list.get(j - 2));
                    list.set(j - 2, temp);
                }
            }
        }
        return list;
    }
    //**************************************************************************
    //Method:       drawEntity
    //Description:  calls insertCircle to draw the entity to the image
    //Parameters:   Entity entity, entity to be drawn
    //Returns:      Nothing
    //Calls:        image.insertCircle

    static void drawEntity(Entity entity) {
        image.insertCircle(entity.x, entity.y, entity.getRadius(), entity.color[0], entity.color[1], entity.color[2], fill);
    }
}
//****************************************************************************
//****************************************************************************
//Class:        Entity
//Description:  Contains attributes for each entity, its position, velocity,
//              acceleration, radius, mass
class Entity {
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
    private double radius;  //maybe should be int?
    private double mass;    // equal to area
    final double pi = 3.14159265358979323846;
    int color[];            //array of an entities rgb values
    private static final int[][] COLORS = {{255,255,255},{0,255,255},{30,144,255},{0,0,255},
            {138,43,226},{192,192,192},{0,255,0},{244,164,96},{255,0,0},{255,255,0}};
    //**************************************************************************
    //Method:       Entity
    //Description:  Constuctor method to create an entity and give it a color
    //Parameters:   double x, double y, double vx, double vy, double r, int size
    //              the size is the size of the display
    //Returns:      Nothing
    //Calls:        setRadiusAndMass
    
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
        if(ratio<=.00001){
            color = COLORS[0];
        }
        else if(ratio<=.00004){
            color = COLORS[1];
        }
        else if(ratio<=.00016){
            color = COLORS[2];
        }
        else if(ratio<=.00064){
            color = COLORS[3];
        }
        else if(ratio<=.00256){
            color = COLORS[4];
        }
        else if(ratio<=.001024){
            color = COLORS[5];
        }
        else if(ratio<=.04096){
            color = COLORS[6];
        }
        else if(ratio<=.16384){
            color = COLORS[7];
        }
        else if(ratio<=.65536){
            color = COLORS[8];
        }
        else{
            color = COLORS[9];
        }
    }
    //**************************************************************************
    //Method:       getRadius
    //Description:  get radius
    //Parameters:   None
    //Returns:      Radius
    //Calls:        Nothing
    double getRadius(){
        return this.radius;
    }
    
    //**************************************************************************
    //Method:       setRadiusAndMass
    //Description:  sets radius and mass since they are interlinked
    //Parameters:   double r, radius
    //Returns:      Nothing
    //Calls:        Nothing
    void setRadiusAndMass(double r){
        this.radius = r;
        this.mass = pi * r * r;
    }
    //**************************************************************************
    //Method:       getMass
    //Description:  gets mass
    //Parameters:   None
    //Returns:      Mass
    //Calls:        Nothing
    double getMass(){
        return this.mass;
    }
   //****************************************************************************
}
//****************************************************************************
//****************************************************************************
//Class:        Collision
//Description:  Contains the indexes in entities of any 2 colliding entities
class Collision {
    int i;  //location in entities for first
    int j;  //location in entities for second
    //**************************************************************************
    //Method:       Collision
    //Description:  Constructor method for creating a collision
    //Parameters:   int i & j, the indexes for the colliding entities
    //Returns:      Nothing
    //Calls:        Nothing
    
    public Collision(int i, int j){
        this.i=i;
        this.j=j;
    }
    //**************************************************************************
}
//**************************************************************************
//**************************************************************************
//Class:        BigCollision
//Description:  Contains all the values for the new entities to be created after
//              colliding the entities

class BigCollisions {

    ArrayList<Integer> indexes;
    double mass;
    double vx;
    double vy;
    double x;
    double y;
    double radius;
    final double pi = 3.14159265358979323846;

    //**************************************************************************
    //Method:       BigCollision
    //Description:  Constructor method for creating a bigcollision, which contains
    //              the doomed entities indexes and the values used to create a new
    //              entity based off the collisions
    //Parameters:   ArrayList<Integer> indexes (doomed), double x, double y, double vx,
    //              double vy, double mass
    //Returns:      Nothing
    //Calls:        Nothing
    public BigCollisions(ArrayList<Integer> indexes, double x, double y, double vx, double vy, double mass) {
        this.indexes = indexes;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = Math.sqrt(mass / pi);
    }
    //**************************************************************************
}
//**************************************************************************
//**************************************************************************
