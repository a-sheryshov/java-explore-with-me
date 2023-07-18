package ru.practicum.ewm.mainservice.comment.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.mainservice.comment.dto.CommentCreateRequestDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentUpdateRequestDto;
import ru.practicum.ewm.mainservice.comment.model.Comment;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {EventMapper.class})
public interface CommentMapper {

    Comment toComment(CommentCreateRequestDto commentDtoCreate);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(CommentUpdateRequestDto commentDtoUpdate, @MappingTarget Comment comment);
}
