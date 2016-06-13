package de.htw.cbir.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
	
	private float saturation = 1;
	private int numOfHistogramBins = 2;
	private int numOfNSquareClusters = 2;
	private int numOfClusterIterations = 6;

	public float getSaturation() {
		return saturation;
	}
	
	
	
	public int getNumOfNSquareClusters() {
		return numOfNSquareClusters;
	}



	public void setNumOfNSquareClusters(int numOfNSquareClusters) {
		System.out.println("numOfNSquareClusters");
		this.numOfNSquareClusters = numOfNSquareClusters;
		fireEvent(new SettingChangeEvent(SettingOption.NUMOFNSQUAREDCLUSTERS, numOfHistogramBins));
	}



	public int getNumOfClusterIterations() {
		return numOfClusterIterations;
	}



	public void setNumOfClusterIterations(int numOfClusterIterations) {
		System.out.println("numOfClusterIterations");
		this.numOfClusterIterations = numOfClusterIterations;
		fireEvent(new SettingChangeEvent(SettingOption.CLUSTERITERATION, numOfHistogramBins));
	}



	public int getNumOfHistogramBins() {
		return numOfHistogramBins;
	}



	public void setNumOfHistogramBins(int numOfHistogramBins) {
		System.out.println("setNumOfHistogramBins");
		this.numOfHistogramBins = numOfHistogramBins;
		fireEvent(new SettingChangeEvent(SettingOption.NUMOFHISTOGRAMMBINS, numOfHistogramBins));
	}



	public void setSaturation(float saturation) {
		this.saturation = saturation;
		fireEvent(new SettingChangeEvent(SettingOption.SATURATION, saturation));
	}
	
	
	// --------------------------------------------------------------------------------
	// ---------------------- Event handling and delegation ---------------------------
	// --------------------------------------------------------------------------------
	/**
	 * Alle Einstellungen die über Kontrollelement geändert werden können
	 * brauchen eine SettingOption. Diese Identifiziert das Event.
	 */
	public static enum SettingOption { SATURATION, NUMOFHISTOGRAMMBINS, NUMOFNSQUAREDCLUSTERS, CLUSTERITERATION };
	
	/**
	 * SettingOption gibt an bei welchen Events die Listener informiert werden sollen.
	 * Jede Option kann nur einen Listener haben.
	 */
	protected Map<SettingOption, List<SettingChangeEventListener>> eventListeners = new HashMap<>();
		
	/**
	 * Feuer ein Event an allen Listener die sich für eine Änderung
	 * and der <SettingOption> Einstellung interessieren.
	 * 
	 * @param settingOpt
	 * @param ev
	 */
	private void fireEvent(SettingChangeEvent event) {
		List<SettingChangeEventListener> listeners = eventListeners.get(event.getSetting());
		if(listeners != null) {
			for (SettingChangeEventListener listener : listeners) {			
				listener.settingChanged(event);
			}
		}
	}
	
	/**
	 * Fügt einen Event Listener hinzu der informiert wird wenn die <SettingOption> 
	 * Einstellung von einem UI Element (vom Anwendert) geändert wurde.
	 * 
	 * @param settingOption
	 * @param actionListener
	 */
	public void addChangeListener(SettingOption settingOption, SettingChangeEventListener actionListener) {
		if(actionListener == null) return;
		
		// gibt es andere Listener für die selbe Setting Option
		List<SettingChangeEventListener> listeners = eventListeners.get(settingOption);
		if(listeners == null)
			eventListeners.put(settingOption, listeners = new ArrayList<>());
		listeners.add(actionListener);
	}

	public void removeChangeListeners() {
		eventListeners.clear();
	}
	
	/**
	 * Ein Event welches die Änderungen der Settings beinhalten.
	 * 
	 * @author Nico
	 *
	 */
	public static class SettingChangeEvent {
		protected SettingOption setting;
		protected Number value;
		public SettingChangeEvent(SettingOption setting, Number value) {
			super();
			this.setting = setting;
			this.value = value;
		}
		public SettingOption getSetting() {
			return setting;
		}
		public Number getValue() {
			return value;
		}
	}
	
	/**
	 * Interface für alle Listener von SettingChangeEvents
	 * 
	 * @author Nico
	 *
	 */
	public static interface SettingChangeEventListener {
		public void settingChanged(SettingChangeEvent event);
	}
}