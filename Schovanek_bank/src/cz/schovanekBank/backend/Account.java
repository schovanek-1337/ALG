/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovanekBank.backend;

/**
 *
 * @author vit.schovanek
 */
public class Account {

	private float balance;
	
	public Account(float initialDeposit){
		this.balance = initialDeposit;
	}
	
	public void deposit(float amount){
		this.balance += amount;
	}
	
	public void withdraw(float amount){
		if (balance - amount < 0){
			throw new IllegalArgumentException("This bank account is not allowed" + 
					"to overdraw.");
		} else {
			this.balance -= amount;
		}
	}
	
	public float getBalance(){
		return balance;
	}
	
}
