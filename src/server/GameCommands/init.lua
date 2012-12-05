local require = require

return
{
	Ping = require("GameCommands.Ping"),
	ChangeFleetTarget = require("GameCommands.ChangeFleetTarget"),
	Jump = require("GameCommands.Jump"),
	UnitScrap = require("GameCommands.UnitScrap"),
	CargoshipLoadUnload = require("GameCommands.CargoshipLoadUnload"),
	TugUnload = require("GameCommands.TugUnload"),
	TugLoad = require("GameCommands.TugLoad"),
}
