/**
 *  Xiaomi Aqara Wall Button V0.2
 *
 *  Copyright 2021 
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
 *
 *  Model WXKG02LM (2 button) - 2018 Revision (lumi.remote.b286acn01):
 *    - Single press of left/right/both button(s) results in button 1/2/3 "pushed" event
 *    - Double click of left/right/both button(s) results in button 3/4/5 "pushed" event
 *    - Hold of left/right/both button(s) for longer than 400ms results in button 1/2/3 "held" event
 *      Details of 2018 revision button press ZigBee messages:
 *         Cluster 0012 (Multistate Input), Attribute 0055
 *         Endpoint 01 = left, 02 = right, 03 = both
 *         Value 0000 = hold, 0001 = single, 0002 = double
 *
 *  Aqara Wall Button WXKG02LM (2018 revision)의 descMap은 
 *  description is read attr - raw: F70B0100120A5500210100, dni: F70B, endpoint: 01, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0001 (Left Single)
 *  description is read attr - raw: F70B0100120A5500210200, dni: F70B, endpoint: 01, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0002 (Left Double)
 *  description is read attr - raw: F70B0100120A5500210000, dni: F70B, endpoint: 01, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0000 (Left Hold)
 *  description is read attr - raw: F70B0200120A5500210100, dni: F70B, endpoint: 02, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0001 (Right Single)
 *  description is read attr - raw: F70B0200120A5500210200, dni: F70B, endpoint: 02, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0002 (Right Double)
 *  description is read attr - raw: F70B0200120A5500210000, dni: F70B, endpoint: 02, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0000 (Right Hold)
 *  description is read attr - raw: F70B0300120A5500210100, dni: F70B, endpoint: 03, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0001 (Both Single)
 *  description is read attr - raw: F70B0300120A5500210200, dni: F70B, endpoint: 03, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0002 (Both Double)
 *  description is read attr - raw: F70B0300120A5500210000, dni: F70B, endpoint: 03, cluster: 0012, size: 10, attrId: 0055, result: success, encoding: 21, value: 0000 (Both Hold)
 *
 *  Aqara Wall Button WXKG07LM fingerprint add(2021.5.22)
 */

import groovy.json.JsonOutput
import physicalgraph.zigbee.clusters.iaszone.ZoneStatus
import physicalgraph.zigbee.zcl.DataType

metadata 
{
   definition (name: "Xiaomi Aqara Wall Button", namespace: "JayN", author: "YooSangBeom/JayN", ocfDeviceType: "x.com.st.d.remotecontroller", mcdSync: true)
   {
      capability "Actuator"
      capability "Battery"
      capability "Button"
      capability "Holdable Button"
      capability "Refresh"
      capability "Sensor"
      capability "Health Check"

      // Aqara Smart Light Switch - dual button - model WXKG02LM (2018 revision)
	  fingerprint deviceId: "5F01", inClusters: "0000,0003,0019,0012,FFFF", outClusters: "0000,0003,0004,0005,0019,0012,FFFF", manufacturer: "LUMI", model: "lumi.remote.b286acn01", deviceJoinName: "Aqara Switch WXKG02LM (2018)", mnmn: "SmartThings", vid: "generic-4-button"
      fingerprint deviceId: "5F01", inClusters: "0000,0003,0019,FFFF,0012", outClusters: "0000,0004,0003,0005,0019,FFFF,0012", manufacturer: "LUMI", model: "lumi.remote.b286acn02", deviceJoinName: "Aqara Switch WXKG07LM", mnmn: "SmartThings", vid: "generic-4-button"
   }

   tiles(scale: 2)
   {  
      multiAttributeTile(name: "button", type: "generic", width: 2, height: 2) 
      {
         tileAttribute("device.button", key: "PRIMARY_CONTROL") 
         {
            attributeState "pushed", label: "Pressed",       icon:"st.Weather.weather14", backgroundColor:"#53a7c0"
            attributeState "double", label: "Pressed Twice", icon:"st.Weather.weather11", backgroundColor:"#53a7c0"
            attributeState "held",   label: "Held",          icon:"st.Weather.weather13", backgroundColor:"#53a7c0"
         }
      }
      valueTile("battery", "device.battery", decoration: "flat", inactiveLabel: false, width: 2, height: 2) 
      {
         state "battery", label: '${currentValue}% battery', unit: ""
      }
      standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) 
      {
         state "default", action: "refresh.refresh", icon: "st.secondary.refresh"
      }

      main(["button"])
      details(["button","battery", "refresh"])      
      
   }
}

