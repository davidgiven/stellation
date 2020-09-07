package ui;

import interfaces.IUi;
import model.SThing;
import model.Properties;
import interfaces.IClock;
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

    public static function newStringViewer(ui: IUi, thing: SThing, property: StringProperty): IUiElement {
        var div = ui.newText("div", "");
        var update = n -> {
            div.setValue(property.get(thing));
        };

        thing.onPropertyChanged(property).handle(update);
        update(Noise);
        return div;
    }

    public static function newCurrentTimeViewer(ui: IUi): IUiElement {
        var clock = inject(IClock);

        var div = ui.newText("div", "");
        var update = time -> {
            div.setValue(formatTime(time));
        };

        clock.onTimeChanged().handle(update);
        update(clock.getTime());
        return div;
    }
}
