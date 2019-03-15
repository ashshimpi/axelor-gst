package com.axelor.gst.service;

import javax.inject.Inject;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

	@Inject
	SequenceRepository sequencerepositary;
	
	@Transactional
	@Override
	public Sequence data(long sequenceId) {
		Sequence sequence=sequencerepositary.find(sequenceId);
		if(sequence == null){
			return null;
		}
		else{
			String prefix=sequence.getPrefix();
			String sufix=sequence.getSuffix();
			int padding=sequence.getPadding();
			String nextnumber;
			int paddingvalue = Integer.parseInt((sequence.getNextNumber().substring(prefix.length(), prefix.length()+padding)))+1;
			String newpadding =Integer.toString(paddingvalue);
			while(newpadding.length()<padding){
				newpadding="0"+newpadding;
			}
			if(sufix==null){
				 nextnumber=prefix+newpadding;
			}
			else {
				nextnumber=prefix+newpadding+sufix;
			}
			System.err.println(nextnumber);
			sequence.setNextNumber(nextnumber);
			sequence = Beans.get(SequenceRepository.class).save(sequence);
			return sequence;
		}
	}
}
