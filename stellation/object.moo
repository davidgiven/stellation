rem object.moo
rem Object superclass
.patch object.moo 3 1
notify(player, "object.moo");

$object.name = "Generic Object";

# --- Constructor -------------------------------------------------------------

.program $god $object:create tnt
	{?owner=player} = args;
	obj = create(this, owner);
	obj.name = this.name;
	return obj;
.

.program $god $object:initialize tnt
.

# --- Destructor --------------------------------------------------------------

.program $god $object:destroy tnt
	notify($god, "destroying "+tostr(this));
	move(this, #-1);
	recycle(this);
.

# --- Move object -------------------------------------------------------------

.program $god $object:moveto tnt
	{whence} = args;
	move(this, whence);
.

# --- Test for ancestry -------------------------------------------------------

.program $god $object:descendentof tnt
	{ancestor} = args;
	i = this;
	while (i != #-1)
		if (i == ancestor)
			return 1;
		endif
		i = parent(i);
	endwhile
	return 0;
.

# --- Return list of all properties -------------------------------------------

.program $god $object:properties tnt
	list = {};
	i = this;
	while (i != #-1)
		for j in (properties(i))
			list = setadd(list, j);
		endfor
		i = parent(i);
	endwhile
	return list;
.

# --- Return list of all verbs ------------------------------------------------

.program $god $object:verbs tnt
	list = {};
	i = this;
	while (i != #-1)
		for j in (verbs(i))
			list = setadd(list, j);
		endfor
		i = parent(i);
	endwhile
	return list;
.

# --- Return list of all contents ---------------------------------------------

.program $god $object:contents tnt
	return this.contents;
.

# --- Return object's name ----------------------------------------------------

.program $god $object:name tnt
	return this.name;
.

.quit

