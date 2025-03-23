package ru.varnavskii.nexign.controller.subscriber.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberIn;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberOut;
import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

@Component
public class SubscriberMapper {

    private final ModelMapper modelMapper;

    public SubscriberMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        addMappings(modelMapper);
    }

    public SubscriberEntity toEntity(SubscriberIn subscriberIn) {
        return modelMapper.map(subscriberIn, SubscriberEntity.class);
    }

    public SubscriberOut toOut(SubscriberEntity subscriber) {
        return modelMapper.map(subscriber, SubscriberOut.class);
    }

    private void addMappings(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<SubscriberIn, SubscriberEntity>() {
            @Override
            protected void configure() {
                skip().setId(null);
                using(phoneNumberConverter).map(source.getPhoneNumber(), destination.getPhoneNumber());
            }
        });

        modelMapper.addMappings(new PropertyMap<SubscriberEntity, SubscriberOut>() {
            @Override
            protected void configure() {
            }
        });
    }

    private final Converter<String, String> phoneNumberConverter = source ->
        source.getSource().replaceAll("\\+", "");
}
