#ifndef UTILS_H
#define UTILS_H

extern int Random(int length);
extern double Random();

template<class T, int N> T RandomOf(T (&array)[N])
{
  return array[Random(N)];
}

#endif
