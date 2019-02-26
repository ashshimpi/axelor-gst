package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;

public class InvoiceServiceImpl implements InvoiceService{

	
	@Override
	public InvoiceLine calculate(InvoiceLine invoiceLine, State invoiceState, State companyState) {
		  BigDecimal igst=BigDecimal.ZERO;
		  BigDecimal sgst=BigDecimal.ZERO;
		  BigDecimal cgst=BigDecimal.ZERO;
		  BigDecimal netAmount=BigDecimal.ZERO;
		  BigDecimal grossAmount=BigDecimal.ZERO;
		  
		  netAmount = invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty()));
		  
		  
		  if(!(invoiceState.equals(companyState))) {
			  
			  igst = netAmount.multiply(invoiceLine.getGstrate());
			  
		  }
		  else {
			  
			  sgst=netAmount.multiply(invoiceLine.getGstrate().divide(new BigDecimal(200)));
			  cgst=netAmount.multiply(invoiceLine.getGstrate().divide(new BigDecimal(200)));
		  }
		  
		  grossAmount=netAmount.add(igst).add(sgst).add(cgst);
		  
		  invoiceLine.setIgst(igst);
		  invoiceLine.setSgst(sgst);
		  invoiceLine.setCgst(cgst);
		  invoiceLine.setGrossAmount(grossAmount);
		  invoiceLine.setNetAmount(netAmount);
		  
		return invoiceLine;
	}

	

	
}
