package interfaces;

interface IClock {
    function setTime(serverTime: Float): Void;
    function getTime(): Float;
}

