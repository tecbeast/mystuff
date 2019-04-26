package com.balancedbytes.game.roborally.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.balancedbytes.game.roborally.model.factory.FactoryBoard;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.PrettyPrint;

public class FactoryBoardReader {
	
	public static void main(String[] args) {
		if ((args == null) || (args.length != 1)) {
			System.err.println("USAGE: java " + FactoryBoardReader.class.getName() + " <filename>");
			return;
		}
		try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
			FactoryBoard floor = new FactoryBoard().fromJson(Json.parse(in));
			System.out.println(floor.toJson().toString(PrettyPrint.singleLine()));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