private getAttrid_Battery() { 0x0020 } //
private getCLUSTER_GROUPS() { 0x0004 }
private getCLUSTER_SCENES() { 0x0005 }
private getCLUSTER_WINDOW_COVERING() { 0x0102 }

private boolean isAqaraWallButton2gang() 
{
   device.getDataValue("model") == "lumi.remote.b286acn01"  //Aqara Smart Light Switch - dual button - model WXKG02LM (2018 revision)
}

private channelNumber(String dni) 
{
   dni.split(":")[-1] as Integer
}

/*
private Map getBatteryEvent(value) 
{
   def result = [:]
   //result.value = value
   //Always value 0
   result.value = 100
   result.name = 'battery'
   result.descriptionText = "${device.displayName} battery was ${result.value}%"
   return result
}
*/

def parse(String description) 
{
   log.debug "description is $description"
   def event = zigbee.getEvent(description)

   if (event) //non-standard 
   {
       sendEvent(event)
       log.debug "[Non Standard] sendEvent $event"
   }
   else 
   {    
      def descMap = zigbee.parseDescriptionAsMap(description)
      
      //That one appeared when a button was pressed but they are said to also appear when the range test button is pressed
      //정상적으로 버튼을 눌렀을때
      if (description?.startsWith("read attr -"))  
      {
         if (descMap.cluster == "0012")  //샤오미 2버튼
         {
            event = parseNonIasButtonMessage(descMap)
         }
	     
      }
      // It seems there is battery voltage information in there somewhere
      //배터리 정보가 올라올때는 catchall로 오는거 같음 (버튼 5회 연속 누르면 배터리 정보 올라옴)
      //현재 배터리 정보를 제대로 가져오지 못하고 임시로 100%처리
      else if (description?.startsWith("catchall:"))    
      {
         
         // Check catchall for battery voltage data to pass to getBatteryResult for conversion to percentage report
			Map resultMap = [:]
			def catchall = zigbee.parse(description)
            log.debug "캐치콜 : $catchall"
			if (catchall.clusterId == 0x0000) {
				def MsgLength = catchall.data.size()
                def value0 = catchall.data.get(0)
                def value1 = catchall.data.get(1)
                log.debug "캐치콜(0번) : $value0, 캐치콜(1번) : $value1"
				// Xiaomi CatchAll does not have identifiers, first UINT16 is Battery
                //캐치콜 : SmartShield(text: null, manufacturerId: 0x0000, direction: 0x01, data: [0x05, 0x00, 0x42, 0x15, 0x6c, 0x75, 0x6d, 0x69, 0x2e, 0x72, 0x65, 0x6d, 0x6f, 0x74, 0x65, 0x2e, 0x62, 0x32, 0x38, 0x36, 0x61, 0x63, 0x6e, 0x30, 0x31], 
                //number: null, isManufacturerSpecific: false, messageType: 0x00, senderShortId: 0xf6eb, isClusterSpecific: false, sourceEndpoint: 0x01, profileId: 0x0104, command: 0x0a, clusterId: 0x0000, destinationEndpoint: 0x01, options: 0x0000)
                //
                //시간이 지나면 "0x01" 또는 "0x02"가 올라온다.
                //캐치콜 : SmartShield(text: null, manufacturerId: 0x115f, direction: 0x01, data: [0x01, 0xff, 0x42, 0x1e, 0x01, 0x21, 0xc7, 0x0b, 0x03, 0x28, 0x27, 0x04, 0x21, 0xa8, 0x13, 0x05, 0x21, 0xec, 0x00, 0x06, 0x24, 0x01, 0x00, 0x00, 0x00, 0x00, 0x08, 0x21, 0x09, 0x14, 0x0a, 0x21, 0x20, 0x18], 
                //number: null, isManufacturerSpecific: true, messageType: 0x00, senderShortId: 0x3b5d, isClusterSpecific: false, sourceEndpoint: 0x01, profileId: 0x0104, command: 0x0a, clusterId: 0x0000, destinationEndpoint: 0x01, options: 0x0000)
				if ((catchall.data.get(0) == 0x01 || catchall.data.get(0) == 0x02) && (catchall.data.get(1) == 0xff)) {
					for (int i = 4; i < (MsgLength-3); i++) {
						if (catchall.data.get(i) == 0x21) { // check the data ID and data type
							// next two bytes are the battery voltage
							resultMap = getBatteryResult((catchall.data.get(i+2)<<8) + catchall.data.get(i+1))
							break
						}
					}
				}
			}
            
			return resultMap
		 
      }
     
      def result = []
      if (event) 
      {
         log.debug "Creating event: ${event}"
         result = createEvent(event)
      } 
      else if (isBindingTableMessage(description))         
      {
         Integer groupAddr = getGroupAddrFromBindingTable(description)
         if (groupAddr != null) 
         {
            List cmds = addHubToGroup(groupAddr)
            result = cmds?.collect 
            { 
               new physicalgraph.device.HubAction(it) 
            }
         } 
         else 
         {
            groupAddr = 0x0000
            List cmds = addHubToGroup(groupAddr) +
            zigbee.command(CLUSTER_GROUPS, 0x00, "${zigbee.swapEndianHex(zigbee.convertToHexString(groupAddr, 4))} 00")
            result = cmds?.collect 
            { 
               new physicalgraph.device.HubAction(it) 
            }
         }
      }
      return result
   }
   log.debug "allevent $event"
}

