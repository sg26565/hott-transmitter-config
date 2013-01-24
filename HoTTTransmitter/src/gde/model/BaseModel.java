package gde.model;

import java.util.List;

abstract public class BaseModel {
	private Vendor vendor;
	private TransmitterType transmitterType;
	private ModelType modelType;
	private String modelName;
	private String info;
	private StickMode stickMode;
	private Module module;
	private DSCOutputType dscOutput;
	private ExtPPMType extPpmType;
	private ThrottleCutOf throttleCutOf;
	private Switch powerOnWarning;
	private Switch autoTrimSwitch;
	private boolean autoTimerReset; 
	private List<Servo> servos;
	private List<Stick> sticks;
	private List<Control> controls;
	private List<Switch> switches;
	private List<Mixer> mixers;
}
