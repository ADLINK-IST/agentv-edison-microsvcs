package com.prismtech.edison.display.types;


/**
* com/prismtech/edison/display/types/LCDColorHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl//display.idl
* Tuesday, February 16, 2016 1:52:37 AM CET
*/

abstract public class LCDColorHelper
{
  private static String  _id = "IDL:com/prismtech/edison/display/types/LCDColor:1.0";

  public static void insert (org.omg.CORBA.Any a, com.prismtech.edison.display.types.LCDColor that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static com.prismtech.edison.display.types.LCDColor extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [4];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[0] = new org.omg.CORBA.StructMember (
            "id",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[1] = new org.omg.CORBA.StructMember (
            "r",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[2] = new org.omg.CORBA.StructMember (
            "g",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[3] = new org.omg.CORBA.StructMember (
            "b",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (com.prismtech.edison.display.types.LCDColorHelper.id (), "LCDColor", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static com.prismtech.edison.display.types.LCDColor read (org.omg.CORBA.portable.InputStream istream)
  {
    com.prismtech.edison.display.types.LCDColor value = new com.prismtech.edison.display.types.LCDColor ();
    value.id = istream.read_short ();
    value.r = istream.read_short ();
    value.g = istream.read_short ();
    value.b = istream.read_short ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, com.prismtech.edison.display.types.LCDColor value)
  {
    ostream.write_short (value.id);
    ostream.write_short (value.r);
    ostream.write_short (value.g);
    ostream.write_short (value.b);
  }

}