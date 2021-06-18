package com.skillfactory.android12.ilya;

import java.util.Scanner;

public interface Marketeer {

    void makeService(Warrior warrior, Scanner scanner);


    default void sayGoodByeNo() {
        System.out.println("Ну нет, так нет. Надумаешь - приходи.");
    }

    default void sayGoodByeYes() {
        System.out.println("Поздравляю с удачной покупкой.");
    }
}
