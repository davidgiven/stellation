#ifndef SCALAR_H
#define SCALAR_H

template<class T> class scalar
{
public:
	scalar(T initialvalue):
		_value(initialvalue)
	{}

	operator T() const { return _value; }

	T operator = (T value) { _value = value; return _value; }
	T operator ++ (int) { return _value++; }
	T operator ++ () { return ++_value; }
	T operator -- (int) { return _value--; }
	T operator -- () { return --_value; }

protected:
	T _value;
};

template<class T, T initialvalue> class iscalar : public scalar<T>
{
public:
	iscalar():
		scalar<T>(initialvalue)
	{}
};

#endif
