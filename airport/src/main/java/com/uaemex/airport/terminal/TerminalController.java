package com.uaemex.airport.terminal;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/terminals")
public class TerminalController {
    private final TerminalService terminalService;

    @GetMapping(path = "/{terminalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Terminal getTerminal(@PathVariable("terminalId") UUID terminalId){
        return terminalService.getTerminal(terminalId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Terminal> getTerminals(){
        return terminalService.getTerminals();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void registerNewTerminal(
            @RequestBody Terminal terminal){
        terminalService.addNewTerminal(terminal);
    }

    @PostMapping(path = "{terminalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void addTerminalBoardingRoom(
            @PathVariable("terminalId") UUID terminalId,
            @RequestParam UUID boardingRoomId){
        terminalService.addTerminalBoardingRoom(terminalId, boardingRoomId);
    }

    @DeleteMapping(path = "{terminalId}/{boarding_roomId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void removeTerminalBoardingRoom(
            @PathVariable("terminalId") UUID terminalId,
            @PathVariable("boarding_roomId") UUID boardingRoomId){
        terminalService.removeTerminalBoardingRoom(terminalId, boardingRoomId);
    }

    @DeleteMapping(path = "{terminalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteTerminal(
            @PathVariable("terminalId") UUID terminalId){
        terminalService.deleteTerminal(terminalId);
    }
}
