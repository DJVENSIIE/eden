package com.lgs.eden.views.login;

import com.lgs.eden.api.Api;
import com.lgs.eden.api.wrapper.LoginResponseData;
import com.lgs.eden.application.AppWindowHandler;
import com.lgs.eden.application.PopupUtils;
import com.lgs.eden.utils.Config;
import com.lgs.eden.utils.ViewsPath;
import com.lgs.eden.utils.helper.LoginRegisterForm;

import com.lgs.eden.application.WindowController;
import com.lgs.eden.views.register.Register;
import com.lgs.eden.views.settings.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller for Login.
 */
public class Login extends LoginRegisterForm {

    // ------------------------------ STATIC ----------------------------- \\

    /**
     * @return Login screen
     **/
    public static Parent getScreen() {
        return LoginRegisterForm.getScreen(ViewsPath.LOGIN.path);
    }

    // ------------------------------ INSTANCE ----------------------------- \\

    @FXML
    private CheckBox rememberMe;

    public Login() {
    }

    @Override
    public void resetForm() {
        // get stored if we do have one
        String stored_username = Config.getStoredUsername();
        this.login.setText(stored_username);
        this.rememberMe.setSelected(!stored_username.isEmpty());
        this.password.setText("tester");
    }

    // ------------------------------ METHODS ----------------------------- \\

    @FXML
    @Override
    public void onSubmitWithButton() { // TODO: translate
        String username = this.login.getText();
        String pwd = this.password.getText();
        StringBuilder error = new StringBuilder(); // for error message

        // testing username, password compatibility with the API
        if (checkUsername(username)) error.append("wrong username\n");
        if (checkPassword(pwd)) error.append("wrong password\n");

        if (error.toString().isEmpty()){ // no error
            // add user
            LoginResponseData response = Api.login(username, pwd);
            if (response.code == 0){ // this is an user id
                // so we are good
                AppWindowHandler.changeToAppWindow(response);
            } else {
                error.append("Invalid credentials\n");
            }

            // store or not username
            Config.lastUsername(username, rememberMe.isSelected());
        }

        if (!error.toString().isBlank()){
            PopupUtils.showPopup(error.toString());
        }
    }

    /**
     * Action to redirect the user to the link to recover his password
     */
    @FXML
    public void onForgotPassword() { // rename @ignore if you use it
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(Api.passwordForgotPage(Config.getLanguage().code)));
            } catch (URISyntaxException | IOException ignoreMeTooBlink) {
                // TODO: add popup related to these exceptions
                System.out.println("exception has occurred");
            }
        }
    }

    @FXML
    public void onSigninIsPressed(ActionEvent ignore) {
        WindowController.setScreen(Register.getScreen());
    }

    @FXML
    public void onSettingsPressed(ActionEvent ignore) {
        Settings.setBackScreen(ViewsPath.LOGIN);
        WindowController.setScreen(Settings.getScreen());
    }

}
