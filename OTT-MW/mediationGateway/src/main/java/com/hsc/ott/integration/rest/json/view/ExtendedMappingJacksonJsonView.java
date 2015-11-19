package com.hsc.ott.integration.rest.json.view;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class ExtendedMappingJacksonJsonView extends MappingJackson2JsonView {

	@SuppressWarnings({"rawtypes" })
	@Override
	protected Object filterModel(Map<String, Object> model){
		Object result = super.filterModel(model);
		if (!(result instanceof Map)){
			return result;
		}

		Map map = (Map) result;
		if (map.size() == 1){
			return map.values().toArray()[0];
		}
		return map;
	}
}