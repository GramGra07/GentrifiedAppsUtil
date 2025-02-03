package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar

class AutoRunner(val allFuncs : List<funcStore>) {
    fun start(index:Int){
        allFuncs[index].start()
    }
    fun end(index:Int){
        allFuncs[index].end()
    }
}

class funcStore(private val func:Runnable,private val endFunc:Runnable){
    fun start(){
        func.run()
    }
    fun end() {
        endFunc.run()
    }
}