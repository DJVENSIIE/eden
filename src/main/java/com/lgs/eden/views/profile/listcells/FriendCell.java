package com.lgs.eden.views.profile.listcells;

import com.lgs.eden.api.wrapper.Friend;
import com.lgs.eden.utils.Utility;
import com.lgs.eden.utils.ViewsPath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class FriendCell extends ListCell<Friend> {

    private Node graphic;

    private FriendCellController controller;

    public FriendCell() {
        FXMLLoader loader = Utility.loadView(ViewsPath.FRIEND_CELL.path);
        graphic = Utility.loadViewPane(loader);
        controller = loader.getController();
    }


}
