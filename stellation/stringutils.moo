rem stringutils.moo
rem Assorted string utilities.

exec add_property(#0, "stringutils", create($object, $god), {$god, "r"})
exec $stringutils.name = "String Utilities"

rem --- Get the first word of a string ----------------------------------------

verb $god $stringutils firstword tnt
.program $stringutils:firstword
	"firstword(string, seperator) = {firstword, restofstring}";
	string = args[1];
	seperator = args[2];
	i = index(string, seperator);
	if (i == 0)
		return {string, ""};
	endif
	if (i == length(string))
		return {string[1..$-1], ""};
	endif
	return {string[1..i-1], string[i+1..$]};
.

rem --- Strip whitespace of front and end of a string -------------------------

verb $god $stringutils strip tnt
.program $stringutils:strip
	"strip(string)";
	string = args[1];
	while ((length(string) > 0) && (string[1] == " "))
		string = string[2..$];
	endwhile
	if (length(string) == 0)
		return "";
	endif
	while ((length(string) > 0) && (string[$] == " "))
		string = string[1..$-1];
	endwhile
	return string;
.

rem --- Convert from base64 ---------------------------------------------------

exec add_property($stringutils, "base64alphabet", "", {$god, "r"})
exec $stringutils.base64alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

verb $god $stringutils frombase64 tnt
.program $stringutils:frombase64
	{string} = args;
	len = length(string);
	string = strsub(string, "=", "A");
	result = "";
	i = 1;
	while (i < len)
		b1 = index(this.base64alphabet, string[i], 1) - 1;
		b2 = index(this.base64alphabet, string[i+1], 1) - 1;
		b3 = index(this.base64alphabet, string[i+2], 1) - 1;
		b4 = index(this.base64alphabet, string[i+3], 1) - 1;
		chunk = (b1*64*64*64) + (b2*64*64) + (b3*64) + b4;
		b1 = chunk / (256*256);
		b2 = (chunk / 256) % 256;
		b3 = chunk % 256;
		if (b1 == 0)
			b1 = 32;
		endif
		if (b2 == 0)
			b2 = 32;
		endif
		if (b3 == 0)
			b3 = 32;
		endif
		result = result + encode_binary(b1, b2, b3);
		i = i + 4;
	endwhile
	return $stringutils:strip(result);
.

rem --- Convert string to uppercase -------------------------------------------

verb $god $stringutils upper tnt
.program $stringutils:upper
	{string} = args;
	list = decode_binary(string, 1);
	for i in [1..length(list)]
		if ((list[i] >= 97) && (list[i] <= 122))
			list[i] = list[i] - 32;
		endif
	endfor
	return encode_binary(list);
.

