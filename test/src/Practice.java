import java.util.Scanner;

public class Practice {
	// Arm program
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double x, y, z; 
		// Coordinate values
		System.out.print("Input X Coordinate: ");
		x = input.nextDouble();
		System.out.print("Input Y Coordinate: ");
		y = input.nextDouble();
		System.out.print("Input Z Coordinate: ");
		z = input.nextDouble();
	
		
		// Calculating angle 1
		double angle1 = Math.toDegrees(Math.atan2(y, x));
		System.out.print("Angle 1: ");
		System.out.println((angle1 < 0) ? angle1 + 360 : angle1);
		// Value of 'a' will always depend on the coordinates
		double a1 = (Math.pow(x, 2) + Math.pow(z, 2));
		double a = Math.sqrt(a1);

		// These values of length are to be decided. By default, it will be 3 feet
		// Each are, for now, in inches
		double b = 36, c = 36;  // b = Arm 1, c = Arm 2

		// Calculating angle 2
		// Number in front of angle represents step of solving equation
		double oneAnglex = (z / a);
		double twoAnglex = Math.toDegrees(Math.asin(oneAnglex));

		double oneAngle2 = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2));
		double twoAngle2 = (a * b * 2);
		double threeAngle2 = (oneAngle2 / twoAngle2);
		double fourAngle2 = Math.toDegrees(Math.acos(threeAngle2));
		double fiveAngle2 = ((fourAngle2 < 0) ? fourAngle2 + 360 : fourAngle2);
		double sixAngle2 = (fiveAngle2 + twoAnglex);
		System.out.print("Angle 2: ");
		System.out.println(sixAngle2);

		// Calculating angle 3
		// Number in front of angle represents step of solving equation
		double oneAngle3 = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2));
		double twoAngle3 = (b * c * 2);
		double threeAngle3 = (oneAngle3 / twoAngle3);
		double fourAngle3 = Math.toDegrees(Math.acos(threeAngle3));
		System.out.print("Angle 3: ");
		System.out.println((fourAngle3 < 0) ? fourAngle3 + 360 : fourAngle3);
		
		//Calculating angle 4
		// parallel to the ground always
		double oneAngle4 = Math.toDegrees(Math.atan2(z,x));
		double twoAngle4 = (180 - fiveAngle2 - fourAngle3);
		double threeAngle4 = (oneAngle4 + twoAngle4 + 90);
		System.out.print("Angle 4: ");
		System.out.print(threeAngle4);
		
		
		// Closing scanners
		input.close();
	}
}
