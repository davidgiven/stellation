
// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

String.prototype.escapeHTML = function()
{
    var div = document.createElement("div");
    div.appendChild(document.createTextNode(this));
    return div.innerHTML;
};

$.fn.findAndSelf = function(selector)
{
	return this.find(selector).add(this.filter(selector))
}