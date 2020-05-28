package cz.plesioEngine.guis;

import cz.plesioEngine.guis.fontMeshCreator.GUIText;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plesio
 */
public class Menu {

	private List<MenuItem> menuOptions = new ArrayList<>();

	/**
	 * Returns the mouse-hovered item.
	 * @param xPos mouse xPos
	 * @param yPos mouse yPos
	 * @return hovered item, null otherwise.
	 */
	public MenuItem getHover(float xPos, float yPos) {

		for (MenuItem option : menuOptions) {

			option.getText().setColor(1, 1, 1);

			int characters = option.getText().getVertexCount() / 6;
			float fontsize = option.getText().getFontSize();

			float a = characters;
			float b = 1;

			if (option.getText().getPosition().y() < yPos
				&& option.getText().getPosition().y() + 0.05f > yPos) {
				option.getText().setColor(0.5f, 0.5f, 0.5f);

				return option;

			}

		}

		return null;
	}

	public void addOption(MenuItem option) {
		menuOptions.add(option);
	}

	public void addOptions(List<GUIText> options) {
		menuOptions.addAll(menuOptions);
	}

	public void removeOption(MenuItem option) {
		menuOptions.remove(option);
	}

}
