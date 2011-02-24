#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <string>
#include <iostream>

using std::string;

#include "hash.h"

int main(int argc, const char* argv[])
{
	bool first = true;
	for (;;)
	{
		string s;

		std::cin >> s;
		if (!std::cin.good())
			break;

		int hash = propertyHash(s.data(), s.size());

		if (!first)
			std::cout << ",\n";
		else
			first = false;

		s[0] = toupper(s[0]);
		std::cout << s << " = " << hash;
	}

	std::cout << "\n";
	return 0;
}
