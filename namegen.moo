rem namegen.moo
rem Name generator
rem $Source: /cvsroot/stellation/stellation/namegen.moo,v $
rem $State: Exp $

.patch namegen.moo 3 1
notify(player, "namegen.moo");

# Name generator is added to $stringutils.

$god:prop($stringutils, "syllables1", {});
$god:prop($stringutils, "syllables2", {});
$god:prop($stringutils, "syllables3", {});
$stringutils.syllables1 = {"an", "ca", "jo", "ka", "kri", "da", "re", "de", "ed", "ma", "ni", "qua", "qa", "li", "la", "in", "on", "an", "un", "ci", "cu", "ce", "co", "xa", "xef", "xii", "xo'o", "xu", "ram", "noq", "mome", "pawa", "limi", "ney"};
$stringutils.syllables2 = {"the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"};
$stringutils.syllables3 = {"drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven", "cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan", "sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"};

.program $god $stringutils:namegen tnt
	{?number=-1} = args;
	if (number == -1)
		s0 = random(2);
		s1 = random(length(this.syllables1));
		s2 = random(length(this.syllables2));
		s3 = random(length(this.syllables3));
	else
		s0 = number % 2;
		number = number / 2;
		s1 = number % length($stringutils.syllables1);
		number = number / length($stringutils.syllables1);
		/* s2 = number % length($stringutils.syllables2); */
		/* number = number / length($stringutils.syllables2); */
		s3 = number % length($stringutils.syllables3);
		number = number / length($stringutils.syllables3);
		if (number > 0)
			s2 = number;
			s1 = 1;
		else
			s0 = 0;
			s2 = 0;
		endif
		s1 = s1 + 1;
		s2 = s2 + 1;
		s3 = s3 + 1;
	endif
	name = this.syllables1[s1];
	if (s0 == 1)
		name = name + this.syllables2[s2];
	endif
	name = name + this.syllables3[s3];
	return name;
.

.quit

rem Revision History
rem $Log: namegen.moo,v $
rem Revision 1.1  2000/07/29 17:53:01  dtrg
rem Initial revision
rem

