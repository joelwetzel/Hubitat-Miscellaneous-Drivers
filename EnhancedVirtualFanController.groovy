/**
 *  Enhanced Virtual Fan Controller
 *
 *  Copyright 2019 Joel Wetzel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

	
metadata {
	definition (name: "Enhanced Virtual Fan Controller", namespace: "joelwetzel", author: "Joel Wetzel", description: "A virtual fan controller that also behaves as a switch.") {
		capability "Refresh"
        capability "Actuator"
		capability "Sensor"
        capability "Switch"
        capability "Switch Level"
		capability "Fan Control"
		
		attribute "lastSpeed", "string"
	}
    
    preferences {
		section {
			input (
				type: "bool",
				name: "enableDebugLogging",
				title: "Enable Debug Logging?",
				required: true,
				defaultValue: false
			)
		}
	}
}


def log (msg) {
	if (enableDebugLogging) {
		log.debug msg
	}
}


def installed () {
    initialize()
    
	log.info "${device.displayName}.installed()"
    updated()
}


def updated () {
    initialize()
    
	log.info "${device.displayName}.updated()"
}


def initialize() {
	log.info "${device.displayName}.initialize()"
	
	// Default values
	sendEvent(name: "switch", value: "off", isStateChange: true)
	sendEvent(name: "level", value: "0", isStateChange: true)
	sendEvent(name: "speed", value: "off", isStateChange: true)
	sendEvent(name: "lastSpeed", value: "low", isStateChange: true)
}


def refresh() {
}


def on() {
    log "${device.displayName}.on()"
	
	def lastSpeed = device.currentValue("lastSpeed")
    
    sendEvent(name: "switch", value: "on", isStateChange: true)
	sendEvent(name: "speed", value: lastSpeed, isStateChange: true)
	sendEvent(name: "level", value: convertSpeedToLevel(lastSpeed), isStateChange: true)
}


def off() {
	log "${device.displayName}.off()"
	
    sendEvent(name: "switch", value: "off", isStateChange: true)
	sendEvent(name: "speed", value: "off", isStateChange: true)
	sendEvent(name: "level", value: 0, isStateChange: true)
}


def setSpeed(speed) {
	log "${device.displayName}.setSpeed($speed)"
	
	def adjustedSpeed = restrictSpeedLevels(speed)						// Only allow certain speed settings.  For example, don't allow "medium-high".
	def adjustedLevel = convertSpeedToLevel(adjustedSpeed)				// Some fan controllers depend on speed, some depend on level.  Convert the speed to a level.
	def adjustedSwitch = (adjustedSpeed == "off") ? "off" : "on"		// If speed is "off", then turn off the switch too.
	
	// Keep track of the last speed while on.  Then if the fan is off, and 
	// we turn it back on, we can go back to the last on speed.
	if (adjustedSpeed != "off") {
		sendEvent(name: "lastSpeed", value: adjustedSpeed, isStateChange: true)		
	}

	sendEvent(name: "switch", value: adjustedSwitch, isStateChange: true)
	sendEvent(name: "speed", value: adjustedSpeed, isStateChange: true)
	sendEvent(name: "level", value: adjustedLevel, isStateChange: true)
}


// If our input is level, convert it to a speed input.
def setLevel(level) {
	log "${device.displayName}.setLevel($level)"
	
	def requestedSpeed = convertLevelToSpeed(level)
	
	setSpeed(requestedSpeed)
}


// This converts speeds back into levels.  These values correspond well to a GE
// Z-Wave Plus Fan Controller, but might need to change for other smart fan
// controllers.
def convertSpeedToLevel(speed) {
	switch (speed) {
		case "off":
			return 0
		case "low":
			return 10
		case "medium":
			return 50
		case "high":
			return 99
		default:
			return 10
	}
}


// This restricts allowed speed levels.  The GE Z-Wave Plus Smart Fan
// Controller doesn't support medium-low, medium-high, or auto, so
// this converts them into something else.
def restrictSpeedLevels(speed) {
	switch (speed) {
		case "off":
			return "off"
		case "low":
			return "low"
		case "medium-low":
			return "medium"
		case "medium":
			return "medium"
		case "medium-high":
			return "high"
		case "high":
			return "high"
		case "on":
			return "medium"
		case "auto":
			return "off"
		default:
			return "medium"
	}
}


// This maps ranges of levels into speed values.  Right now it's set for just
// three speeds and off.
def convertLevelToSpeed(level) {
	if (level == 0) {
		return "off"
	}
	else if (level < 34) {
		return "low"
	}
	else if (level < 67) {
		return "medium"
	}
	else {
		return "high"
	}		
}

