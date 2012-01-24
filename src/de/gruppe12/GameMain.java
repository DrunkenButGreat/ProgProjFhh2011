package de.gruppe12;

import java.io.IOException;

import de.gruppe12.gui.GameGui;
import de.gruppe12.logic.GameLog;
import de.gruppe12.logic.LogicMain;

public class GameMain {
	public static void main(String[] args) {
		// Init GUI
		GameGui gameGui = new GameGui();

		// Init LogicMain
		LogicMain logicMain = new LogicMain();

		// GUI und Logik verknüpfen
		gameGui.getController().setLogicMain(logicMain);

		gameGui.setLocationRelativeTo(null);
		gameGui.setVisible(true);

		try {
			GameLog.init("logfile.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}