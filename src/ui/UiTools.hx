package ui;

import interfaces.IUi;
import model.SThing;
import model.Properties;
import interfaces.IClock;
import utils.Injectomatic.inject;
import tink.CoreApi;

class UiTools {
	public static function newStringViewer(ui: IUi, thing: SThing, property: StringProperty): IUiElement {
		var div = ui.newText("div", "");
		var update = n -> {
			div.setValue(property.get(thing));
		};

		thing.onPropertyChanged(property).handle(update);
		update(Noise);
		return div;
	}

	public static function newTimeViewer(ui: IUi): IUiElement {
		var clock = inject(IClock);

		var div = ui.newText("div", "");
		var update = n -> {
			div.setValue(Std.string(clock.getTime()));
		};

		update(Noise);
		return div;
	}
}
