package edu.rhhs.frc;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static final double GRAYHILL_ENCODER_COUNT = 256;
	public static final double WHEEL_DIAMETER_IN = 6.25;
	
	// USB Port IDs
	public static final int XBOX_USB_ID = 0;
    public static final int JOYSTICK_1_USB_ID = 1;
    public static final int JOYSTICK_2_USB_ID = 2;

    // XBox Controller Buttons
//    public static final int XBOX_SHOOTER_PITCH_ANGLE_LOAD_BUTTON = XboxController.B_BUTTON;
//    public static final int XBOX_SHOOTER_PITCH_ANGLE_HALF_COURT_BUTTON = XboxController.Y_BUTTON;
//    public static final int XBOX_SHOOTER_PITCH_ANGLE_PYRAMID_BACK_BUTTON = XboxController.A_BUTTON;
//    public static final int XBOX_SHOOTER_INTAKE_LOAD_FOUR_BUTTON = XboxController.RIGHT_BUMPER_BUTTON;
//    public static final int XBOX_SHOOTER_INTAKE_EJECT_BUTTON = XboxController.LEFT_BUMPER_BUTTON;
//    public static final int XBOX_LIGHTS_GOLD_BUTTON = XboxController.Y_BUTTON;
//    public static final int XBOX_LIGHTS_OFF_BUTTON = XboxController.X_BUTTON;
    
    // Motor Controller CAN IDs
    public static final int DRIVETRAIN_FRONT_LEFT_CAN_ID = 1;
    public static final int DRIVETRAIN_FRONT_RIGHT_CAN_ID = 2;
    public static final int DRIVETRAIN_REAR_LEFT_CAN_ID = 3;
    public static final int DRIVETRAIN_REAR_RIGHT_CAN_ID = 4;
    public static final int ROBOT_ARM_J1_CAN_ID = 5;
    public static final int ROBOT_ARM_J2_CAN_ID = 6;
    public static final int ROBOT_ARM_J3_CAN_ID = 8;
    public static final int ROBOT_ARM_J4_CAN_ID = 7;
    public static final int BIN_GRABBER_LEFT_CAN_ID = 9;
    public static final int BIN_GRABBER_RIGHT_CAN_ID = 10;
    
    // roboRIO Digital IO Port IDs 
//    public static final int RIGHT_DRIVE_ENCODER_A_DSC_DIO_ID = 1;

    // Pneumatics Control Module CAN IDs
    public static final int TOTE_GRABBER_RETRACT_PNEUMATIC_MODULE_ID = 0;
    public static final int TOTE_GRABBER_EXTEND_PNEUMATIC_MODULE_ID = 1;
    public static final int BIN_GRABBER_CLAW_EXTEND_PNEUMATIC_MODULE_ID = 2;
    public static final int BIN_GRABBER_CLAW_RETRACT_PNEUMATIC_MODULE_ID = 3;
    public static final int BIN_GRABBER_PIVOT_LOCK_EXTEND_PNEUMATIC_MODULE_ID = 4;
    public static final int BIN_GRABBER_PIVOT_LOCK_RETRACT_PNEUMATIC_MODULE_ID = 5;
    public static final int STABILZER_EXTEND_PNEUMATIC_MODULE_ID = 6;
    public static final int STABILZER_RETRACT_PNEUMATIC_MODULE_ID = 7;
}
