package com.axelor.gst;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;

public class GstModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
	}

}
