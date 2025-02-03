package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar

import com.qualcomm.robotcore.hardware.Gamepad
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.GamepadPlus

interface MutableNum<T> {
    var value: T
}

class MutableInt(override var value: Int) : MutableNum<Int>
class MutableDouble(override var value: Double) : MutableNum<Double>

class GainController(
    private val hash: HashMap<String, ButtonConfig>,
    private val gamepad: GamepadPlus,
    private val decreaseButton: Button,
    private val increaseButton: Button
) {
    var finalGains = hashMapOf<String, Int>()
    fun end(l: List<String>){
        lastGain()
        enterGains(l)
        reset()
    }
    private fun enterGains(l:List<String>){
        l.forEach(){
            finalGains[it] = getGain(it)
        }
    }
    private val pastHash = hash.mapValues { it.value.value }

    var gain: Int = 1
    private var lastGain: Int = gain
    init{
        hash.forEach(){
            it.value.gamepad = gamepad
        }
    }
    private fun reset(){
        resetGains()
        resetPastGains()
    }
    private fun resetGains(){
        hash.forEach(){
            it.value.value = MutableInt(0)
        }
    }
    private fun resetPastGains() {
        pastHash.forEach {
            when (it.value) {
                is MutableInt -> (it.value as MutableInt).value = 0
                is MutableDouble -> (it.value as MutableDouble).value = 0.0
            }
        }
    }
    fun gainChanged(l:List<String>): Boolean {
        return l.any { pastHash[it] as Int != getGain(it) }
    }
    fun lastGain(){
        hash.forEach(){
            it.value.value = pastHash[it.key]!!
        }
    }
    private fun updateButtonGain(){
        hash.forEach(){
            it.value.updateValue(gain)
        }
    }
    fun getGain(name: String): Int {
        return hash[name]!!.value.value as Int
    }

    private fun gainControl() {
        if (gamepad.buttonJustPressed(decreaseButton)) {
            gain--
        } else if (gamepad.buttonJustPressed(increaseButton)) {
            gain++
        }
        if (lastGain != gain) {
            lastGain = gain
        }
    }
    fun loop(){
        gainControl()
        updateButtonGain()
    }
}