#include "globals.h"
#include "Writer.h"
#include "Hash.h"

#define PAIR(cid, kid) ((((uint64_t)cid) << 32) | ((uint64_t)kid))
#include "statics-tables.h"

void WriteAllStatics(Writer& writer)
{
	for (int i = 0; i < sizeof(allstatics)/sizeof(*allstatics); i++)
	{
		Hash::Type cid = allstatics[i].cid;
		Hash::Type kid = allstatics[i].kid;

		writer.Write(cid);
		writer.Write(kid);
		writer.Write(GetStringStatic(cid, kid));
	}
}
