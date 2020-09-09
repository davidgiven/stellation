package ui;

import interfaces.IUi;
import model.SThing;
import model.SShip;
import model.SStar;
import model.Properties;
import model.Properties.NAME;
import interfaces.IClock;
import interfaces.IGame;
import utils.Injectomatic.inject;
import tink.CoreApi;
import Std.string;
using StringTools;
using Math;

class UiTools {
    public static function formatTime(time: Float): String {
        var t = time / 3600.0;
        var mh = string(((t * 1000.0) % 1000.0).floor()).lpad("0", 3);
        var h = string((t % 1000.0).floor()).lpad("0", 3);
        var kh = string(((t / 1000.0) % 1000.0).floor()).lpad("0", 3);

        return '${kh}.${h}.${mh}';

    }

	public static function newSeparator(ui: IUi, text: String): IUiElement {
		return ui.newText("div", text)
			.addClasses(["separator"]);
	}

    public static function newStringViewer(ui: IUi, thing: SThing, property: StringProperty): IUiElement {
        var div = ui.newText("span", "");
        var update = n -> {
            div.setValue(property.get(thing));
        };

        thing.onPropertyChanged(property).handle(update);
        update(Noise);
        return div;
    }

    public static function newShipViewer(ui: IUi, ship: SShip): IUiElement {
        var div = ui.newText("a", "")
			.setAttr("href", "#");

		div.onClick().handle(n -> {
			inject(IGame).onShipClicked(ship);
		});

        var update = n -> {
            div.setValue(ship.name);
        };

        ship.onPropertyChanged(NAME).handle(update);
        update(Noise);
        return div;
    }

    public static function newStarViewer(ui: IUi, star: SStar): IUiElement {
        var div = ui.newText("a", "")
			.setAttr("href", "#");

		div.onClick().handle(n -> {
			inject(IGame).onStarClicked(star);
		});

        var update = n -> {
            div.setValue(star.name);
        };

        star.onPropertyChanged(NAME).handle(update);
        update(Noise);
        return div;
    }

    public static function newCurrentTimeViewer(ui: IUi): IUiElement {
        var clock = inject(IClock);

        var div = ui.newText("span", "");
        var update = time -> {
            div.setValue(formatTime(time));
        };

        clock.onTimeChanged().handle(update);
        update(clock.getTime());
        return div;
    }
}
