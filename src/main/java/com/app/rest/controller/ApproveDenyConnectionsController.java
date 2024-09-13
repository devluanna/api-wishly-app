package com.app.rest.controller;

import com.app.domain.model.Connections;
import com.app.domain.model.RequestsByOthers;
import com.app.domain.model.RequestsByYou;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.service.ApproveDenyConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connection")
public class ApproveDenyConnectionsController {

    @Autowired
    ApproveDenyConnectionService approveDenyConnectionService;
    @PutMapping("/update-status")
    public ResponseEntity approveConnection(@RequestBody UpdateRequestDTO responseRequestDTO, Connections connections, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou
    ) {
        approveDenyConnectionService.approveConnectionRequest(responseRequestDTO, connections, requestsByOthers, requestsByYou);
        return ResponseEntity.ok("Request approved successfully!");
    }

    @DeleteMapping("/delete-connection")
    public ResponseEntity denyRequest(@RequestBody UpdateRequestDTO responseRequestDTO, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou) {

        approveDenyConnectionService.denyConnectionRequest(responseRequestDTO, requestsByOthers, requestsByYou);
        return ResponseEntity.ok("Request denied successfully!");
    }


}
