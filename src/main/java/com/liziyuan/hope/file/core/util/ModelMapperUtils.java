package com.liziyuan.hope.file.core.util;


import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.SourceGetter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModelMapperUtils {

	private ModelMapperUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	private static final ModelMapper INSTANCE = new ModelMapper();
	private static final Converter enumConverter = new Converter<Object, Object>() {
		public Object convert(MappingContext<Object, Object> context) {
			if(context.getSource() == null){
				return null;
			}
			if(context.getSource() instanceof Enum){
				return ((Enum) context.getSource()).ordinal();
			}
			if((context.getGenericDestinationType() instanceof Enum || context.getDestinationType().isEnum()) && context.getSource() instanceof Integer){
				return context.getDestinationType().getEnumConstants()[(int)context.getSource()];
			}
			return context.getSource() == null ? null : context.getSource();
		}
	};
	/*static{

		INSTANCE.typeMap(ResourceEngineInstance.class, ResEngineInstance.class)
				.addMappings(mapper -> mapper.using(enumConverter).map(ResourceEngineInstance::getStatus, ResEngineInstance::setStatus))
				.addMappings(mapper -> mapper.using(enumConverter).map(ResourceEngineInstance::getLevel, ResEngineInstance::setLevel));
	}*/

	public static <S, D,V> TypeMap<S, D> typeMap(Class<S> sourceType, Class<D> destinationType,
												 SourceGetter<S> sourceGetter, DestinationSetter<D, V> destinationSetter) {
		TypeMap<S, D> typeMap = INSTANCE.typeMap(sourceType, destinationType);
		typeMap.addMappings(mapper -> mapper.using(enumConverter).map(sourceGetter, destinationSetter));
		return typeMap;
	}

	public static <T, S> T map(S source, Class<T> targetClass) {
		if (source == null)
			return null;
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return INSTANCE.map(source, targetClass);
	}

	public static <T, S> T map(S source, Class<T> targetClass, String dataFormat) {
		if (source == null)
			return null;
		INSTANCE.addConverter(stringToDateConverter(dataFormat));
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return INSTANCE.map(source, targetClass);
	}

	public static <S, T> void map(S source, T destination) {
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		INSTANCE.map(source, destination);
	}

	public static <T, S> List<T> mapList(List<S> sourceList, Class<T> targetItemClass) {
		if (sourceList == null)
			return Collections.emptyList();
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<T> result = new ArrayList<>();
		for (S sourceItem : sourceList) {
			T resultItem = INSTANCE.map(sourceItem, targetItemClass);
			result.add(resultItem);
		}
		return result;
	}



	private static Converter<String, Date> stringToDateConverter(String dataFormat) {
		return new AbstractConverter<String, Date>() {
			@Override
			protected Date convert(String source) {
				SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
				try {
					return sdf.parse(source);
				} catch (ParseException e) {
					return null;
				}
			}
		};
	}

	public static Object test(Object o) {
		for (Field field : o.getClass().getDeclaredFields()) {
			Object value = null;
			try {
				if (field.getType() == int.class || field.getType() == Integer.class) {
					value = Math.random() * 100;
				} else if (field.getType() == float.class || field.getType() == Float.class) {
					value = Math.random() * 100;
				} else if (field.getType() == long.class || field.getType() == Long.class) {
					value = Math.random() * 100;
				} else if (field.getType() == double.class || field.getType() == Double.class) {
					value = Math.random() * 100;
				} else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
					value = Math.random() > 0.5;
				} else {
					value = UUID.randomUUID().toString();
				}
				invoke(o, field.getName(), value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				return null;
			}
		}
		return o;
	}

	private static Object invoke(Object obj, String fieldName, Object value)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String firstWord = fieldName.substring(0, 1).toUpperCase();
		String methodName = String.format("set%s%s", firstWord, fieldName.substring(1));
		Method method = obj.getClass().getMethod(methodName, value.getClass());
		method.invoke(obj, value);
		return obj;
	}
}
