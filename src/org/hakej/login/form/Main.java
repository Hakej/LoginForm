package org.hakej.login.form;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class Main extends Application {
    private static int windowWidth = 500;
    private static int windowHeight = 400;

    private static String fileName = "test.txt";
    private static Text informationTarget;

    private static GridPane grid;
    private static Button signUpBtn;
    private static Button signInBtn;
    private static Button makeFileBtn;
    private static TextField userTextField;
    private static PasswordField pwBox;

    private static Text actionTarget = new Text();

    private static Button signOutBtn;
    @Override
    public void start(Stage primaryStage) {
        loadProgramForSignUp(primaryStage);
    }

    private static void loadProgramForSignUp(Stage primaryStage)
    {
        setWholeStageForSignUp(primaryStage);
        overrideSetOnActionsForSignUp(primaryStage);
        loadScene(primaryStage);
    }

    private static void setWholeStageForSignUp(Stage primaryStage)
    {
        primaryStage.setTitle("Login Form");

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Please enter your login and password.");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        signInBtn = new Button("Sign in");
        signUpBtn = new Button("Sign up");

        HBox hbSignInBtn = new HBox(10);
        hbSignInBtn.setAlignment(Pos.BOTTOM_RIGHT);

        hbSignInBtn.getChildren().add(signUpBtn);
        hbSignInBtn.getChildren().add(signInBtn);

        grid.add(hbSignInBtn, 1, 4);

        makeFileBtn = new Button("Make new file");
        HBox hbBtnMakeFile = new HBox(10);
        hbBtnMakeFile.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnMakeFile.getChildren().add(makeFileBtn);
        grid.add(hbBtnMakeFile, 0, 4);

        grid.add(actionTarget, 1, 6);

        informationTarget = new Text();
        grid.add(informationTarget, 1, 8);
    }

    private static void overrideSetOnActionsForSignUp(Stage primaryStage)
    {
        signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                signUpUser(userTextField.getText(), pwBox.getText());
            }
        });

        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                signInUser(userTextField.getText(), pwBox.getText(), primaryStage);
            }
        });

        makeFileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try
                {
                    makeNewFile();
                    actionTarget.setText("New file is made!");
                } catch(IOException ioe)
                {
                    System.out.println(ioe.toString());
                }
            }
        });
    }

    private static void overrideSetOnActionsForSignIn(Stage primaryStage)
    {
        signOutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                signOut(primaryStage);
            }
        });
    }

    private static void writeLoginAndPasswordToFile(String username, String password) throws IOException
    {
        try
        {
            validateTextInput(username);
            validateTextInput(password);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(username + " " + password + "\n");
            writer.close();
            informationTarget.setFill(Color.BLACK);
            informationTarget.setText("User has been successfully written to file.");
        }
        catch (IncorrectTextException ite)
        {
            informationTarget.setFill(Color.FIREBRICK);
            informationTarget.setText(ite.getMessage());
        }
    }

    private static void validateTextInput(String stringToValidate) throws IncorrectTextException
    {
        if(stringToValidate.isEmpty())
        {
            throw new IncorrectTextException("Neither of fields can be empty!");
        }
        if(stringToValidate.contains(" "))
        {
            throw new IncorrectTextException("Neither of fields can contain spaces!");
        }
    }

    private static void makeNewFile() throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.close();
    }

    private static boolean isUserInFile(String username, String password) throws IOException
    {
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine())
        {
            if (sc.next().equals(username))
            {
                if (sc.next().equals(password))
                {
                    return true;
                }
            }
            sc.nextLine();
        }
        return false;
    }

    private static void signInUser(String username, String password, Stage primaryStage)
    {
        try
        {
            if (isUserInFile(username, password))
            {
                loadProgramForSignIn(primaryStage);
            }
            else
            {
                informationTarget.setFill(Color.FIREBRICK);
                informationTarget.setText("Wrong username or password.");
            }
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.toString());
        }
    }

    private static void signUpUser(String username, String password)
    {
        try
        {
            writeLoginAndPasswordToFile(username, password);
        }
        catch(IOException error)
        {
            System.out.println(error.toString());
        }
    }

    private static void loadProgramForSignIn(Stage primaryStage)
    {
        setWholeStageForSignIn(primaryStage);
        overrideSetOnActionsForSignIn(primaryStage);
        loadScene(primaryStage);
    }

    private static void setWholeStageForSignIn(Stage primaryStage)
    {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("You're logged in!");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        signOutBtn = new Button("Sign out");

        HBox hbsignOutBtn = new HBox(10);
        hbsignOutBtn.setAlignment(Pos.BOTTOM_CENTER);

        hbsignOutBtn.getChildren().add(signOutBtn);

        grid.add(hbsignOutBtn, 1, 4);
    }

    private static void signOut(Stage primaryStage)
    {
        loadProgramForSignUp(primaryStage);
    }

    private static void loadScene(Stage primaryStage)
    {
        Scene scene = new Scene(grid, windowWidth, windowHeight);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
