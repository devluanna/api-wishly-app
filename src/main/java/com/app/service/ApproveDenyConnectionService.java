package com.app.service;

import com.app.domain.model.Connections;
import com.app.domain.model.RequestsByOthers;
import com.app.domain.model.RequestsByYou;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ApproveDenyConnectionService {

    RequestsByOthers approveConnectionRequest(UpdateRequestDTO responseRequestDTO, Connections connections, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou);

    void denyConnectionRequest(UpdateRequestDTO responseRequestDTO, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou);

}
