#ifndef SCALAR_H
#define SCALAR_H

template<class T> class scalar : public noncopyable
{
public:
	scalar(T initialvalue):
		_value(initialvalue)
	{}

	scalar(const scalar<T>& other):
		_value(other._value)
	{}

	operator T&() { return _value; }
	operator const T&() const { return _value; }

	T operator = (const scalar<T>& other) { return _value = other._value; }
	T operator = (T value) { return _value = value; }

protected:
	T _value;
};

template<class T, T initialvalue> class iscalar : public scalar<T>
{
public:
	iscalar():
		scalar<T>(initialvalue)
	{}

	T operator = (T value) { return *((scalar<T>*)this) = value; }
};

#endif
