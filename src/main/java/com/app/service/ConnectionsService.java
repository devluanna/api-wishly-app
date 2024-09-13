package com.app.service;

import com.app.domain.model.*;
import com.app.domain.model.ResponseDTO.ConnectionRequestDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConnectionsService {


    RequestsByYou requestConnection(ConnectionRequestDTO requestDTO, RequestsByYou requestsByYou, RequestsByOthers requestsByOthers);


    Connections removeConnection(UpdateRequestDTO responseRequestDTO, Connections connections);
}
