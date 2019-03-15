package com.axelor.gst.web;

import java.util.List;

import javax.inject.Inject;

import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

public class ProductController {

	@Inject
	public ProductRepository productRepository;

	public void selectedProductprint(ActionRequest request, ActionResponse response) {
		@SuppressWarnings("unchecked")
		List<Integer> productIdsList = (List<Integer>) request.getContext().get("_ids");
		List<String> idsl = Lists.transform(productIdsList, Functions.toStringFunction());
		String productIds = String.join(",", idsl);
		System.err.println(productIds);
		request.getContext().put("productIds", productIds);
	}
}
