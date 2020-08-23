package utils;

import utils.Message;
import utest.Test;
import utest.Assert;

class MessageTest extends TestCase {
	function testEmpty() {
		var m = new Message();
		Assert.same(m.toMap(), new Map<String, String>());
		Assert.same(m.toArray(), []);
		Assert.same("", m.serialise());
	}

	function testInts() {
		var m = new Message();
		m.setInt("foo", 1);
		Assert.same(m.getInt("foo"), 1);
		Assert.isTrue(m.exists("foo"));
		Assert.same(m.toMap(), ["foo" => "1"]);
		Assert.same("foo=1\n", m.serialise());
	}

	function testFloats() {
		var m = new Message();
		m.setFloat("foo", 1.23);
		Assert.floatEquals(m.getFloat("foo"), 1.23);
		Assert.isTrue(m.exists("foo"));
		Assert.same(m.toMap(), ["foo" => "1.23"]);
		Assert.same(m.toArray(), []);
		Assert.same("foo=1.23\n", m.serialise());
	}

	function testStrings() {
		var m = new Message();
		m.setString("foo", "bar");
		Assert.same(m.getString("foo"), "bar");
		Assert.isTrue(m.exists("foo"));
		Assert.same(m.toMap(), ["foo" => "bar"]);
		Assert.same(m.toArray(), []);
		Assert.same("foo=bar\n", m.serialise());
	}

	function testArguments() {
		var m = new Message();
		m.addString("foo");
		m.addString("bar");
		Assert.same(["0" => "foo", "1" => "bar", Message.LENGTH => "2"], m.toMap());
		Assert.same(m.getString(0), "foo");
		Assert.same(m.getString(1), "bar");
		Assert.same(m.length, 2);
		Assert.same(m.toArray(), ["foo", "bar"]);
		Assert.same("0=foo\n1=bar\n_count=2\n", m.serialise());
	}

	function testMixed() {
		var m = new Message();
		m.setString("foo", "bar");
		m.addString("thingy");
		m.setString("baz", "boo");
		Assert.same(["foo" => "bar", "baz" => "boo", "0" => "thingy", Message.LENGTH => "1"], m.toMap());
		Assert.same(["thingy"], m.toArray());
		Assert.same("0=thingy\n_count=1\nbaz=boo\nfoo=bar\n", m.serialise());
		Assert.same("bar", m.getString("foo"));
		Assert.same("boo", m.getString("baz"));
		Assert.same("thingy", m.getString("0"));
		Assert.same("thingy", m.getString(0));
		Assert.same(1, m.length);
	}
}

