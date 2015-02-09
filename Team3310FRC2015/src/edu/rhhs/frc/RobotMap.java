package edu.rhhs.frc;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // USB Port IDs
    public static final int JOYSTICK_1_USB_ID = 1;
    public static final int JOYSTICK_2_USB_ID = 2;
    public static final int XBOX_USB_ID = 0;

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
    public static final int BINGRABBER_RIGHT_CAN_ID = 9;
    public static final int BINGRABBER_LEFT_CAN_ID = 10;
    
    // roboRIO Digital IO Port IDs 
//    public static final int RIGHT_DRIVE_ENCODER_A_DSC_DIO_ID = 1;

    // Pneumatics Control Module CAN IDs
//    public static final int SHIFT_EXTEND_PNEUMATIC_MODULE_ID = 1;
    
}