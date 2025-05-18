package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point

class AvoidanceController(vararg fields: VectorField) {
    private val fields: MutableList<VectorField> = fields.toMutableList()
    private var currentField: VectorField? = null

    fun update(point: Point): DrivePowerCoefficients {
        currentField = inField(point)
        if (currentField != null) {
            return currentField!!.correctionAsDrive(point)
        }
        return DrivePowerCoefficients.zeros()
    }

    fun inField(point: Point): VectorField? {
        for (field in fields) {
            if (field.inField(point)) {
                return field
            }
        }
        return null
    }

    fun addField(field: VectorField) {
        fields.add(field)
    }

    fun removeField(field: VectorField) {
        fields.remove(field)
    }

    fun drawFields() {
        val packet = TelemetryPacket()
        val canvas = packet.fieldOverlay()
        for (field in fields) {
            field.draw(packet, canvas)
        }
    }
}