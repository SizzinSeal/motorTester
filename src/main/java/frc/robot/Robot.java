// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String CANBUS_NAME = "";
  private final TalonFX m_top = new TalonFX(1, CANBUS_NAME);
  private final TalonFX m_bottom = new TalonFX(2, CANBUS_NAME);

  private final DutyCycleOut m_topOut = new DutyCycleOut(0);
  private final DutyCycleOut m_bottomOut = new DutyCycleOut(1);

  private final XboxController m_joystick = new XboxController(0);

  private final ShuffleboardTab m_tab = Shuffleboard.getTab("Motors");
  private final GenericEntry m_topSpeedEntry = m_tab.add("Top Speed", 0)
      .getEntry();
  private final GenericEntry m_bottomSpeedEntry = m_tab.add("Bottom Speed", 0)
      .getEntry();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    /* Configure the devices */
    TalonFXConfiguration topConfiguration = new TalonFXConfiguration();
    TalonFXConfiguration bottomConfiguration = new TalonFXConfiguration();

    /*
     * User can optionally change the configs or leave it alone to perform a factory
     * default
     */
    topConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    bottomConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    m_top.getConfigurator().apply(topConfiguration);
    m_bottom.getConfigurator().apply(bottomConfiguration);
  }

  @Override
  public void robotPeriodic() {
    /* Get the latest speed from the Talons */
    m_topSpeedEntry.setDouble(m_top.getRotorVelocity().getValue());
    m_bottomSpeedEntry.setDouble(m_bottom.getRotorVelocity().getValue());
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    /* Get forward and rotational throttle from joystick */
    /* invert the joystick Y because forward Y is negative */
    final double topspeed = 0.90;
    final double bottomSpeed = 0.90;
    if (m_joystick.getAButtonReleased()) {
      if (m_topOut.Output == 0) {
        m_topOut.Output = topspeed;
        m_bottomOut.Output = bottomSpeed;
      } else {
        m_topOut.Output = 0;
        m_bottomOut.Output = 0;
      }
    }

    m_top.setControl(m_topOut);
    m_bottom.setControl(m_bottomOut);
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    /* Zero out controls so we aren't just relying on the enable frame */
    m_topOut.Output = 0;
    m_bottomOut.Output = 0;
    m_top.setControl(m_topOut);
    m_bottom.setControl(m_bottomOut);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}