// Convert raw 4 digit integer voltage value into percentage based on minVolts/maxVolts range
private Map getBatteryResult(rawValue) {
	// raw voltage is normally supplied as a 4 digit integer that needs to be divided by 1000
	// but in the case the final zero is dropped then divide by 100 to get actual voltage value
	def rawVolts = rawValue / 1000
	def minVolts = voltsmin ? voltsmin : 2.5
	def maxVolts = voltsmax ? voltsmax : 3.0
	def pct = (rawVolts - minVolts) / (maxVolts - minVolts)
	def roundedPct = Math.min(100, Math.round(pct * 100))
	def descText = "Battery at ${roundedPct}% (${rawVolts} Volts)"
	
    // If checkInterval is still 24 hours, set a shorter one.
    
	if ( device.currentValue( 'checkInterval' ) == 86400 )
	{
	// Set checkInterval to two hours and ten minutes now a battery value has arrived.
    	sendEvent( name: 'checkInterval', value: 7800, data: [ protocol: 'zigbee', hubHardwareId: device.hub.hardwareID ] )
                       
 	}
    
	return [
		name: 'battery',
		value: roundedPct,
		unit: "%",
		isStateChange:true,
		descriptionText : "$device.displayName $descText"
	]
}

private Map parseNonIasButtonMessage(Map descMap)
{
    def buttonState
    def buttonNumber = 0
    Map result = [:]
   
   log.debug "[parseNonIasButtonMessage(descMap)] : $descMap"
   //if (descMap.clusterInt == 0x0006)  //수정 전
   if (descMap.cluster == "0012")
   {
      switch(descMap.endpoint)
      {
         case "01":
            buttonNumber = 1
            break
         case "02":
            buttonNumber = 2
            break
         case "03":
            buttonNumber = 3
            break             
      }
      //switch(descMap.data)  //수정 전
      switch(descMap.value)
      {
         //case "[00]":   //수정 전
         case "0001":
            buttonState = "pushed"
            break
         //case "[01]":   //수정 전
         case "0002":
            buttonState = "double"
            break
         //case "[02]":   //수정 전
         case "0000":
            buttonState = "held"
            break
      }
      if (buttonNumber !=0) 
      {
         
         def descriptionText = "button $buttonNumber was $buttonState"
         result = [name: "button", value: buttonState, data: [buttonNumber: buttonNumber], descriptionText: descriptionText, isStateChange: true]
         sendButtonEvent(buttonNumber, buttonState)
         //return createEvent(name: "button$buttonNumber", value: buttonState, data: [buttonNumber: buttonNumber], descriptionText: descriptionText, isStateChange: true)
      }
         result
   }
}

private sendButtonEvent(buttonNumber, buttonState) 
{
   def child = childDevices?.find { channelNumber(it.deviceNetworkId) == buttonNumber }
   if (child)
   {
      def descriptionText = "$child.displayName was $buttonState" // TODO: Verify if this is needed, and if capability template already has it handled
      log.debug "Child Device $child button"
      child?.sendEvent([name: "button", value: buttonState, data: [buttonNumber: 1], descriptionText: descriptionText, isStateChange: true])
   } 
   else 
   {
      log.debug "Child device $buttonNumber not found!"
   }
}

