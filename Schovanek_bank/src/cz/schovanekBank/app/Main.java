/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovanekBank.app;

import cz.schovanekBank.backend.*;

/**
 *
 * @author vit.schovanek
 */
public class Main {
	
	public static void main(String args[]){
		Client[] clients = new Client[3];
		clients[0] = new Person("Pekar");
		clients[1] = new Person("Svecova");
		clients[2] = new Company("Skoda");
		
		clients[0].createAccount(1000);
		clients[0].createAccount(500);
		
		clients[1].createAccount(1200);
		
		clients[2].createAccount(120);
		
		for (Client client : clients) {
			System.out.println(client.getName());
		}
	}
	
}
