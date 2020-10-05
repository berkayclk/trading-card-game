package com.celik;

import com.celik.exception.*;
import com.celik.provider.InputProvider;
import com.celik.provider.OutputProvider;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        InputProvider inputProvider = () -> scanner.next();
        OutputProvider outputProvider = System.out::println;
        TradingCardGame game = new TradingCardGamePlay(inputProvider, outputProvider);

        game.addPlayer("test");
        game.addPlayer("test 2");

        try {
            game.play();
        } catch (TradingCardException e) {
            outputProvider.printOutput(e.getMessage());
        }
        outputProvider.printOutput("--- GAME OVER ---");


        outputProvider.printOutput("\r\n********************** WIINNER **********************");
        game.getWinner().ifPresentOrElse(outputProvider::printOutput, () -> outputProvider.printOutput(" -- "));
    }
}
