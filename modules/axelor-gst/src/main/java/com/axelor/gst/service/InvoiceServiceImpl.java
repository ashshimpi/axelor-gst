package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;

public class InvoiceServiceImpl implements InvoiceService{

	
	@Override
	public InvoiceLine calculateInvoiceLine(InvoiceLine invoiceLine, State invoiceState, State shippingState) {
		  BigDecimal igst=BigDecimal.ZERO;
		  BigDecimal sgst=BigDecimal.ZERO;
		  BigDecimal cgst=BigDecimal.ZERO;
		  BigDecimal netAmount=BigDecimal.ZERO;
		  BigDecimal grossAmount=BigDecimal.ZERO;
		  netAmount = invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty()));
		  if(!(invoiceState.equals(shippingState))) {
			  igst = netAmount.multiply(invoiceLine.getGstRate());
		  }
		  else {
			 sgst=netAmount.multiply(invoiceLine.getGstRate().divide(new BigDecimal(200)));
			 cgst=netAmount.multiply(invoiceLine.getGstRate().divide(new BigDecimal(200)));
		  }
		  grossAmount=netAmount.add(igst).add(sgst).add(cgst);
		  invoiceLine.setIgst(igst);
		  invoiceLine.setSgst(sgst);
		  invoiceLine.setCgst(cgst);
		  invoiceLine.setGrossAmount(grossAmount);
		  invoiceLine.setNetAmount(netAmount);
		return invoiceLine;
	}

	@Override
	public Invoice calculateInvoice(Invoice invoice) {
		BigDecimal netIgst=BigDecimal.ZERO;    	  
		BigDecimal netSgst=BigDecimal.ZERO;
		BigDecimal netCgst=BigDecimal.ZERO; 
		BigDecimal netAmount=BigDecimal.ZERO;
		BigDecimal grossAmount=BigDecimal.ZERO;
		BigDecimal netGstrate=BigDecimal.ZERO;
		List<InvoiceLine> invoiceLines = invoice.getInvoiceItems();
		 
		for(InvoiceLine invoiceLine: invoiceLines){
			netAmount=netAmount.add(invoiceLine.getNetAmount());
			netIgst=netIgst.add(invoiceLine.getIgst());
			netSgst=netSgst.add(invoiceLine.getSgst());
			netCgst=netCgst.add(invoiceLine.getCgst());
			grossAmount=grossAmount.add(invoiceLine.getGrossAmount());
			netGstrate=netGstrate.add(invoiceLine.getGstRate());
			}
		invoice.setNetAmount(netAmount);
		invoice.setNetIgst(netIgst);
		invoice.setNetSgst(netSgst);
		invoice.setNetCgst(netCgst);
		invoice.setGrossAmount(grossAmount);
		invoice.setNetGstrate(netGstrate);
		
		return invoice;
	}

	@Override
	public InvoiceLine setNewInvoice(InvoiceLine invoiceLines) {
		Product product=invoiceLines.getProduct();
		
		if(product.getGstRate()!= new BigDecimal("0"))
		{
			invoiceLines.setGstRate(product.getGstRate());
		}
		if(product.getHsbn()!=null)
		{
			invoiceLines.setHsbn(product.getHsbn());
		}
		if(product.getSalePrice()!=null)
		{
			invoiceLines.setPrice(product.getSalePrice());
		}
		if(invoiceLines.getQty()==0)
		{
			invoiceLines.setQty(1);
		}
		invoiceLines.setItem(product.getName()+""+product.getCode());
		
		System.err.println(invoiceLines);
		return invoiceLines;
	}

	

	

	
}
