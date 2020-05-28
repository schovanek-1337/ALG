package cz.plesioEngine.gameLogic.music;

import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.gameLogic.SettingMaster;
import cz.plesioEngine.toolbox.FuzzyMaths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class MusicTrigger {
	
	private Source musicSource = new Source();
	private int musicBuffer = 0;
	
	private Vector3f position;

	public MusicTrigger(Vector3f position, int buffer) {
		this.position = position;
		this.musicBuffer = buffer;
		register();
	}
	
	public void update(){
		if(checkPlayerNear() && !(musicSource.isPlaying())
			&& SettingMaster.enableMusic){
			musicSource.play(musicBuffer);
		}
		if(musicSource.isPlaying() && !SettingMaster.enableMusic){
			musicSource.stop();
		}
	}
	
	private boolean checkPlayerNear(){
		return FuzzyMaths.compareVectorsFuzzy(
			Camera.getPositionInstance(), position, 50);
	}

	private void register(){
		try {
			LogicMaster.registerMethodToRunEveryFrame(
				MusicTrigger.class.getMethod("update", 
					(Class<?>[]) null), this);
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(MusicTrigger
				.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void reset(){
		register();
	}
	
	public Source getSource(){
		return musicSource;
	}
	
	
	
}
