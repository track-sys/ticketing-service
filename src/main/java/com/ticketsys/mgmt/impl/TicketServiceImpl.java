package com.ticketsys.mgmt.impl;

import com.ticketsys.mgmt.domain.TicketInfo;
import com.ticketsys.mgmt.dto.request.TicketInfoRequest;
import com.ticketsys.mgmt.dto.response.TicketInfoResponse;
import com.ticketsys.mgmt.repository.TicketRepository;
import com.ticketsys.mgmt.service.TicketService;
import com.ticketsys.mgmt.util.MapperUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ticket service implementation.
 *
 * @author mdoss
 */
@Service
@NoArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    @NotNull
    private TicketRepository ticketRepository;

    /**
     * ticketInfo request map to domain (TicketInfo) object.
     * save transient object.
     * @param requestDto
     * @return ticketInfoResponse.
     */
    @Override
    public TicketInfoResponse save(TicketInfoRequest requestDto) {
       TicketInfo entity = MapperUtil.getInstance().mapRequestToTicketInfoDomain(requestDto);
       ticketRepository.save(entity);
       return MapperUtil.getInstance().mapTicketInfoDomainToResponse(entity);
    }

    /**
     * ticketInfo domain find by id, finally map to response object.
     * @param id
     * @return ticketInfoResponse.
     */
    @Override
    public TicketInfoResponse findById(Integer id) {
        TicketInfo entity = ticketRepository.findById(id).get();
        return MapperUtil.getInstance().mapTicketInfoDomainToResponse(entity);
    }

    /**
     *
     * @param page
     * @param size
     * @return page<TicketInfoResponse> object.
     */
    @Override
    public Page<TicketInfoResponse> loadPage(int page, int size) {
        Page<TicketInfo> ticketPage = ticketRepository.findAll(PageRequest.of(page, size, Sort.by("ticketId").descending()));
        if(Objects.isNull(ticketPage) || ticketPage.isEmpty()) {
            return Page.empty();
        }
        List<TicketInfoResponse> responseList = ticketPage.get()
                .map(ticketInfo -> MapperUtil.getInstance().mapTicketInfoDomainToResponse(ticketInfo))
                .collect(Collectors.toList());
        Page<TicketInfoResponse> pages = new PageImpl<>(responseList, ticketPage.getPageable(), responseList.size());
        return pages;
    }
}
