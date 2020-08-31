package model;

import haxe.macro.Context;
import haxe.macro.Expr;
using tink.MacroApi;
using Lambda;

class HasPropertiesMacro {
    public static function build(): Array<Field> {
        var fields = Context.getBuildFields();
        var newfields: Array<Field> = [];

        for (field in fields) {
            switch (field.kind) {
                case FVar(type, element):
                    if (!field.meta.getValues(":sproperty").empty()) {
                        field.kind = FProp('get', 'set', type, null);
                        var property = field.name.toUpperCase();

                        newfields.push({
                                name: "get_" + field.name,
                                access: [Access.APrivate, Access.AInline],
                                kind: FieldType.FFun({
                                    expr: macro return model.Properties.$property.get(this),
                                    ret: type,
                                    args: []
                                }),
                                pos: field.pos
                        });

                        newfields.push({
                                name: "set_" + field.name,
                                access: [Access.APrivate, Access.AInline],
                                kind: FieldType.FFun({
                                    expr: macro return model.Properties.$property.set(this, value),
                                    ret: type,
                                    args: ["value".toArg(type)]
                                }),
                                pos: field.pos
                        });
                    }

                default:
            }
        }

        return fields.concat(newfields);
    }
}

