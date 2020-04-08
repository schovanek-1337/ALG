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
public class Company extends Client {

	public Company(String companyName){
		name = companyName;
	}
	
	@Override
	public String getName() {
		return "firma " + name;
	}
	
}
