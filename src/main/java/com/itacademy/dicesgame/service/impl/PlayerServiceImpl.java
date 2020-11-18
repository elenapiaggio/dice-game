package com.itacademy.dicesgame.service.impl;

import com.itacademy.dicesgame.entity.Game;
import com.itacademy.dicesgame.entity.Player;
import com.itacademy.dicesgame.repository.IGameRepository;
import com.itacademy.dicesgame.repository.IPlayerRepository;
import com.itacademy.dicesgame.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements IPlayerService {

    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    private IGameRepository gameRepository;

    @Override
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @Override
    public Map<String, Double> getAllPlayersWithAvgSuccessRate() {
        List<Player> listAllPlayers = playerRepository.findAll();
        Map<String, Double> mapPlayersWithAvgSuccessRate = new HashMap<String, Double>();

        if(listAllPlayers != null && listAllPlayers.size() > 0){
            List<Game> gamesofActualPlayer = new ArrayList<Game>();

            for(Player player: listAllPlayers){
                gamesofActualPlayer = gameRepository.getGamesByPlayerId(player.getId());

                if(gamesofActualPlayer.size() > 0){
                    String key = player.getName();
                    Double value = player.getSuccessRateByPlayer(gameRepository.getGamesByPlayerId(player.getId()));
                    System.out.println();
                    mapPlayersWithAvgSuccessRate.put(key,value);

                } else{
                    mapPlayersWithAvgSuccessRate.put(player.getName(), (double) 0) ;
                }
            }
        }
        return mapPlayersWithAvgSuccessRate;
    }

    @Override
    public List<Player> getPlayersRanking() {
        List<Player> listAllPlayers = playerRepository.findAll();
        List<Game> listOfGamesActualPlayer = new ArrayList<Game>();

        if(listAllPlayers != null && listAllPlayers.size() > 0){
            try {
                for(Player player: listAllPlayers){
                    listOfGamesActualPlayer = gameRepository.getGamesByPlayerId(player.getId());
                    Double successRate = player.getSuccessRateByPlayer(listOfGamesActualPlayer);
                    player.setSuccessRate(successRate.toString());
                }
                listAllPlayers.sort(Comparator.comparing(Player::getSuccessRate).reversed());
            } catch (Exception e){
                System.out.println("Error -> " + e.getMessage());
            }
        }
        return listAllPlayers;
    }

    @Override
    public Player findPlayer(Long player_id){
        return playerRepository.findById(player_id);
    }

    @Override
    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long player_id, Player player){
        Player oldPlayer = playerRepository.findById(player_id);

        player.setId(player_id);
        player.setRegistration_date(oldPlayer.getRegistration_date());

        return  playerRepository.save(player);
    }

}
