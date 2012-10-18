local math_random = math.random
local require = require
local Datastore = require("Datastore")
local Database = require("Database")
local Utils = require("Utils")
local S = require("S")

local syllables1 =
{
	"An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
	"Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
	"Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
	"Limi", "Ney"
}

local syllables2 =
{
	"the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
}

local syllables3 = 
	{
	"drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
	"cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
	"sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
}

local function create_name()
	local s = syllables1[math_random(#syllables1)]
	if (math_random(2) == 1) then
		s = s .. syllables2[math_random(#syllables2)]
	end
	s = s .. syllables3[math_random(#syllables3)]
	return s
end

local function ordinate()
	local o = math.random() * S.GalacticRadius * 2 - S.GalacticRadius
	o = math.floor(o * 10) / 10
	return o
end

return
{
	InitialiseGalaxy = function (galaxy)
		local positions = {}
		while (#positions < S.NumberOfStars) do
			local x = ordinate()
			local y = ordinate()
			local hash = "x="..x.."y="..y
			if not positions[hash] then
				positions[#positions+1] = {x=x, y=y}
				positions[hash] = true
			end
		end

		local names = {}
		while (#names < S.NumberOfStars) do
			local n = create_name()
			if not names[n] then
				names[#names+1] = n
				names[n] = true
			end
		end
				 
		for i = 1, S.NumberOfStars do
			local s = Datastore.Create("SStar")
			s.Name = names[i]
			s.X = positions[i].x 
			s.Y = positions[i].y
			s.Brightness = 1.0 + math.random()*9.0
			s.AsteroidsC = math.random(10)+10
			s.AsteroidsM = math.random(10)+10

			galaxy.AllLocations = galaxy.AllLocations + s
			galaxy.VisibleStars = galaxy.VisibleStars + s
		end		
	end,
	
	CreatePlayer = function (name, empire, email, password)
		local playercount = Database.SQL(
			"SELECT COUNT(*) FROM players WHERE email = ?"
			):bind(email):step()[1]
		if (playercount > 0) then
			return nil, "PlayerExists"
		end
		 
		local player = Datastore.Create("SPlayer")
		player.Name = name
		player.EmpireName = empire
		player.Email = email
		player.Password = password
		
		local stars = Datastore.Object(0).Galaxy.VisibleStars
		local star = stars[math.random(#stars)]
		
		local fleet = Datastore.Create("SFleet")
		star:Add(fleet)
		fleet.Name = name .. "'s starter fleet"
		player.Fleets = player.Fleets + fleet
		
		fleet:Create("SJumpship")
		fleet:Create("STug")
		fleet:Create("SCargoship")
		
		Database.SQL("INSERT INTO players VALUES (?, ?)")
			:bind(email, player.Oid):step()
		return player
	end
}
