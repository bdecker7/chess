package client;

import ServerFacade.ServerFacade;
import dataaccess.UserSQL;
import dataaccess.exceptions.AlreadyTakenException;
import org.junit.jupiter.api.*;
import records.LogOutRequest;
import records.LoginRequest;
import records.RegisterRequest;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static ServerFacade facade;
    private static Server server;
    ServerFacade facad;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear(){

    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void registerTest() throws Exception {
        RegisterRequest request = new RegisterRequest("player", "password", "p1@email.com");
        var authData = facade.register(request);
        Assertions.assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void registerTestFail() throws Exception {
        RegisterRequest request = new RegisterRequest("playTEST", "password", "p1@email.com");
        facade.register(request);
        RegisterRequest request2 = new RegisterRequest("playTEST", "pass", "p1@email.com");

        IOException thrown = assertThrows(
                IOException.class,
                () -> facade.register(request2)
        );
    }
    @Test
    public void loginTest() throws Exception {
        LoginRequest request = new LoginRequest("player1", "password");
        var authData = facade.login(request);
        Assertions.assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void loginTestFail() throws Exception {
        LoginRequest request = new LoginRequest("player1", "WRONG_PASSWORD");
        IOException thrown = assertThrows(
                IOException.class,
                () -> facade.login(request)
        );
    }
    @Test
    public void logoutTest() throws Exception {
        RegisterRequest request = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(request);
        LogOutRequest request2 = new LogOutRequest(authData.authToken());


    }
    @Test
    public void logoutTestFail() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGameTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGameTestFail() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestFail() {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGameTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGameTestFail() {
        Assertions.assertTrue(true);
    }

}
