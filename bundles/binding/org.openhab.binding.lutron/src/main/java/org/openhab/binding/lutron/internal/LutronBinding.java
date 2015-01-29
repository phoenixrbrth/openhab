/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lutron.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.*;
import org.openhab.binding.lutron.LutronBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The SNMP binding listens to SNMP Traps on the configured port and posts new
 * events of type ({@link StringType} to the event bus.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson - modified binding to support polling SNMP OIDs (SNMP GET) and setting values (SNMP SET).
 * @since 0.9.0
 */
public class LutronBinding extends AbstractActiveBinding<LutronBindingProvider>
		implements ManagedService{


	private abstract class LutronDevice implements Runnable, TelnetNotificationHandler{



		/**
		 * 
		 */
		public String IP;

		/**
		 * 
		 */
		public TelnetClient connection;



		/**
		 * 
		 */
		public LutronDevice() {
			// TODO implement here
		}

		/**
		 * @param String IP
		 */
		public LutronDevice(String IP) {
			// TODO implement here
		}

		/**
		 * @param int component
		 */
		public void enable(int component) {
			// TODO implement here
		}

		/**
		 * @param int component
		 */
		public void disable(int component) {
			// TODO implement here
		}

		/**
		 * @param int component
		 */
		public void press(int component) {
			// TODO implement here
		}

		/**
		 * @param int component
		 */
		public void release(int component) {
			// TODO implement here
		}

		/**
		 * @param int component
		 */
		public abstract void hold(int component);

		/**
		 * @param int component
		 */
		public abstract void multiTap(int component);

		/**
		 * @param int component 
		 * @param int scene
		 */
		public abstract void setScene(int component, int scene);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract int getScene(int component);

		/**
		 * @param int component 
		 * @param int form
		 */
		public void setLED(int component, int form) {
			// TODO implement here
		}

		/**
		 * @param int component
		 * @return
		 */
		public Boolean getLED(int component) {
			// TODO implement here
			return null;
		}

		/**
		 * @param int component 
		 * @param lock Boolean
		 */
		public abstract void setZoneLock(int component, Boolean lock);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract Boolean getZoneLock(int component);

		/**
		 * @param int component 
		 * @param int lock
		 */
		public abstract void setSceneLock(int component, int lock);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract Boolean getSceneLock(int component);

		/**
		 * @param int component 
		 * @param int scene
		 */
		public abstract void setSequenceState(int component, int scene);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract int getSequenceState(int component);

		/**
		 * @param int component 
		 * @param Boolean isOutput
		 */
		public abstract void startRaising(int component, Boolean isOutput);

		/**
		 * @param int component 
		 * @param Boolean isOutput
		 */
		public abstract void startLowering(int component, Boolean isOutput);

		/**
		 * @param int component
		 */
		public abstract void stopRaisingLowering(int component);

		/**
		 * @param int component 
		 * @return
		 */
		public int getBatteryStatus(int component) {
			// TODO implement here
			return 0;
		}

		/**
		 * @param int component 
		 * @param int tilt 
		 * @param int lift
		 */
		public abstract void setLiftandTiltofBlinds(int component, int tilt, int lift);

		/**
		 * @param int component 
		 * @param int lift
		 */
		public abstract void setLiftofBlinds(int component, int lift);

		/**
		 * @param int component 
		 * @param int tilt
		 */
		public abstract void setTiltofBlinds(int component, int tilt);

		/**
		 * @param int component
		 */
		public abstract void HoldRelease(int component);

		/**
		 * @param int component 
		 * @param Boolean state
		 */
		public void setTimeClock(int component, Boolean state) {
			// TODO implement here
		}

		/**
		 * @param int component 
		 * @return
		 */
		public Boolean getTimeClock(int component) {
			// TODO implement here
			return null;
		}

		/**
		 * @param int component 
		 * @return
		 */
		public String getCCIState(int component) {
			// TODO implement here
			return "";
		}

		/**
		 * @param int component 
		 * @param String time
		 */
		public void startFlash(int component, String time) {
			// TODO implement here
		}

		/**
		 * @param int component 
		 * @param String time
		 */
		public void startPulse(int component, String time) {
			// TODO implement here
		}

		/**
		 * @param int component 
		 * @param float percentage 
		 * @param String fadeTime 
		 * @param String delayTime
		 */
		public abstract void setVenetianTiltLevel(int component, float percentage, String fadeTime, String delayTime);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract String[] getVenetianTiltLevel(int component);

		/**
		 * @param int component 
		 * @param Float liftLevelPercentage 
		 * @param Float tiltLevelPercentage 
		 * @param String fadeTime 
		 * @param String delayTime
		 */
		public abstract void setVenetianTiltandLiftLevel(int component, Float liftLevelPercentage, Float tiltLevelPercentage, String fadeTime, String delayTime);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract String[] getVenetianTiltandLiftLevel( int component);

		/**
		 * @param int component
		 */
		public abstract void startRaisingVenetianTilt(int component);

		/**
		 * @param int component
		 */
		public abstract void startLoweringVenetianTilt(int component);

		/**
		 * 
		 */
		public abstract void stopVenetianTilt();

		/**
		 * @param int component
		 */
		public abstract void startRaisingVenetianLift (int component);

		/**
		 * @param int component
		 */
		public abstract void startLoweringVenetianLift (int component);

		/**
		 * @param int component
		 */
		public abstract void stopVenetianLift (int component);

		/**
		 * @param int component 
		 * @param int color
		 */
		public abstract void setDMXColor (int component, int color);

		/**
		 * @param int component 
		 * @param float level
		 */
		public abstract void setDMXLevel (int component, float level);

		/**
		 * @param int component
		 */
		public abstract void motorJogRaise (int component);

		/**
		 * @param int component
		 */
		public abstract void motorJogLower(int component);

		/**
		 * @param int component
		 */
		public abstract void motor4StageJogRaise(int component);

		/**
		 * @param int component
		 */
		public abstract void motor4StageJogLower(int component);

		/**
		 * @param String command 
		 * @return
		 */
		public String[] sendCommand(String command) {
			// TODO implement here
			return null;
		}

		/**
		 * @param int component 
		 * @param float level 
		 * @param String time1 
		 * @param String time2
		 */
		public abstract void setLightLevel(int component, float level, String time1, String time2);

		/**
		 * @param int component 
		 * @return
		 */
		public abstract String[] getLightLevel(int component);

		/**
		 * @param int component 
		 * @param float level 
		 * @param String time
		 */
		public void setZoneLevel( int component,  Float level,  String time) {
			// TODO implement here
		}

		/**
		 * @param int component 
		 * @return
		 */
		public String[] getZoneLevel( int component) {
			// TODO implement here
			return null;
		}

		/**
		 * @return
		 */
		public Boolean isConnectionAlive() {
			// TODO implement here
			return null;
		}

		@Override
		public void receivedNegotiation(int negotiation_code, int option_code) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

	}

	private class RadioRA2 extends LutronDevice
	{

		
		
		//This method is not implemented under RadioRA2
		@Override
		public void hold(int component) {
		
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void multiTap(int component) {
			
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void setScene(int component, int scene) {
			
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public int getScene(int component) {
			
			return -1;
		}

		//This method is not implemented under RadioRA2
		@Override
		public void setZoneLock(int component, Boolean lock) {
			
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public Boolean getZoneLock(int component) {
			
			return null;
		}


		//This method is not implemented under RadioRA2
		@Override
		public void setSceneLock(int component, int lock) {
			
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public Boolean getSceneLock(int component) {
			return null;
		}

		//This method is not implemented under RadioRA2
		@Override
		public void setSequenceState(int component, int scene) {
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public int getSequenceState(int component) {
			return 0;
		}

		@Override
		public void startRaising(int component, Boolean isOutput) {
			// TODO Implement
			
		}

		@Override
		public void startLowering(int component, Boolean isOutput) {
			// TODO Implement
			
		}

		@Override
		public void stopRaisingLowering(int component) {
			// TODO Implement
			
		}

		@Override
		public void setLiftandTiltofBlinds(int component, int tilt, int lift) {
			// TODO Implement
			
		}

		@Override
		public void setLiftofBlinds(int component, int lift) {
			// TODO Implement
			
		}

		@Override
		public void setTiltofBlinds(int component, int tilt) {
			// TODO Implement
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void HoldRelease(int component) {
			
		}

		@Override
		public void setVenetianTiltLevel(int component, float percentage,
				String fadeTime, String delayTime) {
			// TODO implement
			
		}

		@Override
		public String[] getVenetianTiltLevel(int component) {
			// TODO implement
			return null;
		}

		@Override
		public void setVenetianTiltandLiftLevel(int component,
				Float liftLevelPercentage, Float tiltLevelPercentage,
				String fadeTime, String delayTime) {
			// TODO implement
			
		}

		@Override
		public String[] getVenetianTiltandLiftLevel(int component) {
			// TODO implement
			return null;
		}

		@Override
		public void startRaisingVenetianTilt(int component) {
			// TODO implement
			
		}

		@Override
		public void startLoweringVenetianTilt(int component) {
			// TODO implement
			
		}

		@Override
		public void stopVenetianTilt() {
			// TODO implement
			
		}

		@Override
		public void startRaisingVenetianLift(int component) {
			// TODO implement
			
		}

		@Override
		public void startLoweringVenetianLift(int component) {
			// TODO implement
			
		}

		@Override
		public void stopVenetianLift(int component) {
			// TODO implement
			
		}

		@Override
		public void setDMXColor(int component, int color) {
			// TODO implement
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void setDMXLevel(int component, float level) {
			
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void motorJogRaise(int component) {
			
			
		}

		//This method is not implemented under RadioRA
		@Override
		public void motorJogLower(int component) {
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void motor4StageJogRaise(int component) {
			
		}

		//This method is not implemented under RadioRA2
		@Override
		public void motor4StageJogLower(int component) {
			
		}

		@Override
		public void setLightLevel(int component, float level, String time1,
				String time2) {
			//TODO implement
		}

		@Override
		public String[] getLightLevel(int component) {
			return null;
			//TODO implement
		}
	}
	
	
	private static final Logger logger = 
		LoggerFactory.getLogger(LutronBinding.class);

	private static DefaultUdpTransportMapping transport;

	private static final int LUTRON_DEFAULT_PORT = 162;
	/** The local port to bind on and listen to SNMP Traps */
	private static int port = LUTRON_DEFAULT_PORT;

	/** The SNMP community to filter SNMP Traps */
	private static String community;

	private static int timeout = 1500;
	private static int retries = 0;

	/**
	 * the interval to find new refresh candidates (defaults to 1000
	 * milliseconds)
	 */
	private int granularity = 1000;

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();


	public void activate() {
		logger.debug("SNMP binding activated");
		super.activate();
		setProperlyConfigured(true);
	}

	public void deactivate() {
		stopListening();
		logger.debug("SNMP binding deactivated");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshinterval() {
		return granularity;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "SNMP Refresh Service";
	}

	/**
	 * Configures a {@link DefaultUdpTransportMapping} and starts listening on
	 * <code>SnmpBinding.port</code> for incoming SNMP Traps.
	 */
	private void listen() {
		UdpAddress address = new UdpAddress(LutronBinding.port);
		try {
			if (transport != null) {
				transport.close();
				transport = null;
			}
			if (snmp != null) {
				snmp.close();
				snmp = null;
			}

			transport = new DefaultUdpTransportMapping(address);

			// add all security protocols
			SecurityProtocols.getInstance().addDefaultProtocols();
			SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

			// Create Target
			if (LutronBinding.community != null) {
				CommunityTarget target = new CommunityTarget();
				target.setCommunity(new OctetString(LutronBinding.community));
			}

			snmp = new Snmp(transport);

			transport.listen();
			logger.debug("SNMP binding is listening on " + address);
		} catch (IOException ioe) {
			logger.error("SNMP binding couldn't listen to " + address, ioe);
		}
	}

	/**
	 * Stops listening for incoming SNMP Traps
	 */
	private void stopListening() {
		if (transport != null) {
			try {
				transport.close();
			} catch (IOException ioe) {
				logger.error("couldn't close connection", ioe);
			}
			transport = null;
		}

		if (snmp != null) {
			try {
				snmp.close();
			} catch (IOException ioe) {
				logger.error("couldn't close snmp", ioe);
			}
			snmp = null;
		}
	}

	/**
	 * Will be called whenever a {@link PDU} is received on the given port
	 * specified in the listen() method. It extracts a {@link Variable}
	 * according to the configured OID prefix and sends its value to the event
	 * bus.
	 */
	public void processPdu(CommandResponderEvent event) {
		Address addr = event.getPeerAddress();
		if (addr == null) {
			return;
		}
		
		String s = addr.toString().split("/")[0];
		if (s == null) {
			logger.error("TRAP: failed to translate address {}", addr);
			dispatchPdu(addr, event.getPDU());
		} else {
			// Need to change the port to 161, which is what the bindings are configured for since
			// at least some SNMP devices send traps from a random port number. Otherwise the trap
			// won't be found as the address check will fail. It feels like there should be a better
			// way to do this!!!
			Address address = GenericAddress.parse("udp:" + s + "/161");
			dispatchPdu(address, event.getPDU());
		}
	}

	/**
	 * Called when a response from a GET is received
	 * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.ResponseEvent )
	 */
	@Override
	public void onResponse(ResponseEvent event) {
		dispatchPdu(event.getPeerAddress(), event.getResponse());
	}

	private void dispatchPdu(Address address, PDU pdu) {
		if (pdu != null & address != null) {
			logger.debug("Received PDU from '{}' '{}'", address, pdu);
			for (LutronBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Check the IP address
					if (!provider.getAddress(itemName).equals(address)) {
						continue;
					}

					// Check the OID
					OID oid = provider.getOID(itemName);
					Variable variable = pdu.getVariable(oid);
					if (variable != null) {
						Class<? extends Item> itemType = provider.getItemType(itemName);

						// Do any transformations
						String value = variable.toString();
						try {
							value = provider.doTransformation(itemName, value);
						} catch (TransformationException e) {
							logger.error("Transformation error with item {}: {}", itemName, e);
						}

						// Change to a state
						State state = null;
						if (itemType.isAssignableFrom(StringItem.class)) {
							state = StringType.valueOf(value);
						} else if (itemType.isAssignableFrom(NumberItem.class)) {
							state = DecimalType.valueOf(value);
						} else if (itemType.isAssignableFrom(SwitchItem.class)) {
							state = OnOffType.valueOf(value);
						}

						if (state != null) {
							eventPublisher.postUpdate(itemName, state);
						} else {
							logger.debug(
									"'{}' couldn't be parsed to a State. Valid State-Types are String and Number",
									variable.toString());
						}
					} else {
						logger.trace("PDU doesn't contain a variable with OID ‘{}‘", oid.toString());
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.debug("SNMP receive command {} from {}", itemName, command);
		
		LutronBindingProvider providerCmd = null;
		
		for (LutronBindingProvider provider : this.providers) {
			OID oid = provider.getOID(itemName, command);
			if (oid != null) {
				providerCmd = provider;
				break;
			}
		}

		if (providerCmd == null) {
			logger.warn("No match for binding provider [itemName={}, command={}]", itemName, command);
			return;
		}

		logger.debug("SNMP command for {} to {}", itemName, providerCmd.toString());

		// Set up the target
		CommunityTarget target = new CommunityTarget();
			target.setCommunity(providerCmd.getCommunity(itemName, command));
			target.setAddress(providerCmd.getAddress(itemName, command));
			target.setRetries(retries);
			target.setTimeout(timeout);
			target.setVersion(SnmpConstants.version1);

		Variable var = providerCmd.getValue(itemName, command);
		OID oid = providerCmd.getOID(itemName, command);
	    VariableBinding varBind = new VariableBinding(oid,var);

		// Create the PDU
		PDU pdu = new PDU();
			pdu.add(varBind);
			pdu.setType(PDU.SET);
			pdu.setRequestID(new integer32(1));

		logger.debug("SNMP: Send CMD PDU {} {}", providerCmd.getAddress(itemName, command), pdu);

		if (snmp == null) {
			logger.error("SNMP: snmp not initialised - aborting request");
		}
		else {
			sendPDU(target, pdu);
		}
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		for (LutronBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				int refreshinterval = provider.getRefreshinterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				Booleanean needsUpdate;
				if (refreshinterval == 0) {
					needsUpdate = false;
				} else {
					needsUpdate = age >= refreshinterval;
				}

				if (needsUpdate) {
					logger.debug("Item '{}' is about to be refreshed", itemName);

					// Set up the target
					CommunityTarget target = new CommunityTarget();
						target.setCommunity(provider.getCommunity(itemName));
						target.setAddress(provider.getAddress(itemName));
						target.setRetries(retries);
						target.setTimeout(timeout);
						target.setVersion(SnmpConstants.version1);

					// Create the PDU
					PDU pdu = new PDU();
						pdu.add(new VariableBinding(provider.getOID(itemName)));
						pdu.setType(PDU.GET);

					logger.debug("SNMP: Send PDU {} {}", provider.getAddress(itemName), pdu);

					if (snmp == null) {
						logger.error("SNMP: snmp not initialised - aborting request");
					} else {
						sendPDU(target, pdu);
					}

					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		Boolean mapping = false;
		stopListening();

		if (config != null) {
			mapping = true;

			LutronBinding.community = (String) config.get("community");
			if (StringUtils.isBlank(LutronBinding.community)) {
				LutronBinding.community = "public";
				logger.info(
						"didn't find SNMP community configuration -> listen to SNMP community {}",
						LutronBinding.community);
			}

			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString) && portString.matches("\\d*")) {
				LutronBinding.port = integer.valueOf(portString).intValue();
			} else {
				LutronBinding.port = LUTRON_DEFAULT_PORT;
				logger.info(
						"Didn't find SNMP port configuration or configuration is invalid -> listen to SNMP default port {}",
						LutronBinding.port);
			}

			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				LutronBinding.timeout = integer.valueOf(timeoutString).intValue();
				if (LutronBinding.timeout < 0 | LutronBinding.retries > 5) {
					logger.info("SNMP timeout value is invalid (" + LutronBinding.timeout + "). Using default value.");
					LutronBinding.timeout = 1500;
				}
			} else {
				LutronBinding.timeout = 1500;
				logger.info(
						"Didn't find SNMP timeout or configuration is invalid -> timeout set to {}",
						LutronBinding.timeout);
			}

			String retriesString = (String) config.get("retries");
			if (StringUtils.isNotBlank(retriesString)) {
				LutronBinding.retries = integer.valueOf(retriesString).intValue();
				if (LutronBinding.retries < 0 | LutronBinding.retries > 5) {
					logger.info("SNMP retries value is invalid ("
							+ LutronBinding.retries + "). Using default value.");
					LutronBinding.retries = 0;
				}
			} else {
				LutronBinding.retries = 0;
				logger.info(
						"Didn't find SNMP retries or configuration is invalid -> retries set to {}",
						LutronBinding.retries);
			}

		}

		for (LutronBindingProvider provider : providers) {
			if (provider.getInBindingItemNames() != null) {
				mapping = true;
			}
		}

		// Did we find either a trap request, or any bindings
		if (mapping) {
			listen();
		}
	}

	private void sendPDU(CommunityTarget target, PDU pdu) {
		try {
			snmp.send(pdu, target, null, this);
		} catch (IOException e) {
			logger.error("Error sending PDU", e);
		}
	}

	@Override
	public void receivedNegotiation(int negotiation_code, int option_code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected long getRefreshInterval() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	
}


