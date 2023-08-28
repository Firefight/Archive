package studio.archetype.firefight.common

import studio.archetype.firefight.common.util.Service

object CommonEnvironment {
    fun init() {
        Service.initServices()
    }
}