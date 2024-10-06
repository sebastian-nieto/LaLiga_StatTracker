package com.laliga.laliga_analyzed.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/player")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDTO> getPlayers(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String nation) {

        List<Player> players;

        // Fetch players based on query parameters
        if (team != null && position != null) {
            players = playerService.getPlayerByTeamAndPosition(team, position);
        } else if (team != null) {
            players = playerService.getPlayersFromTeam(team);
        } else if (name != null) {
            players = playerService.getPlayersByName(name);
        } else if (position != null) {
            players = playerService.getPlayersByPosition(position);
        } else if (nation != null) {
            players = playerService.getPlayerByNationality(nation);
        } else {
            players = playerService.getPlayers();
        }

        // If players list is null, return an empty list to avoid NullPointerException
        if (players == null) {
            players = Collections.emptyList();
        }

        // Convert List<Player> to List<PlayerDTO> and filter out nulls
        return players.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> addPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = convertToEntity(playerDTO);
        Player createdPlayer = playerService.addPlayer(player);
        return new ResponseEntity<>(convertToDTO(createdPlayer), HttpStatus.CREATED);
    }

    @PutMapping("/{playerName}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable String playerName, @RequestBody PlayerDTO playerDTO) {
        // Ensure playerDTO contains the playerName
        playerDTO.setName(playerName);
        Player player = convertToEntity(playerDTO);
        Player updatedPlayer = playerService.updatePlayer(player);
        if (updatedPlayer != null) {
            return new ResponseEntity<>(convertToDTO(updatedPlayer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{playerName}")
    public ResponseEntity<String> deletePlayer(@PathVariable String playerName) {
        playerService.deletePlayer(playerName);
        return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
    }

    // Helper methods to convert between Player and PlayerDTO
    private PlayerDTO convertToDTO(Player player) {
        if (player == null) {
            return null; // Return null if the player is null to avoid NullPointerException
        }
        PlayerDTO dto = new PlayerDTO();
        dto.setName(player.getName());
        dto.setNation(player.getNation());
        dto.setPosition(player.getPosition());
        dto.setAge(player.getAge());
        dto.setMp(player.getMp());
        dto.setStarts(player.getStarts());
        dto.setMin(player.getMin());
        dto.setGoals(player.getGoals());
        dto.setAst(player.getAst());
        dto.setPk(player.getPk());
        dto.setCrdy(player.getCrdy());
        dto.setCrdr(player.getCrdr());
        dto.setXg(player.getXg());
        dto.setXag(player.getXag());
        dto.setTeam(player.getTeam());
        return dto;
    }

    private Player convertToEntity(PlayerDTO dto) {
        Player player = new Player();
        player.setName(dto.getName());
        player.setNation(dto.getNation());
        player.setPosition(dto.getPosition());
        player.setAge(dto.getAge());
        player.setMp(dto.getMp());
        player.setStarts(dto.getStarts());
        player.setMin(dto.getMin());
        player.setGoals(dto.getGoals());
        player.setAst(dto.getAst());
        player.setPk(dto.getPk());
        player.setCrdy(dto.getCrdy());
        player.setCrdr(dto.getCrdr());
        player.setXg(dto.getXg());
        player.setXag(dto.getXag());
        player.setTeam(dto.getTeam());
        return player;
    }
}
