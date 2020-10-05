package com.celik;

import com.celik.exception.DeadPlayerException;
import com.celik.exception.DoesNotExistException;
import com.celik.exception.InsufficientAmountException;
import com.celik.exception.TurnIsOverException;
import com.celik.provider.InputProvider;
import com.celik.provider.OutputProvider;

import java.util.List;
import java.util.stream.Collectors;

public class TradingCardGamePlay extends TradingCardGame {

    private static String PASS_KEY = "p";

    private static String DEAD_PLAYER_MESSAGE = "Dead player passed";
    private static String NO_PLAYABLE_CARD_MESSAGE = "Player has not any playable card. Passed";
    private static String PASS_MESSAGE = "Player passed";
    private static String INSUFFICIENT_MANNA_ERROR = "Mana is not enough to play the selected card";
    private static String WRONG_FORMAT_ERROR_MESSAGE = "Input is wrong please try again";

    private static String CONTINUE_REQUEST_MESSAGE = "Please provide any key to continue";
    private static String INPUT_REQUEST_MESSAGE =
                            String.format("Please provide a card id to play with active player. (Pass: %s)", PASS_KEY);

    InputProvider inputProvider;
    OutputProvider outputProvider;

    public TradingCardGamePlay(InputProvider inputProvider, OutputProvider outputProvider) {
        super();
        this.inputProvider = inputProvider;
        this.outputProvider = outputProvider;
    }

    public void playRound() throws TurnIsOverException, DeadPlayerException, DoesNotExistException {
        outputProvider.printOutput(lastState());

        if(getActivePlayer().isDead()) {
            outputProvider.printOutput(DEAD_PLAYER_MESSAGE);
            throw new TurnIsOverException();
        }

        if( !getActivePlayer().canPlayCard() ) {
            outputProvider.printOutput(NO_PLAYABLE_CARD_MESSAGE);
            throw new TurnIsOverException();
        }

        outputProvider.printOutput(getActivePlayer().getHand());
        outputProvider.printOutput(INPUT_REQUEST_MESSAGE);
        String command = inputProvider.getInput();
        if( command.toLowerCase().equals(PASS_KEY) ) {
            outputProvider.printOutput(PASS_MESSAGE);
            throw new TurnIsOverException();
        }

        try {

            int cardId = Integer.parseInt(command);
            playCardWithActivePlayer(cardId);

        } catch (NumberFormatException e) {
            outputProvider.printOutput(WRONG_FORMAT_ERROR_MESSAGE);
        } catch (InsufficientAmountException e) {
            outputProvider.printOutput(INSUFFICIENT_MANNA_ERROR);
        }
    }

    @Override
    protected void nextRound() {
        outputProvider.printOutput(CONTINUE_REQUEST_MESSAGE);
        inputProvider.getInput();
    }

    @Override
    protected void notifyPlayer(String message) {
        outputProvider.printOutput(message);
    }

    private String lastState() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n----------------- NEW ROUND -----------------");
        List<Player> deadPlayers = players.stream().filter(p -> p != getActivePlayer() && p.isDead()).collect(Collectors.toList());
        if( !deadPlayers.isEmpty() ) {
            sb.append("\n[DEAD PLAYERS]\n");
            sb.append(deadPlayers.stream().map(String::valueOf).collect(Collectors.joining("\n")));
        }


        List<Player> alivePlayers = players.stream().filter(p -> p != getActivePlayer() && p.isAlive()).collect(Collectors.toList());
        if( !alivePlayers.isEmpty() ) {
            sb.append("\n[TARGET PLAYERS]\n");
            sb.append(alivePlayers.stream().map(String::valueOf).collect(Collectors.joining("\n")));
        }

        sb.append("\n\n[ACTIVE PLAYER]\n");
        sb.append(getActivePlayer());

        return sb.toString();
    }
}
