package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle

/**
 * An easy to use rotation vector class
 * @param zRotation The rotation around the Z axis in degrees
 * @param yRotation The rotation around the Y axis in degrees
 * @param xRotation The rotation around the X axis in degrees
 */
data class EasyRotationVector(var zRotation: Angle, var yRotation: Angle, var xRotation: Angle) {
}