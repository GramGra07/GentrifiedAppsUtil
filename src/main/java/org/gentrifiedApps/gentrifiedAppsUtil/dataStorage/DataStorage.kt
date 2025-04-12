package org.gentrifiedApps.gentrifiedAppsUtil.dataStorage

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D

class DataStorage {
    companion object Storage{
        private var currentPose = Target2D.blank()
        private var alliance: Alliance = Alliance.RED


        @JvmStatic fun setAlliance(alliance: Alliance) {
            this.alliance = alliance
        }
        @JvmStatic fun getAlliance(): Alliance {
            return alliance
        }
        @JvmStatic fun setPose(pose: Target2D) {
            currentPose = pose
        }
        @JvmStatic fun getPose(): Target2D {
            return currentPose
        }
    }
}