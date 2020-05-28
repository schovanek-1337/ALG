package cz.plesioEngine.gameLogic;

import cz.plesioEngine.debugConsole.ConsoleOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

/**
 *
 * @author plesio
 */
public class LogicMaster {

	private static List<Pair<Method, Object>> methodsToRun
		= new ArrayList<>();

	private static List<Pair<Method, Object>> methodsToRunEveryFrame
		= new ArrayList<>();
	
	private static List<Pair<Method, Object>> deletedMethods
		= new ArrayList<>();

	/**
	 * Runs all on-tick method, every tick.
	 */
	public static void tick() {
		for (Pair<Method, Object> pair : methodsToRun) {
			try {
				Method method = pair.getValue0();
				Object object = pair.getValue1();
				method.invoke(object, (Object[]) null);
			} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
				ConsoleOutput.appendToLog(ex.getMessage(), ConsoleOutput.LogType.ERR);
			}
		}
	}

	/**
	 * Runs all per-frame method, every frame.
	 */
	public static void everyFrameUpdate() {
		for (Pair<Method, Object> pair : methodsToRunEveryFrame) {
			try {
				Method method = pair.getValue0();
				Object object = pair.getValue1();
				method.invoke(object, (Object[]) null);
			} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
				ConsoleOutput.appendToLog(ex.getMessage(), ConsoleOutput.LogType.ERR);
			}
		}
		methodsToRunEveryFrame.removeAll(deletedMethods);
		deletedMethods.clear();
	}
	
	public static void clearMethods(){
		methodsToRun.clear();
		methodsToRunEveryFrame.clear();
	}

	/**
	 * Register method to tick every game tick.
	 *
	 * @param m method.
	 * @param o object.
	 */
	public static void registerMethod(Method m, Object o) {
		methodsToRun.add(Pair.with(m, o));
	}

	/**
	 * Register method to tick every frame.
	 *
	 * @param m method.
	 * @param o object.
	 */
	public static void registerMethodToRunEveryFrame(Method m, Object o) {
		methodsToRunEveryFrame.add(Pair.with(m, o));
	}
	
	public static void removePerFrameMethod(Method m, Object o){
		deletedMethods.add(Pair.with(m, o));
	}

}
