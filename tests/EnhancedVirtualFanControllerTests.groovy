package joelwetzel.miscellaneous_drivers.tests

import me.biocomp.hubitat_ci.api.common_api.Log
import me.biocomp.hubitat_ci.device.HubitatDeviceSandbox
import me.biocomp.hubitat_ci.api.device_api.DeviceExecutor
import me.biocomp.hubitat_ci.app.preferences.Input

import spock.lang.Specification

class EnhancedVirtualFanControllerTests extends Specification {
    private HubitatDeviceSandbox sandbox = new HubitatDeviceSandbox(new File('EnhancedVirtualFanController.groovy'))

    def log = Mock(Log)

    def deviceState = [:]

    def deviceExecutor = Spy(DeviceExecutor) {
        _*getLog() >> log
        _*getState() >> deviceState
    }

    def "Script can be parsed"() {
        expect:
            def deviceScript = sandbox.run(api: deviceExecutor)
    }
}
