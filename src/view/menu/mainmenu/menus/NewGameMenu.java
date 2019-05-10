package view.menu.mainmenu.menus;

import javafx.scene.control.Label;
import model.player.PlayerType;
import model.ui.menu.Menu;
import model.ui.menu.MenuButton;
import model.ui.menu.MenuButtonTransition;
import view.GameViewManager;
import view.menu.mainmenu.MenuScene;

public class NewGameMenu extends Menu {
    public NewGameMenu(MenuScene menuScene) {
        super(menuScene);

        GameViewManager gameViewManager = new GameViewManager();

        Label lbl_newGameMenu = MenuScene.createMenuTitle("Choose Game Type");

        addNodeAll(
                lbl_newGameMenu,
                new MenuButton("New Campaign", () -> {
                    menuScene.stopLoop();
                    gameViewManager.createNewGame(menuScene.getPrimaryStage(), PlayerType.ROBOT);
                }),
                new MenuButton("Endless"),
                new MenuButtonTransition("Back", this, Menus.Main, menuScene::closeDoor));
    }
}
