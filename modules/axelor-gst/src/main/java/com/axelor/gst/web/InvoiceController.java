package com.axelor.gst.web;

import javax.inject.Inject;
import javax.xml.ws.Service;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController extends JpaSupport{

	
	@Inject
	public InvoiceService invoiceService;
	
	
	 public void calculate(ActionRequest request, ActionResponse response) {

		    InvoiceLine invoiceLine=request.getContext().asType(InvoiceLine.class);
		    State invoiceState=request.getContext().asType(State.class);
		    State companyState=request.getContext().asType(State.class);
		    invoiceLine=invoiceService.calculate(invoiceLine, invoiceState, companyState);
		   

		    response.setValue("igst", invoiceLine.getIgst());
		 
		  }
}
