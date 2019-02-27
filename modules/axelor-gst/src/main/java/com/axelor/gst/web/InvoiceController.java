package com.axelor.gst.web;

import javax.inject.Inject;
import javax.xml.ws.Service;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController extends JpaSupport{

	
	@Inject
	public InvoiceService invoiceService;
	
	
	 public void calculate(ActionRequest request, ActionResponse response) {

		 	Invoice invoice=request.getContext().getParent().asType(Invoice.class);
		    InvoiceLine invoiceLine=request.getContext().asType(InvoiceLine.class);
		    
		    State invoiceAddresss= invoice.getInvoiceaddress().getState();
		    State shippingAddress=invoice.getShippingaddress().getState();
		    invoiceLine=invoiceService.calculate(invoiceLine, invoiceAddresss, shippingAddress);
		   

		    response.setValue("igst", invoiceLine.getIgst());
		    response.setValue("sgst", invoiceLine.getSgst());
		    response.setValue("cgst", invoiceLine.getCgst());
		 
		  }
}
