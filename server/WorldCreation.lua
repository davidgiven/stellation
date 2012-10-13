local math_random = math.random
local require = require
local Datastore = require("Datastore")

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

return
{
	InitialiseGalaxy = function (galaxy)
		for i = 1, 10 do
			local s = Datastore.Create("SStar")
			s.Name = create_name()
			s.X = math.random() * 10 - 5 
			s.Y = math.random() * 10 - 5 
			s:commit()
		end		
	end
}
