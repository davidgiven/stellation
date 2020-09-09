package interfaces;

import model.SStar;
import model.SShip;

interface IGame {
	public function onStarClicked(star: SStar): Void;
	public function onShipClicked(ship: SShip): Void;
}

