/* main.c
 * gdrender main program
 * $Source: /cvsroot/stellation/gdrender/main.c,v $
 * $State: Exp $
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <gd.h>
#include <gdfontg.h>
#include <gdfontl.h>
#include <gdfonts.h>
#include <gdfontt.h>
#include <gdfontmb.h>

extern char* load_url(char* url);

char buffer[80];
char* cmdstring;
gdImagePtr image;
gdFontPtr font;
int colour;
int width, height;

/* --- Error handling ------------------------------------------------------ */

void error(char* msg, ...)
{
	va_list ap;

	va_start(ap, msg);
	printf("Content-type: text/plain\n\n");
	vprintf(msg, ap);
	printf("\n");
	va_end(ap);
	exit(1);
}

/* --- Lexer --------------------------------------------------------------- */

void expect_char(char c)
{
	if (*cmdstring != c)
		error("Got char %c, expected %c", *cmdstring, c);
	cmdstring++;
}

void get_word(void)
{
	char* p = buffer;
	char c;

	do {
		c = *cmdstring++;
		if (c == '\0')
			error("Unexpected end of string");
		if (c == '.')
			break;
		*p++ = c;
	} while(1);
	*p = '\0';
}

void expect_word(char* target)
{
	get_word();
	if (strcmp(target, buffer) != 0)
		error("Got `%s', expected `%s'", buffer, target);
}

int get_number(void)
{
	get_word();
	return atoi(buffer);
}

/* --- Change colour ------------------------------------------------------- */

void do_colour(void)
{
	int red = get_number();
	int green = get_number();
	int blue = get_number();
	colour = gdImageColorExact(image, red, green, blue);
	if (colour == -1)
		colour = gdImageColorAllocate(image, red, green, blue);
}

/* --- Draw a line --------------------------------------------------------- */

void do_line(void)
{
	int x1 = get_number();
	int y1 = get_number();
	int x2 = get_number();
	int y2 = get_number();
	gdImageLine(image, x1, y1, x2, y2, colour);
}

void do_dash(void)
{
	int x1 = get_number();
	int y1 = get_number();
	int x2 = get_number();
	int y2 = get_number();
	gdImageDashedLine(image, x1, y1, x2, y2, colour);
}

/* --- Rectangles ---------------------------------------------------------- */

void do_box()
{
	int x1 = get_number();
	int y1 = get_number();
	int x2 = get_number();
	int y2 = get_number();
	gdImageRectangle(image, x1, y1, x2, y2, colour);
}

void do_fbox()
{
	int x1 = get_number();
	int y1 = get_number();
	int x2 = get_number();
	int y2 = get_number();
	gdImageFilledRectangle(image, x1, y1, x2, y2, colour);
}

/* --- Text ---------------------------------------------------------------- */

void do_font(void)
{
	get_word();
	if (strcmp(buffer, "g") == 0)
		font = gdFontGiant;
	else if (strcmp(buffer, "l") == 0)
		font = gdFontLarge;
	else if (strcmp(buffer, "s") == 0)
		font = gdFontSmall;
	else if (strcmp(buffer, "t") == 0)
		font = gdFontTiny;
	else if (strcmp(buffer, "mb") == 0)
		font = gdFontMediumBold;
	else
		error("Unknown font `%s'", buffer);
}

void do_text(void)
{
	int x = get_number();
	int y = get_number();
	char* c;
	get_word();

	c = buffer;
	while (*c != '\0')
	{
		if (*c == '+')
			*c = ' ';
		c++;
	}
	
	gdImageString(image, font, x, y, buffer, colour);
}

void do_text_up(void)
{
	int x = get_number();
	int y = get_number();
	char* c;
	get_word();

	c = buffer;
	while (*c != '\0')
	{
		if (*c == '+')
			*c = ' ';
		c++;
	}
	
	gdImageStringUp(image, font, x, y, buffer, colour);
}

/* --- Draw an arc --------------------------------------------------------- */

void do_arc(void)
{
	int cx = get_number();
	int cy = get_number();
	int w = get_number();
	int h = get_number();
	int s = get_number();
	int e = get_number();

	gdImageArc(image, cx, cy, w, h, s, e, colour);
}

/* --- Draw a grid --------------------------------------------------------- */

void do_grid(void)
{
	int x = get_number();
	int y = get_number();
	int xs = get_number();
	int ys = get_number();
	int w = get_number();
	int h = get_number();
	int i;

	for (i=0; i<=w; i++)
		gdImageLine(image, x+(i*xs), y, x+(i*xs), y+(h*ys), colour);

	for (i=0; i<=h; i++)
		gdImageLine(image, x, y+(i*ys), x+(w*xs), y+(i*ys), colour);
}

/* --- Main program -------------------------------------------------------- */

int main(int argc, char* argv[])
{
	cmdstring = getenv("QUERY_STRING");

loop:
	get_word();
	if (strcmp(buffer, "url") == 0)
	{
		cmdstring = load_url(cmdstring);
		goto loop;
	}
	else if (strcmp(buffer, "width") != 0)
		error("Got `%s', expected `url' or `width'", buffer);

	width = get_number();
	expect_word("height");
	height = get_number();
	image = gdImageCreate(width, height);

	colour = gdImageColorAllocate(image, 0, 0, 0);

	/* Main command loop. */

	do {
		get_word();
		if (strcmp(buffer, "colour") == 0)
			do_colour();
		else if (strcmp(buffer, "color") == 0)
			do_colour();
		else if (strcmp(buffer, "line") == 0)
			do_line();
		else if (strcmp(buffer, "dash") == 0)
			do_dash();
		else if (strcmp(buffer, "box") == 0)
			do_box();
		else if (strcmp(buffer, "fbox") == 0)
			do_fbox();
		else if (strcmp(buffer, "font") == 0)
			do_font();
		else if (strcmp(buffer, "text") == 0)
			do_text();
		else if (strcmp(buffer, "textup") == 0)
			do_text_up();
		else if (strcmp(buffer, "arc") == 0)
			do_arc();
		else if (strcmp(buffer, "grid") == 0)
			do_grid();
		else if (strcmp(buffer, "") == 0)
			break;
		else
			error("Unknown command `%s'", buffer);
	} while (1);

	/* Write GIF */

	printf("Content-type: image/gif\n\n");
	gdImageInterlace(image, 1);
	gdImageGif(image, stdout);
	gdImageDestroy(image);
	return 0;
}

/* Revision history
 * $Log: main.c,v $
 * Revision 1.4  2000/08/03 18:26:44  dtrg
 * Replaced "..." local includes with <...> global ones.
 *
 * Revision 1.3  2000/07/31 23:37:11  dtrg
 * Added the `url.' redirection function.
 *
 * Revision 1.2  2000/07/29 23:58:04  dtrg
 * Added grid.
 *
 * Revision 1.1.1.1  2000/07/29 17:10:25  dtrg
 * Initial checkin.
 *
 */

