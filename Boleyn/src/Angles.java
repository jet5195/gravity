//Author:		Steve Donaldson
//Program:		Angles
//Date:			11/8/2016
//Description:	Shows how to use the method computeVectorAngle to compute angles for use in programs that need angles in the
//              range 0-2*PI.
//**************************************************************************************************************************
//**************************************************************************************************************************
//Class:		Angles
//Description:	Show how some results of computing angles in various quadrants
public class Angles {
	//**************************************************************************************************************************
	//Method:		main
	//Description:	Set-up and method calls...
	//Parameters:	none
	//Returns:		nothing
	//Calls:		
	//				getKeyboardInput, getInteger, getDouble, getCharacter in class KeyboardInputClass
	public static void main(String args[]) {
        double a1, a2, a3, a4, a5, a6, a7, a8;
        double PI = Math.PI;
		System.out.println("Angles\n");
        a1 = computeVectorAngle(0,0,1,0);
        a2 = computeVectorAngle(0,0,0,1);
        a3 = computeVectorAngle(0,0,-1,0);
        a4 = computeVectorAngle(0,0,0,-1);
        a5 = computeVectorAngle(0,0,1,1);
        a6 = computeVectorAngle(0,0,-1,1);
        a7 = computeVectorAngle(0,0,-1,-1);
        a8 = computeVectorAngle(0,0,1,-1);
        
        System.out.println("a1 = "+a1+" radians (= "+(a1*180.0/PI)+" degrees)"+"; sin(a1) = "+Math.sin(a1)+"; cos(a1) = "+Math.cos(a1));
        System.out.println("a2 = "+a2+" radians (= "+(a2*180.0/PI)+" degrees)"+"; sin(a2) = "+Math.sin(a2)+"; cos(a2) = "+Math.cos(a2));
        System.out.println("a3 = "+a3+" radians (= "+(a3*180.0/PI)+" degrees)"+"; sin(a3) = "+Math.sin(a3)+"; cos(a3) = "+Math.cos(a3));
        System.out.println("a4 = "+a4+" radians (= "+(a4*180.0/PI)+" degrees)"+"; sin(a4) = "+Math.sin(a4)+"; cos(a4) = "+Math.cos(a4));
        System.out.println("a5 = "+a5+" radians (= "+(a5*180.0/PI)+" degrees)"+"; sin(a5) = "+Math.sin(a5)+"; cos(a5) = "+Math.cos(a5));
        System.out.println("a6 = "+a6+" radians (= "+(a6*180.0/PI)+" degrees)"+"; sin(a6) = "+Math.sin(a6)+"; cos(a6) = "+Math.cos(a6));
        System.out.println("a7 = "+a7+" radians (= "+(a7*180.0/PI)+" degrees)"+"; sin(a7) = "+Math.sin(a7)+"; cos(a7) = "+Math.cos(a7));
        System.out.println("a8 = "+a8+" radians (= "+(a8*180.0/PI)+" degrees)"+"; sin(a8) = "+Math.sin(a8)+"; cos(a8) = "+Math.cos(a8));
                
		System.exit(0);
	}
	//**********************************************************************************************************************
    //Method:       computeVectorAngle
    //Description:	Computes the angle a vector makes with the x-axis and returns a value in the range 0-2*PI assuming a
    //          	counterclockwise (positive) rotation. The Math.atan() method computes an angle in the range -PI/2 to
    //              PI/2, whereas what is frequently needed is the actual (full) angle of rotation.
    //Parameters:	tailX, tailY, headX, headY - endpoints of the vector (e.g., tail=origin, head=terminus)
    //Returns:      vectorAngle	- angle the vector makes with the x-axis (in the range 0-2*PI)
    //Calls:        nothing
    public static double computeVectorAngle(double tailX, double tailY, double headX, double headY) {
        double vectorAngle;
        vectorAngle = Math.atan((headY - tailY) / (headX - tailX));
        if (headX >= tailX) {                               //vector is in 1st or 4th quadrant
            if (headY < tailY)                              //vector is in 4th quadrant
                vectorAngle = 2.0 * Math.PI + vectorAngle;	//angle returned by Math.atan() was negative in this case
        }
            else                                            //headX < tailX (vector is in 2nd or 3rd quadrant)
                vectorAngle += Math.PI;                     //angle returned by Math.atan() could be positive or negative
            return vectorAngle;
    }
	//**************************************************************************************************************************
}
//**************************************************************************************************************************
//**************************************************************************************************************************