def refresh() 
{
    log.debug "Refreshing Battery"
    updated()
    return zigbee.readAttribute(zigbee.POWER_CONFIGURATION_CLUSTER, getAttrid_Battery()) 
}

def configure() 
{
    log.debug "Configuring Reporting, IAS CIE, and Bindings."
    def cmds = []

    return zigbee.configureReporting(zigbee.POWER_CONFIGURATION_CLUSTER, getAttrid_Battery(), DataType.UINT8, 30, 21600, 0x01) +
           //zigbee.enrollResponse() +
           zigbee.readAttribute(zigbee.POWER_CONFIGURATION_CLUSTER, getAttrid_Battery()) +
           zigbee.addBinding(zigbee.ONOFF_CLUSTER) +
           readDeviceBindingTable() // Need to read the binding table to see what group it's using            
           cmds
}

private getButtonName(buttonNum) 
{
   return "${device.displayName} " + buttonNum
}

private void createChildButtonDevices(numberOfButtons) 
{
   state.oldLabel = device.label
   log.debug "Creating $numberOfButtons"
   log.debug "Creating $numberOfButtons children"
   
   for (i in 1..numberOfButtons) 
   {
      log.debug "Creating child $i"
      def child = addChildDevice("smartthings", "Child Button", "${device.deviceNetworkId}:${i}", device.hubId,[completedSetup: true, label: getButtonName(i),
				 isComponent: true, componentName: "button$i", componentLabel: "buttton ${i}"])
      child.sendEvent(name: "supportedButtonValues",value: ["pushed","held","double"].encodeAsJSON(), displayed: false)
      child.sendEvent(name: "numberOfButtons", value: 1, displayed: false)
      child.sendEvent(name: "button", value: "pushed", data: [buttonNumber: 1], displayed: false)
   }
}

def installed() 
{
    def numberOfButtons
    if (isAqaraWallButton2gang()) //버튼 2구지만 동시 눌렀을때 버튼3 발생
    {
       numberOfButtons = 3
    } 
    else
    {
       numberOfButtons = 3
    }
    
    // Set an initial checkInterval of twenty-four hours for the Health Check. Change to
    // two hours and ten minutes when the regular 50-60 minutes battery reports start.
    sendEvent( name: 'checkInterval', value: 86400, displayed: false, data: [ protocol: 'zigbee', hubHardwareId: device.hub.hardwareID ] )
    
    createChildButtonDevices(numberOfButtons) //Todo
    
    //sendEvent(name: "supportedButtonValues", value: ["pushed","held","double"].encodeAsJSON(), displayed: false)
    //sendEvent(name: "numberOfButtons", value: numberOfButtons , displayed: false)
    
    //sendEvent(name: "button", value: "pushed", data: [buttonNumber: 1], displayed: false)
    
    // Initialize default states
    numberOfButtons.times 
    {
        sendEvent(name: "button", value: "pushed", data: [buttonNumber: it+1], displayed: false)
    }
    // These devices don't report regularly so they should only go OFFLINE when Hub is OFFLINE
    sendEvent(name: "DeviceWatch-Enroll", value: JsonOutput.toJson([protocol: "zigbee", scheme:"untracked"]), displayed: false)
}

def updated() 
{
   log.debug "childDevices $childDevices"
   if (childDevices && device.label != state.oldLabel) 
   {
      childDevices.each 
      {
         def newLabel = getButtonName(channelNumber(it.deviceNetworkId))
	 it.setLabel(newLabel)
      }
      state.oldLabel = device.label
    }
}


private Integer getGroupAddrFromBindingTable(description) 
{
   log.info "Parsing binding table - '$description'"
   def btr = zigbee.parseBindingTableResponse(description)
   def groupEntry = btr?.table_entries?.find { it.dstAddrMode == 1 }
   if (groupEntry != null) 
   {
      log.info "Found group binding in the binding table: ${groupEntry}"
      Integer.parseInt(groupEntry.dstAddr, 16)
   } 
   else 
   {
      log.info "The binding table does not contain a group binding"
      null
    }
}

private List addHubToGroup(Integer groupAddr) 
{
   ["st cmd 0x0000 0x01 ${CLUSTER_GROUPS} 0x00 {${zigbee.swapEndianHex(zigbee.convertToHexString(groupAddr,4))} 00}","delay 200"]
}

private List readDeviceBindingTable() 
{
   ["zdo mgmt-bind 0x${device.deviceNetworkId} 0","delay 200"]
}
