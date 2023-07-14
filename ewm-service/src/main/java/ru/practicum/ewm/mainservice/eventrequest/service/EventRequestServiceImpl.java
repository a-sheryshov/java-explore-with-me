package ru.practicum.ewm.mainservice.eventrequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventRequestStatuses;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.eventrequest.mapper.EventRequestMapper;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.eventrequest.repository.EventRequestRepository;
import ru.practicum.ewm.mainservice.user.model.User;
import ru.practicum.ewm.mainservice.util.CommonService;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final CommonService commonService;

    @Override
    @Transactional
    public EventReqRequestDto create(long userId, long eventId) {
        EventRequest eventRequest;

        User user = commonService.findUserOrThrow(userId);
        Event event = commonService.findEventOrThrow(eventId);
        List<EventRequest> confirmedRequests = findConfirmedRequests(event.getId());
        checkCreateAvailability(user, event, confirmedRequests);

        eventRequest = save(user, event);
        log.info("Event request id = {} was created", eventRequest.getId());

        return EventRequestMapper.toEventRequestDto(eventRequest);
    }

    @Override
    public List<EventReqRequestDto> getAll(long userId) {
        List<EventRequest> eventRequests;

        User user = commonService.findUserOrThrow(userId);
        eventRequests = eventRequestRepository.findAllByRequester(user);
        log.info("Event request list for user id = {} was returned", userId);

        return EventRequestMapper.toEventRequestDto(eventRequests);
    }

    @Override
    @Transactional
    public EventReqRequestDto cancel(long userId, long requestId) {
        User user = commonService.findUserOrThrow(userId);
        EventRequest request = commonService.findEventRequestOrThrow(requestId);
        checkCancelAvailability(user, request);
        request.setStatus(commonService.findRequestStatusOrThrow(EventRequestStatuses.CANCELED));
        log.info("Event request id = {} was canceled", requestId);

        return EventRequestMapper.toEventRequestDto(request);
    }

    private EventRequest save(User user, Event event) {
        EventRequest eventRequest = new EventRequest();

        eventRequest.setCreated(LocalDateTime.now());
        eventRequest.setEvent(event);
        eventRequest.setRequester(user);

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            eventRequest.setStatus(commonService.findRequestStatusOrThrow(EventRequestStatuses.PENDING));
        } else {
            eventRequest.setStatus(commonService.findRequestStatusOrThrow(EventRequestStatuses.CONFIRMED));
        }

        return eventRequestRepository.save(eventRequest);
    }

    private void checkCreateAvailability(User user, Event event, List<EventRequest> confirmedRequests) {
        if (Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new OperationFailedException(
                    "Wrong event request - from event initiator"
            );
        }
        if (event.getState().getId() != 2) {
            throw new OperationFailedException(
                    "Wrong event request - event was not published"
            );
        }
        if (confirmedRequests.size() == event.getParticipantLimit() && confirmedRequests.size() != 0) {
            throw new OperationFailedException(
                    "Participant limit reached"
            );
        }
        if (eventRequestRepository.checkRequest(user.getId(), event.getId()) != 0) {
            throw new OperationFailedException(
                    "Request already exists"
            );
        }
    }

    private void checkCancelAvailability(User user, EventRequest request) {
        if (!Objects.equals(request.getRequester().getId(), user.getId())) {
            throw new OperationFailedException(
                    "Could be canceled only by request creator"
            );
        }
    }

    public List<EventRequest> findConfirmedRequests(long eventId) {
        return eventRequestRepository.findConfirmedRequests(eventId);
    }
}
