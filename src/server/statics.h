#ifndef STATICS_H
#define STATICS_H

extern double GetNumberStatic(Hash::Type cid, Hash::Type kid);
extern string GetStringStatic(Hash::Type cid, Hash::Type kid);
extern void WriteAllStatics(Writer& writer);

#endif
