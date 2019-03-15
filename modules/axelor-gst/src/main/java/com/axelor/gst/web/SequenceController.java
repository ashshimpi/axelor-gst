package com.axelor.gst.web;

import javax.inject.Inject;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class SequenceController{
	
	@Inject
	public SequenceService sequenceService;
	public void seqData(ActionRequest request, ActionResponse response) {
		Sequence sequence=request.getContext().asType(Sequence.class);
		String prefix=sequence.getPrefix();
		String sufix=sequence.getSuffix();
		int padding=(int)sequence.getPadding();
		String newpadding = "";
		String newvalue;
		for(int i=0;i<padding;i++){
			newpadding=newpadding+"0";
		}
		if(sufix == null){
			newvalue=prefix+newpadding;
		}
		else {
		newvalue=prefix+newpadding+sufix;
		}
		response.setValue("nextNumber",newvalue );
	}
}
