/**
 *  Virtual Motion Sensor Plus
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
	definition (name: "Virtual Motion Sensor Plus", namespace: "joelwetzel", author: "Joel Wetzel", description: "A virtual motion sensor that also behaves as a switch.  For use with Alexa and HomeKit.") {
		capability "Refresh"
		capability "Sensor"
		capability "Motion Sensor"
		
		capability "Actuator"
		capability "Switch"
		
		command "active"
		command "inactive"
	}
}


def installed () {
	log.info "${device.displayName}.installed()"
    updated()
}


def updated () {
	log.info "${device.displayName}.updated()"
}


def refresh() {
}


def active() {
	on()
}


def inactive() {
	off()
}


def on() {
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "motion", value: "active")
}


def off() {
	sendEvent(name: "switch", value: "off")
    sendEvent(name: "motion", value: "inactive")
}




