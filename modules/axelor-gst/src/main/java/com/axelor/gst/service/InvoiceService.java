package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.State;

public interface InvoiceService {
	
	public InvoiceLine calculate(InvoiceLine invoiceLine, State invoiceState, State companyState) ;
	public Invoice calculateAll(Invoice invoice);

	
}
