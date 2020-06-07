package com.kaue.converter;

import org.springframework.core.convert.converter.Converter;

import com.kaue.model.Item;

public class StringToItemConverter implements Converter<String, Item> {
 
    @Override
    public Item convert(String from) {
        String[] data = from.split(",");
        return new Item();
    }
}
