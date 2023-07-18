package ru.practicum.ewm.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.mapper.CommentMapper;
import ru.practicum.ewm.mainservice.comment.repository.CommentRepository;
import ru.practicum.ewm.mainservice.util.CommonService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPublicServiceImpl implements CommentPublicService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final CommonService commonService;

    @Override
    public List<CommentResponseDto> getCommentsByEventId(Long eventId, int from, int size) {
        commonService.findEventOrThrow(eventId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

}
