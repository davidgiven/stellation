/* webloader.c
 * gdrender HTTP fetcher
 * $Source: /cvsroot/stellation/gdrender/webloader.c,v $
 * $State: Exp $
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

static char* url;
static char* server;
static int port;

static int parseurl(void)
{
	if ((url[0] != 'h') ||
	    (url[1] != 't') ||
	    (url[2] != 't') ||
	    (url[3] != 'p') ||
	    (url[4] != ':') ||
	    (url[5] != '/') ||
	    (url[6] != '/'))
	    	return 0;
	url += 7;
	server = url;

	port = 80;
	while ((*url != ':') && (*url != '/') && (*url != '\0'))
		url++;
	
	if (*url == ':')
	{
		char* p;

		*url++ = '\0';
		p = url;
		while ((*url != '/') && (*url != '\0'))
			url++;
		*url++ = '\0';
		port = atoi(p);
	}
	else
		*url++ = '\0';

	return 0;
}

static int opensocket(void)
{
	struct sockaddr_in name;
	struct hostent* hostinfo;
	int sock;

	name.sin_family = AF_INET;
	name.sin_port = htons(port);
	hostinfo = gethostbyname(server);
	if (!hostinfo)
		return -1;
	name.sin_addr = *(struct in_addr*) hostinfo->h_addr;

	sock = socket(PF_INET, SOCK_STREAM, 0);
	if (sock < 0)
		return -1;

	if (connect(sock, (struct sockaddr*)&name, sizeof(name)) < 0)
		return -1;

	return sock;
}

char* load_url(char* p)
{
	int sock;
	FILE* file;
	char* buf = NULL;

	url = p;
	if (parseurl())
		return NULL;
	sock = opensocket();
	if (sock == -1)
		return NULL;

	file = fdopen(sock, "r+");
	fprintf(file, "GET /%s\n\n", url);
	{
		int size = 0;
		getline(&buf, &size, file);
	}
	fclose(file);

	return buf;
}

/* Revision history
 * $Log: webloader.c,v $
 * Revision 1.1  2000/07/31 23:37:11  dtrg
 * Added the `url.' redirection function.
 *
 */


