package com.axelor.gst.web;

import java.util.List;

import javax.inject.Inject;
import javax.xml.ws.Service;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
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
		    State invoiceAddresss= invoice.getInvoiceAddress().getState();
		    State shippingAddress=invoice.getShippingAddress().getState();
		    invoiceLine=invoiceService.calculate(invoiceLine, invoiceAddresss, shippingAddress);
		    response.setValue("igst", invoiceLine.getIgst());
		    response.setValue("sgst", invoiceLine.getSgst());
		    response.setValue("cgst", invoiceLine.getCgst());
		    response.setValue("grossAmount", invoiceLine.getGrossAmount());
		    response.setValue("netAmount", invoiceLine.getNetAmount());
		  }
	 
	 public void calculateAll(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    invoice=invoiceService.calculateAll(invoice);	
		    response.setValue("netAmount", invoice.getNetAmount());
		    response.setValue("netIgst", invoice.getNetIgst());
		    response.setValue("netSgst", invoice.getNetSgst());
		    response.setValue("netCgst", invoice.getNetCgst());
		    response.setValue("grossAmount", invoice.getGrossAmount());
		    response.setValue("netGstrate", invoice.getNetGstrate());
		 
		  }
	 public void productcontactAll(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    List<Contact> listContact = party.getContact();
		    for(Contact contact: listContact){
		    if(contact.getType().equals("primary")){
		    	response.setValue("partyContact",contact); 
		    	break;
		    }
		    else if(contact.getType().equals("secondary"))
		    {
		    	response.setValue("partyContact",contact.getPrimaryPhone()); 
		    	
		    }
		    
		  }
	 }
	 public void addressInvoice(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    List<Address> listAddress=party.getAddress();
		    for(Address address:listAddress)
		    {
		    	if(address.getType().equals("invoice"))
		    	{
		    		response.setValue("invoiceAddress", address);
		    		break;
		    	}
		    	else if(address.getType().equals("default"))
		    	{
		    		response.setValue("invoiceAddress", address);
		    		
		    	}
		    }
	 }
	 public void addressShipping(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    List<Address> listAddress=party.getAddress();
		    for(Address address:listAddress)
		    {
		    	if(address.getType().equals("shipping"))
		    	{
		    		response.setValue("shippingAddress", address);
		    		break;
		    	}
		    	else if(address.getType().equals("default"))
		    	{
		    		response.setValue("shippingAddress", address);
		    	
		    	}
		    }
	 }
	
}
