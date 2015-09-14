package com.acme.reservations.web.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * Handles the marshalling of app classes to JSON using Jackson mappers customized for the requirements of this application.
 * 
 * @author jevans
 *
 */
public class CustomSerializer {
	private static final Logger logger = LoggerFactory.getLogger(CustomSerializer.class);
	
	private ObjectMapper mapper;
	private ObjectMapper excludingMapper;

	
	public CustomSerializer() {
		Hibernate4Module hbnModule = new Hibernate4Module();
		hbnModule.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
		
    	mapper = new ObjectMapper();
    	mapper.registerModule(hbnModule);
    	mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    	
    	excludingMapper = new ObjectMapper();
    	excludingMapper.registerModule(hbnModule);
    	excludingMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    	excludingMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    	
	}
	
	/**
	 * @param obj an entity/POJO
	 * @return the JSON representation of the supplied object.
	 */
    public String toJson(Object obj) {
    	return toJson(obj, null);
    }
    
    /**
     * @param obj an entity/POJO
     * @param view the Jackson specific view narrowing attribute selection during marshalling
     * @return the JSON representation of a view of the supplied object.
     */
	public String toJson(Object obj, Class<? extends View> view) {
		try {
			String json = null;
			if(view != null)
				json = excludingMapper.writerWithView(view).writeValueAsString(obj);
			else
				json = mapper.writeValueAsString(obj);
			
			logger.trace("Serialized {} to {}", obj, json);
			
			return json;
		}
		catch (JsonProcessingException e) {
			logger.error("Error generating json for object:  {}", obj, e);
			throw new RuntimeException("Error generating json for object:  " + obj, e);
		}
	}
	
}
