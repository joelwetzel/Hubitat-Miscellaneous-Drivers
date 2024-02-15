package joelwetzel.miscellaneous_drivers.tests

import me.biocomp.hubitat_ci.util.integration.IntegrationDeviceSpecification

import spock.lang.Specification

class EnhancedVirtualPresenceSensorTests extends IntegrationDeviceSpecification {
    @Override
    def setup() {
        super.initializeEnvironment(deviceScriptFilename: "EnhancedVirtualPresenceSensor.groovy",
                                    userSettingValues: [switch: "off", presence: "not present"])
    }

    def "Can arrive"() {
        when:
            deviceScript.arrived()

        then:
            device.currentValue("switch") == "on"
            device.currentValue("presence") == "present"
    }

    def "Can depart"() {
        when:
            deviceScript.arrived()

        then:
            device.currentValue("switch") == "on"
            device.currentValue("presence") == "present"

        when:
            deviceScript.departed()

        then:
            device.currentValue("switch") == "off"
            device.currentValue("presence") == "not present"
    }

}
