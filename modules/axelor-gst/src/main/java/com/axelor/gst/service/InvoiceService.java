package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.State;

public interface InvoiceService {
	public InvoiceLine calculateInvoiceLine(InvoiceLine invoiceLine, State invoiceState, State shippingState) ;
	public Invoice calculateInvoice(Invoice invoice);
	public InvoiceLine setNewInvoice(InvoiceLine invoiceLines);
}
