package Handler;

import Model.UserData;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.*;
import dataaccess.UserDAO;

record RegisterRequest(String username, String password, String email){}
