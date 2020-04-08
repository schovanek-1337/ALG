/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovanekBank.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vit.schovanek
 */
public class Person extends Client {
	
	public Person(String personName){
		name = personName;
	}
	
	@Override
	public String getName() {
		Matcher m = Pattern.compile("ova\\b").matcher(name);
		return (m.find()) ? ("Pani " + name) : ("Pan " + name);
	}
	
}