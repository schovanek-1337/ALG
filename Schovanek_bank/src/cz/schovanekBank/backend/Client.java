/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovanekBank.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author vit.schovanek
 */
public abstract class Client {
	
	@SuppressWarnings("ProtectedField")
	protected String name;
	private List<Account> accounts = new ArrayList<>();
	
	public void createAccount(float initialDeposit){
		accounts.add(new Account(initialDeposit));
	}
	
	public float totalBalance(){
		float finalBalance = 0;
		for (Account a : getAccounts()){
			finalBalance += a.getBalance();
		}
		return finalBalance;
	}
	
	public abstract String getName();

	/**
	 * @return the accounts
	 */
	public List<Account> getAccounts() {
		return Collections.unmodifiableList(accounts);
	}
	
}
