package interfaces;

import tink.CoreApi;

@:tink
interface IClock {
    function setTime(serverTime: Float): Void;
    function getTime(): Float;

	function onTimeChanged(): Signal<Float>;
}

