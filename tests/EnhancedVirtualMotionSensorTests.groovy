package joelwetzel.miscellaneous_drivers.tests

import me.biocomp.hubitat_ci.util.integration.IntegrationDeviceSpecification

import spock.lang.Specification

class EnhancedVirtualMotionSensorTests extends IntegrationDeviceSpecification {
    @Override
    def setup() {
        super.initializeEnvironment(deviceScriptFilename: "EnhancedVirtualMotionSensor.groovy",
                                    userSettingValues: [switch: "off", motion: "inactive"])
    }

    def "Motion detected"() {
        when:
            deviceScript.active()

        then:
            device.currentValue("switch") == "on"
            device.currentValue("motion") == "active"
    }

    def "No motion"() {
        when:
            deviceScript.active()

        then:
            device.currentValue("switch") == "on"
            device.currentValue("motion") == "active"

        when:
            deviceScript.inactive()

        then:
            device.currentValue("switch") == "off"
            device.currentValue("motion") == "inactive"
    }
}
