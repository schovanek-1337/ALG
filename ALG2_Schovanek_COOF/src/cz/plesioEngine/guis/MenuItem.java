package cz.plesioEngine.guis;

import cz.plesioEngine.engineTester.MainMenu;
import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javatuples.Pair;

/**
 *
 * @author plesio
 */
public class MenuItem {

	private final GUIText text;

	private final Pair<Object, Method> function;

	public MenuItem(GUIText text, Pair<Object, Method> function) {
		this.text = text;
		this.function = function;
	}

	/**
	 * Runs on activation.
	 */
	public void activate() {
		try {
			function.getValue1().invoke(function.getValue0(),
				function.getValue1().getParameterTypes());
		} catch (IllegalAccessException
			| IllegalArgumentException
			| InvocationTargetException ex) {
			Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public GUIText getText() {
		return text;
	}

}
