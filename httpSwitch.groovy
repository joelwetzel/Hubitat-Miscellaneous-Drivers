/*
 * Http Switch
 *
 * Calls URIs with HTTP GET for switch on or off
 * 
 */
metadata {
    definition(name: "Http Switch", namespace: "joelwetzel", author: "Joel Wetzel") {
        capability "Actuator"
        capability "Switch"
        capability "Sensor"
        capability "Refresh"
    }
}

preferences {
    section("URIs") {
        input "onURI", "text", title: "On URI", required: false
        input "offURI", "text", title: "Off URI", required: false
        input "refreshURI", "text", title: "Refresh URI", required: false
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
    }
}

def logsOff() {
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable", [value: "false", type: "bool"])
}

def updated() {
    log.info "updated..."
    log.warn "debug logging is: ${logEnable == true}"
    if (logEnable) runIn(1800, logsOff)
}

def parse(String description) {
    if (logEnable) log.debug(description)
}

def on() {
    if (logEnable) {
      log.debug "Sending on GET request to [${settings.onURI}]"
    }

    try {
        httpGet(settings.onURI) { resp ->
            if (resp.success) {
                sendEvent(name: "switch", value: "on", isStateChange: true)
            }
            if (logEnable) {
                if (resp.data) {
                  log.debug "${resp.data}"
                }
            }
        }
    } catch (Exception e) {
        log.warn "Call to on failed: ${e.message}"
    }
}

def off() {
    if (logEnable) {
      log.debug "Sending off GET request to [${settings.offURI}]"
    }

    try {
        httpGet(settings.offURI) { resp ->
            if (resp.success) {
                sendEvent(name: "switch", value: "off", isStateChange: true)
            }
            if (logEnable) {
                if (resp.data) {
                  log.debug "${resp.data}"
                }
            }
        }
    } catch (Exception e) {
        log.warn "Call to off failed: ${e.message}"
    }
}

def refresh() {
    if (logEnable) {
      log.debug "Sending refresh GET request to [${settings.refreshURI}]"
    }

    try {
        httpGet(settings.refreshURI) { resp ->
            if (resp.success) {
              // TODO - parse the response and send the correct event
                sendEvent(name: "switch", value: "off", isStateChange: true)
            }
            if (logEnable) {
                if (resp.data) {
                  log.debug "${resp.data}"
                }
            }
        }
    } catch (Exception e) {
        log.warn "Call to refresh failed: ${e.message}"
    }
}