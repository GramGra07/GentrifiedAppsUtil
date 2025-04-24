package org.gentrifiedApps.gentrifiedAppsUtil.dataStorage

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

class DataStorage {
    companion object Storage {
        private var currentPose = Target2D.blank()
        private var alliance: Alliance = Alliance.RED


        /**
         * Sets the alliance
         * @param alliance The alliance to set
         */
        @JvmStatic
        fun setAlliance(alliance: Alliance) {
            this.alliance = alliance
        }

        /**
         * Gets the alliance
         * @return The alliance
         */
        @JvmStatic
        fun getAlliance(): Alliance {
            return alliance
        }

        /**
         * Sets the pose
         * @param pose The pose to set
         */
        @JvmStatic
        fun setPose(pose: Target2D) {
            currentPose = pose
        }

        /**
         * Gets the pose
         * @return The pose
         */
        @JvmStatic
        fun getPose(): Target2D {
            return currentPose
        }

        internal fun getData(): Pair<Alliance, Target2D> {
            return Pair(alliance, currentPose)
        }

        /**
         * Sets the data
         * @param alliance The alliance to set
         * @param pose The pose to set
         */
        @JvmStatic
        fun setData(alliance: Alliance, pose: Target2D) {
            this.alliance = alliance
            this.currentPose = pose
        }

        internal val dataStore = DataStore()

        /**
         * Initializes the data store to file
         */
        @JvmStatic
        fun initDataStore() {
            dataStore.init()
        }

        /**
         * Writes the data store to file direct from the DataStorage
         */
        @JvmStatic
        fun writeDataStore() {
            dataStore.writeToFile()
        }

        /**
         * Reads the data store from file and sets the data in the DataStorage
         */
        @JvmStatic
        fun readDataStore() {
            val data = dataStore.readFromFile()
            this.setPose(data.second)
            this.setAlliance(data.first)
        }
    }
}