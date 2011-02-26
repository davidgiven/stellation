#ifndef AUTH_H
#define AUTH_H

extern string CreateAuthenticationCookie(Reader& reader);
extern Database::Type CheckAuthenticationCookie(const string& cookie);

#endif
