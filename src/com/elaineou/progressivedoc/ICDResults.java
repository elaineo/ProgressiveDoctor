package com.elaineou.progressivedoc;


public class ICDResults {
	private String treatment;
	private String maxc;
	private String minc;
	
	public ICDResults(String t, String max, String min) {
		treatment = t;
		maxc = "$"+max;
		minc = "$"+min;
	}
	public String getTreatment() {
		return treatment;
	}
	public String getMax() {
		return maxc;
	}
	public String getMin() {
		return minc;
	}
}
