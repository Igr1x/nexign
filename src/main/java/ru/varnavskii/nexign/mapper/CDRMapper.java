package ru.varnavskii.nexign.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.varnavskii.nexign.dto.CDRIn;
import ru.varnavskii.nexign.dto.CDROut;
import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.entity.SubscriberEntity;
import ru.varnavskii.nexign.enumeration.CallType;
import ru.varnavskii.nexign.service.SubscriberService;

@Component
public class CDRMapper {

    private SubscriberService subscriberService;
    private final ModelMapper modelMapper;

    public CDRMapper(SubscriberService subscriberService, ModelMapper modelMapper) {
        this.subscriberService = subscriberService;
        this.modelMapper = modelMapper;
        addMappings(modelMapper);
    }

    public CDREntity toEntity(CDRIn cdrIn) {
        return modelMapper.map(cdrIn, CDREntity.class);
    }

    public CDROut toOut(CDREntity cdrEntity) {
        return modelMapper.map(cdrEntity, CDROut.class);
    }

    private void addMappings(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<CDRIn, CDREntity>() {
            @Override
            protected void configure() {
                skip().setId(null); // ID пусть остается null
                using(callTypeConverter).map(source.getCallType(), destination.getCallType());
                using(subscriberByPhoneConverter).map(source.getCallingPhone(), destination.getCalling());
                using(subscriberByPhoneConverter).map(source.getReceivingPhone(), destination.getReceiving());
            }
        });

        modelMapper.addMappings(new PropertyMap<CDREntity, CDROut>() {
            @Override
            protected void configure() {
                using(phoneFromSubscriberConverter).map(source.getCalling(), destination.getCallingPhone());
                using(phoneFromSubscriberConverter).map(source.getReceiving(), destination.getReceivingPhone());
            }
        });
    }

    private final Converter<String, CallType> callTypeConverter = ctx ->
        switch (ctx.getSource()) {
            case "01" -> CallType.OUTGOING;
            case "02" -> CallType.INCOMING;
            default -> throw new IllegalArgumentException("Unexpected call type: " + ctx.getSource());
        };

    private final Converter<String, SubscriberEntity> subscriberByPhoneConverter = ctx -> {
        var phone = ctx.getSource().replace("+", ""); // Убираем "+"
        return subscriberService.getSubscriberByPhoneOrThrowException(phone);
    };

    private final Converter<SubscriberEntity, String> phoneFromSubscriberConverter = ctx ->
        ctx.getSource().getPhoneNumber();
}
