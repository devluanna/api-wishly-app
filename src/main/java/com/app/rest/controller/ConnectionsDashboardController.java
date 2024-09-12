package com.app.rest.controller;

import com.app.domain.model.Connections;

import com.app.domain.model.RequestsByOthers;
import com.app.domain.model.RequestsByYou;
import com.app.domain.model.ResponseDTO.ConnectionRequestDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.service.ConnectionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
public class ConnectionsDashboardController {

    @Autowired
    ConnectionsService connectionsService;


    @PostMapping("/add-user")
    public ResponseEntity requestConnection(@RequestBody ConnectionRequestDTO requestDTO, RequestsByYou requestsByYou, RequestsByOthers requestsByOthers) {

        RequestsByYou userAdded = connectionsService.requestConnection(requestDTO, requestsByYou, requestsByOthers);

        return ResponseEntity.ok(userAdded);
    }

    @PutMapping("/update-status")
    public ResponseEntity approveConnection(@RequestBody UpdateRequestDTO responseRequestDTO, Connections connections, RequestsByOthers requestsByOthers
                                                              ) {
        RequestsByOthers updatedConnection = connectionsService.approveConnectionRequest(responseRequestDTO, connections, requestsByOthers);
        return ResponseEntity.ok("Request approved successfully!");
    }



}
