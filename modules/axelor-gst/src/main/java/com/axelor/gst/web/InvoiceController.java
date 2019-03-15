package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController extends JpaSupport{
	@Inject
	public InvoiceService invoiceService;
	@Inject
	public SequenceService sequenceService;
	@Inject
	public ProductRepository productRepository;
	
	 public void calculateInvoiceLine(ActionRequest request, ActionResponse response) {
		 	Invoice invoice=request.getContext().getParent().asType(Invoice.class);
		 	InvoiceLine invoiceLine=request.getContext().asType(InvoiceLine.class);
		    State invoiceAddresss= invoice.getInvoiceAddress().getState();
		    State shippingAddress=invoice.getShippingAddress().getState();
		    invoiceLine=invoiceService.calculateInvoiceLine(invoiceLine, invoiceAddresss, shippingAddress);
		    response.setValue("igst", invoiceLine.getIgst());
		    response.setValue("sgst", invoiceLine.getSgst());
		    response.setValue("cgst", invoiceLine.getCgst());
		    response.setValue("grossAmount", invoiceLine.getGrossAmount());
		    response.setValue("netAmount", invoiceLine.getNetAmount());
		  }
	 public void calculateInvoice(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    invoice=invoiceService.calculateInvoice(invoice);	
		    response.setValue("netAmount", invoice.getNetAmount());
		    response.setValue("netIgst", invoice.getNetIgst());
		    response.setValue("netSgst", invoice.getNetSgst());
		    response.setValue("netCgst", invoice.getNetCgst());
		    response.setValue("grossAmount", invoice.getGrossAmount());
		    response.setValue("netGstrate", invoice.getNetGstrate());
		  }
	 public void partyContact(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    List<Contact> listContact = party.getContact();
		    for(Contact contact: listContact){
		    if(contact.getType().equals("primary")){
		    	response.setValue("partyContact",contact); 
		    	break;
		    }
		    else if(contact.getType().equals("secondary")){
		    	response.setValue("partyContact",contact.getPrimaryPhone()); 
		    	break;
		    }
		  }
	 }
	 public void addressInvoice(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    List<Address> listAddress=party.getAddress();
		    for(Address address:listAddress){
		    	if(address.getType().equals("invoice")){
		    		response.setValue("invoiceAddress", address);
		    		break;
		    	}
		    	else if(address.getType().equals("default")){
		    		response.setValue("invoiceAddress", address);
		    	}
		    }
	 }
	 public void addressShipping(ActionRequest request, ActionResponse response) {
		    Invoice invoice=request.getContext().asType(Invoice.class);
		    Party party = invoice.getParty();
		    boolean addressvalue=invoice.getAddressValue();
		    List<Address> listAddress=party.getAddress();
		    if(addressvalue==false){
			    for(Address address:listAddress){
			    	if(address.getType().equals("shipping")){
			    		response.setValue("shippingAddress", address);
			    		break;
			    	}
			    	else if(address.getType().equals("default")){
			    		response.setValue("shippingAddress", address);
			    	}
			    }
		    }
		    else {
		    	response.setValue("shippingAddress", invoice.getInvoiceAddress());
		    }
	 }
	 public void setReference(ActionRequest request, ActionResponse response) {
		 Invoice invoice=request.getContext().asType(Invoice.class);
		 if(invoice.getReference()==null) {
			 MetaModel model=Beans.get(MetaModelRepository.class).all().filter("self.name = ?","Invoice").fetchOne();
			 long modelId =model.getId();
			 Sequence sequence=Beans.get(SequenceRepository.class).all().filter("self.model = ?",modelId).fetchOne();
			 if(sequence==null){
				 response.setError("not found sequence");
			 }
			 else{
				 long sequenceId =sequence.getId();
				 sequence=sequenceService.data(sequenceId);	
				 response.setValue("reference", sequence.getNextNumber());
			 }
		 }
	 }
	 public void selectedProduct(ActionRequest request, ActionResponse response){
		 Invoice invoice=request.getContext().asType(Invoice.class);
		 List<Integer> productId=new ArrayList<>();
		 productId = (List<Integer>) request.getContext().get("_ids");
		 
		 try {
			 System.err.println(productId);
		 List<InvoiceLine> invoiceLines= new ArrayList<>();
		 for(Integer product:productId) {
			Product product2=productRepository.find(product.longValue());
			System.err.println(product2);
			InvoiceLine invoiceLine=new InvoiceLine();
			invoiceLine.setProduct(product2);
			try {
				invoiceLine=invoiceService.setNewInvoice(invoiceLine);
				invoiceLines.add(invoiceLine);
			}
			catch(Exception ex) {
				System.err.println(ex);
			}
		 }	 
		 response.setValue("invoiceItems", invoiceLines);
		 }
		 catch (Exception e) {
			 System.err.println();
		 }
	 }
}
