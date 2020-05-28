package cz.plesioEngine.audio;

import java.io.IOException;

/**
 *
 * @author plesio
 */
public class Tester {

	public static void main(String[] args) throws IOException {
		AudioMaster.init();
		//AudioMaster.setListenerData(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

		int buffer = AudioMaster.loadSound("res/soundtrack/e1m1/session.ogg");
		Source source = new Source();
		source.setLooping(true);
		source.play(buffer);

		char c = ' ';
		while (c != 'q') {
			c = (char) System.in.read();

			if (c == 'p') {
				if (source.isPlaying()) {
					source.pause();
				} else {
					source.continuePlaying();
				}
			}
		}

		source.delete();
		AudioMaster.cleanUp();

	}

}
