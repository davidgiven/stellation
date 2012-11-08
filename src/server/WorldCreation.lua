local math_random = math.random
local require = require
local Datastore = require("Datastore")
local Database = require("Database")
local Utils = require("Utils")
local Classes = require("Classes")
local SGalaxy = Classes.SGalaxy

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

local function round_to_tenths(n)
	return math.floor(n*10) / 10
end

local function ordinate()
	local o = math.random() * SGalaxy.statics.GalacticRadius * 2 - SGalaxy.statics.GalacticRadius
	o = round_to_tenths(o)
	return o
end

return
{
	InitialiseGalaxy = function (galaxy)
		local universe = Datastore.Object(0)
		universe.Owner = universe
		galaxy.Owner = universe
		
		local positions = {}
		while (#positions < galaxy.NumberOfStars) do
			local theta = math.random() * 2 * math.pi
			local r = math.random() * SGalaxy.statics.GalacticRadius
			local x = round_to_tenths(math.sin(theta) * r)
			local y = round_to_tenths(math.cos(theta) * r)
			local hash = "x="..x.."y="..y
			if not positions[hash] then
				positions[#positions+1] = {x=x, y=y}
				positions[hash] = true
			end
		end

		local names = {}
		while (#names < galaxy.NumberOfStars) do
			local n = create_name()
			if not names[n] then
				names[#names+1] = n
				names[n] = true
			end
		end
				 
		for i = 1, galaxy.NumberOfStars do
			local s = Datastore.Create("SStar")
			s.Owner = universe
			s.Name = names[i]
			s.X = positions[i].x 
			s.Y = positions[i].y
			s.Brightness = 1.0 + round_to_tenths(math.random()*9.0)
			s.AsteroidsC = math.random(10)+10
			s.AsteroidsM = math.random(10)+10
			s.ResourcesA = 0
			s.ResourcesO = 0
			s.ResourcesM = 0

			galaxy.AllLocations:Add(s)
			galaxy.VisibleStars:Add(s)
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
		player.Owner = player
		player.Name = name
		player.EmpireName = empire
		player.Email = email
		player.Password = password
		
		local stars = Datastore.Object(0).Galaxy.VisibleStars
		local star = stars:RandomItem()
		
		local fleet = Datastore.Create("SFleet")
		star:Add(fleet)
		fleet.Owner = player
		fleet.Name = name .. "'s starter fleet"
		player.Fleets:Add(fleet)
		
		fleet:Create("SJumpship")
		fleet:Create("STug")
		fleet:Create("SCargoship")
		
		Database.SQL("INSERT INTO players VALUES (?, ?)")
			:bind(email, player.Oid):step()
		return player
	end
}
