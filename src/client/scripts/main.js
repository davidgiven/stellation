"use strict";

define(
	["jquery", "GameStateMachine"],
	function ($, GameStateMachine)
	{
		return {
			main: GameStateMachine.Start
		}; 
	}
);

