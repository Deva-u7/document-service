package com.document.mapper;

import com.document.dto.DocumentDTO;
import com.document.entity.Document;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AbstractModelMapper {
    public abstract DocumentDTO toDTO(Document document);
}
