package com.events;

import java.util.EventObject;

public class SemaphoreWaitEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public SemaphoreWaitEvent(Object source) {
		super(source);
		
	}

}
